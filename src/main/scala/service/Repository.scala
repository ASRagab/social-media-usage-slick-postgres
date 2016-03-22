package service

import dataaccess.dto.{BaseDTO, DataTransferLayer}
import scala.concurrent.Await
import scala.concurrent.duration._


trait RepositoryDBConfig {

  type R <: t.DAO
  type S <: t.GenericDataTable[R]
  type A <: BaseDTO

  def useH2: Boolean = false

  lazy val (config, driver) = {
    if (useH2)
    ("social_media_usage_h2", slick.driver.H2Driver)
    else
    ("social_media_usage_postgres", slick.driver.PostgresDriver)
  }

  import driver.api._

  protected val dtl = new DataTransferLayer(driver)
  protected val db = Database.forConfig(config)
  protected val t = dtl.Tables

  def exec[T](action: DBIO[T]): T = Await.result(db.run(action), 30 seconds)

  def resultMapper[A, B](action: DBIO[Seq[B]])(implicit rowMapper: B => A): Seq[A] = {
    val result = exec(action)
    result.map(rowMapper)
  }

  def requestMapper[A, B](dtos: Seq[A])(implicit dtoMapper: A => B): Seq[B] = {
    dtos.map(dtoMapper)
  }
}

abstract class Repository extends RepositoryDBConfig {

  import driver.api._
  val tableQuery: TableQuery[S]

  def getByName(name: String): Option[A] = {
    val query = tableQuery.filter(_.name === name).result

    resultMapper(query) match {
      case list: Seq[A] if list.nonEmpty => Some(list.head)
      case _ => None
    }
  }

  def getByID(id: Int): Option[A] = {
    val query = tableQuery.filter(_.id === id).result

    resultMapper(query) match {
      case list: Seq[A] if list.nonEmpty => Some(list.head)
      case _ => None
    }
  }

  def getAll: Option[Seq[A]] = {
    val query = tableQuery.result

    resultMapper(query) match {
      case list: Seq[A] if list.nonEmpty => Some(list)
      case _ => None
    }
  }
}



