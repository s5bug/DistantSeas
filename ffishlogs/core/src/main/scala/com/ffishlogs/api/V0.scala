package com.ffishlogs.api

import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.dsl.io.*
import org.typelevel.log4cats.Logger

case class V0(logger: Logger[IO]) {

  final val service = HttpRoutes.of[IO] {
    case GET -> Root => Ok("""{"api":"ffishlogs","version":"0"}""")
  }
  
}
