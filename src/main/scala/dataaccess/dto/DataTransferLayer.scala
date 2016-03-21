package dataaccess.dto

import java.sql.Date
import slick.driver.JdbcProfile

trait BaseDTO

class DataTransferLayer(val driver: JdbcProfile) {

  import dataaccess.schema.Tables

  object Tables extends {
    val profile = driver
  } with Tables

  object MapperImplicits {
    implicit val agencyRowMapper: Tables.AgencyRow => AgencyDTO = AgencyDTO.AgencyDTOable.convertFromRow
    implicit val platformRowMapper: Tables.PlatformRow => PlatformDTO = PlatformDTO.PlatformDTOable.convertFromRow
    implicit val viewMaxActionsRowMapper: Tables.ViewSmusMaxActionsOnDateRow => MediaUsageDTO = MediaUsageDTO.MediaUsageDTOable.convertFromViewRow
    implicit val samplesRowMapper: Tables.SocialMediaUsageSamplesRow => MediaUsageDTO = MediaUsageDTO.MediaUsageDTOable.convertFromRow

    implicit val agencyDTOMapper: AgencyDTO => Tables.AgencyRow = AgencyDTO.AgencyDTOable.convertToRow
    implicit val platFormDTOMapper: PlatformDTO => Tables.PlatformRow = PlatformDTO.PlatformDTOable.convertToRow
    implicit val samplesDTOMapper: MediaUsageDTO => Tables.SocialMediaUsageSamplesRow = MediaUsageDTO.MediaUsageDTOable.convertToRow
  }

  object LocalImplicits {

    implicit class DTOOps[A <: BaseDTO](a: A)(implicit ev: DTOable[A]) {
      def name = ev.name(a)
      def id = ev.id(a)
    }
  }

  sealed trait DTOable[A <: BaseDTO] {
    def id(a: A): Int
    def name(a: A): String
  }

  case class AgencyDTO(id: Int, name: String) extends BaseDTO
  object AgencyDTO {

    implicit object AgencyDTOable extends DTOable[AgencyDTO] {
      def convertToRow(o: AgencyDTO) = Tables.AgencyRow(o.id, Some(o.name))
      def convertFromRow(o: Tables.AgencyRow) = AgencyDTO(o.id, o.name.getOrElse(""))

      override def id(a: AgencyDTO) = a.id
      override def name(a: AgencyDTO) = a.name
    }
  }

  case class PlatformDTO(id: Int, name: String) extends BaseDTO

  object PlatformDTO {

    implicit object PlatformDTOable extends DTOable[PlatformDTO] {
      def convertToRow(o: PlatformDTO) = Tables.PlatformRow(o.id, Some(o.name))
      def convertFromRow(o: Tables.PlatformRow) = PlatformDTO(o.id, o.name.getOrElse(""))

      override def id(a: PlatformDTO) = a.id
      override def name(a: PlatformDTO) = a.name
    }
  }

  case class MediaUsageDTO(id: Int, agency: AgencyDTO, platform: PlatformDTO, actions: Int, sampleDate: Date, url: String) extends BaseDTO

  object MediaUsageDTO {

    implicit object MediaUsageDTOable extends DTOable[MediaUsageDTO] {
      def convertToRow(o: MediaUsageDTO) =
        Tables.SocialMediaUsageSamplesRow(o.id, o.agency.id, Some(o.platform.id), Some(o.url), Some(o.sampleDate), Some(o.actions), None)

      def convertFromRow(o: Tables.SocialMediaUsageSamplesRow) =
        MediaUsageDTO(o.id, AgencyDTO(o.agencyid, ""), PlatformDTO(o.platformid.getOrElse(0), ""), o.actions.getOrElse(0), o.sampleDate.orNull, o.url.getOrElse(""))

      def convertFromViewRow(o: Tables.ViewSmusMaxActionsOnDateRow) =
        MediaUsageDTO(0, AgencyDTO(0, o.name.getOrElse("")), null, o.maxactions.getOrElse(0), o.maxdate.orNull, o.url.getOrElse(""))

      override def id(a: MediaUsageDTO) = a.id
      override def name(a: MediaUsageDTO) = ""
    }
  }
}

