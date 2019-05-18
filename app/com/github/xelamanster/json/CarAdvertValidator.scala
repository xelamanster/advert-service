package com.github.xelamanster.json

import com.github.xelamanster.model.{AdvertValidationError, CarAdvert}
import com.github.xelamanster.model.AdvertActionError.implicits._
import cats.data.Validated
import cats.implicits._

object CarAdvertValidator {

  def validate(advert: CarAdvert): Either[AdvertValidationError, CarAdvert] =
    (validateMileage(advert) |+| validateFirstRegistration(advert))
      .toEither
      .map(_ => advert)

  private def validateMileage(advert: CarAdvert): Validated[AdvertValidationError, Unit] =
    if(advert.isNew != advert.mileage.isEmpty) {
      AdvertValidationError(message(CarAdvert.fields.Mileage, advert.isNew)).invalid
    } else {
      ().valid
    }

  private def validateFirstRegistration(advert: CarAdvert): Validated[AdvertValidationError, Unit] =
    if(advert.isNew != advert.firstRegistration.isEmpty) {
      AdvertValidationError(message(CarAdvert.fields.FirstRegistration, advert.isNew)).invalid
    } else {
      ().valid
    }

  private def message(fieldName: String, isNew: Boolean) = {
    val description =
      if(isNew) "field must not be specified"
      else "field is missing"

    s"Wrong $fieldName, $description."
  }
}
