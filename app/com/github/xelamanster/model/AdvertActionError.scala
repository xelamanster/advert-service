package com.github.xelamanster.model

import java.util.UUID

sealed trait AdvertActionError
final case class AdvertNotFound(id: UUID) extends AdvertActionError
