package app

import service.AgencyDataService

/**
  * Created by ASRagab on 2/23/16.
  */
object Runner extends App {

  val AgencyDataService = new AgencyDataService

  val agencies = AgencyDataService.getAll
  agencies.get foreach {
    case x: AgencyDataService.dto => println(x.name)
    case _ => println("Nothing found")
  }


  val agency = AgencyDataService.getByID(10)
  agency foreach {
    case x: AgencyDataService.dto => println(x.name)
    case _ => println("Agency not found")
  }

}
