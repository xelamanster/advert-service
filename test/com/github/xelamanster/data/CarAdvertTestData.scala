package com.github.xelamanster.data

import com.github.xelamanster.model.{CarAdvert, Fuel}
import com.github.xelamanster.utils.JsonUtils.defaultDateFormat

import java.time.LocalDateTime
import java.util.UUID

object CarAdvertTestData {
  final val newId = UUID.fromString("11b1f9cd-f6f4-4548-88f9-0cc5ee50227f")
  final val usedId = UUID.fromString("11b1f9cd-f6f4-4548-88f9-0cc5ee50327f")
  final val title = "title"
  final val fuel = Fuel.Diesel
  final val price = 2
  final val isNew = true

  final val newMileage = None
  final val usedMileageValue = 3
  final val usedMileage = Some(usedMileageValue)

  final val newFirstRegistration = None
  final val usedFirstRegistrationValue = "18/05/2019 12:43:50"
  final val usedFirstRegistration = Some(LocalDateTime.from(defaultDateFormat.parse(usedFirstRegistrationValue)))

  final val newCarAdvert =
    CarAdvert(newId, title, fuel, price, isNew, newMileage, newFirstRegistration)

  final val newCarAdvertJson =
    s"""{"id":"$newId","title":"$title","fuel":"${fuel.entryName}","price":$price,"new":$isNew}"""

  final val usedCarAdvert =
    CarAdvert(usedId, title, fuel, price, !isNew, usedMileage, usedFirstRegistration)

  final val usedCarAdvertJson =
    s"""{"id":"$usedId","title":"$title","fuel":"${fuel.entryName}","price":$price,"new":${!isNew},"mileage":$usedMileageValue,"first registration":"$usedFirstRegistrationValue"}"""

}
