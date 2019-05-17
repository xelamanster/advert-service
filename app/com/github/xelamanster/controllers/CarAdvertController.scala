package com.github.xelamanster.controllers

import com.github.xelamanster.dao.CarAdvertDAO
import com.github.xelamanster.model.AdvertNotFound
import com.github.xelamanster.model.CarAdvert.implicits._
import com.github.xelamanster.utils.JsonUtils._

import java.util.UUID
import javax.inject.{Inject, Singleton}
import play.api.mvc.{Action, AnyContent, Controller}
import io.circe.syntax._

import scala.concurrent.ExecutionContext

@Singleton
class CarAdvertController @Inject()(dao: CarAdvertDAO)(implicit ec: ExecutionContext) extends Controller {

  def get(id: UUID): Action[AnyContent] = Action.async {
    dao.get(id).map {
      case Right(advert) => Ok(advert.asJson.pretty(dropNullValues)).as(JsonType)
      case Left(error: AdvertNotFound) => NotFound(error.toString)
      case Left(error) => InternalServerError(error.toString)
    }
  }

}
