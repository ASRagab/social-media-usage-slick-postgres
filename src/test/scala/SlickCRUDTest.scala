import org.scalatest.{Matchers, FunSpecLike}
import slick.dao.AgencyDAO
import slick.dao.AgencyDAO.AgencyDAODomainObject
import slick.dao.PlatformDAO.PlatformDAODomainObject
import slick.dao.MediaUsageDAO.MediaUsageDAODomainObject
import slick.lifted.TableQuery
import slick.schema.Tables._
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

        val list: Seq[AgencyDAO] = result.map(o => AgencyDAODomainObject.convertFromRow(o))
        list.length should equal(75)


      }
    }

    describe("Platform CRUD Tests") {
      it("should select all platforms") {
        val platforms = Platform
        val platformAction = platforms.result

        val result = exec(platformAction)
        val list = result.map(o => PlatformDAODomainObject.convertFromRow(o))
        list.length should equal(12)
      }
    }

    describe("ViewSmusMaxActionsOnDate CRUD Tests") {
      it("should get all max action media usages") {
        val maxMediaUsages = ViewSmusMaxActionsOnDate
        val maxMediaUsagesAction = maxMediaUsages.result

        val result = exec(maxMediaUsagesAction)
        val list = result.map(o => MediaUsageDAODomainObject.convertFromViewRow(o))
        list.length should equal(74)
      }
    }

    describe("SocialMediaUsageSample CRUD Tests") {
      it("should get all social media usages") {
        val mediaUsage = SocialMediaUsageSamples
        val mediaUsageAction = mediaUsage.result

        val result = exec(mediaUsageAction)
        val list = result.map(o => MediaUsageDAODomainObject.convertFromRow(o))
        list.length should equal(3777)
      }
    }
  }

  def exec[T](action: DBIO[T]): T = Await.result(db.run(action), 4 seconds)
}
