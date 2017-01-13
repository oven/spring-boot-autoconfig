Spring Boot Autoconfiguration workshop
==============

Dette prosjektet inneholder oppgaver til en workshop om Spring Boot Autoconfiguration.
Klon repoet og følg oppgavene under. [NOE om prosjektstruktur her]


Oppgave 1 (Lage Spring Boot prosjektet)
---------
I denne oppgaven skal vi begynne omtrent fra scratch, og skal ende opp med en rest-service som sier `Hello, World!`

Begynn med å klone github-prosjektet. Der finner du et tomt prosjekt som heter `webapp`. 

```
git clone https://github.com/oven/spring-boot-autoconfig.git
```

For å gjøre dette om til et spring-boot-prosjekt må du legge til spring-boot-starter-parent som parent i `pom.xml`

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>1.4.1.RELEASE</version>
    <relativePath/>
</parent>
```

Deretter må du finne `Main`-klassen og annotere denne med `@SpringBootApplication` og kalle `SpringApplication.run()`. Dette starter Spring. 

```java
@SpringBootApplication
public class Main {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Main.class, args);
    }
}
```

Start og se at det virker. Du burde se en banner hvor det står Spring Boot, før applikasjonen avslutter.

```
$ mvn spring-boot:run
[...]

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v1.4.1.RELEASE)
```

Legg til `spring-boot-starter-web` som en dependency i pom.xml. Dette vil inkludere Tomcat og alt som trengs for en webapp. Hvis man ønsker å bruke Jetty eller Undertow i stedet legger man den bare til som en egen dependency og ekskluderer Tomcat fra spring-boot-starter-web.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

Nå kan du legge til en REST-controller
```java
package no.bouvet.webapp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {
    @GetMapping
    public String hello() {
        return "hello, world!";
    }
}
```

Start og se at det virker.
```
mvn spring-boot:run
[...]

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v1.4.1.RELEASE)
2017-01-13 22:12:46.545  INFO 28226 --- [           main] s.b.c.e.t.TomcatEmbeddedServletContainer : 
Tomcat started on port(s): 8080 (http)
```

Åpne et nytt kommandovindu og test tjenesten:
```
$ curl http://localhost:8080

hello, world!
```



Oppgave 2 (Legge til autoconfig prosjektet)
---------
Mer dokumentasjon: 

http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-developing-auto-configuration.html

Hele vitsen med autokonfig er at konfigurasjonen skal kunne ligge i et  annet prosjekt, slik at flere prosjekter bare trenger å legge til en dependency til autoconfig-prosjektet, og så virker det. 
Derfor skal vi lage et separat autoconfig prosjekt. 
Vi har laget et tomt prosjekt som heter `parking-autoconfig` som vi nå skal fylle ut.

Vi begynner med å lage en veldig enkel konfigurasjonsklasse i `parking-autoconfig`-prosjektet:

```java
package no.bouvet.autoconfig;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ParkingClientAutoConfiguration {
    public ParkingClientAutoConfiguration() {
        System.out.println("***** Configuring parking client");
    }
}
```

Denne inneholder foreløping ingen konfigurasjon, vi vil først bare verifisere at den blir kjørt.

Legg til en avhengighet til `parking-autoconfig` i pom.xml i webapp prosjektet:

```xml
<dependency>
    <groupId>no.bouvet</groupId>
    <artifactId>parking-autoconfig</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

Selv om vi nå har lagt til autoconfig-klassen vår så er ikke det nok. 
Spring scanner ikke klasser som ligger i andre jar-filer og vil dermed 
ikke oppdage at vi har en konfigurasjonsklasse der. Derfor må vi alltid ha en fil som heter `META-INF/spring.factories` i alle autoconfig prosjekter.  Denne inneholder en liste av alle klassene som Spring skal laste.
Vi har allerede laget filen i `resources/META-INF` men den er tom. Kopier inn følgende:
```
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
no.bouvet.autoconfig.ParkingClientAutoConfiguration
```
Dette forteller Spring at ParkingClientAutoConfiguration er en autokonfigurasjonsklasse.

1. Start webapp-prosjektet og sjekk at hello world skrives ut i loggen for å bekrefte alt fungerer som det skal.


Oppgave 3 (Enkel autoconfig for ParkingClient)
---------
I repoet ligger det et prosjekt som heter parking-rest-client. 
Dette er en veldig enkel REST-klient som henter informasjon om antall parkeringsplasser. 
Denne skal vi bruke for å representere et tredjepartsbibliotek som vi skal lage autokonfigurasjon for.

Vi begynner med å legge den til i pom.xml:

```xml
<dependency>
    <groupId>no.bouvet</groupId>
    <artifactId>parking-rest-client</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

Så lager vi en veldig enkel REST-metode som bruker den:

```java
@Autowired
ParkingClient parkingClient;

@GetMapping("/parking")
public CarParkStatus[] parking() throws IOException {
    return parkingClient.getData();
}
```

Hvis vi prøver å kjøre dette fungerer det ikke. Det er fordi `ParkingClient` ikke er en Spring bean så `@Autowired` fungerer ikke og `parkingClient` er `null`. 

Normalt ville man nå kanskje instansiert og konfigurert ParkingClient i en @Bean metode slik at den kan brukes, men vi vil heller gjøre dette med autoconfig. 

Vi begynner med å legge til en @Bean-metode i ParkingClientAutoconfig som kan produsere en bean for oss: 
```java
@Bean
public ParkingClient parkingClient() {
    ParkingClient parkingClient = new ParkingClient();
    return parkingClient;
}
```

For at dette skal kompilere må vi også legge til en avhengighet på `parking-rest-client`, men siden vi ikke vil dra inn denne avhengigheten i alle prosjekter som bruker autoconfig-example er den optional: 
```xml
<dependency>
    <groupId>no.bouvet</groupId>
    <artifactId>parking-rest-client</artifactId>
    <version>1.0-SNAPSHOT</version>
    <optional>true</optional>
</dependency>
```




1. Lag en enkel health-check for klienten i autoconfig. Bruker property.
1. Funker denne? Nei. Vi har ikke health-registry eller property. Og hva hvis vi ikke har klienten i alle prosjekter?
1. Legg til ConditionalOnClass(health, klient) og ConditionalOnProperty.
1. Installer, restart og test.



Oppgave 
---------
1. Lag et enkelt log-filter i **autoconfig-prosjektet**. (Copy-paste)
1. Installer.
```
mvn clean install
```
1. Legg til Autoconfig-prosjektet som dependency i webapp-prosjektet.
```xml
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

Oppgave 
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





Oppgave 
---------
Gjøre det samme med metrics?
