import { type Request, type Response } from "express"
import pool from "../util/pool"

// Connects to the RDBMS with a test query
const connectToDB = async (_: Request, res: Response) => {
  try {
    const client = await pool.connect()
    console.log("Sucessfully connected to database.")

    try {
      const query = `
        SELECT books.title, JSON_AGG(CONCAT(authors.first_name, ' ', authors.last_name)) AS authors FROM books_authors
        JOIN books ON books_authors.book_id = books.id
        JOIN authors ON books_authors.author_id = authors.id
        GROUP BY books.title
        ORDER BY books.title;
      `
      
      const { rows } = await pool.query(query)
      res.json(rows)
    } catch (err: any) {
      res.status(500).json({ ERROR: err.stack })
    } finally {
      client.release()
      console.log("Connection to PostGreSQL database successfully closed.")
    }
  } catch (err: any) {
    res.status(500).json({ ERROR: err.message })
  }
}

export default connectToDB