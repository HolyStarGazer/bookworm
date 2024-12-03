import { type Request, type Response } from "express"
import jwt from "jsonwebtoken"
import pool from "../util/pool"

// TODO: Create a proper query to fetch all user books
export const fetchAllBooks = async (req: Request, res: Response) => {
  try {
    const instance = await pool.connect()
    try {
      const token = req.get("authorization")

      if (!token) {
        res.status(404).json({ error: "Malformed/Missing authorization header" })
        return
      }
      
      // extracting the username from the token
      const { username } = jwt.verify(token as string, process.env.ACCESS_TOKEN_SECRET as string) as Record<string, unknown>
      
      if (!username) {
        res.status(404).json({ error: "Username of request does not exist" })
        return
      }

      const { rows } = await instance.query("SELECT books FROM get_user_books WHERE username = $1", [username]);
      res.json(rows[0].books)
    } catch (err) {
      res.status(500).json({ error: err instanceof Error ? err.message : "Something went wrong." })
    } finally {
      instance.release()
    }
  } catch (err) {
    res.status(500).json({ error: err instanceof Error ? err.message : "Something went wrong." })
  }
}