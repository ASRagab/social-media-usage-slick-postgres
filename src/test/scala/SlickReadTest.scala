
import java.sql.Date

import dataaccess.dto.DataTransferLayer
import dataaccess.schema.DAO
import org.scalatest.{FunSpecLike, Matchers}
import slick.driver.PostgresDriver.api._
import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by ASRagab on 2/6/16.
  */
class SlickReadTest extends FunSpecLike with Matchers {

  private val db = Database.forConfig("social_media_usage_postgres")
  private val dtl = new DataTransferLayer(slick.driver.PostgresDriver)
  private val t = dtl.Tables

  import dtl._
  import t._
  import MapperImplicits._

  describe("Slick Read Tests") {
    describe("Agency Read Tests") {
      val agencies = Agency

      it("should select all agencies") {
        val action = agencies.result
        val list = resultMapper[AgencyDTO, AgencyRow, DBIO[Seq[AgencyRow]]](action)
        list.length should equal(75)
      }

      it("should select one agency") {
        val query = for {
          agency <- agencies if agency.id === 1
        } yield agency

        val action = query.result
        val list = resultMapper[AgencyDTO, AgencyRow, DBIO[Seq[AgencyRow]]](action)
        assert(list.length == 1)
        assert(list.head.id == 1)
        assert(list.head.name == "EDC")
      }
    }

    describe("Platform Read Tests") {
      val platforms = Platform

      it("should select all platforms") {
        val action = platforms.result
        val list = resultMapper[PlatformDTO, PlatformRow, DBIO[Seq[PlatformRow]]](action)
        list.length should equal(12)
      }

      it("should select one platform") {
        val action = platforms.filter(_.id === 1).result
        val list = resultMapper[PlatformDTO, PlatformRow, DBIO[Seq[PlatformRow]]](action)

        assert(list.length == 1)
        assert(list.head.id == 1)
        assert(list.head.name == "Foursquare")
      }
    }

    describe("ViewSmusMaxActionsOnDate Read Tests") {
      val maxMediaUsages = ViewSmusMaxActionsOnDate

      it("should get all max action media usages") {
        val action = maxMediaUsages.result
        val list = resultMapper[MediaUsageDTO, ViewSmusMaxActionsOnDateRow, DBIO[Seq[ViewSmusMaxActionsOnDateRow]]](action)

        list.length should equal(74)
      }

      it("should select one max action media usage") {
        val action = maxMediaUsages.filter(_.name === "Mayor's Office").result
        val list = resultMapper[MediaUsageDTO, ViewSmusMaxActionsOnDateRow, DBIO[Seq[ViewSmusMaxActionsOnDateRow]]](action)

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
        val list = resultMapper[MediaUsageDTO, SocialMediaUsageSamplesRow, DBIO[Seq[SocialMediaUsageSamplesRow]]](action)

        list.length should equal(3777)
      }

      it("should get one social media usage") {
        val action = mediaUsage.filter(_.id === 1).result
        val list = resultMapper[MediaUsageDTO, SocialMediaUsageSamplesRow, DBIO[Seq[SocialMediaUsageSamplesRow]]](action)

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

  def resultMapper[A <: BaseDTO, B <: DAO, S <: DBIO[Seq[B]]](action: S)(implicit rowMapper: B => A): Seq[A] = {
    val result = exec(action)
    result.map(rowMapper)
  }

  def exec[T](action: DBIO[T]): T = Await.result(db.run(action), 4 seconds)
}
