import express from "express"
import dotenv from "dotenv"

const app = express()

dotenv.config()

import connectToDB from "./middleware/connect"

app.use(express.json())

// if the root path is visited, sample data is received in JSON format
app.use("^/$", connectToDB)

app.listen(3000, () => 
  console.log("PostgreSQL server running on PORT 3000")
)

export default app