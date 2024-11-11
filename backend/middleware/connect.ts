import { type Request, type Response } from "express"
import pool from "../util/pool"

// Fetches sample data as a test query to ensure that the RDBMS connection is working properly
const fetchDBSample = async (_: Request, res: Response) => {  
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

export default fetchDBSample