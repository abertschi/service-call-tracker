- yo here is work in progress!

# service-call-tracker

service-call-tracker is a method call marshaller for Java. It can record method invocations and is able to stub their implementation by replaying previously recorded method calls. Created with the aim to provide more reliable test data in development and testing tiers  of an enterprise application, service-call-tracker can be used to provide a set of always-available test data.

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

## Data Storage

The default configuration marshalles method calls to a file of key-value pairs of `<call/>`. The method arguments placed in `<request/>` as the key and their return value placed in `<response/>` acts as the value. 

`<payload/>` sections within `<request/>` and `<response/>` of a `<call/>` contain the marshalled method calls.

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

### Stacktrace

There is support to throw exceptions from `<stacktrace/>` as a response to a matching request.
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
The library [stacktrace-unserialize](https://github.com/abertschi/stacktrace-unserialize) is used to convert stacktraces to throwable.

### Groovy Scripting

If a static stacktrace is not enough, a Groovy Shell can be fired up and an exception or any other response put into `<script/>`
can dynamically be formed during execution.

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

These global variable are available in your Groovy script.

| Properties | Description          |
|------------------|---|
| request.payload  | Access to fields of the current request                    
| response.payload | Access to fields of the response payload tag
| stacktrace       | The stacktrace as instance of Throwable if set in the stacktrace tag of the response
| system           | Access to Java system variables (i.e. system.mySystemVarName)
| env              | Access to environment variables



| Priority  | Tag          
|------------------|---|
| 1   | script
| 2  | stacktrace
| 3   | payload  | 


### EL Expressions

To put expressions into the `<payload/>` sections of the `<request/>` and `<response/>` objects of a call, a syntax valid to the Java Expression Language can be used.

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

### Regular expressions

In any field of the request payload, you can use regular rexpressions the change the request matching behaviour.

The example below throws an exception for any request given.
The calls with the lowest index in the file is checked first to match the current index.

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

Some common regular expressions are predefined and accessible as `#{regex.<name>}`.

| Properties | Description          |
|------------------|---|
| regex.any  | Ignore field
| regex.numeric | Match only if field is numeric


## Getting started

### Configuration

### Integration testing with JBoss Arquillian
