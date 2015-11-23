package controllers

import play.api.Logger
import play.api.mvc._
import models._
import play.api.libs.json._
import utils.Implicits._

class Application extends Controller {

  def index = Action {
    try {
      Ok(Json.toJson(SpecMap.updateRedisData.toList))
        .withHeaders(ACCESS_CONTROL_ALLOW_ORIGIN -> "*")
    } catch {
      case e:Exception =>
        Logger.error("Cannot stringify the JSON Object", e)
        InternalServerError("Cannot stringify the JSON Object")
    }
  }

  def getSpec(name:String) = Action {
    // todo: Ugly, fix it
    SpecMap.updateRedisData
    try {
      Ok(Json.toJson(SpecMap.getSpec(name)))
        .withHeaders(ACCESS_CONTROL_ALLOW_ORIGIN -> "*")
    } catch {
      case e:Exception =>
        Logger.error("Cannot stringify the JSON Object", e)
        InternalServerError("Cannot stringify the JSON Object")
    }
  }
}
