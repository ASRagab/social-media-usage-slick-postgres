package service

/**
  * Created by ASRagab on 2/23/16.
  */
class AgencyRepository extends BaseRepository {
  import t._
  import dtl._

  val repository = new Repository[AgencyRow, Agency, AgencyDTO](t.Agency)
}

class PlatformRepository extends BaseRepository {
  import t._
  import dtl._

  val repository = new Repository[PlatformRow, Platform, PlatformDTO](t.Platform)
}
