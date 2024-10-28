import { Pool } from "pg"

// created a connection instance to connect to the RDBMS
// added environmental variables for an extra layer of security
const {
  POSTGRES_HOST,
  POSTGRES_USER,
  POSTGRES_PASSWORD,
  POSTGRES_PORT
} = process.env

const pool = new Pool({
  host: POSTGRES_HOST,
  user: POSTGRES_USER,
  password: POSTGRES_PASSWORD,
  port: Number(POSTGRES_PORT),
  ssl: { rejectUnauthorized: false }
})

export default pool