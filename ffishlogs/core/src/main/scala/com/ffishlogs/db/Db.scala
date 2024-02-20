package com.ffishlogs.db

import cats.effect.{IO, Resource}
import porcupine.{Database, Query}

trait Db {
  
  def createUsersTable: IO[Unit]
  
  def createVoyagesTable: IO[Unit]
  def createVoyagesIndex: IO[Unit]
  
  def createEventsTable: IO[Unit]
  def createEventsIndex: IO[Unit]
  
}

object Db {
  
  private final case class OfDb(statements: Statements[porcupine.Statement[IO, *, *]]) extends Db {

    override def createUsersTable: IO[Unit] =
      statements.createUsersTable.execute

    override def createVoyagesTable: IO[Unit] =
      statements.createVoyagesTable.execute
    override def createVoyagesIndex: IO[Unit] =
      statements.createVoyagesIndex.execute

    override def createEventsTable: IO[Unit] =
      statements.createEventsTable.execute
    override def createEventsIndex: IO[Unit] =
      statements.createEventsIndex.execute
    
  }
  
  def ofDatabase(internal: Database[IO]): Resource[IO, Db] = {
    val prepare = [A, B] => (q: Query[A, B]) => internal.prepare(q)
    val preparedStatements = Statements.query.traverseK(prepare)
    preparedStatements.map(OfDb.apply)
  }

}
