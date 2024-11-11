import dotenv from "dotenv"

if (process.env.NODE_ENV !== "production")
dotenv.config()

import express from "express"
const app = express()

import extRouter from "./routers/extRouter"
import showContentNotFound from "../middleware/notFound"
import fetchDBSample from "../middleware/connect"

app.use(express.json())

// if the root path is visited, sample data is received in JSON format
app.use("^/$", fetchDBSample)

/* 
  Hardcover is the external API we have used for this project.
  To import all the data, we used GraphQL to extract all the proper data in our project
  and inserted it appropriately.
*/
// a route for inserting external data to our database
app.use("^/ext", extRouter)

// If user enters an invalid path, show ERROR JSON message with a status of 404
app.use("*", showContentNotFound)

app.listen(3000, () => 
  console.log("PostgreSQL server running on PORT 3000")
)

export default app