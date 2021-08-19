scalaVersion := "2.13.3"

name := "sangria-straw"
organization := "io.github.d-exclaimation"
version := "0.1"
homepage := Some(url("https://github.com/d-exclaimation/sangria-straw"))
scmInfo := Some(
  ScmInfo(url("https://github.com/d-exclaimation/sangria-straw"), "git@github.com:d-exclaimation/sangria-straw.git")
)
developers := List(
  Developer("d-exclaimation", "d-exclaimation", "thisoneis4business@gmail.com",
    url("https://github.com/d-exclaimation")
  )
)
licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))
publishMavenStyle := true

crossPaths := false

publishTo := {
  val nexus = "https://s01.oss.sonatype.org"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

libraryDependencies ++= {
  val sangriaVer = "2.1.3"
  Seq(
    "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2",
    "org.sangria-graphql" %% "sangria" % sangriaVer
  )
}
