package com.github.xelamanster

import com.amazonaws.regions.Regions
import com.amazonaws.services.dynamodbv2.{AmazonDynamoDBAsync, AmazonDynamoDBAsyncClient}
import com.google.inject.AbstractModule

class Module extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[AmazonDynamoDBAsync]).toInstance(fakeAmazonClient)
  }

  def fakeAmazonClient =
    AmazonDynamoDBAsyncClient
    .asyncBuilder()
    .withRegion(Regions.US_WEST_1)
      .build()
}
