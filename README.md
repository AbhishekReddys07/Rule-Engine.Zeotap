# Rule Engine Project

## Overview
This project implements a rule engine that allows users to create, combine, and evaluate rules using an Abstract Syntax Tree (AST). The engine supports dynamic rule creation and evaluation based on user-defined attributes.

## Table of Contents
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Design Structure](#design-structure)
- [API Endpoints](#api-endpoints)
- [Installation](#installation)
- [Usage](#usage)
- [Cloning the Repository](#cloning-the-repository)
- [Running the Application](#running-the-application)
- [Dependencies](#dependencies)
- [License](#license)

## Features
- **Rule Creation**: Create individual rules and generate their AST representation.
- **Rule Combination**: Combine multiple rules to reflect complex logic.
- **Rule Evaluation**: Evaluate rules against dynamic attributes.
- **Error Handling**: Implement error handling for invalid rule strings and formats.
- **Data Validation**: Validate attributes to ensure data integrity.
- **Rule Modification**: Modify existing rules as needed.

## Technologies Used
- **Java**: Programming language for backend development.
- **Spring Boot**: Framework for building the RESTful service.
- **Hibernate**: ORM for database interactions.
- **MySQL**: Database management system for rule storage.
- **HTML/CSS/JavaScript**: Frontend technologies for user interaction.
- **JSON**: Format for data exchange.

## Design Structure
The project follows a three-tier architecture:

1. **Presentation Layer**: 
   - **Description**: Responsible for user interaction.
   - **Technologies**: HTML, CSS, JavaScript.

2. **Service Layer**:
   - **Description**: Contains business logic for rule operations.
   - **Components**: 
     - **RuleService**: Handles rule creation, combination, and evaluation logic.

3. **Data Layer**:
   - **Description**: Manages data persistence and retrieval.
   - **Technologies**: Hibernate for ORM, MySQL for database.

### Main Components
- **Node Class**: Represents the nodes in the AST.
- **RuleController**: Manages HTTP requests for creating, combining, and evaluating rules.
- **Entity Classes**: Defines the database entities for rules and conditions.

## API Endpoints
### 1. Create Rule
- **Endpoint**: `POST /create_rule`
- **Description**: Creates a new rule and returns its AST representation.
- **Request Body**:
    ```json
    {
        "rule_string": "(age > 30 AND department = 'Sales')"
    }
    ```

### 2. Combine Rules
- **Endpoint**: `POST /combine_rules`
- **Description**: Combines multiple rules into a single AST.
- **Request Body**:
    ```json
    {
        "rules": [
            "(age > 30 AND department = 'Sales')",
            "(salary < 50000 OR experience > 5)"
        ]
    }
    ```

### 3. Evaluate Rule
- **Endpoint**: `POST /evaluateRule`
- **Description**: Evaluates a rule against provided data attributes.
- **Request Body**:
    ```json
    {
        "rule": "(age > 30 AND department = 'Sales')",
        "data": {
            "age": 35,
            "department": "Sales",
            "salary": 60000,
            "experience": 3
        }
    }
    ```
This endpoint can be called after using either the `create_rule` or `combine_rules` functions to determine if the user meets the specified criteria.


## Installation
### Prerequisites
- Ensure you have the following installed:
  - **Java 8**: Required for running the application.
  - **Docker**: For running MySQL in a container.

### Steps to Install
1. **Clone the Repository**:
   ```bash
   git clone https://[github.com/yourusername/rule-engine-project.git](https://github.com/AbhishekReddys07/Rule-Engine.Zeotap) :  cd rule-engine-project

2. **Run MySQL Container: Use Docker to run a MySQL instance:**
docker run --name mysql-container -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=rule_

3. **Build the Application: Use Maven to build the project:**
   ./mvnw clean install

## Usage
> Start the Application:./mvnw spring-boot:run
> Access the Application: Open your browser and navigate to http://localhost:8080 to access the application interface.
> Interact with API Endpoints: Use tools like Postman to send requests to the defined API endpoints.
Note: This will start the application on localhost:8080.

## Dependencies
Docker or Podman: For running containers.
MySQL Workbench: For database management.
Maven: For building and managing the project.
Java 8: Ensure you have at least Java 8 installed but best to use Java 17 or higher.

