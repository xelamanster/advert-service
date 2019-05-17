package com.github.xelamanster.controllers

import com.github.xelamanster.dao.CarAdvertDAO
import com.github.xelamanster.model.{AdvertActionError, AdvertJsonActionError, AdvertNotFound, CarAdvert}
import java.util.UUID

import com.github.xelamanster.json.CarAdvertConverter
import com.github.xelamanster.utils.HttpContentType
import javax.inject.{Inject, Singleton}
import play.api.mvc.{Action, AnyContent, Controller}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CarAdvertController @Inject()(dao: CarAdvertDAO)
                                   (implicit ec: ExecutionContext) extends Controller {

  def get(id: UUID): Action[AnyContent] = Action.async {
    dao.get(id).map {
      case Right(advert) => SuccessfulAdvertAction(advert)
      case Left(error: AdvertNotFound) => NotFound(error.toString)
      case Left(error) => InternalServerError(error.toString)
    }
  }

  def add(): Action[AnyContent] = Action.async { request =>
    request.body.asText match {
      case Some(text) => CarAdvertConverter.decode(text) match {
        case Right(advert: CarAdvert) => add(advert)
        case Left(failure: AdvertJsonActionError) => Future.successful(NotAcceptable(failure.toString))
      }
      case None => Future.successful(UnsupportedMediaType)
    }
  }

  private def add(advert: CarAdvert) = dao.add(advert).map {
    case Right (resultAdvert) => SuccessfulAdvertAction (resultAdvert)
    case Left (error) => InternalServerError(error.toString)
  }

  def delete(id: UUID): Action[AnyContent] = Action.async {
    dao.delete(id).map(_ => Ok)
  }

  private def SuccessfulAdvertAction(advert: CarAdvert) =
    Ok(CarAdvertConverter.encode(advert)).as(HttpContentType.Json)

}
