name := "slick-codegen-postgres"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.1.1",
  "org.postgresql" % "postgresql" % "9.3-1100-jdbc41",
  "com.h2database" % "h2" % "1.4.186",
  "ch.qos.logback" % "logback-classic" % "1.1.2",
  "com.typesafe.slick" %% "slick-codegen" % "3.1.1",
  "org.scalactic" %% "scalactic" % "2.2.6",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test"
)

scalacOptions ++= Seq("-feature", "-deprecation", "-unchecked", "-language:postfixOps")
