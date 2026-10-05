# Using Jakarta EE at the SDV Hackathon

![Jakarta EE official logo](jakarta_ee_logo_schooner_color_horizontal_default.svg)

## What Is Jakarta EE 11?

Jakarta EE is an open, community-driven platform for building modern cloud-native Java applications.

It defines a set of standard APIs that applications can use when deployed to a Jakarta EE compatible runtime or application server.

Jakarta EE provides APIs for enterprise operations and  extensions for Java standard library that are typicall useful in enterprise applications, including:

* data persistence
* transactions
* message sending and receiving
* HTTP request handling
* XML and JSON handling
* SOAP and REST services
* HTML pages

Learn more: [Jakarta EE 11 Release](https://jakarta.ee/release/11/)

For this hackathon, Jakarta EE fits by providing a powerful server engine to process data from embedded nodes and sensors in your Software-Defined Vehicle architecture, persist them and connect them to external services, including AI agents and analytical workflows.

    Important: There is no standalone Jakarta EE challenge at this hackathon.

    Jakarta EE is a technology you can choose to use to extend either of the two main challenges.

    You do not have to use Jakarta EE to complete a challenge. But if you want to extend the challenge to push your solution to enterprise processing space and even analyze the data using LLM, you can use Jakarta EE to get there.

And don't feel limited to the examples below.

## Requirements

- Java 25 or later
- Maven 3.8 or later
- One of the supported Jakarta EE servers:
  - GlassFish 8.0.0 or later
  - Payara 7.2026.1 or later
  - WildFly 39.0.1.Final or later
  - Open Liberty 26.0.0.5 or later

## Quick Start

### 1. Clone and Build

```bash
git clone <repository-url>
cd tech-jakarta-ee-hourglass
mvn clean package
```

### 2. Run on Your Preferred Server

#### GlassFish
```bash
mvn clean package cargo:run -Pglassfish
```

#### Payara
```bash
mvn clean package cargo:run -Ppayara
```

#### WildFly
```bash
mvn clean package wildfly:run -Pwildfly
```

#### Open Liberty
```bash
mvn clean package liberty:run
```

### 3. Test the Server

If you run with Maven directly, test the endpoint at:

`http://localhost:8080/sdv-processing/api/hello`

## Docker Compose Quick Start

### 1. Start the Server

```bash
docker compose up -d
```

### 2. Follow Logs

```bash
docker compose logs -f hourglass
```

### 3. Test the Endpoint

```bash
curl http://localhost:28080/sdv-processing/api/hello
```

Expected response:

`Hello from Dukes!`

### 4. Stop the Server

```bash
docker compose down
```

## Challenge: Consume events by a Jakarta EE application and process them

A Jakarta EE application can extend both main hackathon challenges by consuming events and processing them further.

**For the Hack to the Future challenge:**

Consume Guardian State Machine Events

Extend the [Hack to the Future](https://github.com/Eclipse-SDV-Hackathon-Chapter-Four/Hack-to-the-Future) setup to publish events from the Guardian and consume them by a Jakarta EE application.

Either consume all events that impact the Guardian state, or just events when the state changes (CLEAR, MONITORING, WARNING, etc.) 

**For the Whodunit challenge**

Consume Battery Guardian Events

Extend the [Whodunit](https://github.com/Eclipse-SDV-Hackathon-Chapter-Four/Doctor-Whodunit/tree/example-first-steps) demo to publish battery events from the Guardian service and consume them by a Jakarta EE application.

Jakarta EE application will receive either all temperature events, or only events for the detected faults. It will then process them further.

#### Idea 1

Publish the events from the Guardian service to the OpenMQ broker using the OpenMQ C binding, as messages to a topic in OpenMQ. Consume the messages in a Jakarta EE application.

OpenMQ C binding is specific to the OpenMQ broker. Only GlassFish and Payara use and start OpenMQ automatically by default. For other servers, either run a standalone OpenMQ broker and connect to it via a Jakarta EE resource adapter, or use another approach described in other idea sections. 

`BatteryGuardianEventsBean` consumes the `battery.guardian.events` topic on the
server's default JMS broker using `java:comp/DefaultJMSConnectionFactory`.
The application defines the topic at `java:app/jms/BatteryGuardianEvents` and
logs incoming text messages. Other message types are logged by message ID.
The subscription is non-durable, so it receives events only while active.

How to use the OpenMQ C bindings:

Prerequisites:

    gcc, g++, libstdc-dev
    libnspr4-dev
    libnss3-dev
    libssl3-dev
    ant

Build OpenMQ client library:

Download the source code of [OpenMQ](https://github.com/eclipse-ee4j/openmq).

Then run:

```
cd mq/main/packager-opensource
ant buildcclient
```

This will build a shared library in the `mq/binary` directory for your system, e.g. `mq/binary/linux/opt/obj/cclient/libmqcrt.so.6.6.0`

Install it into the system as `mqcrt` library. E.g. on Linux, rename it to `libmqcrt.so` and copy it to `/usr/lib` directory. Or set the `LD_LIBRARY_PATH` to point to the directory that contains the library file.

An example client in C: [Producer.c](https://github.com/eclipse-ee4j/openmq/blob/b68a32106183471c23bf25d21fc80eb1882ce0aa/mq/src/share/cclient/examples/C/producer_consumer/Producer.c)

#### Idea 2

Instead of using OpenMQ-specific C binding, create a small bridge Java application, that listens to POST HTTP requests and forwards them as JMS messages.

Guardian service will send events as POST requests, the bridge app will turn them into JMS messages, and a Jakarta EE application will consume them via `BatteryGuardianEventsBean` as in Idea 1.

#### Idea 3

Use MQTT broker instead of a JMS broker. MQTT is not supported by standard Jakarta EE APIs. You can either use Azul's [MQTT cloud connector](https://docs.azul.com/payara/technical-documentation/ecosystem/connector-suites/cloud-connectors/mqtt.html) (the "jakarta" version MQTT-1.0.0) - it uses the standard JCA connetor atchitecture and should work with any Jakarta EE server. Or you can use any suitable Java MQTT client.

### Process Battery Guardian events

#### Idea 1

Store the events into an SQL database. Use Jakarta Persistence and an external SQL database, e.g. PostgreSQL, using its JDBC driver.

#### Idea 2

Store events into a time-series database using Jakarta NoSQL and Eclipse JNoSQL (https://github.com/eclipse-jnosql/jnosql-databases#time-series). 

GlassFish 8 supports Jakarta Data repositories over NoSQL databases natively - define a Jakarta Data repository and configure the corresponding JNoSQL database driver. Follow the [GlassFish documentation](https://docs.omnifish.ee/glassfish/latest/application-development-guide/jakarta-data.html#configuring-nosql-data-repositories)

#### Idea 3

Use LLM omdel to summarize the events collected over a specified interval. You can create a UI using Jakarta Faces and optinally Primefaces library, which allows specifying the interval and displays the reply from the LLM.