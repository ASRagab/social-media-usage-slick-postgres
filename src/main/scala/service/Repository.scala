package service

import dataaccess.dto.{BaseDTO, DataTransferLayer}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

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

  def exec[T](action: DBIO[T]): Future[T] = db.run(action)

  def resultMapper[A, B](action: DBIO[Seq[B]])(implicit rowMapper: B => A): Future[Seq[A]] = {
    val result = exec(action).flatMap(r => Future {
      r.map(rowMapper)
    })
    result
  }

  def requestMapper[A, B](dtos: Seq[A])(implicit dtoMapper: A => B): Seq[B] = {
    dtos.map(dtoMapper)
  }
}

trait Repository extends RepositoryDBConfig {
  import driver.api._
  val tableQuery: TableQuery[S]

  def getByName(name: String): Future[Seq[A]]
  def getByID(id: Int): Future[Seq[A]]
  def getAll: Future[Seq[A]]
}



