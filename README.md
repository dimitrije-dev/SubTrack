<p align="center">
  <img src="src/main/resources/static/img/logo.png" alt="SubTrack Logo" width="160" />
</p>

<h1 align="center">SubTrack</h1>
<p align="center">
  Modern Spring Boot MVC dashboard za pracenje digitalnih pretplata.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-0B132A?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21" />
  <img src="https://img.shields.io/badge/Spring_Boot-3.3.x-0A1C3D?style=for-the-badge&logo=springboot&logoColor=6DB33F" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/Spring_MVC-Web-0A1C3D?style=for-the-badge&logo=spring&logoColor=6DB33F" alt="Spring MVC" />
  <img src="https://img.shields.io/badge/Thymeleaf-Server_Side-0A1C3D?style=for-the-badge&logo=thymeleaf&logoColor=005F0F" alt="Thymeleaf" />
  <img src="https://img.shields.io/badge/Maven-Build-0A1C3D?style=for-the-badge&logo=apachemaven&logoColor=C71A36" alt="Maven" />
  <img src="https://img.shields.io/badge/Spring_AOP-Enabled-0A1C3D?style=for-the-badge&logo=spring&logoColor=6DB33F" alt="Spring AOP" />
</p>

## Project Overview
SubTrack je desktop web aplikacija koja pomaze korisniku da prati:
- aktivne pretplate
- troskove po ciklusu naplate
- datume obnove
- podsetnike i budzet status

Aplikacija koristi in-memory application scope storage (bez baze), zato je odlicna za razvoj, demo i akademske projekte.

## Tech Stack
- Backend: Spring Boot + Spring MVC
- View layer: Thymeleaf templates
- Styling: Static CSS (bez React/Vue/Angular)
- Storage: `ApplicationStorage` (`@ApplicationScope`)
- Build: Maven Wrapper (`./mvnw`)

## Current Architecture
- Package root: `rs.ac.metropolitan.subtrack`
- Layering:
- `model` za domenske objekte
- `storage` za centralno memorijsko skladiste
- `service` za poslovnu logiku
- `controller` za HTTP rute i view model
- `templates` + `static` za UI

## Run Locally
Iz root direktorijuma projekta:

```bash
./mvnw clean test
./mvnw spring-boot:run
```

Aplikacija je dostupna na:
- [http://localhost:8080](http://localhost:8080)
- [http://localhost:8080/dashboard](http://localhost:8080/dashboard)

## Main Routes
- `/dashboard`
- `/subscriptions`
- `/calendar`
- `/analytics`
- `/reminders`
- `/budgets`
- `/settings`

## Development Workflow
1. Pokreni aplikaciju:
```bash
./mvnw spring-boot:run
```
2. Menjaj HTML/CSS i radi refresh browsera.
3. Menjaj Java kod i Spring DevTools ce uraditi restart.
4. Pre svakog commit-a pokreni:
```bash
./mvnw clean test
```

## Project Rules
- Bez JPA i bez SQL baze.
- Bez Spring Security (u ovoj fazi projekta).
- Bez frontend framework-a i build alata.
- Sve ostaje u `rs.ac.metropolitan.subtrack` namespace-u.

## Status Snapshot
Zavrseno:
- Spring Boot setup
- model sloj + enum-i
- application scope storage
- demo data initializer
- subscription/category/provider/payment/reminder servisi
- dashboard + subscriptions + dodatne dashboard stranice
- responsive SaaS styling i logo integracija

Sledece:
- category/provider CRUD UI
- payment/reminder full pages
- AOP audit log + performance aspekti
- final dokumentacija i screenshotovi

## Useful Commands
```bash
# test
./mvnw clean test

# run
./mvnw spring-boot:run

# git check
git status
```
