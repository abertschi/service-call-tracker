![service-call-tracker](sct.png)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/ch.abertschi.sct/service-call-tracker-parent/badge.svg?style=flat)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22ch.abertschi.sct%22)
[![Build Status](https://travis-ci.org/abertschi/service-call-tracker.svg?branch=master)](https://travis-ci.org/abertschi/service-call-tracker) 
[![codecov](https://codecov.io/gh/abertschi/service-call-tracker/branch/master/graph/badge.svg)](https://codecov.io/gh/abertschi/service-call-tracker)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/368d59ada8a7491aa5499be3f8906ac7)](https://www.codacy.com/app/abertschi/service-call-tracker?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=abertschi/service-call-tracker&amp;utm_campaign=Badge_Grade)
[![Apache 2](http://img.shields.io/badge/license-APACHE2-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

------

# service-call-tracker



service-call-tracker is a method call marshaller for Java. It can record method invocations and is able to stub their implementation by replaying previously recorded method calls. Created with the aim to provide more reliable test data in development and testing tiers of an enterprise application, service-call-tracker can be used to provide a set of always-available test data.
In combination with the [JBoss Arquillian Extension for service-call-tracker](https://github.com/abertschi/arquillian-service-call-tracker-extension), service-call-tracker is capable of serializing calls to 3rd-party code making your integration tests independent of availability and data consistency of 3rd-party code within your application.

## Install

The project artefacts are available on maven central.

```xml  
<dependency>
  <groupId>ch.abertschi.sct</groupId>
  <artifactId>service-call-tracker-api</artifactId>
</dependency>
<dependency>
  <groupId>ch.abertschi.sct</groupId>
  <artifactId>service-call-tracker-impl</artifactId>
</dependency>
```

## Getting started

In order to gain control over method invocations, service-call-tracker must be hooked into your code
and an instance of `ch.abertschi.sct.api.invocation.InvocationContext` must be built.

There are various ways how to intercept method calls in Java such as:

- [Around Advice with AspectJ](https://eclipse.org/aspectj/doc/next/adk15notebook/ataspectj-pcadvice.html)
  - See [AspectJ Extension for AspectJ](https://github.com/abertschi/arquillian-extension-aspectj)
- [Arquillian Extension for service-call-tracker](https://github.com/abertschi/arquillian-service-call-tracker-extension)
- Java Dynamic Proxy API
- Any bean container framework providing interceptors (ie. EJB, CDI)

### Recording 

In order to record calls, create an instance of `ch.abertschi.sct.api.Configuration`, enable recording and
fire up `ServiceCallTracker#invoke(config: Configuration)`.

```java
Configuration config = new Configuration();
config.setRecordingEnabled(true);
config.setRecordingSource(new File("my-recordings.xml"));
ServiceCallTracker serviceCallTracker = new ServiceCallTracker(config);

// gain access to a method call and build an InvocationContext
InvocationContext currentCall = ...

// invoke method an records response to my-recordings.xml
Object result = serviceCallTracker.invoke(currentCall);
```

### Replaying 

```java
Configuration config = new Configuration();
config.setReplayingEnabled(true);
config.setReplayingSource(new File("my-replayings.xml"));
ServiceCallTracker serviceCallTracker = new ServiceCallTracker(config);

// gain access to a method call and build an InvocationContext
InvocationContext currentCall =  ...

// build respone from my-replayings.xml if currentCall previously recorded
Object result = serviceCallTracker.invoke(currentCall);
```

## Data Storage

The default configuration marshalles method calls to a file of key-value pairs of `<call>`. The method arguments placed in `<request>` act as the key and their return value placed in `<response>` acts as the value. 

`<payload>` sections within `<request>` and `<response>` contain the marshalled method calls.

```xml
<storage>
  <calls>
    <call>
      <request>
        <payload class="object-array">
          <string>Peter Parker</string>
        </payload>
      </request>
      <response>
        <payload class="ch.abertschi.sct.domain.Customer">
          <name>Peter Parker</name>
          <yearOfBirth>1970</yearOfBirth>
          <comment>I am spiderman!</comment>
        </payload>
        <stacktrace/>
        <script/>
      </response>
    </call>
    ...
  </calls>
</storage>
```

- This example unmarshalls the response payload object above if an intercepted method with the String argument *Peter Parker* is called.

### Stacktrace

There is support to throw exceptions from `<stacktrace>` as a response to a matching request.
This is useful to test some worse-case scenarios.

```xml
<call>
  <request>
    <payload class="object-array">
      <string>Peter Parker</string>
    </payload>
  </request>
  <response>
    <stacktrace>
      Exception in thread "main" java.lang.OutOfMemoryError: Something bad happened
       at Main.main(StackTraceUnserialize.java:107) 
       at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) 
       at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) 
       at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) 
       at java.lang.reflect.Method.invoke(Method.java:497) 
       at com.intellij.rt.execution.application.AppMain.main(AppMain.java:140) 
       ... 6 more;
      </stacktrace>
      <script />
  </response>
</call>
```
- The example above throws an OutOfMemoryError with the given stacktrace if an intercepted method with the String argument *Peter Parker* is called.

The library [stacktrace-unserialize](https://github.com/abertschi/stacktrace-unserialize) is used to convert a stacktrace to a throwable.

### Groovy Scripting

If a static stacktrace is not enough, a Groovy Shell can be fired up to build a response. Groovy code within `<script>` is evaluated at runtime.

```xml
<call>
  <request>
    <payload class="object-array">
      <string>Peter Parker</string>
    </payload>
  </request>
  <response>
  <stacktrace/>
    <script>
      <!CDATA[[
        String msg = request.payload.string + " was not found because an error happened!"
        throw new RuntimeException(msg)
      ]]>
    </script>
  </response>
</call>
```

- The example above throws a RuntimeException if an intercepted method with the String argument *Peter Parker* is called.

These global variable are available in your Groovy script.

| Properties | Description          |
|------------------|---|
| request.payload  | Access fields of the current request                    
| response.payload | Access fields of the response payload tag
| stacktrace       | The stacktrace as instance of Throwable if set in the stacktrace tag of the response
| system           | Access Java system variables (i.e. system.mySystemVarName)
| env              | Access environment variables


You can set a script, a stacktrace and a payload as a response for a call.
In your Groovy script you have access to the throwable instance of the stacktrace tag and the return object of the payload tag.
Once you set a script, the stacktrace and payload tags are ignored and you need to return or throw an objects within the script.

The table below shows the priority of execution of these tags (high to low)

| Priority  | Tag          
|------------------|---|
| 1   | script
| 2  | stacktrace
| 3   | payload  | 


### EL Expressions

You can use the Java Expression Language to write expressions within the `<payload>` sections of the `<request>` and `<response>` objects.

These objects are preconfigured:

| Properties | Description          |
|------------------|---|
| request.payload  | Access fields of the current request                    
| response.payload | Access fields of the response payload tag
| system           | Access Java system variables (i.e. system.mySystemVarName)
| env              | Access environment variables

```xml
<call>
  <request>
    <payload class="object-array">
      <string>Peter Parker</string>
      <int>1</int>
    </payload>
  </request>
  <response>
  <payload class="ch.abertschi.sct.domain.Customer">
    <name>#{request.payload.string}</name>
    <yearOfBirth>1970</yearOfBirth>
    <comment>I am spiderman!</comment>
    </payload>
  </response>
</call>
```

### Regular expressions (experimental)

In any field of the request payload, you can use regular rexpressions to alter the request matching behaviour.

The example below throws an exception for any request given. Calls with the lowest index in the file are checked first.

```xml
<call>
  <request>
    <payload>*.</payload>
  </request>
  <response>
    <stacktrace>java.lang.Exception: I always throw an Exception</stacktrace>
  </response>
</call>
```

Some common regular expressions are predefined and accessible with as `#{regex.<name>}`.

| Properties | Description          |
|------------------|---|
| regex.any  | Ignore field
| regex.numeric | Match only if field is numeric



