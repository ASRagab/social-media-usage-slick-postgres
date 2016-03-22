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
    implicit val agencyRowMapper: Tables.AgencyRow => AgencyDTO = AgencyDTO.convertFromRow
    implicit val platformRowMapper: Tables.PlatformRow => PlatformDTO = PlatformDTO.convertFromRow
    implicit val viewMaxActionsRowMapper: Tables.ViewSmusMaxActionsOnDateRow => MediaUsageDTO = MediaUsageDTO.convertFromViewRow
    implicit val samplesRowMapper: Tables.SocialMediaUsageSamplesRow => MediaUsageDTO = MediaUsageDTO.convertFromRow

    implicit val agencyDTOMapper: AgencyDTO => Tables.AgencyRow = AgencyDTO.convertToRow
    implicit val platFormDTOMapper: PlatformDTO => Tables.PlatformRow = PlatformDTO.convertToRow
    implicit val samplesDTOMapper: MediaUsageDTO => Tables.SocialMediaUsageSamplesRow = MediaUsageDTO.convertToRow
  }

  case class AgencyDTO(id: Int, name: String) extends BaseDTO
  case class PlatformDTO(id: Int, name: String) extends BaseDTO
  case class MediaUsageDTO(id: Int, agency: AgencyDTO, platform: PlatformDTO, actions: Int, sampleDate: Date, url: String) extends BaseDTO

  object AgencyDTO {
    def convertToRow(o: AgencyDTO) = Tables.AgencyRow(o.id, Some(o.name))
    def convertFromRow(o: Tables.AgencyRow) = AgencyDTO(o.id, o.name.getOrElse(""))
  }

  object PlatformDTO {
    def convertToRow(o: PlatformDTO) = Tables.PlatformRow(o.id, Some(o.name))
    def convertFromRow(o: Tables.PlatformRow) = PlatformDTO(o.id, o.name.getOrElse(""))
  }

  object MediaUsageDTO {
    def convertToRow(o: MediaUsageDTO) =
      Tables.SocialMediaUsageSamplesRow(o.id, o.agency.id, Some(o.platform.id), Some(o.url), Some(o.sampleDate), Some(o.actions), None)

    def convertFromRow(o: Tables.SocialMediaUsageSamplesRow) =
      MediaUsageDTO(o.id, AgencyDTO(o.agencyid, ""), PlatformDTO(o.platformid.getOrElse(0), ""), o.actions.getOrElse(0), o.sampleDate.orNull, o.url.getOrElse(""))

    def convertFromViewRow(o: Tables.ViewSmusMaxActionsOnDateRow) =
      MediaUsageDTO(0, AgencyDTO(0, o.name.getOrElse("")), null, o.maxactions.getOrElse(0), o.maxdate.orNull, o.url.getOrElse(""))
  }

}

