import { type Request, type Response } from "express"

function showContentNotFound(_: Request, res: Response) {
  res.status(404).json({ ERROR: "The request path does not have any content. Try looking for another!" })
}

export default showContentNotFound