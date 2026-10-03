# Jakarta EE Hourglass AI Modeler

![Jakarta EE official logo](jakarta_ee_logo_schooner_color_horizontal_default.svg)

## Challenge

This is a template project for the Jakarta EE Hourglass AI Modeler challenge.

## What Is Jakarta EE 11?

Jakarta EE is an open, community-driven platform for building modern cloud-native Java applications.

Jakarta EE 11 continues that direction with a strong focus on developer productivity and performance, including the new Jakarta Data 1.0 specification and broad API updates across the platform profiles.

Learn more: [Jakarta EE 11 Release](https://jakarta.ee/release/11/)

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
cd hourglass-template
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
mvn clean package wildfly:run
```

#### Open Liberty
```bash
mvn clean package liberty:run
```

### 3. Test the Server

If you run with Maven directly, test the endpoint at:

`http://localhost:8080/clepsammia/api/hello`

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
curl http://localhost:28080/clepsammia/api/hello
```

Expected response:

`Hello from Dukes!`

### 4. Stop the Server

```bash
docker compose down
```


