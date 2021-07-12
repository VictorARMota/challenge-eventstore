# CHALLENGE EVENTSTORE MASTER

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

*** TESTS: ***
Since I was unable to implemented automated test, i wrote some tests so you could run them manually.

- GET http://localhost:8080/event - Checking Preloaded Events:
```
[
    {
        "id": 1,
        "type": "Start",
        "timestamp": 1626092401409
    },
    {
        "id": 2,
        "type": "Hello",
        "timestamp": 1626092401459
    }
]
```

- POST http://localhost:8080/event - Inserting Events on memory:
```
{
    "type" : "Test",
    "timestamp" : 123456
}
```

- DELETE http://localhost:8080/event/Start - Deleting the Start Event
```
Event Removed

[
    {
        "id": 2,
        "type": "Hello",
        "timestamp": 1626092401459
    },
    {
        "id": 3,
        "type": "Test",
        "timestamp": 123456
    }
]
```

- POST http://localhost:8080/event - Inserting more Events on memory:
```
{
    "type" : "Test",
    "timestamp" : 123123
}
```

- POST http://localhost:8080/event - Inserting more Events on memory:
```
{
    "type" : "Test",
    "timestamp" : 999999
}
```

- GET http://localhost:8080/query?type=Test&startTime=111111&endTime=555555 - Query for only 2 Test Events:
```
Query Generated

[
    {
        "id": 3,
        "type": "Test",
        "timestamp": 123456
    },
    {
        "id": 4,
        "type": "Test",
        "timestamp": 123123
    }
]
```

- GET http://localhost:8080/query/current - Trying to get current Event without going next to get the IllegalStateException:
```
{
    "timestamp": "2021-07-10T12:27:02.152+00:00",
    "status": 500,
    "error": "Internal Server Error",
    "message": "",
    "path": "/query/current"
}
```
  > Console:
  ```
  2021-07-10 09:27:02.145 ERROR 61192 --- [nio-8080-exec-5] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed; nested exception is java.lang.IllegalStateException: Current Event Not Found.] with root cause

java.lang.IllegalStateException: Current Event Not Found.
	at net.intelie.challenges.EventIteratorClass.current(EventIteratorClass.java:65) ~[classes/:na]
	at net.intelie.challenges.EventController.currentEvent(EventController.java:127) ~[classes/:na]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:na]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:78) ~[na:na]
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:na]
	at java.base/java.lang.reflect.Method.invoke(Method.java:567) ~[na:na]
	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:197) ~[spring-web-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:141) ~[spring-web-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:106) ~[spring-webmvc-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:894) ~[spring-webmvc-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:808) ~[spring-webmvc-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87) ~[spring-webmvc-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1063) ~[spring-webmvc-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:963) ~[spring-webmvc-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1006) ~[spring-webmvc-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:898) ~[spring-webmvc-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:655) ~[tomcat-embed-core-9.0.48.jar:4.0.FR]
	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:883) ~[spring-webmvc-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:764) ~[tomcat-embed-core-9.0.48.jar:4.0.FR]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:228) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:163) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53) ~[tomcat-embed-websocket-9.0.48.jar:9.0.48]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:190) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:163) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100) ~[spring-web-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119) ~[spring-web-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:190) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:163) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93) ~[spring-web-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119) ~[spring-web-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:190) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:163) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201) ~[spring-web-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119) ~[spring-web-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:190) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:163) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:202) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:97) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:542) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:143) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:78) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:357) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:382) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:65) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:893) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1723) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1130) ~[na:na]
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:630) ~[na:na]
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at java.base/java.lang.Thread.run(Thread.java:831) ~[na:na]
  ```

- GET http://localhost:8080/query/next - Moving to the next Event:
```
true
```

- GET http://localhost:8080/query/current - Retrieving the First Event:
```
{
  "id":3,
  "type":"Test",
  "timestamp":123456
}
```

- DELETE http://localhost:8080/query/current - Removing the First Event:
```
Event Removed
```

- GET http://localhost:8080/query/current - Trying to retrieve the current event after removing the previous one without calling moveNext:
```
{
    "timestamp": "2021-07-10T12:33:13.684+00:00",
    "status": 500,
    "error": "Internal Server Error",
    "message": "",
    "path": "/query/current"
}
```
  > Console:
  ```
  2021-07-10 09:33:13.683 ERROR 61192 --- [nio-8080-exec-2] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed; nested exception is java.lang.IllegalStateException: Current Event Not Found.] with root cause

java.lang.IllegalStateException: Current Event Not Found.
	at net.intelie.challenges.EventIteratorClass.current(EventIteratorClass.java:65) ~[classes/:na]
	at net.intelie.challenges.EventController.currentEvent(EventController.java:127) ~[classes/:na]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:na]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:78) ~[na:na]
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:na]
	at java.base/java.lang.reflect.Method.invoke(Method.java:567) ~[na:na]
	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:197) ~[spring-web-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:141) ~[spring-web-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:106) ~[spring-webmvc-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:894) ~[spring-webmvc-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:808) ~[spring-webmvc-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87) ~[spring-webmvc-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1063) ~[spring-webmvc-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:963) ~[spring-webmvc-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1006) ~[spring-webmvc-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:898) ~[spring-webmvc-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:655) ~[tomcat-embed-core-9.0.48.jar:4.0.FR]
	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:883) ~[spring-webmvc-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:764) ~[tomcat-embed-core-9.0.48.jar:4.0.FR]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:228) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:163) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53) ~[tomcat-embed-websocket-9.0.48.jar:9.0.48]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:190) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:163) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100) ~[spring-web-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119) ~[spring-web-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:190) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:163) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93) ~[spring-web-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119) ~[spring-web-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:190) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:163) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201) ~[spring-web-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119) ~[spring-web-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:190) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:163) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:202) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:97) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:542) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:143) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:78) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:357) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:382) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:65) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:893) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1723) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1130) ~[na:na]
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:630) ~[na:na]
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at java.base/java.lang.Thread.run(Thread.java:831) ~[na:na]
  ```
  
- GET http://localhost:8080/query/next - Going to the next Event:
```
true
```

- GET http://localhost:8080/query/current - Retrieving second Event, now the only one:
```
{
  "id":4,
  "type":"Test",
  "timestamp":123123
}
```
- GET http://localhost:8080/query/next - Going to the next Event, to end the iterator:
```
false
```

- GET http://localhost:8080/query/current - Trying to retrieve the current event after reaching the end of eventList:
```
{
    "timestamp": "2021-07-10T12:34:46.279+00:00",
    "status": 500,
    "error": "Internal Server Error",
    "message": "",
    "path": "/query/current"
}
```
  > Console:
  ```
  2021-07-10 09:34:46.269 ERROR 61192 --- [nio-8080-exec-8] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed; nested exception is java.lang.IllegalStateException: Current Event Not Found.] with root cause

java.lang.IllegalStateException: Current Event Not Found.
	at net.intelie.challenges.EventIteratorClass.current(EventIteratorClass.java:65) ~[classes/:na]
	at net.intelie.challenges.EventController.currentEvent(EventController.java:127) ~[classes/:na]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:na]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:78) ~[na:na]
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:na]
	at java.base/java.lang.reflect.Method.invoke(Method.java:567) ~[na:na]
	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:197) ~[spring-web-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:141) ~[spring-web-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:106) ~[spring-webmvc-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:894) ~[spring-webmvc-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:808) ~[spring-webmvc-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87) ~[spring-webmvc-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1063) ~[spring-webmvc-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:963) ~[spring-webmvc-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1006) ~[spring-webmvc-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:898) ~[spring-webmvc-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:655) ~[tomcat-embed-core-9.0.48.jar:4.0.FR]
	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:883) ~[spring-webmvc-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:764) ~[tomcat-embed-core-9.0.48.jar:4.0.FR]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:228) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:163) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53) ~[tomcat-embed-websocket-9.0.48.jar:9.0.48]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:190) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:163) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100) ~[spring-web-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119) ~[spring-web-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:190) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:163) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93) ~[spring-web-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119) ~[spring-web-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:190) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:163) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201) ~[spring-web-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119) ~[spring-web-5.3.9-SNAPSHOT.jar:5.3.9-SNAPSHOT]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:190) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:163) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:202) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:97) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:542) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:143) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:78) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:357) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:382) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:65) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:893) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1723) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1130) ~[na:na]
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:630) ~[na:na]
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61) ~[tomcat-embed-core-9.0.48.jar:9.0.48]
	at java.base/java.lang.Thread.run(Thread.java:831) ~[na:na]
  ```

