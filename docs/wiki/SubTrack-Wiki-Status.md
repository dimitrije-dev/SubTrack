# SubTrack - Status Projekta i Teorijska Osnova

## 1. Kratak opis projekta
SubTrack je Spring Boot MVC web aplikacija za pracenje digitalnih pretplata (subscriptions), troskova, obnova i budzeta.

Tehnoloski okvir:
- Backend: Spring Boot + Spring MVC
- View sloj: Thymeleaf
- Stilizacija: staticki CSS (bez React/Vue/Angular)
- Podaci: in-memory (`ApplicationStorage`, bez baze)
- AOP: planirano za audit/performance aspekte

Osnovni package projekta:
`rs.ac.metropolitan.subtrack`

---

## 2. Sta je uradjeno do sada

### Zavrsene stavke
1. Initial Spring Boot MVC project setup
2. Add model classes and enums
3. Implement application scope storage
4. Add demo data initializer
5. Implement subscription service logic
6. Add category and provider services
7. Add payment and reminder services
9. Add dashboard controller and template
10. Implement subscription CRUD controller and views
18. Add CSS responsive dashboard styling

### Delimicno uradjeno
8. Create dashboard service with cost calculations
- DashboardService postoji i koristi se.
- Deo KPI logike je trenutno mock/hardcoded za UX prikaz i treba ga finalno prebaciti na 100% realne kalkulacije iz storage-a.

### Prakticno implementirano vise od plana po fazama
Iako neke stavke na listi jos nisu formalno zatvorene, projekat vec ima:
- vise dashboard stranica (`/dashboard`, `/subscriptions`, `/calendar`, `/analytics`, `/reminders`, `/budgets`, `/settings`)
- moderan SaaS izgled sa sidebar/header/cards/tabelama
- logo u static assets

---

## 3. Sta jos treba da se uradi

Preostale stavke iz roadmap-a:
11. Implement category CRUD
12. Implement provider CRUD
13. Implement payment records
14. Implement renewal reminders
15. Add Spring AOP audit logging
16. Add audit log page
17. Add performance aspect for service methods
19. Write theory documentation for Spring, IoC, DI and AOP
20. Final cleanup and screenshots

Napomena:
- Stavka 19 je ovim dokumentom zapoceta (teorijski deo ispod), ali moze da se prosiri kao posebna wiki strana sa dijagramima i primerima.

---

## 4. Trenutna arhitektura (high-level)

### 4.1 Model sloj
Klase (domen):
- `Subscription`, `Category`, `Provider`, `PaymentRecord`, `RenewalReminder`, `UserProfile`, `Budget`, `ActivityLog`, `AuditLog`
- enum-i: `BillingCycle`, `SubscriptionStatus`

### 4.2 Storage sloj
`ApplicationStorage` (`@Component`, `@ApplicationScope`):
- centralno memorijsko skladiste za liste i user profile
- jedan shared bean kroz ceo lifecycle aplikacije

### 4.3 Service sloj
Servisi:
- `SubscriptionService`
- `CategoryService`
- `ProviderService`
- `PaymentService`
- `ReminderService`
- `DashboardService`

### 4.4 Controller sloj
Kontroleri:
- `DashboardController`
- `SubscriptionController`
- `CalendarController`
- `AnalyticsController`
- `ReminderController`
- `BudgetController`
- `SettingsController`

### 4.5 View sloj (Thymeleaf)
Template/fregmenti:
- `dashboard.html`, `subscriptions.html`, `calendar.html`, `analytics.html`, `reminders.html`, `budgets.html`, `settings.html`
- `subscription-form.html`, `subscription-detail.html`
- `fragments/sidebar.html`, `fragments/header.html`
- `layout.html`

---

## 5. Teorijska osnova projekta

## 5.1 Spring Boot
Spring Boot ubrzava razvoj tako sto automatski konfigurise aplikaciju na osnovu dependency-ja i konvencija.

Prednosti u ovom projektu:
- brzi start bez rucnog XML setup-a
- ugrađen embedded server (Tomcat)
- jednostavno pokretanje preko `mvn spring-boot:run`

## 5.2 Spring MVC
Spring MVC primenjuje obrazac:
- Controller prima HTTP zahtev
- Service obradjuje poslovnu logiku
- Model prenosi podatke
- Thymeleaf renderuje HTML view

Ovim je postignuta jasna separacija odgovornosti i lakse odrzavanje koda.

## 5.3 IoC i DI
IoC (Inversion of Control):
- objekti se ne kreiraju rucno po aplikaciji,
- njima upravlja Spring kontejner.

DI (Dependency Injection):
- zavisnosti se ubacuju kroz konstruktor.
- primer: `DashboardController(DashboardService dashboardService)`

Benefiti:
- manja sprega (low coupling)
- testabilnost
- laksa zamena implementacija

## 5.4 Scope bean-ova i `@ApplicationScope`
`ApplicationStorage` je `@ApplicationScope`, sto znaci:
- postoji jedna instanca storage bean-a za celu aplikaciju
- svi kontroleri/servisi rade nad istim in-memory podacima
- podaci traju dok aplikacija radi
- restart aplikacije resetuje podatke

To je planska zamena za bazu u skolskom projektu.

## 5.5 Thymeleaf
Thymeleaf je server-side template engine.

Prednosti:
- prirodna integracija sa Spring MVC (`Model` atributi)
- lako templating fragmenta (`sidebar`, `header`)
- bez frontend build procesa

## 5.6 AOP (planirana faza)
AOP (Aspect-Oriented Programming) odvaja cross-cutting concerns od business logike:
- audit logovanje akcija
- merenje performansi metoda

Planirano:
- aspekt za audit (`@Around`/`@AfterReturning`)
- aspekt za performance telemetry nad service metodama
- prikaz audit log zapisa na posebnoj stranici

---

## 6. Predlog redosleda za nastavak
1. Category CRUD (controller + view)
2. Provider CRUD (controller + view)
3. Payment records (controller + view)
4. Renewal reminders (controller + view)
5. DashboardService refactor na 100% realne kalkulacije iz storage-a
6. AOP audit + performance aspekti
7. Audit log page
8. Final cleanup + screenshotovi + final wiki polish

---

## 7. Kako pokrenuti i proveriti
Iz root direktorijuma projekta:

```bash
./mvnw clean test
./mvnw spring-boot:run
```

Glavne rute:
- `http://localhost:8080/dashboard`
- `http://localhost:8080/subscriptions`
- `http://localhost:8080/calendar`
- `http://localhost:8080/analytics`
- `http://localhost:8080/reminders`
- `http://localhost:8080/budgets`
- `http://localhost:8080/settings`

---

## 8. Zakljucak
Projekat je uspesno presao iz osnovnog setup-a u funkcionalan MVC dashboard prototip sa in-memory storage slojem i vise servisnih komponenti.

Sledece faze su fokusirane na zatvaranje CRUD funkcionalnosti za preostale entitete i uvodjenje AOP audita/performance merenja, cime se projekat zaokruzuje i funkcionalno i teorijski.
