import pool from "../util/pool"
import argon2 from "argon2"
import { type Request, type Response } from "express"
import jwt from "jsonwebtoken"

export const registerNewUser = async (req: Request, res: Response) => {
  try {
    const instance = await pool.connect()

    try {
      const { email, username, password } = req.body

      console.log(req.body)
      
      
      if (!(username && password)) {
        res.status(400).json({ ERROR: "Username or password is missing." })
        return
      }

      const { rows: foundUsername } = await instance.query("SELECT username FROM users WHERE username = $1", [username])

      if (foundUsername.length > 0) {
        res.status(409).json({ CONFLICT: "Username already exists." })
        return
      }

      const { rows: foundEmail } = await instance.query("SELECT email FROM users WHERE email = $1", [email])

      if (foundEmail.length > 0) {
        res.status(409).json({ CONFLICT: "Email already exists." })
        return
      }

      const hashedPassword = await argon2.hash(password)

      await instance.query("INSERT INTO users(email, username, password) VALUES ($1, $2, $3)", [email, username, hashedPassword])

      // Generate a JWT token on account creation
      const refreshToken = jwt.sign(
        { username },
        process.env.REFRESH_TOKEN_SECRET as string,
        { expiresIn: "2d" }
      )

      res.json({ "token": refreshToken })
    } catch (err) {
      res.status(500).json({ ERROR: err instanceof Error ? err.message : "Something went wrong." })
    }
  } catch (err) {
    res.status(500).json({ ERROR: err instanceof Error ? err.message : "Something went wrong." })
  }
}

export const loginUser = async (req: Request, res: Response) => {
  try {
    const instance = await pool.connect()
    
    try {
      const { username, password } = req.body

      if (!(username && password)) {
        res.status(400).json({ ERROR: "Username or password is missing." })
        return
      }
      
      const { rows: passwordResults } = await instance.query("SELECT password FROM users WHERE username = $1", [username])
      const passwordMatch = await argon2.verify(passwordResults[0], password)
      
      if (!(passwordResults.length > 0 && passwordMatch)) {
        res.status(401).json({ ERROR: "Username or password is incorrect." })
        return
      }

      const refreshToken = jwt.sign(
        { username },
        process.env.REFRESH_TOKEN_SECRET as string,
        { expiresIn: "2d" }
      )

      res.json({ "token": refreshToken })
    } catch (err) {
      res.status(500).json({ ERROR: err instanceof Error ? err.message : "Something went wrong." })
    } finally {
      instance.release()
    }
  } catch (err: any) {
    res.status(500).json({ ERROR: err instanceof Error ? err.message : "Something went wrong." })
  }
}

export const createNewAccessToken = (req: Request, res: Response) => {
  const { refreshToken } = req.body

  if (!refreshToken) {
    res.status(401).json({ ERROR: "A refresh token is required to refresh an access token" })
    return
  }
  
  try {
    // Will throw an error if token was not found
    const decodedRefreshToken = jwt.verify(refreshToken, process.env.REFRESH_TOKEN_SECRET as string) as Record<string, unknown>
    
    const refreshedAccessToken = jwt.sign(
      { username: decodedRefreshToken.username },
      process.env.ACCESS_TOKEN_SECRET as string,
      { expiresIn: "15m" }
    )
    res.json({ "accessToken": refreshedAccessToken })
  } catch (err) {
    res.status(500).json({ ERROR: err instanceof Error ? err.message : "Something went wrong." })
  }
}