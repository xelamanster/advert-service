package com.github.xelamanster.model.dynamodb

import com.github.xelamanster.model.CarAdvert
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType._
import org.scanamo.{DynamoFormat, Table}
import org.scanamo.semiauto._
import CarAdvertTable.implicits._

object CarAdvertTable {

  object implicits {
    implicit val formatFarm: DynamoFormat[CarAdvert] = deriveDynamoFormat[CarAdvert]
  }

  object fields {
    final val Id = TableField("UUID", 'id, S)
  }

  private final val TableName = "CarAdvert"

  val table: Table[CarAdvert] = Table[CarAdvert](TableName)
}

case class TableField(name: String, attribute: Symbol, attributeType: ScalarAttributeType))
