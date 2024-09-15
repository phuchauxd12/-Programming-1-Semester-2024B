# -Programming-1-Semester-2024B

## Project Overview

This project implements a comprehensive Car Dealership Management System (CDMS) for Auto136, a family-owned dealership
striving to streamline operations and enhance customer service. The system focuses on efficient management of service
appointments, sales transactions, and accurate recordkeeping.

## Features

### Data Management:

CRUD operations (Create, Read, Update, Delete) for Cars, Parts, Services, Transactions, Employees, and Clients.
Soft delete functionality for Cars and Employees.
Data loaded from input files initially, with updates instantly reflected in the files.

### User Management:

Three user roles: Manager, Employee (Salesperson & Mechanic), and Client.
Login with predefined username/password and role-based permissions.
User activity logging with history review capabilities (Manager can review all logs).

### Sales & Service Management:

Manage service appointments, including mechanic assignment, service types, and parts used.
Track sales transactions, including clients, salespeople, purchased items (cars & parts), discounts, and total amounts.
Membership management with Silver, Gold, and Platinum tiers based on spending thresholds and associated discounts.

### Statistics & Reporting:

Manager-specific functionalities:
Car sales calculations (monthly, daily, weekly).
Revenue calculations (daily, weekly, monthly) for services, car sales per salesperson, and overall revenue.
Listings of cars sold, transactions, services performed, and parts sold (daily, weekly, monthly).
Employee-specific functionalities:
Daily, weekly, and monthly revenue calculations.
Listings of cars managed and services performed (daily, weekly, monthly).

## Installation

To run this project on your local machine:

1. Clone the repository:
    ```bash
    git clonehttps://github.com/phuchauxd12/-Programming-1-Semester-2024B.git
    ```
2. Run the project at Main.java

## Users

Below are the predefined usernames, passwords, and their respective roles:

| Username    | Password     | Role        |
|-------------|--------------|-------------|
| manager     | pass123      | Manager     |
| m\_chris    | password123  | Mechanic    |
| m\_toto     | password123  | Mechanic    |
| s\_jackson  | password123  | Salesperson |
| s\_mina     | password123  | Salesperson |
| park\_jihyo | password1234 | Client      |
| im\_nayeon  | password012  | Client      |
| kim\_minji  | password678  | Client      |
| young\_k    | password678  | Client      |