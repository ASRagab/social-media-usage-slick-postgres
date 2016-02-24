package dataaccess.schema

import slick.codegen.SourceCodeGenerator
import slick.driver.PostgresDriver
import slick.driver.PostgresDriver.api._
import slick.jdbc.meta._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

/**
  * Created by ASRagab on 2/6/16.
  */
object CreateSchema extends App {
    val db = Database.forConfig("social_media_usage_postgres")
    val tablesAndViews = MTable.getTables(None, None, None, Some(Seq("TABLE", "VIEW")))
    val modelAction = PostgresDriver.createModel(Some(tablesAndViews))
    val modelFuture = db.run(modelAction)

    // customize code generator
    val codegenFuture = modelFuture.map(model => new SourceCodeGenerator(model))

    val path = getClass.getResource("").getPath

    Await.ready(
      codegenFuture.map(_.writeToFile("slick.driver.PostgresDriver", path, "dal", "Tables", "Tables.scala")), 20 seconds)
}
