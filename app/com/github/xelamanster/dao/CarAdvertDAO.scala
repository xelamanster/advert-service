package com.github.xelamanster.dao

import com.github.xelamanster.dao.dynamodb.DynamoDbCarAdvertDao
import com.github.xelamanster.model.{AdvertActionError, CarAdvert}
import com.google.inject.ImplementedBy
import java.util.UUID
import scala.concurrent.Future

@ImplementedBy(classOf[DynamoDbCarAdvertDao])
trait CarAdvertDAO {
  def get(id: UUID): Future[Either[AdvertActionError, CarAdvert]]
  def add(advert: CarAdvert): Future[Either[AdvertActionError, CarAdvert]]
}
