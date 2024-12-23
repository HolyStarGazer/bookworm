import pool from "../util/pool"
import argon2 from "argon2"
import { type Request, type Response } from "express"
import jwt from "jsonwebtoken"

export const getUser = async (req: Request, res: Response) => {
  try {
    const instance = await pool.connect()

    try {
      const { username } = req.params

      if (!username) {
        res.status(400).json({ error: "Username was not supplied." })
        return
      }

      const { rows } = await instance.query('SELECT id, username, email, "isAdmin" FROM users WHERE username = $1', [username])

      res.json(rows[0])
    } catch (err) {
      res.status(500).json({ error: err instanceof Error ? err.message : "Something went wrong." })
    } finally {
      instance.release()
    }
  } catch (err) {
    res.status(500).json({ error: err instanceof Error ? err.message : "Something went wrong." })
  }
}

export const registerNewUser = async (req: Request, res: Response) => {
  try {
    const instance = await pool.connect()

    try {
      const { username, password } = req.body      
      
      if (!(username && password)) {
        res.status(400).json({ error: "Username or password is missing." })
        return
      }

      const { rows: foundUsername } = await instance.query("SELECT username FROM users WHERE username = $1", [username])

      if (foundUsername.length > 0) {
        res.status(409).json({ error: "Username already exists." })
        return
      }

      const hashedPassword = await argon2.hash(password)

      await instance.query("INSERT INTO users(username, password) VALUES ($1, $2)", [username, hashedPassword])

      res.json({ success: "User successfully created!" })
    } catch (err) {
      res.status(500).json({ error: err instanceof Error ? err.message : "Something went wrong." })
    }
  } catch (err) {
    res.status(500).json({ error: err instanceof Error ? err.message : "Something went wrong." })
  }
}

// Refresh tokens are only created when the user logs in
export const loginUser = async (req: Request, res: Response) => {
  try {
    const instance = await pool.connect()
    
    try {
      const { username, password } = req.body

      if (!(username && password)) {
        res.status(400).json({ error: "Username or password is missing." })
        return
      }
      
      const { rows: passwordResults } = await instance.query("SELECT password FROM users WHERE username = $1", [username])
      const hashedPassword = passwordResults[0].password
      const passwordMatch = await argon2.verify(hashedPassword, password)
      
      if (!(passwordResults.length > 0 && passwordMatch)) {
        res.status(401).json({ error: "Username or password is incorrect." })
        return
      }

      const refreshToken = jwt.sign(
        { username },
        process.env.REFRESH_TOKEN_SECRET as string,
        { expiresIn: "2d" }
      )

      res.json({ "token": refreshToken })
    } catch (err) {
      res.status(500).json({ error: err instanceof Error ? err.message : "Something went wrong." })
    } finally {
      instance.release()
    }
  } catch (err: any) {
    res.status(500).json({ error: err instanceof Error ? err.message : "Something went wrong." })
  }
}

// For handling expired access tokens or creating an access token for the first time
export const createNewAccessToken = (req: Request, res: Response) => {
  try {
    const refreshToken = req.get("authorization")

    if (!refreshToken) {
      res.status(401).json({ error: "A refresh token is required to refresh an access token" })
      return
    }

    try {
      // Will throw an error if token is invalid
      const decodedRefreshToken = jwt.verify(refreshToken, process.env.REFRESH_TOKEN_SECRET as string) as Record<string, unknown>
      
      const refreshedAccessToken = jwt.sign(
        { username: decodedRefreshToken.username },
        process.env.ACCESS_TOKEN_SECRET as string,
        { expiresIn: "15m" }
      )

      res.json({ "token": refreshedAccessToken })
    } catch (err) {
      res.status(400).json({ error: "Malformed or invalid refresh token" })
    }
  } catch (err) {
    res.status(500).json({ error: err instanceof Error ? err.message : "Something went wrong." })
  }
}