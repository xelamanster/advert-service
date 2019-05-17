package com.github.xelamanster

import com.github.xelamanster.setup.CarAdvertLocalDbSetup
import com.github.xelamanster.data.CarAdvertTestData._
import com.github.xelamanster.model.AdvertNotFound
import com.github.xelamanster.model.dynamodb.CarAdvertTable
import com.github.xelamanster.utils.JsonUtils

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync
import org.scalatest.BeforeAndAfterEach
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.Application
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.Helpers._
import play.api.test._
import org.scanamo.ScanamoAsync
import scala.concurrent.ExecutionContext

class CarAdvertIntegrationSpec extends PlaySpec with MockitoSugar with OneAppPerSuite with BeforeAndAfterEach {

  override lazy val app: Application = new GuiceApplicationBuilder()
    .overrides(bind[AmazonDynamoDBAsync].toInstance(CarAdvertLocalDbSetup.localClient))
    .build()

  lazy implicit val executionContext: ExecutionContext = app.injector.instanceOf[ExecutionContext]

  override def beforeEach(): Unit =
    CarAdvertLocalDbSetup.createTable()

  override def afterEach(): Unit =
    CarAdvertLocalDbSetup.deleteTable()

  "Car advert service" should {

    "get advert by id" in {
      val response = for {
        _ <- ScanamoAsync.exec(CarAdvertLocalDbSetup.localClient)(CarAdvertTable.table.put(newCarAdvert))
        val Some(get) = route(FakeRequest(GET, s"/advert/${newId.toString}"))
        getResult <- get
      } yield getResult

      status(response) mustBe OK
      contentAsString(response) mustBe newCarAdvertJson
      contentType(response) mustBe Some(JsonUtils.JsonType)
    }

    "add advert by id" in {
      val Some(add) = route(FakeRequest(POST, s"/advert/").withTextBody(newCarAdvertJson))

      status(add) mustBe OK
      contentAsString(add) mustBe newCarAdvertJson
      contentType(add) mustBe Some(JsonUtils.JsonType)
    }

    "delete advert by id" in {
      val response = for {
        _ <- ScanamoAsync.exec(CarAdvertLocalDbSetup.localClient)(CarAdvertTable.table.put(newCarAdvert))
        val Some(delete) = route(FakeRequest(DELETE, s"/advert/${newId.toString}"))
        _ <- delete
        val Some(get) = route(FakeRequest(GET, s"/advert/${newId.toString}"))
        getResult <- get
      } yield getResult

      status(response) mustBe NOT_FOUND
      contentAsString(response) mustBe AdvertNotFound(newId).toString
      contentType(response) mustBe Some(JsonUtils.TextType)
    }

  }
}
