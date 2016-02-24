package service

/**
  * Created by ASRagab on 2/23/16.
  */
class AgencyDataService extends DataService {
  import t._
  import dtl._
  import MapperImplicits._
  import driver.api._

  override type dto = dtl.AgencyDTO
  override type dao = AgencyRow
  private val agencies = Agency

  override def getByName(name: String) = {
    val query = agencies.filter(_.name === name).result

    resultMapper[dto, dao](query) match {
      case list: Seq[dto] => Some(list.head)
      case _ => None
    }
  }

  override def getByID(id: Int) = {
    val query = agencies.filter(_.id === id).result

    resultMapper[dto, dao](query) match {
      case list: Seq[dto] if list.nonEmpty => Some(list.head)
      case _ => None
    }
  }

  override def getAll() = {
    val query = agencies.result

    resultMapper[dto, dao](query) match {
      case list: Seq[dto] => Some(list)
      case _ => None
    }
  }
}
