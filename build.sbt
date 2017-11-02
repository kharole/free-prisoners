name := "free-prisoners"

organization := "miciek"

version := "1.0-SNAPSHOT"

scalaVersion := "2.12.4"

resolvers += Resolver.jcenterRepo

libraryDependencies ++= {
  val catsV = "1.0.0-MF"
  val akkaV = "2.5.6"
  val configV = "1.3.1"
  val scalatest = "3.0.1"
  Seq(
    "org.typelevel" %% "cats-free" % catsV,
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-remote" % akkaV,
    "com.typesafe" % "config" % configV,
    "org.scalatest" %% "scalatest" % scalatest % Test,
    "com.typesafe.akka" %% "akka-testkit" % akkaV % Test
  )
}

fork := true
connectInput in run := true
