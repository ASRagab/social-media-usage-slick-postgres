package app

import service.{PlatformRepository, AgencyRepository}

/**
  * Created by ASRagab on 2/23/16.
  */
object Runner extends App {

  val agency = new AgencyRepository
  val platform = new PlatformRepository

  val agencyRepo = agency.repository
  agencyRepo.getAll.foreach(println)

  val platformRepo = platform.repository
  platformRepo.getAll.foreach(println)
}
