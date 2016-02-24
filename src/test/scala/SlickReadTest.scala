import java.sql.Date
import dataaccess.dto.DataTransferLayer
import org.scalatest.{FunSpecLike, Matchers}
import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by ASRagab on 2/6/16.
  */
case class chooseDBConfig(useH2: Boolean) {
  def getConfig() = if (useH2) ("social_media_usage_h2", slick.driver.H2Driver) else ("social_media_usage_postgres", slick.driver.PostgresDriver)
}

class SlickReadTest extends FunSpecLike with Matchers {

  val useH2 = false;
  val (dbConfig, driver) = chooseDBConfig(useH2).getConfig

  import driver.api._

  private val db = Database.forConfig(dbConfig)
  private val dtl = new DataTransferLayer(driver)
  private val t = dtl.Tables

  import dtl._
  import t._
  import MapperImplicits._

  if (useH2) exec(schema)
  describe("Slick Read Tests") {
    describe("Agency Read Tests") {
      val agencies = Agency

      it("should select all agencies") {
        val action = agencies.result
        val list = resultMapper[AgencyDTO, AgencyRow](action)
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
        val list = resultMapper[AgencyDTO, AgencyRow](action)
        assert(list.length == 1)
        assert(list.head.id == 1)
        assert(list.head.name == "EDC")
      }
    }

    describe("Platform Read Tests") {
      val platforms = Platform

      it("should select all platforms") {
        val action = platforms.result
        val list = resultMapper[PlatformDTO, PlatformRow](action)
        list.length should equal(12)
      }

      it("should select one platform") {
        val action = platforms.filter(_.id === 1).result


        val list = resultMapper[PlatformDTO, PlatformRow](action)

        assert(list.length == 1)
        assert(list.head.id == 1)
        assert(list.head.name == "Foursquare")
      }
    }

    describe("ViewSmusMaxActionsOnDate Read Tests") {
      val maxMediaUsages = ViewSmusMaxActionsOnDate

      it("should get all max action media usages") {
        val action = maxMediaUsages.result
        val list = resultMapper[MediaUsageDTO, ViewSmusMaxActionsOnDateRow](action)

        list.length should equal(74)
      }

      it("should select one max action media usage") {
        val action = maxMediaUsages.filter(_.name === "Mayor's Office").result
        val list = resultMapper[MediaUsageDTO, ViewSmusMaxActionsOnDateRow](action)

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
        val list = resultMapper[MediaUsageDTO, SocialMediaUsageSamplesRow](action)

        list.length should equal(3777)
      }

      it("should get one social media usage") {
        val action = mediaUsage.filter(_.id === 1).result
        val list = resultMapper[MediaUsageDTO, SocialMediaUsageSamplesRow](action)

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

    describe("More complex queries") {
      //      val mediaUsage = SocialMediaUsageSamples
      //      val staging = StagingNycSocialMediaUsage
      //      val agencies = Agency
      //      val platform = Platform
      //      it("should run") {
      //
      ////        val q = for {
      ////          usageSample <- mediaUsage if usageSample.sampleDate === staging.map(_.sampleDate).max
      ////        } yield usageSample
      //
      //        val q = mediaUsage join staging on {
      //          case (usage, row) => usage.sampleDate === staging.map(_.sampleDate).max
      //        }
      //
      //        val action = q.distinct.result
      //        val results = exec(action)
      //        results.size should be > 10
      //      }
    }
  }

  def resultMapper[A, B](action: DBIO[Seq[B]])(implicit rowMapper: B => A): Seq[A] = {
    val result = exec(action)
    result.map(rowMapper)
  }

  def exec[T](action: DBIO[T]): T = Await.result(db.run(action), 30 seconds)
}
