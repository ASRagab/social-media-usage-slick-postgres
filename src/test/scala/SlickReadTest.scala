import java.sql.Date

import org.scalatest.{BeforeAndAfterAll, FunSpecLike, Matchers}
import service.RepositoryDBConfig
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class SlickReadTest extends FunSpecLike with Matchers with RepositoryDBConfig with BeforeAndAfterAll {

  override def useH2 = false
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

  def neverBlockExcept[A](results: Future[Seq[A]]): Seq[A] = {
    val r = Await.result(results, 20 seconds)
    r
  }

  describe("Slick Read Tests") {
    describe("Agency Read Tests") {
      val agencies = Agency

      it("should select all agencies") {
        val action = agencies.result
        val list = neverBlockExcept(resultMapper[AgencyDTO, AgencyRow](action))
        list.length should equal(75)
      }

      it("should select one agency") {
        def byName(name: Rep[String]) = {
          val q = agencies.filter(a => a.name like name).take(2)
          Compiled(q)
        }

        val query = for {
          agency <- agencies if agency.id === 1
        } yield agency

        val action = query.result
        val list = neverBlockExcept(resultMapper[AgencyDTO, AgencyRow](action))
        assert(list.length == 1)
        assert(list.head.id == 1)
        assert(list.head.name == "EDC")
      }
    }

    describe("Platform Read Tests") {
      val platforms = Platform

      it("should select all platforms") {
        val action = platforms.result
        val list = neverBlockExcept(resultMapper[PlatformDTO, PlatformRow](action))
        list.length should equal(12)
      }

      it("should select one platform") {
        val action = platforms.filter(_.id === 1).result


        val list = neverBlockExcept(resultMapper[PlatformDTO, PlatformRow](action))

        assert(list.length == 1)
        assert(list.head.id == 1)
        assert(list.head.name == "Foursquare")
      }
    }

    describe("ViewSmusMaxActionsOnDate Read Tests") {
      val maxMediaUsages = ViewSmusMaxActionsOnDate

      it("should get all max action media usages") {
        val action = maxMediaUsages.result
        val list = neverBlockExcept(resultMapper[MediaUsageDTO, ViewSmusMaxActionsOnDateRow](action))

        list.length should equal(74)
      }

      it("should select one max action media usage") {
        val action = maxMediaUsages.filter(_.name === "Mayor's Office").result
        val list = neverBlockExcept(resultMapper[MediaUsageDTO, ViewSmusMaxActionsOnDateRow](action))

        assert(list.length == 1)
        assert(list.head.agency != null)
        assert(list.head.agency.name == "Mayor's Office")
        assert(list.head.actions == 126661)
      }
    }

    describe("SocialMediaUsageSample Read Tests") {
      val mediaUsage = SocialMediaUsageSamples

      it("should get all social media usages") {
        val action = mediaUsage.result
        val list = neverBlockExcept(resultMapper[MediaUsageDTO, SocialMediaUsageSamplesRow](action))

        list.length should equal(3777)
      }

      it("should get one social media usage") {
        val action = mediaUsage.filter(_.id === 1).result
        val list = neverBlockExcept(resultMapper[MediaUsageDTO, SocialMediaUsageSamplesRow](action))

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
}
