# SmartLedger AI

A full-stack **bank transaction fraud detection system** built using Java Spring Boot, MySQL, and Google Gemini AI.

## Features

- Detects suspicious bank transactions
- Calculates fraud score from **0–100**
- Classifies transactions as **SAFE** or **SUSPICIOUS**
- Uses Google Gemini to generate fraud explanations
- Stores transactions in MySQL
- Provides REST APIs
- Simple HTML/CSS/JavaScript dashboard

## Tech Stack

- **Frontend:** HTML, CSS, JavaScript
- **Backend:** Java 17, Spring Boot
- **Database:** MySQL
- **ORM:** Spring Data JPA / Hibernate
- **AI:** Google Gemini API
- **Build Tool:** Maven

## How It Works

```text
Frontend
   ↓
Spring Boot REST API
   ↓
Fraud Detection Service
   ↓
Fraud Score
   ↓
Gemini AI Explanation
   ↓
MySQL Database
```

### Fraud Rules

| Rule | Score |
|---|---:|
| Amount > ₹50,000 | +30 |
| Sender = Receiver | +30 |
| Transaction between 12 AM–5 AM | +20 |
| Amount divisible by ₹10,000 | +20 |

```text
0–49   → SAFE
50–100 → SUSPICIOUS
```

## Project Structure

```text
smartledger_ai/
├── backend/
│   ├── src/main/java/
│   │   ├── controller/
│   │   ├── model/
│   │   ├── repository/
│   │   └── service/
│   └── pom.xml
│
├── frontend/
│   ├── index.html
│   ├── script.js
│   └── style.css
│
├── .env
├── .gitignore
└── README.md
```

## Requirements

- Java 17
- Maven
- MySQL 8+
- Google Gemini API Key

## Setup

Create the database:

```sql
CREATE DATABASE smartledger;
```

Configure environment variables:

```env
DB_USERNAME=root
DB_PASSWORD=your_mysql_password
GEMINI_API_KEY=your_gemini_api_key
```

Run the backend:

```bash
cd backend
mvn spring-boot:run
```

Backend runs at:

```text
http://localhost:8080
```

Open `frontend/index.html` to use the dashboard.

## API Endpoints

```text
POST /transactions/add
GET  /transactions/all
GET  /transactions/one/{id}
```

## Limitations

This project uses **rule-based fraud detection** and is intended for **educational and portfolio purposes**, not production banking.
