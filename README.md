# SubTrack

Spring Boot MVC aplikacija za upravljanje i pracenje digitalnih pretplata.

## Tehnologije

- Backend: Spring Boot (Spring MVC)
- Frontend: Thymeleaf (`templates`) + static resursi (`static`)

## Pokretanje aplikacije (backend + frontend)

U ovom projektu frontend je deo iste Spring Boot aplikacije (nije poseban React/Vue projekat).

Iz root direktorijuma:

```bash
./mvnw clean test
./mvnw spring-boot:run
```

Aplikacija ce biti dostupna na:

`http://localhost:8080`

## Kako da pratis napredak izmena

1. Drzi server pokrenut:

```bash
./mvnw spring-boot:run
```

2. Otvori u browser-u:

`http://localhost:8080`

3. Kada menjas:
- HTML (`src/main/resources/templates/...`) samo osvezi stranicu.
- CSS (`src/main/resources/static/css/...`) samo osvezi stranicu.
- Java klase (`src/main/java/...`) DevTools automatski restartuje aplikaciju.

## Korisne komande

- Pokretanje testova:

```bash
./mvnw clean test
```

- Brza provera statusa izmena:

```bash
git status
```
