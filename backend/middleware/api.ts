import { type Request, type Response } from "express"
import jwt from "jsonwebtoken"
import pool from "../util/pool"

// TODO: Create a proper query to fetch all user books
export const fetchAllBooks = async (req: Request, res: Response) => {
  try {
    const instance = await pool.connect()
    try {
      const token = req.get("authorization")

      // extracting the username from the token
      const { username } = jwt.verify(token as string, process.env.REFRESH_TOKEN_SECRET as string) as Record<string, unknown>
      const { rows } = await instance.query("SELECT * FROM books WHERE ", []);      
    } catch (err) {
      res.status(500).json({ error: err instanceof Error ? err.message : "Something went wrong." })
    } finally {
      instance.release()
    }
  } catch (err) {
    res.status(500).json({ error: err instanceof Error ? err.message : "Something went wrong." })
  }
}