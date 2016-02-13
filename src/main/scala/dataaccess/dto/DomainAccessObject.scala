package dataaccess.dto

import java.sql.Date
import dataaccess.schema.Tables
import Tables._

/**
  * Created by ASRagab on 2/7/16.
  */
package object Implicits {
  implicit class DTOOps[A <: BaseDTO](a: A)(implicit ev: DTOable[A]) {
    def name = ev.name(a)
    def id = ev.id(a)
  }
}

trait BaseDTO
trait DTOable[A <: BaseDTO] {
  def id(a: A): Int
  def name(a: A): String
}

case class AgencyDTO(val id: Int, val name: String) extends BaseDTO
object AgencyDTO {
  implicit object AgencyDTOable extends DTOable[AgencyDTO] {
    def convertToRow(o: AgencyDTO) = AgencyRow(o.id, Some(o.name))
    def convertFromRow(o: AgencyRow) = AgencyDTO(o.id, o.name.getOrElse(""))

    override def id(a: AgencyDTO) = a.id
    override def name(a: AgencyDTO) = a.name
  }
}

case class PlatformDTO(val id: Int, val name: String) extends BaseDTO
object PlatformDTO {
  implicit object PlatformDTOable extends DTOable[PlatformDTO] {
    def convertToRow(o: PlatformDTO) = PlatformRow(o.id, Some(o.name))
    def convertFromRow(o: PlatformRow) = PlatformDTO(o.id, o.name.getOrElse(""))

    override def id(a: PlatformDTO) = a.id
    override def name(a: PlatformDTO) = a.name
  }
}

case class MediaUsageDTO(val id: Int, val agency: AgencyDTO, val platform: PlatformDTO, val actions: Int, val sampleDate: Date, val url: String) extends BaseDTO
object MediaUsageDTO {
  implicit object MediaUsageDTOable extends DTOable[MediaUsageDTO] {
    def convertToRow(o: MediaUsageDTO) =
      SocialMediaUsageSamplesRow(o.id, o.agency.id, Some(o.platform.id), Some(o.url), Some(o.sampleDate), Some(o.actions), None)

    def convertFromRow(o: SocialMediaUsageSamplesRow) =
      MediaUsageDTO(o.id, AgencyDTO(o.agencyid, ""), PlatformDTO(o.platformid.getOrElse(0), ""), o.actions.getOrElse(0), o.sampleDate.orNull, o.url.getOrElse(""))

    def convertFromViewRow(o: ViewSmusMaxActionsOnDateRow) =
      MediaUsageDTO(0, AgencyDTO(0, o.name.getOrElse("")), null, o.maxactions.getOrElse(0), o.maxdate.orNull, o.url.getOrElse(""))

    override def id(a: MediaUsageDTO) = a.id
    override def name(a: MediaUsageDTO) = ""
  }
}

