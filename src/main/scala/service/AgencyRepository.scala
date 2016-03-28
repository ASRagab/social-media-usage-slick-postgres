package service

import scala.concurrent.Future

/**
  * Created by ASRagab on 2/23/16.
  */
object AgencyRepository extends Repository {
  import t._
  import dtl._
  import MapperImplicits._
  import driver.api._

  implicit val tableQuery = Agency
  type R = AgencyRow
  type S = Agency
  type A = AgencyDTO

  def getByName(name: String): Future[Seq[A]] = {
    val query = tableQuery.filter(_.name === name).result
    resultMapper(query)
  }

  def getByID(id: Int): Future[Seq[A]] = {
    val query = tableQuery.filter(_.id === id).result
    resultMapper(query)
  }

  def getAll: Future[Seq[A]] = {
    val query = tableQuery.result
    resultMapper(query)
  }
}

object PlatformRepository extends Repository {
  import t._
  import dtl._
  import MapperImplicits._
  import driver.api._

  implicit val tableQuery = Platform
  type R = PlatformRow
  type S = Platform
  type A = PlatformDTO

  def getByName(name: String): Future[Seq[A]] = {
    val query = tableQuery.filter(_.name === name).result
    resultMapper(query)
  }

  def getByID(id: Int): Future[Seq[A]] = {
    val query = tableQuery.filter(_.id === id).result
    resultMapper(query)
  }

  def getAll: Future[Seq[A]] = {
    val query = tableQuery.result
    resultMapper(query)
  }
}

object MediaUsageRepository extends Repository {
  import t._
  import dtl._
  import driver.api._
  import MapperImplicits._

  implicit val tableQuery = SocialMediaUsageSamples
  type R = SocialMediaUsageSamplesRow
  type S = SocialMediaUsageSamples
  type A = MediaUsageDTO

  def getByName(name: String): Future[Seq[A]] = {
    val query = tableQuery.filter(_.name === name).result
    resultMapper(query)
  }

  def getByID(id: Int): Future[Seq[A]] = {
    val query = tableQuery.filter(_.id === id).result
    resultMapper(query)
  }

  def getAll: Future[Seq[A]] = {
    val query = tableQuery.result
    resultMapper(query)
  }

  def getByAgency(agency: Int): Future[Seq[A]] = {
    val query = tableQuery.filter(_.agencyid === agency).result
    resultMapper(query)
  }
}