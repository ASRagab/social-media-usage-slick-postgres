import dataaccess.dto._
import scala.concurrent.Await
import scala.concurrent.duration._

import org.scalatest.FunSpecLike
import slick.driver.H2Driver.api._

class SlickCRUDTest extends FunSpecLike {

  private val dtl = new DataTransferLayer(slick.driver.H2Driver)
  private val t = dtl.Tables
  private val db = Database.forConfig("social_media_usage_h2")

  import dtl._
  import t._
  import MapperImplicits._

  describe("Slick CRUD Test") {
    describe("Agency CRUD Tests") {
      val agencies = Agency
      exec(schema)

      describe("Agency Insert Test") {
        it("should insert some rows") {
          val rows: Seq[AgencyRow] = daoMapper(Seq(AgencyDTO(101, "Dept. of Ballers"), AgencyDTO(102, "Sanitation Now, Sanitation Forever"), AgencyDTO(103, "Shimmer")))
          val action = agencies ++= rows
          exec(action)
        }
      }
    }
  }

  def dtoMapper[A, B](action: DBIO[Seq[B]])(implicit rowMapper: B => A): Seq[A] = {
    val result = exec(action)
    result.map(rowMapper)
  }

  def daoMapper[A, B](dtos: Seq[A])(implicit dtoMapper: A => B): Seq[B] = {
    dtos.map(dtoMapper)
  }

  def exec[T](action: DBIO[T]): T = Await.result(db.run(action), 4 seconds)
}
