package com.github.xelamanster.model

import java.util.UUID

sealed trait AdvertActionError

sealed trait AdvertDBActionError extends AdvertActionError
final case class AdvertNotFound(id: UUID) extends AdvertDBActionError
final case class DBAccessError(description: String) extends AdvertDBActionError

sealed trait AdvertJsonActionError extends AdvertActionError
final case class AdvertParseError(description: String) extends AdvertJsonActionError
final case class AdvertValidationError(description: String) extends AdvertJsonActionError
