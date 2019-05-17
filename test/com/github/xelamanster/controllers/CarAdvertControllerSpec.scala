package com.github.xelamanster.controllers

import com.github.xelamanster.dao.dynamodb.DynamoDbCarAdvertDao
import com.github.xelamanster.model.{AdvertNotFound, CarAdvert, Fuel}
import org.scalatestplus.play._
import play.api.test.Helpers._
import play.api.test._
import org.scalatest.mock.MockitoSugar
import java.time.LocalDateTime
import java.util.UUID
import scala.concurrent.Future
import org.mockito.Mockito._

import play.api.libs.concurrent.Execution.Implicits.defaultContext

import com.github.xelamanster.utils.JsonUtils._

class CarAdvertControllerSpec extends PlaySpec with OneAppPerSuite with MockitoSugar {

  private val newId = UUID.fromString("11b1f9cd-f6f4-4548-88f9-0cc5ee50227f")
  private val usedId = UUID.fromString("11b1f9cd-f6f4-4548-88f9-0cc5ee50327f")
  private val title = "title"
  private val fuel = Fuel.Diesel
  private val price = 2
  private val isNew = true

  private val newMileage = None
  private val usedMileageValue = 3
  private val usedMileage = Some(usedMileageValue)

  private val newFirstRegistration = None
  private val usedFirstRegistrationValue = "18/05/2019 12:43:50"
  private val usedFirstRegistration = Some(LocalDateTime.from(defaultDateFormat.parse(usedFirstRegistrationValue)))

  private val newCarAdvert =
    CarAdvert(newId, title, fuel, price, isNew, newMileage, newFirstRegistration)

  private val newCarAdvertJson =
    s"""{"id":"$newId","title":"$title","fuel":"${fuel.entryName}","price":$price,"new":$isNew}"""

  private val usedCarAdvert =
    CarAdvert(usedId, title, fuel, price, !isNew, usedMileage, usedFirstRegistration)

  private val usedCarAdvertJson =
    s"""{"id":"$usedId","title":"$title","fuel":"${fuel.entryName}","price":$price,"new":${!isNew},"mileage":$usedMileageValue,"first registration":"$usedFirstRegistrationValue"}"""

  "CarAdvertControllerSpec get()" should {

    "respond with correct new car advert" in {
      val dao = mock[DynamoDbCarAdvertDao]
      when(dao.get(newId)).thenReturn(Future.successful(Right(newCarAdvert)))

      val controller = new CarAdvertController(dao)
      val response = controller.get(newId)(FakeRequest())

      status(response) mustBe OK
      contentType(response) mustBe Some(JsonType)
      contentAsString(response) mustBe newCarAdvertJson
    }

    "respond with correct used car advert" in {
      val dao = mock[DynamoDbCarAdvertDao]
      when(dao.get(usedId)).thenReturn(Future.successful(Right(usedCarAdvert)))

      val controller = new CarAdvertController(dao)
      val response = controller.get(usedId)(FakeRequest())

      status(response) mustBe OK
      contentType(response) mustBe Some(JsonType)
      contentAsString(response) mustBe usedCarAdvertJson
    }

    "respond with NOT_FOUND" in {
      val expected = s"AdvertNotFound(${newId.toString})"

      val dao = mock[DynamoDbCarAdvertDao]
      when(dao.get(newId)).thenReturn(Future.successful(Left(AdvertNotFound(newId))))

      val controller = new CarAdvertController(dao)
      val response = controller.get(newId)(FakeRequest())

      status(response) mustBe NOT_FOUND
      contentType(response) mustBe Some(TextType)
      contentAsString(response) mustBe expected
    }

    "respond with INTERNAL_SERVER_ERROR" in {
       pending
    }

  }

  "CarAdvertControllerSpec add()" should {

    "return advert for new car after it were added" in {
      val dao = mock[DynamoDbCarAdvertDao]
      when(dao.add(newCarAdvert)).thenReturn(Future.successful(Right(newCarAdvert)))

      val controller = new CarAdvertController(dao)
      val response = controller.add()(FakeRequest().withTextBody(newCarAdvertJson))

      contentAsString(response) mustBe newCarAdvertJson
      status(response) mustBe OK
      contentType(response) mustBe Some(JsonType)
    }

    "return advert for used car after it were added" in {
      pending
    }

    "return UnsupportedMediaType if request body content has unexpected type" in {
      pending
    }

    "return NotAcceptable if json structure were unexpected" in {
      pending
    }
  }

}
