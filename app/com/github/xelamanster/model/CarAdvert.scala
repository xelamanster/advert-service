package com.github.xelamanster.model

import com.github.xelamanster.utils.JsonUtils.implicits._

import java.time.LocalDateTime
import java.util.UUID

import io.circe.{Decoder, Encoder}

object CarAdvert {
  object fields {
    final val Id = "id"
    final val Title = "title"
    final val Fuel = "fuel"
    final val Price = "price"
    final val New = "new"
    final val Mileage = "mileage"
    final val FirstRegistration = "first registration"
  }

  object implicits {
    implicit val carAdvertDecoder: Decoder[CarAdvert] =
      Decoder.forProduct7(fields.Id, fields.Title, fields.Fuel, fields.Price, fields.New, fields.Mileage, fields.FirstRegistration)(CarAdvert.apply)

    implicit val carAdvertEncoder: Encoder[CarAdvert] =
      Encoder.forProduct7(fields.Id, fields.Title, fields.Fuel, fields.Price, fields.New, fields.Mileage, fields.FirstRegistration)(a =>
        (a.id, a.title, a.fuel, a.price, a.isNew, a.mileage, a.firstRegistration))
  }
}

case class CarAdvert(
    id: UUID,
    title: String,
    fuel: Fuel,
    price: Int,
    isNew: Boolean,
    mileage: Option[Int],
    firstRegistration: Option[LocalDateTime])
