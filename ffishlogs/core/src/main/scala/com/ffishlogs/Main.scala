package com.ffishlogs

import cats.effect.{ExitCode, IO, IOApp, Resource}
import cats.syntax.all.*
import com.ffishlogs.api.V0
import com.ffishlogs.db.Db
import org.http4s.HttpRoutes
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.{Router, Server}
import org.typelevel.log4cats.{Logger, LoggerFactory}
import org.typelevel.log4cats.slf4j.Slf4jFactory
import porcupine.Database
import scala.util.control.NoStackTrace

object Main extends IOApp {

  final case class InitializationError(message: String, cause: Throwable) extends Throwable with NoStackTrace
  
  override def run(args: List[String]): IO[ExitCode] = {
    implicit val loggerFactory: LoggerFactory[IO] = Slf4jFactory.create[IO]
    loggerFactory.create.flatMap { implicit logger: Logger[IO] =>
      val server = for {
        c <- config(logger).evalTap(printConfig(logger, _))
        db <- database(logger, c)
        serv <- httpServer(db, c)
      } yield serv
      // Print and exit for the errors we know about, hard crash for the ones we don't
      server.useForever.recoverWith {
        case InitializationError(message, throwable) =>
          logger.error(throwable)(message).as(ExitCode.Error)
      }
    }
  }
  
  def printConfig(logger: Logger[IO], c: Config): IO[Unit] =
    logger.info("Config:") >> c.logInfo(logger)
  
  def config(logger: Logger[IO]): Resource[IO, Config] =
    Config.config.load[IO].toResource.adaptError(InitializationError("Error loading ffishlogs configuration", _))
  
  def database(logger: Logger[IO], c: Config): Resource[IO, Db] =
    Database.open[IO](c.sqlite.database.toString).adaptError(InitializationError("Error opening ffishlogs database", _))
      .flatMap(Db.ofDatabase).adaptError(InitializationError("Error initializing ffishlogs database", _))
  
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
