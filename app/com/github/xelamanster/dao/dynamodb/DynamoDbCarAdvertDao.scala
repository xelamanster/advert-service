package com.github.xelamanster.dao.dynamodb

import com.github.xelamanster.dao.CarAdvertDAO
import com.github.xelamanster.model.{AdvertActionError, AdvertDBActionError, AdvertNotFound, CarAdvert, CarAdvertUpdate, CarAdvertsScanResult, DBAccessError}
import com.github.xelamanster.model.dynamodb.TableField
import com.github.xelamanster.model.dynamodb.CarAdvertTable._
import com.github.xelamanster.model.dynamodb.CarAdvertTable.implicits._

import cats.implicits._
import cats._
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync
import java.util.UUID
import javax.inject.{Inject, Singleton}
import org.scanamo.ops.ScanamoOps
import org.scanamo.syntax._
import org.scanamo.{DynamoFormat, ScanamoAsync}
import org.scanamo.error.DynamoReadError
import org.scanamo.update.UpdateExpression
import scala.concurrent.{ExecutionContext, Future}

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

  override def getAll(): Future[CarAdvertsScanResult] =
    execute(
      table.scan.map { scan =>

        val (allErrors, allValues) =
          scan.foldLeft(List.empty[AdvertDBActionError], List.empty[CarAdvert]) {

            case((errors, values), Right(value)) =>
              (errors, value +: values)

            case((errors, values), Left(error)) =>
              (toAdvertDBActionError(error) +: errors, values)
        }

        CarAdvertsScanResult(allValues, allErrors)
      }
    )

  override def add(advert: CarAdvert): Future[Either[AdvertActionError, CarAdvert]] =
    execute(
      table.put(advert).map {
        _.map(_.leftMap(toAdvertDBActionError))
          .getOrElse(Right(advert))
      }
    )

  override def modify(id: UUID, update: CarAdvertUpdate): Future[Either[AdvertActionError, CarAdvert]] =
    execute {
      table.update(fields.Id.attribute -> id.toString, setFields(update)).map {
        _.leftMap(toAdvertDBActionError)
      }
    }


  def setFields(update: CarAdvertUpdate): UpdateExpression =
    List(
      updateField(fields.Title, _.title),
      updateField(fields.Price, _.price),
      updateField(fields.Fuel, _.fuel),
      updateField(fields.New, _.isNew),
      updateField(fields.Mileage, _.mileage),
      updateField(fields.FirstRegistration, _.firstRegistration)
    ).flatMap(_ (update))
      .reduce(Semigroup.combine(_, _))

  private def updateField[T: DynamoFormat](field: TableField, update: CarAdvertUpdate => Option[T]) =
    update.andThen(
      _.map(v => set(field.attribute -> v))
    )

  override def delete(id: UUID): Future[Unit] =
    execute(
      table.delete(fields.Id.attribute -> id.toString).map(_ => ())
    )

  private def execute[A](op: ScanamoOps[A]) =
    ScanamoAsync.exec(client)(op)

  private def toAdvertDBActionError(v: DynamoReadError) =
    DBAccessError(v.toString)
}
