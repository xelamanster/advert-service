package com.github.xelamanster.dao.dynamodb

import com.github.xelamanster.dao.CarAdvertDAO
import com.github.xelamanster.model.{AdvertActionError, AdvertDBActionError, AdvertNotFound, CarAdvert, DBAccessError}
import com.github.xelamanster.model.dynamodb.CarAdvertTable._
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync
import java.util.UUID

import javax.inject.{Inject, Singleton}
import org.scanamo.ops.ScanamoOps
import org.scanamo.syntax._
import org.scanamo.ScanamoAsync

import scala.concurrent.{ExecutionContext, Future}
import cats.implicits._
import org.scanamo.error.DynamoReadError

@Singleton
class DynamoDbCarAdvertDao @Inject()(client: AmazonDynamoDBAsync)
                                    (implicit ec: ExecutionContext) extends CarAdvertDAO {

  override def get(id: UUID): Future[Either[AdvertActionError, CarAdvert]] =
    execute(
      table.get(fields.Id.attribute -> id.toString).map {
        _.map(_.leftMap(toAdvertDBActionError))
          .getOrElse(Left(AdvertNotFound(id)))
      }
    )

  override def delete(id: UUID): Future[Unit] =
    execute(
      table.delete(fields.Id.attribute -> id.toString).map(_ => Unit)
    )

  override def add(advert: CarAdvert): Future[Either[AdvertActionError, CarAdvert]] =
    execute(
      table.put(advert).map {
        _.map(_.leftMap(toAdvertDBActionError))
          .getOrElse(Right(advert))
      }
    )

  private def execute[A](op: ScanamoOps[A]) =
    ScanamoAsync.exec(client)(op)

  private def toAdvertDBActionError(v: DynamoReadError): AdvertDBActionError = DBAccessError(v.toString)
}
