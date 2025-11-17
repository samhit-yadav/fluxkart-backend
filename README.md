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
Description: Identifies and consolidates contact information based on email and/or phone number.

Request Body:

```
{
    "email": "example@domain.com",
    "phoneNumber": "1234567890"
}
```

Response Format:
```
{
    "contact": {
        "primaryContactId": 1,
        "emails": ["primary@email.com", "secondary@email.com"],
        "phoneNumbers": ["1234567890", "0987654321"],
        "secondaryContactIds": [2, 3, 4]
    }
}
```


🧪 Test Cases
Test Case 1: New Contact Creation.

Scenario: First-time contact with unique email and phone.

Request:
```
{
	"email": "mcfly@hillvalley.edu",
	"phoneNumber": "123456"
}
```

Expected Response:
```
{
    "contact": {
        "primaryContactId": 1,
        "emails": ["john@example.com"],
        "phoneNumbers": ["123456"],
        "secondaryContactIds": []
    }
}
```

Test Case 2: Email Match - New Phone.

Scenario: Existing email with new phone number.

Request:
```
{
    "email": "john@example.com",
    "phoneNumber": "9876543210"
}
```

Expected Response:
```
{
    "contact": {
        "primaryContactId": 1,
        "emails": ["john@example.com"],
        "phoneNumbers": ["1234567890", "9876543210"],
        "secondaryContactIds": [2]
    }
}
```

Test Case 3: Phone Match - New Email.

Scenario: Existing phone with new email address.

Request:
```
{
    "email": "john.doe@company.com",
    "phoneNumber": "1234567890"
}
```

Expected Response:
```
{
    "contact": {
        "primaryContactId": 1,
        "emails": ["john@example.com", "john.doe@company.com"],
        "phoneNumbers": ["1234567890"],
        "secondaryContactIds": [2]
    }
}
```

Test Case 4: Contact Consolidation.

Scenario: Two separate primary contacts get linked through a common contact.

Setup:

sql-- Two existing primary contacts.
```
INSERT INTO contacts VALUES (1, '1111111111', 'alice@example.com', NULL, 'PRIMARY', '2023-04-01', '2023-04-01', NULL);
INSERT INTO contacts VALUES (2, '2222222222', 'bob@example.com', NULL, 'PRIMARY', '2023-04-02', '2023-04-02', NULL);
```

Request: Contact with Alice's email and Bob's phone.
```
{
    "email": "alice@example.com",
    "phoneNumber": "2222222222"
}
```

Expected Response:
```
{
    "contact": {
        "primaryContactId": 1,
        "emails": ["alice@example.com", "bob@example.com"],
        "phoneNumbers": ["1111111111", "2222222222"],
        "secondaryContactIds": [2]
    }
}
```


