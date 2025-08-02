Springboot Finance Tracker
A secure, responsive personal finance web application built using Spring Boot and MySQL. Track income, expenses, budgets, and visualize your financial health—all in one user-friendly interface.

🚀 Features Overview
🔐 Authentication & Security
User registration and secure login with encrypted passwords (BCrypt)

Session-based authentication; user data privacy

CSRF-protected forms

💸 Transaction Management
Add, edit, and delete income and expense records

Categorize transactions (e.g., Food & Dining, Shopping, Salary, etc.)

Date-wise entry, quick access to recent transactions

Real-time filtering by type, date, or category

💹 Dashboard & Analytics
Overview cards: Total Income, Expenses, Balance, Budget Used

Transaction and expense summaries for the current period

Quick stats: transaction count, active budgets, categories used

Visual highlights for budgets exceeded or nearing limit

📊 Budget Planning
Create and manage monthly budgets per category

See remaining, spent, and total budget for each category

Color-coded progress bars: green (safe), yellow (warning), red (exceeded)

Alerts and warnings when a budget is approached or exceeded

📱 Responsive UI & UX
Clean, Bootstrap-styled templates

Mobile-friendly navigation and layout

Consistent branding & icons via Font Awesome

🛠️ Technology Stack
Backend: Spring Boot, Spring Security, Spring Data JPA (Hibernate)

Frontend: Thymeleaf templating, Bootstrap 5, Font Awesome

Database: MySQL (or H2; easy to swap in application.properties)

Testing: JUnit integration tests for core logic

✨ Live Previews (Screenshots)
Add screenshots here of your dashboard, transactions, and budgets pages

![Dashboard Screenshot ![Budgets Screenshot 🏗️ Project Structure

text
src/
  main/
    java/
    └── com/example/financetracker/
        controller/
        service/
        repository/
        entity/
        dto/
    resources/
        templates/
          dashboard.html
          budgets.html
          transactions.html
          login.html
          register.html
        static/
          css/
          js/
        application.properties
  test/
pom.xml
⚡ Getting Started
1. Clone the Repository
text
git clone https://github.com/sushant607/Springboot-Finance-Tracker.git
cd Springboot-Finance-Tracker
2. Configure the Database
Edit src/main/resources/application.properties:

text
spring.datasource.url=jdbc:mysql://localhost:3306/your_db_name
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password
spring.jpa.hibernate.ddl-auto=update
By default, the app works with MySQL. You can switch to H2 for testing (jdbc:h2:mem:financedb).

3. Build & Run
text
./mvnw clean install
./mvnw spring-boot:run
Visit http://localhost:8080

🎯 Usage Guide
Register & Log In
New users: Click Register, fill in your details, and log in.

Returning users: Use your username and password.

Add Transactions
Go to the Transactions page.

Select Income or Expense, enter the category, description, amount, and date.

Transactions are listed most recent first. You can edit or delete any record from the list.

Set Budgets
Switch to Budgets.

Pick a category, enter your monthly budget limit, and save.

The budget summary visualizes each category's performance for the month.

Visualize and Monitor
Dashboard shows your income, expenses, net balance, and budget usage.

Quick stats and highlights indicate financial health instantly.

🗃️ Key Code Features
Role-based security (can be extended for admin/audit roles)

Service layer for business logic separation and easy testing

Repository pattern for data access abstraction

Form validation at both the UI and backend levels

Modular UI templates (header/footer, page fragments for maintainability)

⚙️ Configuration
Change database type: Tweak spring.datasource.url in properties file.

Switch to a different port: Add server.port=YOUR_PORT in properties.
