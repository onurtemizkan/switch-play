package controllers

import play.api.mvc._
import models._
import play.api.libs.json._
import utils.Implicits._

class Application extends Controller {

  def index = Action {
    Ok(Json.toJson(SpecMap.updateRedisData.toList)).withHeaders(ACCESS_CONTROL_ALLOW_ORIGIN -> "*")
    // Todo: exception handling
  }

  def getSpec(name:String) = Action {
    SpecMap.updateRedisData
    Ok(Json.toJson(SpecMap.getSpec(name))).withHeaders(ACCESS_CONTROL_ALLOW_ORIGIN -> "*");
    // Todo: exception handling
  }
}
