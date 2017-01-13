Spring Boot Autoconfiguration workshop
==============

Dette prosjektet inneholder oppgaver til en workshop om Spring Boot Autoconfiguration.
Klon repoet og følg oppgavene under. [NOE om prosjektstruktur her]


Oppgave 1
---------
1. Klon gitHub prosjekt. Finn tomt maven prosjekt. (Eller lag nytt?)
1. Bootify it: Legg til spring-boot-starter-parent som parent i pom.xml
```
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>1.4.1.RELEASE</version>
</parent>
```
1. Start og se at det virker.
```
mvn spring-boot:run
```
1. Legg til spring-boot-starter-web som en dependency i pom.xml. Dette vil inkludere Tomcat
og alt som trengs for en webapp. Hvis man ønsker å bruke Jetty eller Undertow i stedet legger 
man den bare til som en egen dependency og eksluderer Tomcat fra spring-boot-starter-web.
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```
1. Start og se at det virker og starter på port 8080. 
```
mvn spring-boot:run
```
1. Lag en REST-controller (Copy-paste)
1. Start og se at det virker.
```
mvn spring-boot:run
```
1. Legg til Actuators?


Oppgave 2
---------
1. Lag et enkelt log-filter i **autoconfig-prosjektet**. (Copy-paste)
1. Installer.
```
mvn clean install
```
1. Legg til Autoconfig-prosjektet som dependency i webapp-prosjektet.
```
<dependency>
    <groupId>no.bouvet</groupId>
    <artifactId>autoconfig-example</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```
1. Restart og se at logfilteret virker.
```
mvn spring-boot:run
```
1. Men hva hvis hovedprosjektet ikke er et webprosjekt?
Hvis vi fjerner spring-boot-starter-web fra pom.xml vil filteret feile.
Legg til @ConditionalOnWebApplication på filteret prøv om det virker da.

Husk å legge til spring-boot-starter-web igjen etterpå.

Oppgave 3
---------
1. Legg til JodaTimeAutoConfig og JodaTimeSerializer i autoconfig prosjektet. (Copy-paste)
1. Installer.
```
mvn clean install
```
1. Test. Det feiler. Hvorfor?

1. Legg til @ConditionalOnClass på JodaTimeAutoconfig.
```
@Configuration
@ConditionalOnClass(LocalDate.class)
public class JodaTimeAutoconfig {
...
```
1. Installer igjen
```
mvn clean install
```
1. Test igjen nå.


Oppgave 4
---------
1. Lag REST-klienten. Copy-paste?
1. Lag en enkel health-check for klienten i autoconfig. Bruker property.
1. Funker denne? Nei. Vi har ikke health-registry eller property. Og hva hvis vi ikke har klienten i alle prosjekter?
1. Legg til ConditionalOnClass(health, klient) og ConditionalOnProperty.
1. Installer, restart og test.


Oppgave 5
---------
Gjøre det samme med metrics?
