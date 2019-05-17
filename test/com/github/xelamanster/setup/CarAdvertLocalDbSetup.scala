package com.github.xelamanster.setup

import com.github.xelamanster.model.dynamodb.CarAdvertTable

import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.dynamodbv2.model.{AttributeDefinition, CreateTableRequest, CreateTableResult, DeleteTableResult, KeySchemaElement, KeyType, ProvisionedThroughput, ScalarAttributeType}
import com.amazonaws.services.dynamodbv2.{AmazonDynamoDBAsync, AmazonDynamoDBAsyncClient}

import scala.collection.JavaConverters._

object CarAdvertLocalDbSetup {
  private val dummyCredentials = new BasicAWSCredentials("dummy", "credentials")
  private val localEndpoint = new EndpointConfiguration("http://localhost:8000", "")
  private val arbitraryThroughput = new ProvisionedThroughput(1L, 1L)

  lazy val localClient: AmazonDynamoDBAsync = {
    AmazonDynamoDBAsyncClient
      .asyncBuilder()
      .withCredentials(new AWSStaticCredentialsProvider(dummyCredentials))
      .withEndpointConfiguration(localEndpoint)
      .build()
  }

  def deleteTable(): DeleteTableResult =
    localClient.deleteTable(CarAdvertTable.TableName)

  def createTable(): CreateTableResult =
    localClient.createTable(
      new CreateTableRequest()
        .withTableName(CarAdvertTable.TableName)
        .withAttributeDefinitions(attributeDefinitions(CarAdvertTable.fields.Id.definition) )
        .withKeySchema(new KeySchemaElement(CarAdvertTable.fields.Id.attribute.name, KeyType.HASH))
        .withProvisionedThroughput(arbitraryThroughput)
    )

  private def attributeDefinitions(attributes: (Symbol, ScalarAttributeType)*) =
    attributes.map {
      case (symbol, attributeType) => new AttributeDefinition(symbol.name, attributeType)
    }.asJava
}
