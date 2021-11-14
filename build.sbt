scalaVersion := "2.13.3"

name := "soda"
ThisBuild / organization := "io.github.d-exclaimation"
ThisBuild / version := "0.5.0"
ThisBuild / organizationHomepage := Some(url("https://www.dexclaimation.com"))
ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/d-exclaimation/soda"),
    "scm:git@github.d-exclaimation/soda.git"
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

ThisBuild / description := "Enchant your experience of GraphQL with some soda"
ThisBuild / licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))
ThisBuild / homepage := Some(url("https://github.com/d-exclaimation/soda"))

ThisBuild / pomIncludeRepository := { _ => false }

ThisBuild / publishTo := {
  val nexus = "https://s01.oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

ThisBuild / publishMavenStyle := true

ThisBuild / versionScheme := Some("early-semver")

libraryDependencies ++= {
  val sangriaVer = "2.1.5"
  Seq(
    "org.sangria-graphql" %% "sangria" % sangriaVer,
    "org.scalatest" %% "scalatest" % "3.2.9" % Test,
  )
}
