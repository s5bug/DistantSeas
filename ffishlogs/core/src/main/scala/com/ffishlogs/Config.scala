package com.ffishlogs

import cats.effect.IO
import cats.syntax.all.*
import ciris.*
import ciris.http4s.*
import com.comcast.ip4s.{Host, Port, ipv4, port}
import java.nio.file.{InvalidPathException, Path, Paths}
import org.typelevel.log4cats.Logger

final case class Config(
    httpServer: Config.HttpServer,
    sqlite: Config.Sqlite
) {
  
  def logInfo(logger: Logger[IO], indent: Int = 2): IO[Unit] = {
    val istr = " " * indent
    logger.info(istr ++ "Http Server:") >>
    httpServer.logInfo(logger, indent + 2) >>
    logger.info(istr ++ "Sqlite:") >>
    sqlite.logInfo(logger, indent + 2)
  }
  
}

object Config {

  final case class HttpServer(host: Host, port: Port) {
    def logInfo(logger: Logger[IO], indent: Int): IO[Unit] = {
      val istr = " " * indent
      logger.info(s"${istr}${HttpServer.hostProp} = ${host}") >>
      logger.info(s"${istr}${HttpServer.portProp} = ${port}")
    }
  }
  
  object HttpServer {
    final val hostProp = "ffishlogs.httpserver.host"
    final val portProp = "ffishlogs.httpserver.port"
    val config: ConfigValue[IO, HttpServer] = for {
      host <- prop(hostProp).or(env("FFISHLOGS_HTTPSERVER_HOST")).as[Host].default(ipv4"0.0.0.0")
      port <- prop(portProp).or(env("FFISHLOGS_HTTPSERVER_PORT")).as[Port].default(port"8080")
    } yield HttpServer(host, port)
  }

  final case class Sqlite(database: Path) {
    def logInfo(logger: Logger[IO], indent: Int): IO[Unit] = {
      val istr = " " * indent
      logger.info(s"${istr}${Sqlite.databaseProp} = ${database}")
    }
  }

  implicit final val pathConfigDecoder: ConfigDecoder[String, Path] =
    ConfigDecoder[String]
      .mapOption("Path")(str => try Paths.get(str).some catch { case _: InvalidPathException => None })
  object Sqlite {
    final val databaseProp = "ffishlogs.sqlite.database"
    val config: ConfigValue[IO, Sqlite] = for {
      database <- prop(databaseProp).or(env("FFISHLOGS_SQLITE_DATABASE")).as[Path]
    } yield Sqlite(database)
  }

  val config: ConfigValue[IO, Config] = (HttpServer.config, Sqlite.config).mapN(Config.apply)
  
}
