package com.github.xelamanster

import com.github.xelamanster.setup.CarAdvertLocalDbSetup
import com.github.xelamanster.data.CarAdvertTestData._
import com.github.xelamanster.model.AdvertNotFound
import com.github.xelamanster.utils.HttpContentType
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync
import org.scalatest.BeforeAndAfterEach
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.Application
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.Helpers._
import play.api.test._

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
        _ <- CarAdvertLocalDbSetup.put(newCarAdvert)
        Some(get) = route(FakeRequest(GET, s"/advert/${newId.toString}"))
        getResult <- get
      } yield getResult

      status(response) mustBe OK
      contentAsString(response) mustBe newCarAdvertJson
      contentType(response) mustBe Some(HttpContentType.Json)
    }

    "get all adverts" in {
      val response = for {
        _ <- CarAdvertLocalDbSetup.put(usedCarAdvert)
        _ <- CarAdvertLocalDbSetup.put(newCarAdvert)
        Some(getAll) = route(FakeRequest(GET, "/advert/"))
        scanResult <- getAll
      } yield scanResult

      status(response) mustBe OK
      contentAsString(response) mustBe scanJson(usedCarAdvertJson, newCarAdvertJson)
      contentType(response) mustBe Some(HttpContentType.Json)
    }

    "get all adverts sorted by Id" in {
      val response = for {
        _ <- CarAdvertLocalDbSetup.put(usedCarAdvert)
        _ <- CarAdvertLocalDbSetup.put(newCarAdvert)
        Some(getAll) = route(FakeRequest(GET, "/advert/?sortBy=id"))
        scanResult <- getAll
      } yield scanResult

      status(response) mustBe OK
      contentAsString(response) mustBe scanJson(usedCarAdvertJson, newCarAdvertJson)
      contentType(response) mustBe Some(HttpContentType.Json)
    }

    "add advert by id" in {
      val Some(add) = route(FakeRequest(POST, s"/advert/").withTextBody(newCarAdvertJson))

      status(add) mustBe OK
      contentAsString(add) mustBe newCarAdvertJson
      contentType(add) mustBe Some(HttpContentType.Json)
    }

    "modify advert by id" in {
      val patchRequest = FakeRequest(PATCH, s"/advert/${newId.toString}").withTextBody(patchJson)

      val response = for {
        _ <- CarAdvertLocalDbSetup.put(newCarAdvert)
        Some(patch) = route(patchRequest)
        patchResult <- patch
      } yield patchResult

      status(response) mustBe OK
      contentAsString(response) mustBe patchResultJson
      contentType(response) mustBe Some(HttpContentType.Json)
    }

    "delete advert by id" in {
      val response = for {
        _ <- CarAdvertLocalDbSetup.put(newCarAdvert)
        Some(delete) = route(FakeRequest(DELETE, s"/advert/${newId.toString}"))
        _ <- delete
        Some(get) = route(FakeRequest(GET, s"/advert/${newId.toString}"))
        getResult <- get
      } yield getResult

      status(response) mustBe NOT_FOUND
      contentAsString(response) mustBe AdvertNotFound(newId).toString
      contentType(response) mustBe Some(HttpContentType.Text)
    }

  }
}
