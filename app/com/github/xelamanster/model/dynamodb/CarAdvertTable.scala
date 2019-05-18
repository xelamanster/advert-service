package com.github.xelamanster.model.dynamodb

import com.github.xelamanster.model.{CarAdvert, Fuel}
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType._
import org.scanamo.{DynamoFormat, Table}
import org.scanamo.semiauto._
import java.time.LocalDateTime
import CarAdvertTable.implicits._

object CarAdvertTable {

  object implicits {
    implicit val timeFormat: DynamoFormat[LocalDateTime] =
      DynamoFormat.coercedXmap[LocalDateTime, String, IllegalArgumentException](LocalDateTime.parse)(_.toString)

    implicit val fuelFormat: DynamoFormat[Fuel] =
      DynamoFormat.coercedXmap[Fuel, String, IllegalArgumentException](Fuel.withName)(_.entryName)

    implicit val advertFormat: DynamoFormat[CarAdvert] = deriveDynamoFormat[CarAdvert]
  }

  object fields {
    final val Id = TableField("UUID", 'id, S)
    final val Title = TableField("title", S)
    final val Fuel = TableField("fuel", S)
    final val Price = TableField("price", N)
    final val New = TableField("isNew", S)
    final val Mileage = TableField("mileage", N)
    final val FirstRegistration = TableField("firstRegistration", S)
  }

  final val TableName = "CarAdvert"

  val table: Table[CarAdvert] = Table[CarAdvert](TableName)
}

object TableField {

  def apply(name: String, attributeType: ScalarAttributeType): TableField =
    new TableField(name, Symbol(name), attributeType)
}

case class TableField(name: String, attribute: Symbol, attributeType: ScalarAttributeType) {
  def definition: (Symbol, ScalarAttributeType) = attribute -> attributeType
}
