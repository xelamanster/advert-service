package com.github.xelamanster.model

import com.github.xelamanster.model.CarAdvert.implicits._
import com.github.xelamanster.model.AdvertActionError.implicits._
import io.circe.Encoder

object CarAdvertsScanResult {

  object fields {
    final val Adverts = "adverts"
    final val Errors = "errors"
  }

  object implicits {
    implicit val scanEncoder: Encoder[CarAdvertsScanResult] =
      Encoder.forProduct2(fields.Adverts, fields.Errors)(scan => (scan.adverts, scan.errors))
  }

  def apply(error: AdvertDBActionError): CarAdvertsScanResult =
    new CarAdvertsScanResult(List.empty, List(error))
}

case class CarAdvertsScanResult(adverts: List[CarAdvert], errors: List[AdvertDBActionError]) {
  def containsOnlyErrors: Boolean = adverts.isEmpty && errors.nonEmpty
}
