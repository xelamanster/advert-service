package com.github.xelamanster.model

import java.util.UUID

import io.circe.{Decoder, Encoder}

import CarAdvert.fields._

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
      Decoder.forProduct7(Id, Title, Fuel, Price, New, Mileage, FirstRegistration)(CarAdvert.apply)

    implicit val carAdvertEncoder: Encoder[CarAdvert] =
      Encoder.forProduct7(Id, Title, Fuel, Price, New, Mileage, FirstRegistration)(a =>
        (a.id, a.title, a.fuel, a.price, a.isNew, a.mileage, a.firstRegistration)
      )
  }
}

case class CarAdvert(
    id: UUID,
    title: String,
    fuel: String,
    price: Int,
    isNew: Boolean,
    mileage: Option[Int],
    firstRegistration: Option[String])
