package com.github.xelamanster.dao.dynamodb

import com.github.xelamanster.model.{AdvertActionError, CarAdvert}
import org.scanamo.error.DynamoReadError

class ErrorConverter {
  def toAdvertActionError(v: Either[DynamoReadError, CarAdvert]): Either[AdvertActionError, CarAdvert] = ???
}
