package com.ffishlogs.db

import cats.Applicative
import cats.syntax.all._
import porcupine.{Query, sql}

final case class StatementsOnce[F[_, _]](
  createUsersTable: F[Unit, Unit],
  createVoyagesTable: F[Unit, Unit],
  createVoyagesIndex: F[Unit, Unit],
  createEventsTable: F[Unit, Unit],
  createEventsIndex: F[Unit, Unit]
) {
  
  def execute[M[_]](execution: F[Unit, Unit] => M[Unit])(using app: Applicative[M]): M[Unit] =
    Vector(
      createUsersTable,
      createVoyagesTable,
      createVoyagesIndex,
      createEventsTable,
      createEventsIndex
    ).traverse_(execution)
  
}

object StatementsOnce {

  final val query: StatementsOnce[Query] = StatementsOnce(
    createUsersTable =
      sql"""CREATE TABLE IF NOT EXISTS users (
           |  discordId INTEGER PRIMARY KEY,
           |  discordAccessToken TEXT NOT NULL,
           |  discordRefreshToken TEXT NOT NULL,
           |  discordRefreshInstant TEXT NOT NULL,
           |  -- 0: Display records using the XIV character in lodestoneId
           |  -- 1: Display records using Discord username (retrieved via OAuth)
           |  -- 2: Display records with an [adjective bnpcname] pseudo-anonymization
           |  nameDisplayEnum INTEGER NOT NULL,
           |  CHECK ( nameDisplayEnum BETWEEN 0 AND 2 )
           |)
           |""".stripMargin.command,
    createVoyagesTable =
      sql"""CREATE TABLE IF NOT EXISTS voyages (
           |  voyageId INTEGER PRIMARY KEY AUTOINCREMENT,
           |  ownerId INTEGER NOT NULL,
           |  logVersion INTEGER NOT NULL,
           |  distantSeasVersion TEXT NOT NULL,
           |  timestampInstant TEXT NOT NULL
           |)
           |""".stripMargin.command,
    createVoyagesIndex =
      sql"""CREATE UNIQUE INDEX IF NOT EXISTS ownerToVoyages ON voyages (ownerId, voyageId)""".command,
    createEventsTable =
      sql"""CREATE TABLE IF NOT EXISTS events (
           |  eventId INTEGER PRIMARY KEY AUTOINCREMENT,
           |  voyageId INTEGER NOT NULL,
           |  timestampInstant TEXT NOT NULL,
           |  -- 0: EnterBoat
           |  -- 1: ExitBoat
           |  -- 2: ZoneChanged
           |  -- 3: SpectralStarted
           |  -- 4: SpectralEnded
           |  -- 5: PointsUpdate
           |  -- 6: TotalPointsUpdate
           |  -- 7: FishCatch
           |  -- 8: MissionUpdate
           |  eventTypeEnum INTEGER NOT NULL,
           |  -- EnterBoat#route
           |  -- ZoneChanged#zone
           |  -- PointsUpdate#points
           |  -- TotalPointsUpdate#totalPoints
           |  -- FishCatch#item
           |  route INTEGER NULL,
           |  -- EnterBoat#missions[0].row
           |  -- ZoneChanged#time
           |  -- FishCatch#large
           |  -- MissionUpdate#state.row
           |  mission1Row INTEGER NULL,
           |  -- EnterBoat#missions[0].objective
           |  -- MissionUpdate#state.objective
           |  mission1Objective TEXT NULL,
           |  -- EnterBoat#missions[0].progress
           |  -- ZoneChanged#weather
           |  -- FishCatch#points
           |  -- MissionUpdate#state.progress
           |  mission1Progress INTEGER NULL,
           |  -- EnterBoat#missions[0].total
           |  -- MissionUpdate#state.total
           |  mission1Total INTEGER NULL,
           |  -- Remaining values only exist in EnterBoat
           |  mission2Row INTEGER NULL,
           |  mission2Objective TEXT NULL,
           |  mission2Progress INTEGER NULL,
           |  mission2Total INTEGER NULL,
           |  mission3Row INTEGER NULL,
           |  mission3Objective TEXT NULL,
           |  mission3Progress INTEGER NULL,
           |  mission3Total INTEGER NULL,
           |  
           |  -- eventTypeEnum
           |  CHECK ( eventTypeEnum BETWEEN 0 AND 8 ),
           |  
           |  -- route
           |  CHECK ( (eventTypeEnum IN (0, 2, 5, 6, 7)) <> (route IS NULL) ),
           |  
           |  -- mission1Row
           |  CHECK ( (eventTypeEnum IN (0, 2, 7, 8)) <> (mission1Row IS NULL) ),
           |  CHECK ( (eventTypeEnum IS NOT 2) OR (mission1Row BETWEEN 0 AND 3) ),
           |  CHECK ( (eventTypeEnum IS NOT 7) OR (mission1Row BETWEEN 0 AND 1) ),
           |  
           |  -- mission1Objective
           |  CHECK ( (eventTypeEnum IN (0, 8)) <> (mission1Objective IS NULL) ),
           |  
           |  -- mission1Progress
           |  CHECK ( (eventTypeEnum IN (0, 2, 7, 8)) <> (mission1Progress IS NULL) ),
           |  CHECK ( (eventTypeEnum IS NOT 2) OR (mission1Progress BETWEEN 0 AND 16 OR mission1Progress IS 145) ),
           |  
           |  -- mission1Total
           |  CHECK ( (eventTypeEnum IN (0, 8)) <> (mission1Total IS NULL) ),
           |  
           |  -- EnterBoat
           |  CHECK ( (eventTypeEnum IS NOT 0) <> (NULL NOT IN (
           |    mission2Row,
           |    mission2Objective,
           |    mission2Progress,
           |    mission2Total,
           |    mission3Row,
           |    mission3Objective,
           |    mission3Progress,
           |    mission3Total
           |  )) ),
           |  CHECK ( (eventTypeEnum IS 0) <> ((
           |    mission2Row,
           |    mission2Objective,
           |    mission2Progress,
           |    mission2Total,
           |    mission3Row,
           |    mission3Objective,
           |    mission3Progress,
           |    mission3Total
           |  ) IS (NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL)) )
           |)
           |""".stripMargin.command,
    createEventsIndex =
      sql"""CREATE UNIQUE INDEX IF NOT EXISTS voyagesToEvents ON events (voyageId, eventId)""".command
  )
  
}
