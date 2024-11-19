import express from "express"
import * as hardcoverAPI from "../../middleware/hardcoverAPI"

const router = express.Router()

// Inserts all books from Hardcover to the RDBMS
// The current max is 64 because their api is not configured correctly
router.post("/books", hardcoverAPI.insertBooks)

// Inserts all categories from Hardcover to the RDBMS
router.post("/categories", hardcoverAPI.insertBookCategories)

// Inserts all book statuses (active, read, new) from Hardcover to the RDBMS
router.post("/statuses", hardcoverAPI.insertStatuses)

// Inserts all book tags from Hardcover to the RDBMS
router.post("/tags", hardcoverAPI.insertTags)

// Inserts all book authors from Hardcover to the RDBMS
router.post("/authors", hardcoverAPI.insertAuthors)

// Inserts all book publishers from Hardcover to the RDBMS
router.post("/publishers", hardcoverAPI.insertPublishers)

// Inserts all reviews of a book to the RDBMS
router.post("/book-reviews", hardcoverAPI.insertReviews)

export default router