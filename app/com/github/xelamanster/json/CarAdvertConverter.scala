package com.github.xelamanster.json

import com.github.xelamanster.model.{AdvertJsonActionError, AdvertParseError, CarAdvert, CarAdvertUpdate, CarAdvertsScanResult}
import com.github.xelamanster.utils.JsonUtils.dropNullValues
import io.circe.parser.parse
import com.github.xelamanster.model.CarAdvert.implicits._
import com.github.xelamanster.model.CarAdvertsScanResult.implicits._
import com.github.xelamanster.model.CarAdvertUpdate.implicits._
import io.circe.syntax._
import cats.implicits._

object CarAdvertConverter {

  def decodeUpdate(currentAdvert: CarAdvert, updateText: String): Either[AdvertJsonActionError, CarAdvertUpdate] =
    parse(updateText)
      .flatMap(_.as[CarAdvertUpdate])
      .leftMap(error => AdvertParseError(error.getMessage))
      .flatMap(CarAdvertUpdateValidator.validate(currentAdvert))

  def decode(text: String): Either[AdvertJsonActionError, CarAdvert] =
    parse(text)
      .flatMap(_.as[CarAdvert])
      .leftMap(error => AdvertParseError(error.getMessage))
      .flatMap(CarAdvertValidator.validate)

  def encode(advertScan: CarAdvertsScanResult): String =
    advertScan.asJson.pretty(dropNullValues)

  def encode(advert: CarAdvert): String =
    advert.asJson.pretty(dropNullValues)
}
