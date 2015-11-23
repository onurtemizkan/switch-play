package models

import scala.collection.mutable.Map
import play.api.libs.json._
import com.redis._
import play.api.Logger

object SpecMap {

  val specMap:Map[String, String] = Map.empty[String, String]

  val redisClient:RedisClient = {
    try {
      new RedisClient("localhost", 6379)
    } catch {
      case e:Exception => {
        Logger.error("Cannot create the redis client, Retrying", e)
        // todo: Wait between
        redisClient
      }
    }
  }

  def getSpec(specName:String) = {
    Json.parse(specMap.get(specName).getOrElse("{}"))
  }

  def updateRedisData = {
   redisClient.hgetall("List").getOrElse(List()).map {
     tuple:Tuple2[String, String] =>
      specMap += tuple
      SpecElement(tuple._1, tuple._2.toString())
    }
  }
}
