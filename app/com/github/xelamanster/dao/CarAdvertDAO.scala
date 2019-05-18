package com.github.xelamanster.dao

import com.github.xelamanster.dao.dynamodb.DynamoDbCarAdvertDao
import com.github.xelamanster.model.{AdvertActionError, CarAdvert, CarAdvertUpdate, CarAdvertsScanResult}
import com.google.inject.ImplementedBy
import java.util.UUID

import scala.concurrent.Future

@ImplementedBy(classOf[DynamoDbCarAdvertDao])
trait CarAdvertDAO {
  def add(advert: CarAdvert): Future[Either[AdvertActionError, CarAdvert]]
  def get(id: UUID): Future[Either[AdvertActionError, CarAdvert]]
  def getAll(): Future[CarAdvertsScanResult]
  def modify(id: UUID, update: CarAdvertUpdate): Future[Either[AdvertActionError, CarAdvert]]
  def delete(id: UUID): Future[Unit]
}
