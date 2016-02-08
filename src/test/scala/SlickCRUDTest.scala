
import java.sql.Date
import dataaccess.dao._
import dataaccess.schema.{DatabaseAccessObject, Tables}
import org.scalatest.{Matchers, FunSpecLike}
import AgencyDAO.AgencyDAODomainObject
import PlatformDAO.PlatformDAODomainObject
import MediaUsageDAO.MediaUsageDAODomainObject
import Tables._
import slick.lifted.AbstractTable
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
      val agencies = Agency

      it("should select all agencies") {
        val list = mapper[AgencyDAO, AgencyRow, TableQuery[Agency], Agency](agencies, AgencyDAODomainObject.convertFromRow)
        list.length should equal(75)
      }

      it("should select one agency") {
        val action = agencies.filter(_.id === 1).result
        val result = exec(action)

        val list = result.map(AgencyDAODomainObject.convertFromRow)
        assert(list.length == 1)
        assert(list.head.id == 1)
        assert(list.head.name == "EDC")
      }
    }

    describe("Platform CRUD Tests") {
      val platforms = Platform

      it("should select all platforms") {
        val platformAction = platforms.result
        val result = exec(platformAction)
        val list = result.map(PlatformDAODomainObject.convertFromRow)

        list.length should equal(12)
      }

      it("should select one platform") {
        val action = platforms.filter(_.id === 1).result
        val result = exec(action)
        val list = result.map(PlatformDAODomainObject.convertFromRow)

        assert(list.length == 1)
        assert(list.head.id == 1)
        assert(list.head.name == "Foursquare")
      }
    }

    describe("ViewSmusMaxActionsOnDate CRUD Tests") {
      val maxMediaUsages = ViewSmusMaxActionsOnDate

      it("should get all max action media usages") {
        val result = exec(maxMediaUsages.result)
        val list = result.map(MediaUsageDAODomainObject.convertFromViewRow)
        list.length should equal(74)
      }

      it("should select one max action media usage") {
        val action = maxMediaUsages.filter(_.name === "Mayor's Office").result
        val result = exec(action)
        val list = result.map(MediaUsageDAODomainObject.convertFromViewRow)

        assert(list.length == 1)
        assert(list.head.agency != null)
        assert(list.head.agency.name == "Mayor's Office")
        assert(list.head.actions == 126661)
      }
    }

    describe("SocialMediaUsageSample CRUD Tests") {
      val mediaUsage = SocialMediaUsageSamples

      it("should get all social media usages") {

        val mediaUsageAction = mediaUsage.result

        val result = exec(mediaUsageAction)
        val list = result.map(MediaUsageDAODomainObject.convertFromRow)
        list.length should equal(3777)
      }

      it("should get one social media usage") {
        val action = mediaUsage.filter(_.id === 1).result
        val result = exec(action)
        val list = result.map(MediaUsageDAODomainObject.convertFromRow)

        list.length should equal(1)
        val usage = list.head
        usage shouldNot be(null)

        usage.agency shouldNot be(null)
        usage.agency.id should equal(58)

        usage.platform shouldNot be(null)
        usage.platform.id should equal(3)

        usage.actions should equal(505)

        val dateString: String = "2012-12-12"
        val date: Date = Date.valueOf(dateString)
        usage.sampleDate should equal(date)

        usage.url shouldNot be(null)
      }
    }
  }
  def mapper[A <: BaseDAO, B <: DatabaseAccessObject, T <: TableQuery[E], E <: AbstractTable[_]](query: T, f: B => A) : Seq[A] = {
    val result = exec(query.result)
    result.map(o => f(o.asInstanceOf[B]))
  }

  def exec[T](action: DBIO[T]): T = Await.result(db.run(action), 4 seconds)
}
