import org.scalatest.{Matchers, FunSpecLike}
import slick.lifted.TableQuery
import slick.schema.Tables.Agency
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import slick.driver.PostgresDriver.api._

/**
  * Created by ASRagab on 2/6/16.
  */
class SlickCRUDTest extends FunSpecLike with Matchers {
  private val db = Database.forConfig("social_media_usage_postgres")

  describe("Slick CRUD Tests") {
    describe("Agency CRUD Tests") {
      it("should select all agencies") {
        val agencies = Agency
        val agencyAction = agencies.result

        val result = exec(agencyAction)
        result.length should equal(75)

      }
    }
  }

  def exec[T](action: DBIO[T]): T = Await.result(db.run(action), 4 seconds)
}
