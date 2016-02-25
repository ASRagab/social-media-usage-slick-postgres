package dataaccess.schema

import slick.driver.H2Driver

// AUTO-GENERATED Slick data model
trait BaseDAO
trait Tables {

  val profile: slick.driver.JdbcProfile

  import profile.api._
  import slick.model.ForeignKeyAction

  trait DAO extends BaseDAO {
    val id: Int
    val name: Option[String]
  }

  trait GenericDataTable[T <: DAO] extends Table[T] {
    def id: Rep[Int]
    def name: Rep[Option[String]]
  }

  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  lazy val schema = (Agency.schema ++ Platform.schema ++ SocialMediaUsageSamples.schema ++ StagingNycSocialMediaUsage.schema ++ ViewSmusMaxActionsOnDate.schema).create
  lazy val schemaDrop = (Agency.schema ++ Platform.schema ++ SocialMediaUsageSamples.schema ++ StagingNycSocialMediaUsage.schema ++ ViewSmusMaxActionsOnDate.schema).drop

  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Agency
    *
    * @param id   Database column id SqlType(serial), AutoInc, PrimaryKey
    * @param name Database column name SqlType(varchar), Length(200,true), Default(None) */
  case class AgencyRow(id: Int, name: Option[String] = None) extends DAO

  implicit def GetResultAgencyRow(implicit e0: GR[Int], e1: GR[Option[String]]): GR[AgencyRow] = GR {
    prs => import prs._
      AgencyRow.tupled((<<[Int], <<?[String]))
  }

  class Agency(_tableTag: Tag) extends Table[AgencyRow](_tableTag, "agency") with GenericDataTable[AgencyRow] {
    def * = (id, name) <>(AgencyRow.tupled, AgencyRow.unapply)

    def ? = (Rep.Some(id), name).shaped.<>({ r => import r._; _1.map(_ => AgencyRow.tupled((_1.get, _2))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    val name: Rep[Option[String]] = column[Option[String]]("name", O.Length(200, varying = true), O.Default(None))
  }

  lazy val Agency: slick.lifted.TableQuery[Agency] = new TableQuery(tag => new Agency(tag))

  /** Entity class storing rows of table Platform
    *
    * @param id   Database column id SqlType(serial), AutoInc, PrimaryKey
    * @param name Database column name SqlType(varchar), Length(100,true), Default(None) */
  case class PlatformRow(id: Int, name: Option[String] = None) extends DAO

  implicit def GetResultPlatformRow(implicit e0: GR[Int], e1: GR[Option[String]]): GR[PlatformRow] = GR {
    prs => import prs._
      PlatformRow.tupled((<<[Int], <<?[String]))
  }

  class Platform(_tableTag: Tag) extends Table[PlatformRow](_tableTag, "platform") with GenericDataTable[PlatformRow] {
    def * = (id, name) <>(PlatformRow.tupled, PlatformRow.unapply)

    def ? = (Rep.Some(id), name).shaped.<>({ r => import r._; _1.map(_ => PlatformRow.tupled((_1.get, _2))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    val name: Rep[Option[String]] = column[Option[String]]("name", O.Length(100, varying = true), O.Default(None))
  }

  lazy val Platform = new TableQuery(tag => new Platform(tag))

  /** Entity class storing rows of table SocialMediaUsageSamples
    *
    * @param id         Database column id SqlType(serial), AutoInc
    * @param agencyid   Database column agencyid SqlType(int4)
    * @param platformid Database column platformid SqlType(int4), Default(None)
    * @param url        Database column url SqlType(varchar), Length(500,true), Default(None)
    * @param sampleDate Database column sample_date SqlType(date), Default(None)
    * @param actions    Database column actions SqlType(int4), Default(None)
    * @param createDate Database column create_date SqlType(date) */
  case class SocialMediaUsageSamplesRow(id: Int, agencyid: Int, platformid: Option[Int] = None, url: Option[String] = None, sampleDate: Option[java.sql.Date] = None, actions: Option[Int] = None, createDate: Option[java.sql.Date]) extends BaseDAO

  implicit def GetResultSocialMediaUsageSamplesRow(implicit e0: GR[Int], e1: GR[Option[Int]], e2: GR[Option[String]], e3: GR[Option[java.sql.Date]]): GR[SocialMediaUsageSamplesRow] = GR {
    prs => import prs._
      SocialMediaUsageSamplesRow.tupled((<<[Int], <<[Int], <<?[Int], <<?[String], <<?[java.sql.Date], <<?[Int], <<?[java.sql.Date]))
  }

  class SocialMediaUsageSamples(_tableTag: Tag) extends Table[SocialMediaUsageSamplesRow](_tableTag, "social_media_usage_samples") {

    def * = (id, agencyid, platformid, url, sampleDate, actions, createDate) <>(SocialMediaUsageSamplesRow.tupled, SocialMediaUsageSamplesRow.unapply)
    def ? = (Rep.Some(id), Rep.Some(agencyid), platformid, url, sampleDate, actions, createDate).shaped.<>({ r => import r._; _1.map(_ => SocialMediaUsageSamplesRow.tupled((_1.get, _2.get, _3, _4, _5, _6, _7))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    val id: Rep[Int] = column[Int]("id", O.AutoInc)
    val agencyid: Rep[Int] = column[Int]("agencyid")
    val platformid: Rep[Option[Int]] = column[Option[Int]]("platformid", O.Default(None))
    val url: Rep[Option[String]] = column[Option[String]]("url", O.Length(500, varying = true), O.Default(None))
    val sampleDate: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("sample_date", O.Default(None))
    val actions: Rep[Option[Int]] = column[Option[Int]]("actions", O.Default(None))
    val createDate: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("create_date")

    val pk = if(profile != H2Driver) primaryKey("social_media_usage_samples_pkey", (id, agencyid))
    lazy val agencyFk = foreignKey("social_media_usage_samples_agencyid_fkey", agencyid, Agency)(r => r.id, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
    lazy val platformFk = foreignKey("social_media_usage_samples_platformid_fkey", platformid, Platform)(r => Rep.Some(r.id), onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
    val index1 = index("idx_agency_sample_dae", (agencyid, sampleDate))
    val index2 = index("uq_agency_url_sample", (agencyid, url, sampleDate), unique = true)
  }

  lazy val SocialMediaUsageSamples = new TableQuery(tag => new SocialMediaUsageSamples(tag))

  /** Entity class storing rows of table StagingNycSocialMediaUsage
    *
    * @param agency     Database column agency SqlType(varchar), Length(200,true), Default(None)
    * @param platform   Database column platform SqlType(varchar), Length(100,true), Default(None)
    * @param url        Database column url SqlType(varchar), Length(500,true), Default(None)
    * @param sampleDate Database column sample_date SqlType(date), Default(None)
    * @param action     Database column action SqlType(int4), Default(None) */
  case class StagingNycSocialMediaUsageRow(agency: Option[String] = None, platform: Option[String] = None, url: Option[String] = None, sampleDate: Option[java.sql.Date] = None, action: Option[Int] = None) extends BaseDAO

  implicit def GetResultStagingNycSocialMediaUsageRow(implicit e0: GR[Option[String]], e1: GR[Option[java.sql.Date]], e2: GR[Option[Int]]): GR[StagingNycSocialMediaUsageRow] = GR {
    prs => import prs._
      StagingNycSocialMediaUsageRow.tupled((<<?[String], <<?[String], <<?[String], <<?[java.sql.Date], <<?[Int]))
  }

  class StagingNycSocialMediaUsage(_tableTag: Tag) extends Table[StagingNycSocialMediaUsageRow](_tableTag, "staging_nyc_social_media_usage") {
    def * = (agency, platform, url, sampleDate, action) <>(StagingNycSocialMediaUsageRow.tupled, StagingNycSocialMediaUsageRow.unapply)

    val agency: Rep[Option[String]] = column[Option[String]]("agency", O.Length(200, varying = true), O.Default(None))
    val platform: Rep[Option[String]] = column[Option[String]]("platform", O.Length(100, varying = true), O.Default(None))
    val url: Rep[Option[String]] = column[Option[String]]("url", O.Length(500, varying = true), O.Default(None))
    val sampleDate: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("sample_date", O.Default(None))
    val action: Rep[Option[Int]] = column[Option[Int]]("action", O.Default(None))
  }

  lazy val StagingNycSocialMediaUsage = new TableQuery(tag => new StagingNycSocialMediaUsage(tag))

  /** Entity class storing rows of table ViewSmusMaxActionsOnDate
    *
    * @param name       Database column name SqlType(varchar), Length(200,true), Default(None)
    * @param maxdate    Database column maxdate SqlType(date), Default(None)
    * @param maxactions Database column maxactions SqlType(int4), Default(None)
    * @param url        Database column url SqlType(varchar), Length(500,true), Default(None) */
  case class ViewSmusMaxActionsOnDateRow(name: Option[String] = None, maxdate: Option[java.sql.Date] = None, maxactions: Option[Int] = None, url: Option[String] = None) extends BaseDAO

  implicit def GetResultViewSmusMaxActionsOnDateRow(implicit e0: GR[Option[String]], e1: GR[Option[java.sql.Date]], e2: GR[Option[Int]]): GR[ViewSmusMaxActionsOnDateRow] = GR {
    prs => import prs._
      ViewSmusMaxActionsOnDateRow.tupled((<<?[String], <<?[java.sql.Date], <<?[Int], <<?[String]))
  }

  class ViewSmusMaxActionsOnDate(_tableTag: Tag) extends Table[ViewSmusMaxActionsOnDateRow](_tableTag, "view_smus_max_actions_on_date") {
    def * = (name, maxdate, maxactions, url) <>(ViewSmusMaxActionsOnDateRow.tupled, ViewSmusMaxActionsOnDateRow.unapply)

    val name: Rep[Option[String]] = column[Option[String]]("name", O.Length(200, varying = true), O.Default(None))
    val maxdate: Rep[Option[java.sql.Date]] = column[Option[java.sql.Date]]("maxdate", O.Default(None))
    val maxactions: Rep[Option[Int]] = column[Option[Int]]("maxactions", O.Default(None))
    val url: Rep[Option[String]] = column[Option[String]]("url", O.Length(500, varying = true), O.Default(None))
  }

  lazy val ViewSmusMaxActionsOnDate = new TableQuery(tag => new ViewSmusMaxActionsOnDate(tag))
}
