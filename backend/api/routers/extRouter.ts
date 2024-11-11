import express from "express"
import * as bookwormAPI from "../../middleware/hardcoverAPI"

const router = express.Router()

// Inserts all books from Hardcover to the RDBMS
// The current max is 64 because their api is not configured correctly
router.post("/books", bookwormAPI.insertBooks)

// Inserts all categories from Hardcover to the RDBMS
router.post("/categories", bookwormAPI.insertBookCategories)

// Inserts all book statuses (active, read, new) from Hardcover to the RDBMS
router.post("/statuses", bookwormAPI.insertStatuses)

// Inserts all book tags from Hardcover to the RDBMS
router.post("/tags", bookwormAPI.insertTags)

// Inserts all book authors from Hardcover to the RDBMS
router.post("/authors", bookwormAPI.insertAuthors)

// Inserts all book publishers from Hardcover to the RDBMS
router.post("/publishers", bookwormAPI.insertPublishers)

// Inserts all reviews of a book to the RDBMS
router.post("/book-reviews", bookwormAPI.insertReviews)

export default router