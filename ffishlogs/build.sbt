// https://typelevel.org/sbt-typelevel/faq.html#what-is-a-base-version-anyway
ThisBuild / tlBaseVersion := "0.0" // your current series x.y

ThisBuild / organization := "com.ffishlogs"
ThisBuild / organizationName := "ffishlogs"
ThisBuild / startYear := Some(2024)
ThisBuild / developers := List(
  // your GitHub handle and name
  tlGitHubDev("NotNite", "NotNite"),
  tlGitHubDev("s5bug", "Aly")
)

val Scala331 = "3.3.1"
ThisBuild / crossScalaVersions := Seq(Scala331)
ThisBuild / scalaVersion := Scala331 // the default Scala
ThisBuild / tlJdkRelease := Some(21)
ThisBuild / scalacOptions ++= Seq(
  "-no-indent", "-old-syntax",
  "-Wvalue-discard",
  "-Wnonunit-statement",
)
ThisBuild / libraryDependencies += compilerPlugin("com.github.ghik" % "zerowaste" % "0.2.16" cross CrossVersion.full)

lazy val root = tlCrossRootProject.aggregate(core)

lazy val core = project
  .in(file("core"))
  .settings(
    name := "ffishlogs",
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core" % "2.10.0",
      "org.typelevel" %% "cats-effect" % "3.5.3",
      "co.fs2" %% "fs2-core" % "3.9.4",
      "co.fs2" %% "fs2-io" % "3.9.4",
      "is.cir" %% "ciris" % "3.5.0",
      "is.cir" %% "ciris-http4s" % "3.5.0" intransitive(),
      "org.typelevel" %% "log4cats-core" % "2.6.0", 
      "org.typelevel" %% "log4cats-slf4j" % "2.6.0",
      "org.slf4j" % "slf4j-api" % "2.0.10",
      "org.slf4j" % "slf4j-jdk14" % "2.0.10",
      "org.http4s" %% "http4s-ember-server" % "1.0.0-M40",
      "org.http4s" %% "http4s-dsl" % "1.0.0-M40",
      "org.xerial" % "sqlite-jdbc" % "3.45.1.0",
      "com.armanbilge" %%% "porcupine" % "0.0.1",
    ),
    mainClass := Some("com.ffishlogs.Main"),
    nativeImageJvm := "graalvm-java21",
    nativeImageVersion := "21.0.2",
    nativeImageOptions ++= Seq("--no-fallback", "--initialize-at-build-time"),
  )
  .enablePlugins(NativeImagePlugin)
