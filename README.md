# service-call-tracker

Service-call-tracker creates snapshots of method calls.  

It can be used to mock method calls for testing purpose and
is the core of the [arquillian-service-call-tracker extension](https://github.com/abertschi/arquillian-service-call-tracker-extension).

Method calls are marshaled to files of key-value pairs where the method signature acts as key.
A file acts as a storage for various method calls.

~~~~~~
<dependency>
    <groupId>org.sct</groupId>
    <artifactId>service-call-tracker-bom</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
~~~~~~
