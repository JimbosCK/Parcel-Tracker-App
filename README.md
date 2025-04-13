# Parcel Tracker

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

A simple web application for creating and tracking parcels.

## Overview

Parcel Tracker is a web-based application built using the Java Spring Boot framework. It allows users to:

* **Create Parcels:** Generate unique tracking codes for new shipments by providing an initial location.
* **Track Parcels:** View the current location and status of parcels using their tracking codes.
* **User Accounts:** (Currently implemented - *You can remove this line if you rolled back the user features*) Users can create accounts to potentially access more features in the future.

This project is intended as a learning exercise and a basic implementation of a parcel tracking system.

## Technologies Used

* **Backend:** Java, Spring Boot
* **Templating Engine:** Thymeleaf
* **Frontend:** HTML, CSS, Bootstrap
* **Build Tool:** Gradle
* **Database:** H2 (in-memory for development)
 on your current state*)

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
    *(Replace the URL with your repository's URL)*

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
* Basic user account creation. (*Remove if you rolled back*)
* Responsive design using Bootstrap.
* Clean and user-friendly interface.

## Future Enhancements

* Implement parcel history tracking.
* Add user roles and authorization.
* Allow logged-in users to manage their created parcels.
* Integrate with a real-world mapping service.
* More detailed parcel status updates.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Author

[Dimitris C. Karakasis/JimbosCK]

---