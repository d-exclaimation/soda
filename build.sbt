scalaVersion := "2.13.3"

ThisBuild / name := "graphql-soda"
ThisBuild / organization := "io.github.d-exclaimation"
ThisBuild / version := "0.4.0"
ThisBuild / organizationHomepage := Some(url("https://www.dexclaimation.com"))
ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/d-exclaimation/graphql-soda"),
    "scm:git@github.d-exclaimation/graphql-soda.git"
  )
)

ThisBuild / developers := List(
  Developer(
    id = "d-exclaimation",
    name = "Vincent",
    email = "thisoneis4business@gmail.com",
    url = url("https://www.dexclaimation.com")
  )
)

crossPaths := false

ThisBuild / description := "Enchant your experience of graphql with some soda"
ThisBuild / licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))
ThisBuild / homepage := Some(url("https://github.com/d-exclaimation/graphql-soda"))

// Remove all additional repository other than Maven Central from POM
ThisBuild / pomIncludeRepository := { _ => false }

ThisBuild / publishTo := {
  val nexus = "https://s01.oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

ThisBuild / publishMavenStyle := true

ThisBuild / versionScheme := Some("early-semver")

libraryDependencies ++= {
  val sangriaVer = "2.1.3"
  Seq(
    "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2",
    "org.sangria-graphql" %% "sangria" % sangriaVer
  )
}
