package app

import service.{AgencyRepository, MediaUsageRepository, PlatformRepository}

/**
  * Created by ASRagab on 2/23/16.
  */
object Runner extends App {

  AgencyRepository.getAll.foreach(println)
  PlatformRepository.getAll.foreach(println)
  MediaUsageRepository.getAll.foreach(println)

  val array = Array(AgencyRepository getByID 1,
    PlatformRepository getByID 1,
    MediaUsageRepository getByID 1,
    AgencyRepository getByName "DSNY",
    PlatformRepository getByName "WordPress")

  val mediaUsages = MediaUsageRepository.getByAgency(5)

  mediaUsages foreach( mu => mu foreach println)

  array foreach {
    r => println(r getOrElse "not found")
  }

}
