import dataaccess.dto.BaseDTO
import dataaccess.schema.DAO
import org.scalatest.FunSpecLike
import slick.driver.H2Driver.api._

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by ASRagab on 2/13/16.
  */
class SlickCRUDTest extends FunSpecLike {
  private val db = Database.forConfig("social_media_usage_h2")



  def resultMapper[A <: BaseDTO, B <: DAO, S <: DBIO[Seq[B]]](action: S)(implicit rowMapper: B => A): Seq[A] = {
    val result = exec(action)
    result.map(rowMapper)
  }

  def exec[T](action: DBIO[T]): T = Await.result(db.run(action), 4 seconds)
}
