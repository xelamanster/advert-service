package com.github.xelamanster.controllers

import com.github.xelamanster.dao.dynamodb.DynamoDbCarAdvertDao
import com.github.xelamanster.model.AdvertNotFound
import com.github.xelamanster.data.CarAdvertTestData._
import com.github.xelamanster.utils.HttpContentType
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play._
import play.api.test.Helpers._
import play.api.test._

import scala.concurrent.{ExecutionContext, Future}

class CarAdvertControllerSpec extends PlaySpec with OneAppPerSuite with MockitoSugar {

  lazy implicit val executionContext: ExecutionContext = app.injector.instanceOf[ExecutionContext]

  "CarAdvertControllerSpec get()" should {

    "respond with correct new car advert" in {
      val dao = mock[DynamoDbCarAdvertDao]
      when(dao.get(newId)).thenReturn(Future.successful(Right(newCarAdvert)))

      val controller = new CarAdvertController(dao)
      val response = controller.get(newId)(FakeRequest())

      status(response) mustBe OK
      contentAsString(response) mustBe newCarAdvertJson
      contentType(response) mustBe Some(HttpContentType.Json)
    }

    "respond with correct used car advert" in {
      val dao = mock[DynamoDbCarAdvertDao]
      when(dao.get(usedId)).thenReturn(Future.successful(Right(usedCarAdvert)))

      val controller = new CarAdvertController(dao)
      val response = controller.get(usedId)(FakeRequest())

      status(response) mustBe OK
      contentAsString(response) mustBe usedCarAdvertJson
      contentType(response) mustBe Some(HttpContentType.Json)
    }

    "respond with NOT_FOUND" in {
      val expected = s"AdvertNotFound(${newId.toString})"

      val dao = mock[DynamoDbCarAdvertDao]
      when(dao.get(newId)).thenReturn(Future.successful(Left(AdvertNotFound(newId))))

      val controller = new CarAdvertController(dao)
      val response = controller.get(newId)(FakeRequest())

      status(response) mustBe NOT_FOUND
      contentAsString(response) mustBe expected
      contentType(response) mustBe Some(HttpContentType.Text)
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

      status(response) mustBe OK
      contentAsString(response) mustBe newCarAdvertJson
      contentType(response) mustBe Some(HttpContentType.Json)
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

  "CarAdvertControllerSpec delete()" should {

    "return OK in case of successful removal" in {
      pending
    }

  }

  "CarAdvertControllerSpec getAll()" should {

    "return all adverts" in {
      pending
    }

    "return adverts along with errors" in {
      pending
    }

    "return INTERNAL_SERVER_ERROR" in {
      pending
    }

  }

}
