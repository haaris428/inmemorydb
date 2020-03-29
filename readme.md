this is a maven project so you will need maven to run the project

I use following two commands

Clean package - including unit tests

```mvn clean package```

Run CLI

```
 mvn exec:java -Dexec.mainClass="com.brainfish.InMemoryDBApplication"
```