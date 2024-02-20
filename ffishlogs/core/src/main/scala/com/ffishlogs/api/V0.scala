package com.ffishlogs.api

import cats.effect.IO
import cats.syntax.all._
import com.ffishlogs.db.Db
import org.http4s.HttpRoutes
import org.http4s.dsl.io.*
import org.typelevel.log4cats.{Logger, LoggerFactory}

final case class V0(logger: Logger[IO], db: Db) {

  final val service = HttpRoutes.of[IO] {
    case GET -> Root => Ok("""{"api":"ffishlogs","version":"0"}""")
  }
  
}

object V0 {
  
  def of(loggerFactory: LoggerFactory[IO], db: Db): IO[V0] = {
    (loggerFactory.create, IO.pure(db)).mapN(V0.apply)
  }
  
}
