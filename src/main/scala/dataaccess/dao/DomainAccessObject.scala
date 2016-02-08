package dataaccess.dao

import java.sql.Date
import dataaccess.schema.{Tables, DatabaseAccessObject}
import Tables._

/**
  * Created by ASRagab on 2/7/16.
  */
package object Implicits {
  implicit class DomainObjectOps[A <: BaseDAO](a: A)(implicit ev: DomainObject[A]) {
    def name = ev.name(a)
    def id = ev.id(a)
  }
}

trait BaseDAO
trait DomainObject[A <: BaseDAO] {
  def id(a: A): Int
  def name(a: A): String
}

case class AgencyDAO(val id: Int, val name: String) extends BaseDAO
object AgencyDAO {
  implicit object AgencyDAODomainObject extends DomainObject[AgencyDAO] {
    def convertToRow(o: AgencyDAO) = AgencyRow(o.id, Some(o.name))
    def convertFromRow(o: AgencyRow) = AgencyDAO(o.id, o.name.getOrElse(""))

    override def id(a: AgencyDAO) = a.id
    override def name(a: AgencyDAO) = a.name
  }
}

case class PlatformDAO(val id: Int, val name: String) extends BaseDAO
object PlatformDAO {
  implicit object PlatformDAODomainObject extends DomainObject[PlatformDAO] {
    def convertToRow(o: PlatformDAO) = PlatformRow(o.id, Some(o.name))
    def convertFromRow(o: PlatformRow) = PlatformDAO(o.id, o.name.getOrElse(""))

    override def id(a: PlatformDAO) = a.id
    override def name(a: PlatformDAO) = a.name
  }
}

case class MediaUsageDAO(val id: Int, val agency: AgencyDAO, val platform: PlatformDAO, val actions: Int, val sampleDate: Date, val url: String) extends BaseDAO
object MediaUsageDAO {
  implicit object MediaUsageDAODomainObject extends DomainObject[MediaUsageDAO] {
    def convertToRow(o: MediaUsageDAO) =
      SocialMediaUsageSamplesRow(o.id, o.agency.id, Some(o.platform.id), Some(o.url), Some(o.sampleDate), Some(o.actions), None)

    def convertFromRow(o: SocialMediaUsageSamplesRow) =
      MediaUsageDAO(o.id, AgencyDAO(o.agencyid, ""), PlatformDAO(o.platformid.getOrElse(0), ""), o.actions.getOrElse(0), o.sampleDate.orNull, o.url.getOrElse(""))

    def convertFromViewRow(o: ViewSmusMaxActionsOnDateRow) =
      MediaUsageDAO(0, AgencyDAO(0, o.name.getOrElse("")), null, o.maxactions.getOrElse(0), o.maxdate.orNull, o.url.getOrElse(""))

    override def id(a: MediaUsageDAO) = a.id
    override def name(a: MediaUsageDAO) = ""
  }
}

