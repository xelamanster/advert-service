package com.github.xelamanster.model

import java.util.UUID

import cats.kernel.Semigroup
import io.circe.Encoder

object AdvertActionError {

  object implicits {

    implicit val dbErrorEncoder: Encoder[AdvertDBActionError] =
      Encoder.encodeString.contramap(_.toString)

    implicit val validationSemigroup: Semigroup[AdvertValidationError] = new Semigroup[AdvertValidationError] {
      override def combine(x: AdvertValidationError, y: AdvertValidationError): AdvertValidationError =
        AdvertValidationError(s"x,${System.lineSeparator()}y")
    }

  }
}

sealed trait AdvertActionError

sealed trait AdvertDBActionError extends AdvertActionError
final case class AdvertNotFound(id: UUID) extends AdvertDBActionError
final case class DBAccessError(description: String) extends AdvertDBActionError

sealed trait AdvertJsonActionError extends AdvertActionError
final case class AdvertParseError(description: String) extends AdvertJsonActionError
final case class AdvertValidationError(description: String) extends AdvertJsonActionError
