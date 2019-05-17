package com.github.xelamanster.module

import com.amazonaws.regions.Regions
import com.amazonaws.services.dynamodbv2.{AmazonDynamoDBAsync, AmazonDynamoDBAsyncClient}
import com.google.inject.AbstractModule

class DynamoDbModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[AmazonDynamoDBAsync]).toInstance(defaultDynamoDbClient)
  }

  private def defaultDynamoDbClient =
    AmazonDynamoDBAsyncClient
      .asyncBuilder()
      .withRegion(Regions.US_WEST_1)
      .build()
}
