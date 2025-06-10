# FluxKart - Contact Identity Resolution API

A full-stack application for contact identity resolution based on email addresses and phone numbers.

## 🚀 Live Demo

- Frontend: https://fluxkart-frontend.vercel.app
- Backend API: https://fluxkart-backend-02yd.onrender.com

## 🛠 Tech Stack

Backend: Java, Spring Boot, PostgreSQL, Maven  
Frontend: React, Vite, JavaScript  
Deployment: Render (Backend), Vercel (Frontend)

## 📚 API Endpoints

### GET /
Health check endpoint

### POST /identify
Identify and resolve contact information

Request:
json
{
  "email": "john.doe@example.com",
  "phoneNumber": "1234567890"
}


Response:
json
{
  "contact": {
    "primaryContactId": 1,
    "emails": ["john.doe@example.com"],
    "phoneNumbers": ["1234567890"],
    "secondaryContactIds": []
  }
}
```




