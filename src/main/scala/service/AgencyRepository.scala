package service

/**
  * Created by ASRagab on 2/23/16.
  */
object AgencyRepository extends Repository {
  import t._
  import dtl._

  implicit val tableQuery = Agency
  type R = AgencyRow
  type S = Agency
  type A = AgencyDTO


}

object PlatformRepository extends Repository {
  import t._
  import dtl._

  implicit val tableQuery = Platform
  type R = PlatformRow
  type S = Platform
  type A = PlatformDTO
}

object MediaUsageRepository extends Repository {
  import t._
  import dtl._
  import driver.api._

  implicit val tableQuery = SocialMediaUsageSamples
  type R = SocialMediaUsageSamplesRow
  type S = SocialMediaUsageSamples
  type A = MediaUsageDTO

  def getByAgency(agency: Int): Option[Seq[MediaUsageDTO]] = {
    val query = tableQuery.filter(_.agencyid === agency).result

    resultMapper(query) match {
      case list: Seq[A] if list.nonEmpty => Some(list)
      case _ => None
    }
  }
}