import { Pool } from "pg"

const pool = new Pool({
  host: process.env.POSTGRES_HOST,
  database: process.env.DB_NAME,
  user: process.env.POSTGRES_USER,
  password: process.env.POSTGRES_PASSWORD,
  port: Number(process.env.POSTGRES_PORT),
  ssl: { rejectUnauthorized: false }
})

export default pool