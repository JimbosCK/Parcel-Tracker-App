# Parcel Tracker

A simple web application for creating and tracking parcels.
This project is intended as a learning exercise and to gain familiarity with Java/Spring Boot and Kafka technologies.

## Table of Contents

* [Overview](#overview)
* [Technologies Used](#technologies-used)
* [Getting Started](#getting-started)
* [Features](#features)
* [System Architecture](#system-architecture)
* [Architecture Highlights](#architecture-highlights)
* [Future Enhancements](#future-enhancements)

## Overview

Parcel Tracker is a web-based application built using the Java Spring Boot framework. It allows users to:

* **Create Parcels:** Generate unique tracking codes for new shipments by providing an initial location.
* **Track Parcels:** View the current location and status of parcels using their tracking codes.
* **Monitor Live Statistics:** A dedicated control panel provides real-time aggregated statistics of all parcels, reflecting status changes instantly using Kafka.
* **Barcodes:** Generating barcodes for tracking codes.

## Technologies Used

* **Backend:** Java, Spring Boot
* **Messaging:** Apache Kafka (for event-driven communication)
* **Real-time Communication:** WebSockets (Spring STOMP over SockJS)
* **Templating Engine:** Thymeleaf
* **Frontend:** HTML, CSS, Bootstrap
* **Build Tool:** Gradle
* **Database:** PostgreSQL

## Getting Started

To run the application locally, follow these steps:

1.  **Prerequisites:**
    * Java Development Kit (JDK) 17 or higher
    * Gradle 7.0 or higher
    * PostgreSQL installed and running
    * **Apache Kafka**: A running Kafka broker instance (e.g., via Docker or a local installation).

2.  **Clone the Repository:**
    ```bash
    git clone git@github.com:JimbosCK/parcel-tracker-app.git
    cd parcel-tracker-app
    ```

3.  **Configure Database Environment Variables:**
    You can configure the PostgreSQL database connection details by setting environment variables. This can be done in a few ways:

    ```json
    "env": {
        "DATABASE_URL": "jdbc:postgresql://localhost:5432/your_database_name",
        "DATABASE_USERNAME": "your_username",
        "DATABASE_PASSWORD": "your_password"
    }
    ```

    **Note:** Replace `your_database_name`, `your_username`, and `your_password` with your actual PostgreSQL credentials.

4.  **Build the Application:**
    ```bash
    .\gradlew.bat build
    ```

5.  **Run the Application:**
    ```bash
    .\gradlew.bat bootRun
    ```

6.  **Access the Application:**
    Open your web browser and navigate to `http://localhost:8080`.

## Features

* Create new parcel entries.
* Track existing parcels using their tracking code.
* Parcel history (location, status and ETA changes are saved and displayed).
* Barcode generation for parcels.
* **Real-time Parcel Statistics:** View live aggregated counts of parcels by status (PENDING, IN_TRANSIT, DELIVERED, RETURNED) on a dedicated control panel.
* **Event-Driven Architecture:** Parcel status changes are published to and consumed from Apache Kafka topics.
* **Real-time UI Updates:** The control panel automatically updates using WebSockets whenever a parcel status changes, providing an immediate visual representation of the system's state.
* Responsive design using Bootstrap.
* Clean and user-friendly interface.

## System Architecture

This diagram illustrates the core components and data flow for the real-time control panel implementation, showcasing the integration of Kafka for event processing and WebSockets for live UI updates.

1.  Initial Page Load / Data Retrieval
2.  Parcel Status Update (Producing an Event)
3.  Kafka Event Consumption and Aggregation
4.  Real-time UI Update via WebSockets

![Control Panel Implementation Diagram](control%20panel%20diagram.png)

### Architecture Highlights

* **Kafka Integration:** Utilizes Apache Kafka as a robust, scalable message broker for decoupling parcel update events from their processing.
* **WebSocket Communication:** Employs Spring's STOMP over SockJS to establish communication for pushing real-time updates to connected clients.
* **Event-Driven Aggregation:** A dedicated service (`ParcelStatsAggregator`) consumes Kafka events to maintain in-memory, up-to-date statistics which are then broadcast to the UI.

## Future Enhancements

* Add user roles and authorization.
* Expand Kafka usage for other internal system events.
