package com.github.xelamanster.setup

import com.github.xelamanster.model.dynamodb.{CarAdvertTable, TableField}
import com.github.xelamanster.model.CarAdvert
import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.dynamodbv2.model.{AttributeDefinition, CreateTableRequest, CreateTableResult, DeleteTableResult, GlobalSecondaryIndex, KeySchemaElement, KeyType, Projection, ProjectionType, ProvisionedThroughput, ScalarAttributeType}
import com.amazonaws.services.dynamodbv2.{AmazonDynamoDBAsync, AmazonDynamoDBAsyncClient}
import org.scanamo.ScanamoAsync
import org.scanamo.error.DynamoReadError

import scala.collection.JavaConverters._
import scala.concurrent.{ExecutionContext, Future}

trait CarAdvertLocalDbSetup {
  private val dynamoDbEndpointEnv = "DYNAMO_ENDPOINT"
  private val dynamoDbDefaultEndpoint = "http://localhost:8000"
  private val dynamoDbEndpoint = sys.env.getOrElse(dynamoDbEndpointEnv, dynamoDbDefaultEndpoint)
  private val dummyCredentials = new BasicAWSCredentials("dummy", "credentials")
  private val localEndpoint = new EndpointConfiguration(dynamoDbEndpoint, "")
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
        .withAttributeDefinitions(attributeDefinitions(CarAdvertTable.indexes:_*))
        .withKeySchema(new KeySchemaElement(CarAdvertTable.fields.Id.attribute.name, KeyType.HASH))
        .withGlobalSecondaryIndexes(CarAdvertTable.indexes.map(gsi):_*)
        .withProvisionedThroughput(arbitraryThroughput)
    )

  def put(advert: CarAdvert)
         (implicit ec: ExecutionContext): Future[Option[Either[DynamoReadError, CarAdvert]]] =

    ScanamoAsync.exec(localClient)(CarAdvertTable.table.put(advert))

  private def attributeDefinitions(fields: TableField*) =
    fields.distinct.map(
      field => new AttributeDefinition(field.attribute.name, field.attributeType)
    ).asJava

  private def gsi(field: TableField) =
    new GlobalSecondaryIndex()
      .withIndexName(field.name)
      .withKeySchema(new KeySchemaElement(field.attribute.name, KeyType.HASH))
      .withProvisionedThroughput(arbitraryThroughput)
      .withProjection(new Projection().withProjectionType(ProjectionType.ALL))
}
