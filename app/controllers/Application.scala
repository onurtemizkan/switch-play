package controllers

import com.redis._
import play.api.mvc._
import models._
import play.api.libs.json._
import utils.Implicits._

class Application extends Controller {
  def index = Action {
    val client = new RedisClient("localhost", 6379) // Todo : Throws exception, handle it.
    val specList = client.hgetall("List").get // Todo : getOrElse
    val res = specList.map{ tuple =>
      SpecElement(tuple._1, tuple._2.toBoolean)
    }
    Ok(Json.toJson(res.toList)).withHeaders(ACCESS_CONTROL_ALLOW_ORIGIN -> "*") // Todo: exception handling
  }
}
