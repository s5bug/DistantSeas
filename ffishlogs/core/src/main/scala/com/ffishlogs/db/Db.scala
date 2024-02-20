package com.ffishlogs.db

import cats.effect.{IO, Resource}
import cats.syntax.all._
import porcupine.{Database, Query}

trait Db {
  
}

object Db {
  
  private final case class OfDb(statements: StatementsMany[porcupine.Statement[IO, *, *]]) extends Db {
    
  }
  
  def ofDatabase(internal: Database[IO]): Resource[IO, Db] = {
    val executeStatementsOnce: IO[Unit] = StatementsOnce.query.execute(internal.execute)
    val prepare = [A, B] => (q: Query[A, B]) => internal.prepare(q)
    val prepareStatementsMany = StatementsMany.query.traverseK(prepare)
    Resource.eval(executeStatementsOnce) >> prepareStatementsMany.map(OfDb.apply)
  }

}
