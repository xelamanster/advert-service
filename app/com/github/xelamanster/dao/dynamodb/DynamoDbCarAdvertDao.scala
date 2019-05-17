package com.github.xelamanster.dao.dynamodb

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync
import com.github.xelamanster.dao.CarAdvertDAO
import com.github.xelamanster.model.{AdvertActionError, AdvertNotFound, CarAdvert}
import com.github.xelamanster.model.dynamodb.CarAdvertTable._
import java.util.UUID
import javax.inject.{Inject, Singleton}
import org.scanamo.ops.ScanamoOps
import org.scanamo.syntax._
import org.scanamo.ScanamoAsync
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class DynamoDbCarAdvertDao @Inject()(client: AmazonDynamoDBAsync, converter: ErrorConverter)
                                    (implicit ec: ExecutionContext) extends CarAdvertDAO {

  override def get(id: UUID): Future[Either[AdvertActionError, CarAdvert]] =
    execute(
      table.get(fields.Id.attribute -> id.toString).map {
        _.map(converter.toAdvertActionError)
          .getOrElse(Left(AdvertNotFound(id)))
      }
    )

  private def execute[A](op: ScanamoOps[A]) =
    ScanamoAsync.exec(client)(op)
}
