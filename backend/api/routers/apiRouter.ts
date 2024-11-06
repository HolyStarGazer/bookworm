import express from "express"
import * as bookwormAPI from "../../middleware/bookwormAPI"

// fetchBookCategories,
//   fetchBooks,
//   fetchStatuses,
//   fetchTags,
//   fetchAuthors,
//   fetchPublishers,
//   fetchReviews

const route = express.Router()

route.post("/categories", bookwormAPI.fetchBookCategories)

route.post("/statuses", bookwormAPI.fetchStatuses)

route.post("/tags", bookwormAPI.fetchTags)

route.post("/authors", bookwormAPI.fetchAuthors)

route.post("/publishers", bookwormAPI.fetchPublishers)

route.post("/book-reviews", bookwormAPI.fetchReviews)

export default route