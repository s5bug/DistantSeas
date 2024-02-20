package com.ffishlogs.db

import cats.Applicative
import porcupine.Query

final case class StatementsMany[F[_, _]]() {
  def traverseK[M[_], G[_, _]](preparation: [A, B] => F[A, B] => M[G[A, B]])(using app: Applicative[M]): M[StatementsMany[G]] =
    app.pure(StatementsMany[G]())
}

object StatementsMany {
  
  final val query: StatementsMany[Query] = StatementsMany()
  
}
