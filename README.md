- yo here is work in progress!

# service-call-tracker

service-call-tracker is a method call marshaller for Java. It can record method invocations and is able to stub their implementation by replaying previously recorded method calls. Created with the aim to provide more reliable test data in development and testing tiers  of an enterprise application, service-call-tracker can be used to provide a set of always-available test data.

## Install

The project artefacts are available on maven central.

## Data Storage

The default configuration marshalles method calls to a file of key-value pairs of `<call/>`. The method arguments as the key (`<request/>`) and their return value as the value (`<response/>`). 

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

### Groovy Scripting

If a static stacktrace is not enough, a Groovy Shell `<script/>` can be fired up and an exception or any other response
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
        String msg = request.payload.string + "was not found because an error happened!"
        throw new RuntimeException(msg)
      ]]>
    </script>
  </response>
</call>
```


| Properties       
|------------------|---|---|---|---|
| request.payload  |   |   |   |   |
| response.payload |   |   |   |   |
| stacktrace       |   |   |   |   |
| system           |   |   |   |   |
| env              |   |   |   |   |


## Getting started

### Configuration


### Integration testing with JBoss Arquillian
