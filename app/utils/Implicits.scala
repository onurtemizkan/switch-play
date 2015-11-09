package utils

import models._
import play.api.libs.json._

object Implicits {

  implicit val SpecElementWrites = new Writes[SpecElement] {
    def writes(element: SpecElement) =
      Json.obj("active" -> element.active)
  }

  implicit val ElementListWrites = new Writes[List[SpecElement]] {
    def writes(list: List[SpecElement]) = {
      var jsObject = Json.obj()
      list map { elm =>
        jsObject = jsObject + Tuple2(elm.name, Json.toJson(elm))
      }
      jsObject
    }
  }
}
