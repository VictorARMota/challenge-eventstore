##################################### CHALLENGE EVENTSTORE MASTER ######################################

This application was created to store Events as well as filter them by Type and Time.

*** HTTP ROUTES: ***
- GET http://localhost:8080/event - Returns all Events on repository.
- POST http://localhost:8080/event - Stores an Event.
  Body Sample:
  {
  	"type" : "Test",
  	"timestamp" : 123456
  }
- DELETE http://localhost:8080/event/{type} - Removes all Events of given type.
- GET http://localhost:8080/query?type={type}&startTime={startTime}&endTime={endTime} - Generates an
  EventIterator of given type and timestamp range.
- GET http://localhost:8080/query/next - Iterates to the next Event from the generated Query.
- GET http://localhost:8080/query/current - Returns current selected Event from the generated Query.
- DELETE http://localhost:8080/query/current - Removes current Event from the generated Query and from 
  Repository

*** LIBRARIES USED: ***
- JUnit - Pre-installed to create automated test enviroments.
- Spring Framework Starter - Utilized to transform the application into an MVC webservice.
- Spring Framework Data JPA - Utilized to access objects from the database and create custom queries.
- H2 Database - Utilized to create an H2 in-memory Database.

*** THREAD SAFETY: ***
This Application is on a Transaction Control on the EventIteratorClass and EventStoreClass, so it would 
maintain thread safety as well as data safety. The Atomicity is also implemented on counters and flags.

*** METHODS: ***
- EventStoreClass
  > insert(Event event).
  > removeAll(String type).
  > query(String type, long startTime, long endTime).

- EventIteratorClass
  > moveNext().
  > current().
  > remove().
  > close().

- EventController
  > all().
  > newEvent(Event event).
  > deleteEventsByType(String type).
  > query(String type, long startTime, long endTime).
  > moveNext().
  > currentEvent().
  > removeEvent().
