package service
import dataaccess.dto.{BaseDTO, DataTransferLayer}
import dataaccess.schema.DAO
import slick.dbio.DBIO
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

case class chooseDBConfig(useH2: Boolean) {
  def getConfig() = if(useH2) ("social_media_usage_h2", slick.driver.H2Driver) else ("social_media_usage_postgres", slick.driver.PostgresDriver)
}

trait BaseDataService {
  val useH2 = false
  val (dbConfig, driver) = chooseDBConfig(useH2).getConfig

  import driver.api._

  protected val dtl = new DataTransferLayer(driver)
  protected val db = Database.forConfig(dbConfig)
  protected val t = dtl.Tables
}

trait DataService extends BaseDataService {
  type dto <: BaseDTO
  type dao <: DAO

  def getByName(name: String): Option[dto]
  def getByID(id: Int): Option[dto]
  def getAll(): Option[Seq[dto]]

  def exec[T](action: DBIO[T]): T = Await.result(db.run(action), 30 seconds)

  def resultMapper[A, B](action: DBIO[Seq[B]])(implicit rowMapper: B => A): Seq[A] = {
    val result = exec(action)
    result.map(rowMapper)
  }
}
