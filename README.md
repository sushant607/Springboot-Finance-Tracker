# Springboot Finance Tracker

A lightweight personal finance tracking web application built using **Spring Boot**. This project enables users to manage income, expenses, and monitor their financial health all from a secure, modern and easy-to-use interface.

## Features

- Add, edit and delete income and expense records
- Categorize transactions for better budgeting insights
- View summaries and analytics for your finances
- Responsive design for both desktop and mobile
- Built with Java and Spring Boot for scalability and easy dependency management

## Getting Started

### Prerequisites

- Java 11 or newer
- Maven 3.x
- MySQL or any JDBC-compatible database

### Installation

1. **Clone the repository:**

   ```bash
   git clone https://github.com/sushant607/Springboot-Finance-Tracker.git
   cd Springboot-Finance-Tracker
   ```

2. **Configure your database:**

   Update your `src/main/resources/application.properties` with your MySQL database settings.

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/your_db
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

3. **Build the project:**

   ```bash
   ./mvnw clean install
   ```

4. **Run the application:**

   ```bash
   ./mvnw spring-boot:run
   ```

5. Access the app at `http://localhost:8080/`

## Project Structure

```
src/
  main/
    java/
      (Java source files)
    resources/
      application.properties
.gitattributes
.gitignore
pom.xml
mvnw / mvnw.cmd
```

## Technologies Used

- **Spring Boot** (Backend REST APIs)
- **Java**
- **Maven** (Build tool)
- **HTML** (Front-end templates)
- **MySQL** (or any JDBC database)

## Contributing

Pull requests are welcome! For major changes, please open an issue first to discuss what you would like to change.

## License

This project is open-source and available under the [MIT License](LICENSE).

## Acknowledgements

This project was developed as a simple demonstration of using Spring Boot for a personal finance tracker.

## Contact

For questions or suggestions, reach out via GitHub Issues.

[1] https://github.com/sushant607/Springboot-Finance-Tracker
