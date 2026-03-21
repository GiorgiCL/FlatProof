# FlatProof

Tamper-evident apartment handover and damage dispute system.

## Problem

Rental and handover disputes often happen because landlords and tenants disagree about the original condition of a property. Photos can be edited, reports can be changed later and timestamps are often not trusted enough.

## Solution

FlatProof is a backend system that allows users to create inspection reports for properties, attach room condition records and evidence files, finalize reports, generate deterministic SHA-256 hashes and anchor final report hashes on blockchain for later integrity verification.

## Core Features

- User registration and login with JWT authentication
- Property management
- Inspection report creation
- Room inspections and condition items
- Evidence file upload with SHA-256 file hashing
- Report finalization and tamper detection
- Blockchain anchoring of final report hashes
- Blockchain verification of finalized reports

## Tech Stack

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- MySQL
- Web3j
- Ganache
- Solidity
- Remix IDE

## Why Blockchain Is Used

Blockchain is used only for one narrow and justified purpose: anchoring finalized report hashes.

The system stores only the final report hash on-chain, not the whole report or uploaded files. This provides tamper evidence and proof that a report existed in a specific finalized form.

## Project Architecture

[put diagram here later]

## API Overview

### Auth
- POST /api/auth/register
- POST /api/auth/login

### Properties
- POST /api/properties
- GET /api/properties
- GET /api/properties/{id}

### Reports
- POST /api/properties/{propertyId}/reports
- GET /api/reports/{reportId}
- GET /api/properties/{propertyId}/reports
- POST /api/reports/{reportId}/finalize
- GET /api/reports/{reportId}/verify-hash

### Rooms and Items
- POST /api/reports/{reportId}/rooms
- POST /api/rooms/{roomId}/items
- GET /api/reports/{reportId}/details

### Evidence
- POST /api/reports/{reportId}/evidence
- POST /api/items/{itemId}/evidence
- GET /api/reports/{reportId}/evidence

### Blockchain
- POST /api/reports/{reportId}/anchor
- GET /api/reports/{reportId}/verify-blockchain

## Local Setup

### Prerequisites
- Java 17
- Maven
- MySQL
- Ganache
- Remix IDE

### Environment
Use application-local.properties for local secrets and local blockchain settings.

### Run
1. Start MySQL
2. Start Ganache
3. Ensure deployed contract address and Ganache private key are configured
4. Start Spring Boot application

## Demo Flow

1. Register or login
2. Create a property
3. Create an inspection report
4. Add rooms and condition items
5. Upload evidence
6. Finalize the report
7. Verify local hash
8. Anchor report on blockchain
9. Verify against blockchain

Client / Postman
↓
Controllers
↓
Services
↓
Repositories
↓
MySQL

Services
↓
File Storage (uploads folder)

Services
↓
ReportHashService
↓
BlockchainAnchorService
↓
Ganache / Smart Contract

## Future Improvements

- PDF export
- Better exception taxonomy
- More integration tests
- Image type validation
- Public testnet deployment
- Frontend client

## Author

Giorgi Tabatadze