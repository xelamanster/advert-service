package com.github.xelamanster.json

import com.github.xelamanster.model.{AdvertValidationError, CarAdvert, CarAdvertUpdate}
import com.github.xelamanster.model.AdvertActionError.implicits._
import cats.data.Validated
import cats.implicits._

object CarAdvertUpdateValidator {

  def validate(currentAdvert: CarAdvert)(advertUpdate: CarAdvertUpdate): Either[AdvertValidationError, CarAdvertUpdate] =
    (validateMileage(currentAdvert, advertUpdate) |+| validateFirstRegistration(currentAdvert, advertUpdate))
      .toEither
      .map(_ => advertUpdate)

  private def validateMileage(currentAdvert: CarAdvert, advertUpdate: CarAdvertUpdate): Validated[AdvertValidationError, Unit] = {
    val statusChanged = advertUpdate.isNew.nonEmpty && !advertUpdate.isNew.contains(currentAdvert.isNew)
    val mileageModified = advertUpdate.mileage.nonEmpty && advertUpdate.mileage != currentAdvert.mileage

    if(currentAdvert.isNew && !statusChanged && mileageModified) {
      AdvertValidationError(message(CarAdvert.fields.Mileage, currentAdvert.isNew)).invalid
    } else {
      ().valid
    }
  }

  private def validateFirstRegistration(currentAdvert: CarAdvert, advertUpdate: CarAdvertUpdate): Validated[AdvertValidationError, Unit] = {
    val statusChanged = advertUpdate.isNew.nonEmpty && !advertUpdate.isNew.contains(currentAdvert.isNew)
    val firstRegistrationModified = advertUpdate.firstRegistration.nonEmpty && advertUpdate.firstRegistration != currentAdvert.firstRegistration

    if(currentAdvert.isNew && !statusChanged && firstRegistrationModified) {
      AdvertValidationError(message(CarAdvert.fields.FirstRegistration, currentAdvert.isNew)).invalid
    } else {
      ().valid
    }
  }

  private def message(fieldName: String, isNew: Boolean) = {
    val description =
      if(isNew) "field must not be specified"
      else "field is missing"

    s"Wrong $fieldName, $description."
  }
}
