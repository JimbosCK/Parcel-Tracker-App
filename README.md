# Parcel Tracker

A simple web application for creating and tracking parcels.
This project is intended as a learning exercise and to gain familiarity with Java/Spring Boot and Kafka technologies.

## Overview

Parcel Tracker is a web-based application built using the Java Spring Boot framework. It allows users to:

* **Create Parcels:** Generate unique tracking codes for new shipments by providing an initial location.
* **Track Parcels:** View the current location and status of parcels using their tracking codes.
* **Barcodes:** Generating barcodes for tracking codes.

## Technologies Used

* **Backend:** Java, Spring Boot
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
* Parcel history (location,status and ETA changes are saved and displayed).
* Barcode generation for parcels.
* Responsive design using Bootstrap.
* Clean and user-friendly interface.

## Future Enhancements

* Add user roles and authorization.
* Real-time Parcel Status Updates using Kafka.
