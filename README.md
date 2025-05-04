# Waste Wise: Efficient Waste Segregation & Tracking System

A desktop-based waste management system designed to efficiently handle waste pickup requests from residents, assign them to collectors, and manage operations via an admin interface.

## Project Overview

"Waste Wise" promotes sustainable waste management through proper tracking and segregation mechanisms. The system has three main user roles:

1. **Resident** — Requests waste pickup, provides waste type, quantity, and address.
2. **Collector** — Views assigned pickups, updates collection status.
3. **Admin** — Manages users, assigns collectors to requests, views feedback and generates reports.

## Features

- User Registration & Login (Residents, Collectors, Admin)
- Submit Waste Pickup Request
- Admin assigns pickup requests to Collectors
- Collectors update pickup status
- Admin views reports, requests, users, and feedback
- Residents can submit feedback on service
- Simple, user-friendly GUI built with Java Swing
- MySQL database integration with JDBC

## Tech Stack

- Frontend/UI: Java Swing
- Backend/Logic: Core Java (JDBC)
- Database: MySQL
- IDE: VS Code (or any Java IDE)

## Setup Instructions

### Prerequisites

1. Java Development Kit (JDK) 8 or higher
2. MySQL Server 5.7 or higher
3. MySQL Connector/J (JDBC driver for MySQL)

### Database Setup

1. Install MySQL Server if not already installed
2. Create a new database:
   ```sql
   CREATE DATABASE waste_wise_db;