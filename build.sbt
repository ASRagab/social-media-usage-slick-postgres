name := "slick-codegen-postgres"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.1.1",
  "org.postgresql" % "postgresql" % "9.3-1100-jdbc41",
  "ch.qos.logback" % "logback-classic" % "1.1.2",
  "com.typesafe.slick" %% "slick-codegen" % "3.1.1"
)

//slick <<= slickCodeGenTask // register manual sbt command
////sourceGenerators in Compile <+= slickCodeGenTask // register automatic code generation on every compile, remove for only manual use
//
//
//// code generation task
//lazy val slick = TaskKey[Seq[File]]("gen-tables")
//lazy val slickCodeGenTask = (sourceManaged, dependencyClasspath in Compile, runner in Compile, streams) map { (dir, cp, r, s) =>
//  val outputDir = (dir / "slick").getPath // place generated files in sbt's managed sources folder
//val url = "jdbc:postgresql://localhost:5432/mydb" // connection info
//val jdbcDriver = "org.postgresql.Driver"
//  val slickDriver = "slick.driver.PostgresDriver"
//  val pkg = "dao"
//  val user = "codegen"
//  val pass = "trustno1"
//  toError(r.run("slick.codegen.SourceCodeGenerator", cp.files, Array(slickDriver, jdbcDriver, url, outputDir, pkg, user, pass), s.log))
//  val fname = outputDir + "/dao/Tables.scala"
//  Seq(file(fname))
//}