import dotenv from "dotenv"

if (process.env.NODE_ENV !== "production")
dotenv.config()

import express from "express"
const app = express()

import apiRouter from "./routers/apiRouter"
import showContentNotFound from "../middleware/notFound"
import connectToDB from "../middleware/connect"

app.use(express.json())

// if the root path is visited, sample data is received in JSON format
app.use("^/$", connectToDB)

app.use("^/api", apiRouter)

app.use("*", showContentNotFound)

app.listen(3000, () => 
  console.log("PostgreSQL server running on PORT 3000")
)

export default app