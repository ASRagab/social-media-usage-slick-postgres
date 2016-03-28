package app

import service.{AgencyRepository, MediaUsageRepository, PlatformRepository}
import scala.concurrent.ExecutionContext.Implicits.global

import scala.util.{Failure, Success}

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

  mediaUsages onComplete {
    case Success(list) if list.nonEmpty => list foreach println
  }

  array foreach {
    r => r.onComplete {
      case Success(list) if list.nonEmpty => println(list.head)
      case Failure(e) => println(e.getMessage)
    }
  }

  Thread.sleep(1000)
}
