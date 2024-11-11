export type Book = {
  title: string
  subtitle?: string,
  book_category_id: number
  book_status_id: number
  contributions: Array<{ author_id: number }>
  description: string
  image: {
    id: number
    url: string
  }
  pages?: number
  rating?: number
  ratings_count: number
  ratings_distribution: Array<{ count: number, rating: number }>
  release_date?: string
  release_year?: number
  reviews_count: number
  users_read_count: number
  taggings: Array<{ tag_id: number }>
  default_physical_edition?: {
    id: number
    isbn_10: string
    isbn_13: string
    publisher_id?: number
  }
  default_ebook_edition?: {
    id: number
    isbn_10: string
    isbn_13: string
    publisher_id?: number
  }
  default_cover_edition?: {
    id: number
    isbn_10: string
    isbn_13: string
    publisher_id?: number
  } 
  default_audio_edition?: {
    id: number
    isbn_10: string
    isbn_13: string
    publisher_id?: number
  }
  id: number
}