# advert-service
The service allow users to place new car adverts and view, modify and delete existing car adverts.

Car adverts have the following fields:
* **id** (_required_): **guid**;
* **title** (_required_): **string**;
* **fuel** (_required_): gasoline or diesel; (represented by enum type Fuel)
* **price** (_required_): **integer**;
* **new** (_required_): **boolean**;
* **mileage** (_only for used cars_): **integer**;
* **first registration** (_only for used cars_): **date** without time.

# Implemented functionality:

* have functionality to return list of all car adverts;
* optional sorting by any field specified by query parameter, default sorting - by **id**;

Sorting is implemented partially, by using SGI. Adverts could be sorted using any field except **new**.
Default sorting were not added, because of doubts that sorting by guid makes sense.
Should be better understand data request patterns and then optimise methods.
Was thinking about adding pagination, but dismissed this idea because of lack of time.

* have functionality to return data for single car advert by id;
* have functionality to add car advert;

Basic validation were added, checks on **mileage** and **first registration**, whether they should
be in the advert or not, based on the value of the **new**.

* have functionality to modify car advert by id;

Checks if advert with **id** exists in the system before update.
Basic validation were added, also based on **mileage** and **first registration**.
Current limitation - not possible to set these fields back to None during modification.

* have functionality to delete car advert by id;

Operation returns Ok result every time. Should be possible to improve result action based on
response metadata from DeleteItemResult.

* have validation (see required fields and fields only for used cars);

* accept and return data in JSON format, use standard JSON date format for the
**first registration** field.

For date were used "dd/MM/yyyy HH:mm:ss" format.

Added and enabled CORS filter.

# Testing

Added unit test specs for CarAdvertController and util classes.
Added integration test for running on dynamodb, and also covers CarAdvertDao, which doesn't have
unit tests (potentially for dao could be added more thin integration layer).

Many of the tests set to pending, and coverage of the added test cases far from full,
covering mostly happy paths.

# Notes:

Use:
Play Framework 2.4.11.

DynamoDb local - should be running for correct working of the integration spec.
DynamoDb endpoint could be set using env variable "DYNAMO_ENDPOINT", default value "http://localhost:8000"
Default dynamoDb client specified in the DynamoDBModule relies on system AWS configuration, and
should be overridden to use local dynamodb without configuration.

For development were used docker image "docker run -p 8000:8000 amazon/dynamodb-local".


Scanamo - for connection to db.

enumeratum - for enums, because of easy declaration and integration with other libraries.


