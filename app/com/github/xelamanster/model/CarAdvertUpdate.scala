package com.github.xelamanster.model

import java.time.LocalDateTime

import io.circe.Decoder
import com.github.xelamanster.utils.JsonUtils.implicits._

object CarAdvertUpdate {

  object fields {
    final val Title = "title"
    final val Fuel = "fuel"
    final val Price = "price"
    final val New = "new"
    final val Mileage = "mileage"
    final val FirstRegistration = "first registration"
  }

  object implicits {
    implicit val carAdvertUpdateDecoder: Decoder[CarAdvertUpdate] =
      Decoder.forProduct6(fields.Title, fields.Fuel, fields.Price, fields.New, fields.Mileage, fields.FirstRegistration)(CarAdvertUpdate.apply)
  }
}

case class CarAdvertUpdate(
    title: Option[String],
    fuel: Option[Fuel],
    price: Option[Int],
    isNew: Option[Boolean],
    mileage: Option[Int],
    firstRegistration: Option[LocalDateTime])
