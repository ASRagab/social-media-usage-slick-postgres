import org.scalatest.{BeforeAndAfterAll, FunSpecLike}
import service.RepositoryDBConfig

class SlickCRUDTest extends FunSpecLike with RepositoryDBConfig with BeforeAndAfterAll {

  override def useH2 = true
  import driver.api._
  import dtl._
  import t._
  import MapperImplicits._

  override def beforeAll = {
    if(useH2) exec(schema)
  }

  override def afterAll = {
    if(useH2) exec(schemaDrop)
  }

  describe("Slick CRUD Test") {
    describe("Agency CRUD Tests") {
      val agencies = Agency

      describe("Agency Insert Test") {
        it("should insert some rows") {
          val rows: Seq[AgencyRow] = requestMapper(Seq(AgencyDTO(101, "Dept. of Ballers"), AgencyDTO(102, "Sanitation Now, Sanitation Forever"), AgencyDTO(103, "Shimmer")))
          val action = agencies ++= rows
          exec(action)
        }
      }
    }
  }
}
