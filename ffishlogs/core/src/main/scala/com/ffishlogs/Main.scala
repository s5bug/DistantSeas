package com.ffishlogs

import cats.effect.{ExitCode, IO, IOApp}
import cats.syntax.all.*
import ciris.*
import com.comcast.ip4s.{Host, Port, ipv4, port}
import org.http4s.HttpRoutes
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Router
import org.typelevel.log4cats.{Logger, LoggerFactory}
import org.typelevel.log4cats.slf4j.Slf4jFactory

object Main extends IOApp {
  
  override def run(args: List[String]): IO[ExitCode] = Config.config.load[IO].flatMap {
    case c @ Config(httpServerConfig, sqliteConfig) =>
      implicit val loggerFactory: LoggerFactory[IO] = Slf4jFactory.create[IO]
      loggerFactory.create.flatMap { implicit logger: Logger[IO] =>
        import org.http4s.dsl.io._
        val service = HttpRoutes.of[IO] {
          case GET -> Root => Ok("Hello, world!")
        }

        val app = Router("/" -> service).orNotFound

        val server = EmberServerBuilder
          .default[IO]
          .withHost(httpServerConfig.host)
          .withPort(httpServerConfig.port)
          .withHttpApp(app)
          .build

        logger.info("Config:") >>
        c.logInfo(logger) >>
        server.useForever
      }
  }
  
}
