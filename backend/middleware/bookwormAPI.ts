import { type Request, type Response, type NextFunction } from "express"
import pool from "../util/pool"

async function fetchGraphQL(operationsDoc: string, operationName: string, variables: Record<string, any>) {
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
}

const operationsDoc = `
  query dataBookCategories {
    book_categories {
      name
      id
    }
  }
  
  query dataBooks {
    books(order_by: {id: asc}) {
      id
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
    }
  }
  
  query dataStatuses {
    book_statuses {
      id
      name
    }
  }
  
  query dataTags {
    tag_categories {
      id
      category
      tags(order_by: {id: asc}) {
        tag
        id
      }
    }
  }
  
  query dataAuthors {
    authors(order_by: {id: asc}) {
      id
      name
      title
      books_count
      users_count
    }
  }
  
  query dataPublishers {
    publishers(order_by: {id: asc}) {
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
}

const fetchBooks = async (_: Request, res: Response) => {
  try {
    const { data } = await fetchGraphQL(operationsDoc, "dataBooks", {})
    res.json({ data })
  } catch (err: any) {
    res.status(500).json({ ERROR: err.message })
  }
}

const fetchStatuses = async (_: Request, res: Response) => {
  try {
    const { data } = await fetchGraphQL(operationsDoc, "dataStatuses", {})
    res.json({ data })
  } catch (err: any) {
    res.status(500).json({ ERROR: err.message })
  }
}

const fetchTags = async (_: Request, res: Response) => {
  try {
    const { data } = await fetchGraphQL(operationsDoc, "dataTags", {})
    res.json({ data })
  } catch (err: any) {
    res.status(500).json({ ERROR: err.message })
  }
} 

const fetchAuthors = async (_: Request, res: Response) => {
  try {
    const { data } = await fetchGraphQL(operationsDoc, "dataAuthors", {})
    res.json({ data })
  } catch (err: any) {
    res.status(500).json({ ERROR: err.message })
  }
}

const fetchPublishers = async (_: Request, res: Response) => {
  try {
    const { data } = await fetchGraphQL(operationsDoc, "dataPublishers", {})
    res.json({ data })
  } catch (err: any) {
    res.status(500).json({ ERROR: err.message })
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