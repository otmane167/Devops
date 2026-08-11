# Devops — Produit Management

A Java application for managing products (`produit-management`), built as part of a DevOps learning track (CI/CD, build automation, etc.).

## Overview

This repository contains a `produit-management` module implementing product management logic in Java. It's intended as a base project for practicing DevOps workflows — build automation, containerization, and CI/CD pipelines (e.g. with Jenkins — see the companion `TP_jenkins` repo).

## Tech Stack

- **Language:** Java
- **Module:** `produit-management`

## Project Structure

```
Devops/
└── produit-management/   # Core Java module for product management
```

## Getting Started

### Prerequisites
- JDK 11+ (adjust to match the module's actual target version)
- Maven or Gradle, depending on the build tool used inside `produit-management`

### Build & Run

```bash
git clone https://github.com/otmane167/Devops.git
cd Devops/produit-management

# If Maven:
mvn clean install
mvn spring-boot:run   # or the appropriate run command

# If Gradle:
./gradlew build
./gradlew run
```
*(Adjust the commands above once you confirm the build tool used in `produit-management`.)*
