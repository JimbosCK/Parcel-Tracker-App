# Parcel Tracker

A simple web application for creating and tracking parcels.

## Overview

Parcel Tracker is a web-based application built using the Java Spring Boot framework. It allows users to:

* **Create Parcels:** Generate unique tracking codes for new shipments by providing an initial location.
* **Track Parcels:** View the current location and status of parcels using their tracking codes.
* **Barcodes:** Generating barcodes for tracking codes.

This project is intended as a learning exercise and to gain familiarity with Java/Spring Boot technologies.

## Technologies Used

* **Backend:** Java, Spring Boot
* **Templating Engine:** Thymeleaf
* **Frontend:** HTML, CSS, Bootstrap
* **Build Tool:** Gradle
* **Database:** H2 (in-memory for development)

## Getting Started

To run the application locally, follow these steps:

1.  **Prerequisites:**
    * Java Development Kit (JDK) 17 or higher
    * Gradle 7.0 or higher

2.  **Clone the Repository:**
    ```bash
    git clone git@github.com:JimbosCK/parcel-tracker-app.git
    cd parcel-tracker-app
    ```

3.  **Build the Application:**
    ```bash
    .\gradlew.bat build  
    ```

4.  **Run the Application:**
    ```bash
    .\gradlew.bat bootrun
    ```

5.  **Access the Application:**
    Open your web browser and navigate to `http://localhost:8080`.

## Features

* Create new parcel entries.
* Track existing parcels using their tracking code.
* Parcel history (location,status and ETA changes are saved and displayed).
* Barcode generation for parcels.
* Responsive design using Bootstrap.
* Clean and user-friendly interface.

## Future Enhancements

* Add user roles and authorization.
* Allow logged-in users to manage their created parcels.
* Integrate with a real-world mapping service.
* More detailed parcel status updates.

## Author

[Dimitris C. Karakasis/JimbosCK]

---
