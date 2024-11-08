import { type Request, type Response } from "express"
import pool from "../util/pool"

// Connects to the RDBMS with a test query
const connectToDB = async (_: Request, res: Response) => {  
  try {
    const client = await pool.connect()

    try {
      const query = "SELECT * FROM samples"
      
      const { rows } = await pool.query(query)
      res.json(rows)
    } catch (err: any) {
      res.status(500).json({ ERROR: err.stack })
    } finally {
      client.release()
    }
  } catch (err: any) {
    res.status(500).json({ ERROR: err.message })
  }
}

export default connectToDB