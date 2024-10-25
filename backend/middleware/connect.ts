import { type Request, type Response } from "express"
import pool from "../util/pool"

const connectToDB = async (_: Request, res: Response) => {
  try {
    const client = await pool.connect()
    console.log("Sucessfully connected to database.")

    try {
      const results = await pool.query("SELECT * FROM public.samples")
      res.json({ results: results.rows })
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