import express from "express"
import * as apiMiddleware from "../../middleware/api"

const router = express.Router()

// TODO: Create simple database fetching logic for users
router.get("/books", apiMiddleware.fetchAllBooks)

export default router