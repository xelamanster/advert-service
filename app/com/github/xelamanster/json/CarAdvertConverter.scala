package com.github.xelamanster.json

import com.github.xelamanster.model.{AdvertActionError, AdvertJsonActionError, AdvertParseError, CarAdvert}
import com.github.xelamanster.utils.JsonUtils.dropNullValues
import io.circe.parser.parse
import com.github.xelamanster.model.CarAdvert.implicits._
import io.circe.syntax._
import cats.implicits._

object CarAdvertConverter {

  def decode(text: String): Either[AdvertJsonActionError, CarAdvert] =
    parse(text)
      .flatMap(_.as[CarAdvert])
      .leftMap(error => AdvertParseError(error.getMessage))

  def encode(advert: CarAdvert): String =
    advert.asJson.pretty(dropNullValues)
}
