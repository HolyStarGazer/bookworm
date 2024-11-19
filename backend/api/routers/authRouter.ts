import express from "express"
import * as authMiddleware from "../../middleware/auth"

const router = express.Router()

router.post("/register", authMiddleware.registerNewUser)

router.post("/login", authMiddleware.loginUser)

router.post("/refresh", authMiddleware.createNewAccessToken)

export default router