import { type Request, type Response } from "express"
import { type Book } from "../util/types/book"
import { type Author } from "../util/types/author"
import { type Tag } from "../util/types/tag"
import pool from "../util/pool"


async function fetchGraphQL(operationsDoc: string, operationName: string, variables: Record<string, unknown>) {
  try {
    const result = await fetch(
      "https://api.hardcover.app/v1/graphql",
      {
        method: "POST",
        headers: {
          'content-Type': 'application/json',
          'authorization': process.env.BEARER_TOKEN
        } as HeadersInit,
        body: JSON.stringify({
          query: operationsDoc,
          variables: variables,
          operationName: operationName
        })
      }
    )
  
    return await result.json()
  } catch (err: any) {
    console.error(err.message)
  }
}

const operationsDoc = `
  query dataBookCategories {
    book_categories {
      name
      id
    }
  }
  
  query dataBooks($booksLimit: Int!, $booksOffset: Int!) {
    books(order_by: {id: asc}, limit: $booksLimit, offset: $booksOffset) {
      title
      subtitle
      book_category_id
      book_status_id
      contributions {
        author_id
      }
      description
      image {
        id
        url
      }
      pages
      rating
      ratings_count
      ratings_distribution
      release_date
      release_year
      reviews_count
      users_read_count
      taggings {
        tag_id
      }
      default_physical_edition {
        id
        isbn_10
        isbn_13
        publisher_id
      }
      default_ebook_edition {
        id
        isbn_10
        isbn_13
        publisher_id
      }
      default_cover_edition {
        id
        isbn_10
        isbn_13
        publisher_id
      }
      default_audio_edition {
        id
        isbn_10
        isbn_13
        publisher_id
      }
      id
    }
  }
  
  query dataStatuses {
    book_statuses {
      id
      name
    }
  }
  
  query dataTags($tagId: bigint) {
    tags(where: {id: {_eq: $tagId}}) {
      id
      tag
    }
  }
  
  query dataAuthors($authorId: Int!) {
    authors(order_by: {id: asc}, where: {id: {_eq: $authorId}}) {
      id
      name
      title
      books_count
      users_count
    }
  }
  
  query dataPublishers($publisherId: bigint) {
    publishers(order_by: {id: asc}, where: {id: {_eq: $publisherId}}) {
      id
      name
    }
  }
  
  query dataReviews {
    users {
      id
      name
      user_books(where: {_or: {review: {_is_null: false}}, rating: {_is_null: false}}) {
        book_id
        rating
        review
      }
    }
  }
`

const fetchBookCategories = async (_: Request, res: Response) => {
  try {
    const instance = await pool.connect()
  
    try {
      const { data } = await fetchGraphQL(operationsDoc, "dataBookCategories", {})
      const categories: Array<{ name: string, id: number }> = data.book_categories

      if(!categories)
        throw new Error("book_categories array does not exist")

      const query = `INSERT INTO categories VALUES ${categories.map((_, i) => `($${i + 1})`).join(", ")}`
      const values = categories.map(c => c.name)

      await instance.query(query, values)

      res.json({ SUCCESS: "Insert successful!" })
    } catch (err: any) {
      res.status(500).json({ ERROR: err.message })
    } finally {
      instance.release()
    }
  } catch (err: any) {
    res.status(500).json({ ERROR: err.message })
  }
}

const fetchBooks = async (_: Request, res: Response) => {
  try {
    const instance = await pool.connect()

    try {
      const { data: { books } } = await fetchGraphQL(operationsDoc, "dataBooks", { booksLimit: 100, booksOffset: 0 })

      const authorsArr: Array<number> = []
      const taggingsArr: Array<number> = []
      const publishersSet: Array<number> = []

      for (const book of books) {        
        const { contributions, taggings } = book
        const publishers: Array<number | null | undefined> = [
          book.default_physical_edition?.publisher_id,
          book.default_ebook_edition?.publisher_id,
          book.default_cover_edition?.publisher_id,
          book.default_audio_edition?.publisher_id
        ]

        for (const c of contributions)  {
          const id = c.author_id
          
          if (!authorsArr.includes(id)) {
            const { data: { authors } } = await fetchGraphQL(operationsDoc, "dataAuthors", { authorId: id })
            const foundAuthor: Author = authors[0]

            authorsArr.push(id)

            const authorQuery = `
              INSERT INTO authors(name, title, books_count, users_count, id)
              VALUES ($1, $2, $3, $4, $5)
              ON CONFLICT (id) DO NOTHING
            `
            
            const authorValues = [foundAuthor.name, foundAuthor.title, foundAuthor.books_count, foundAuthor.users_count, foundAuthor.id]
            
            await instance.query(authorQuery, authorValues)
          }
        }
        
        for (const t of taggings) {
          const id = t.tag_id
          
          if (!taggingsArr.includes(id)) {
            const { data: { tags } } = await fetchGraphQL(operationsDoc, "dataTags", { tagId: id })
            const foundTag: Tag = tags[0]    
            
            taggingsArr.push(id)

            const tagQuery = `
              INSERT INTO tags(tag, id)
              VALUES ($1, $2)
              ON CONFLICT (id) DO NOTHING
            `

            const tagValues = [foundTag.tag, foundTag.id]
            
            await instance.query(tagQuery, tagValues)
          }
        }


        for (const p of publishers) {
          if (p && !publishersSet.includes(p)) {
            const { data: { publishers } } = await fetchGraphQL(operationsDoc, "dataPublishers", { publisherId: p })
            const foundPublisher = publishers[0]

            publishersSet.push(p)

            const publisherQuery = `
              INSERT INTO publishers(name, id)
              VALUES ($1, $2)
            `
            const publisherValues = [foundPublisher.name, foundPublisher.id]

            await instance.query(publisherQuery, publisherValues)
          }
        }
        
        const booksQuery = `INSERT INTO 
          books(
            id, title,
            subtitle,
            book_category_id, book_status_id,
            contributions,
            description,
            image, pages,
            rating, ratings_count, ratings_distribution,
            release_date, release_year,
            reviews_count, users_read_count,
            taggings,
            default_physical_edition, default_ebook_edition, default_cover_edition, default_audio_edition
          ) VALUES \n(${Array.from({ length: 21 }, (_, i) => `$${i + 1}`).join(",\n")})
        `

        const booksValues = [
          book.id, book.title,
          book.subtitle,
          book.book_category_id, book.book_status_id,
          book.contributions ? JSON.stringify(book.contributions) : null,
          book.description,
          book.image ? JSON.stringify(book.image) : null, 
          book.pages, book.rating, book.ratings_count, 
          book.ratings_distribution ? JSON.stringify(book.ratings_distribution) : null,
          book.release_date, book.release_year,
          book.reviews_count, book.users_read_count,
          book.taggings ? JSON.stringify(book.taggings) : null,
          book.default_physical_edition ? JSON.stringify(book.default_physical_edition) : null, 
          book.default_ebook_edition ? JSON.stringify(book.default_ebook_edition) : null,
          book.default_cover_edition ? JSON.stringify(book.default_cover_edition) : null,
          book.default_audio_edition ? JSON.stringify(book.default_audio_edition) : null
        ]

        await instance.query(booksQuery, booksValues)
      }

      res.json({ SUCCESS: "All inserts successful!" })
    } catch (err: any) {
      res.status(500).json({ ERROR: err.stack })
    } finally {
      instance.release()
    }
  } catch (err: any) {
    res.status(500).json({ ERROR: err.message })
  }
}

const fetchStatuses = async (_: Request, res: Response) => {
  try {
    const instance = await pool.connect()
    
    try {
      const { data } = await fetchGraphQL(operationsDoc, "dataStatuses", {})
      const bookStatuses: Array<{ name: string, id: number }> = data.book_statuses
      
      if (!bookStatuses)
        throw new Error("book_statuses array does not exist")

      const query = `INSERT INTO book_statuses(name, id) VALUES ${bookStatuses.map((_, i) => `($${i * 2 + 1}, $${i * 2 + 2})`).join(", ")}`
      const values = bookStatuses.flatMap(s => [s.name, s.id])

      await instance.query(query, values)
      res.json({ SUCCESS: "Insert successful!" })
    } catch (err: any) {
      res.status(500).json({ ERROR: err.message })
    } finally {
      instance.release()
    }
  } catch (err: any) {
    console.error(err.message)
  }
}

const fetchTags = async (_: Request, res: Response) => {
  try {
    const instance = await pool.connect()

    try {
      const { data } = await fetchGraphQL(operationsDoc, "dataTags", {})
      const tags = data.
      
      res.json({ data })
    } catch (err: any) {
      res.status(500).json({ ERROR: err.message })
    } finally {
      instance.release()
    }
  } catch (err: any) {
    console.error(err.message)
  }
  
} 

const fetchAuthors = async (_: Request, res: Response) => {
  try {
    const instance = await pool.connect()

    try {
      const { data } = await fetchGraphQL(operationsDoc, "dataAuthors", {})
      
      if (!data.authors)
        throw new Error("authors array does not exist")

      const authors: Array<Author> = data.authors

      const query = `INSERT INTO authors(name, title, books_count, users_count, id) VALUES\n${authors.map((_, i) => `(${Array.from({ length: 5 }, (_, j) => `$${i * 5 + (j + 1)}`).join(", ")})`).join(",\n")}`
      const values = authors.flatMap(a => [a.name, a.title, a.books_count, a.users_count, a.id])

      await instance.query(query, values)

      res.json({ SUCCESS: "Insert successful!" })
    } catch (err: any) {
      res.status(500).json({ ERROR: err.message })
    }
  } catch (err: any) {
    console.error(err.message)
  }
  
}

const fetchPublishers = async (_: Request, res: Response) => {
  try {
    const instance = await pool.connect()

    try {
      const { data } = await fetchGraphQL(operationsDoc, "dataPublishers", {})
      if (!data.publishers)
        throw new Error("Publishers array was not found")

      const publishers: Array<{ id: number, name: string }> = data.publishers
      
      const query = `INSERT INTO publishers(name, id) VALUES ${publishers.map((_, i) => `($${i * 2 + 1}, $${i * 2 + 2})`).join(", ")}`
      const values = publishers.flatMap(p => [p.name, p.id])
      
      await pool.query(query, values)
      res.json({ SUCCESS: "Insert successful!" })
      
    } catch (err: any) {
      res.status(500).json({ ERROR: err.message })
    } finally {
      instance.release()
    }
  } catch (err: any) {
    console.error(err.message)
  }
  
}

const fetchReviews = async (_: Request, res: Response) => {
  try {
    const { data } = await fetchGraphQL(operationsDoc, "dataReviews", {})
    res.json({ data })
  } catch (err: any) {
    res.status(500).json({ ERROR: err.message })
  }
}

export {
  fetchBookCategories,
  fetchBooks,
  fetchStatuses,
  fetchTags,
  fetchAuthors,
  fetchPublishers,
  fetchReviews
}