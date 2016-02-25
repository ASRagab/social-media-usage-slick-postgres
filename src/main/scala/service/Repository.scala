package service

import dataaccess.dto.{BaseDTO, DataTransferLayer}
import scala.concurrent.Await
import scala.concurrent.duration._

case class chooseDBConfig(useH2: Boolean) {
  val getConfig = if (useH2) ("social_media_usage_h2", slick.driver.H2Driver) else ("social_media_usage_postgres", slick.driver.PostgresDriver)
}

trait BaseRepository {
  val (dbConfig, driver) = chooseDBConfig(useH2 = false).getConfig

  import driver.api._

  protected val dtl = new DataTransferLayer(driver)
  protected val db = Database.forConfig(dbConfig)
  protected val t = dtl.Tables

  def exec[T](action: DBIO[T]): T = Await.result(db.run(action), 30 seconds)

  def resultMapper[A, B](action: DBIO[Seq[B]])(implicit rowMapper: B => A): Seq[A] = {
    val result = exec(action)
    result.map(rowMapper)
  }

  class Repository[R <: t.DAO, S <: t.GenericDataTable[R], A <: BaseDTO](tableQuery: TableQuery[S]) {

    def getByName(name: String): Option[A] = {
      val query = tableQuery.filter(_.name === name).result

      resultMapper(query) match {
        case list: Seq[A] => Some(list.head)
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
        case list: Seq[A] => Some(list)
        case _ => None
      }
    }
  }
}


