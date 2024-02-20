package com.ffishlogs

import cats.effect.{ExitCode, IO, IOApp, Resource}
import com.ffishlogs.api.V0
import com.ffishlogs.db.Db
import org.http4s.HttpRoutes
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.{Router, Server}
import org.typelevel.log4cats.{Logger, LoggerFactory}
import org.typelevel.log4cats.slf4j.Slf4jFactory
import porcupine.Database

object Main extends IOApp {
  
  override def run(args: List[String]): IO[ExitCode] = {
    implicit val loggerFactory: LoggerFactory[IO] = Slf4jFactory.create[IO]
    loggerFactory.create.flatMap { implicit logger: Logger[IO] =>
      Config.config.load[IO].flatMap { config =>
        prepareDatabase(config).use { db =>
          httpServer(db, config).useForever
        }
      }
    }
  }
  
  def logConfig(logger: Logger[IO], c: Config): IO[Unit] =
    logger.info("Config:") >> c.logInfo(logger)
  
  def prepareDatabase(c: Config): Resource[IO, Db] =
    Database.open[IO](c.sqlite.database.toString).flatMap(Db.ofDatabase)
  
  def httpServer(db: Db, c: Config)(implicit loggerFactory: LoggerFactory[IO]): Resource[IO, Server] =
    Resource.suspend {
      for {
        v0 <- V0.of(loggerFactory, db)
      } yield {
        val app = Router(
          "/api/v0" -> v0.service
        ).orNotFound

        val server = EmberServerBuilder
          .default[IO]
          .withHost(c.httpServer.host)
          .withPort(c.httpServer.port)
          .withHttpApp(app)
          .build
    
        server
      }
    }

}
