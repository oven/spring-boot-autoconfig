Spring Boot Autoconfiguration workshop
==============

Dette prosjektet inneholder oppgaver til en workshop om Spring Boot Autoconfiguration.
Klon repoet og følg oppgavene under.


# Oppgave 1 (Lage Spring Boot prosjektet)

I denne oppgaven skal vi begynne omtrent fra scratch, og skal ende opp med en rest-service som sier `Hello, World!`

Begynn med å klone github-prosjektet. Der finner du et tomt prosjekt som heter `webapp`. 

```
git clone https://github.com/oven/spring-boot-autoconfig.git
```

For å gjøre dette om til et spring-boot-prosjekt må du legge til spring-boot-starter-parent som parent i `webapp/pom.xml`
Legg også til `spring-boot-starter-web` som en dependency i pom.xml. Dette vil inkludere Tomcat og alt som trengs for en webapp. Hvis man ønsker å bruke Jetty eller Undertow i stedet legger man den bare til som en egen dependency og ekskluderer Tomcat fra spring-boot-starter-web.


```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>1.4.1.RELEASE</version>
    <relativePath/>
</parent>
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
<dependency>

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

Nå kan du starte prosjektet og se at det virker. 
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

Dette gir deg en webapplikasjon som lytter på port 8080, men foreløpig gjør den ingenting. 

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



# Oppgave 2 (Legge til autoconfig prosjektet)

Mer dokumentasjon: 

http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-developing-auto-configuration.html

Hele vitsen med autokonfig er at konfigurasjonen skal kunne ligge i et  annet prosjekt, slik at flere prosjekter bare trenger å legge til en dependency til autoconfig-prosjektet, og så virker det. 
Derfor skal vi lage et separat autoconfig prosjekt. 
Vi har laget et tomt prosjekt som heter `autoconfig` som vi nå skal fylle ut.

- [ ] Lag en klasse i autoconfig-prosjektet, og kall den `ParkingClientAutoConfiguration`
- [ ] Annoter denne klassen med `@Configuration`, slik at den blir en spring-configklasse
- [ ] Registrer klassen i `autoconfig/src/main/resources/META-INF/spring.factories`
- [ ] Legg til avhengighet til `autoconfig` i `webapp/pom.xml`
- [ ] Skriv ut noe til skjermen i constructoren til `ParkingClientAutoConfiguration`, og se at dette kommer frem når du starter `webapp`

<details>
<summary><b>Klikk her for løsning</b></summary>

## Løsning

Vi begynner med å lage en veldig enkel konfigurasjonsklasse i `autoconfig`-prosjektet:

```java
package no.bouvet.autoconfig;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ParkingClientAutoConfiguration {
    public ParkingClientAutoConfiguration() {
        System.out.println("\n\n***** Configuring parking client\n\n");
    }
}
```

Denne inneholder foreløping ingen konfigurasjon, vi vil først bare verifisere at den blir kjørt.

Legg til en avhengighet til `autoconfig` i pom.xml i webapp prosjektet:

```xml
<dependency>
    <groupId>no.bouvet</groupId>
    <artifactId>autoconfig</artifactId>
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

Start webapp-prosjektet og sjekk at `Configuring parking client` skrives ut i loggen for å bekrefte alt fungerer som det skal.
</details>

# Oppgave 3 (Enkel autoconfig for ParkingClient)

I repoet ligger det et prosjekt som heter parking-rest-client. 
Dette er en veldig enkel REST-klient som henter informasjon om antall parkeringsplasser. 
Denne skal vi bruke for å representere et tredjepartsbibliotek som vi skal lage autokonfigurasjon for.

- [ ] Legg til avhengighet til parking-rest-client i `webapp/pom.xml`
- [ ] Lag en rest-endpoint som bruker en autowired ParkingClient til å returnere noe data
- [ ] Lag en configuration-klasse i `autoconfig` som oppretter en ParkingClient og setter endpoint til https://www.bergen.kommune.no/wsproxy/parkering.json
- [ ] Start applikasjonen og se at det virker


<details>
<summary><b>Klikk her for løsning</b></summary>

## Løsning

Vi begynner med å legge den til i `webapp/pom.xml`:

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
    parkingClient.setEndpoint(URI.create("https://www.bergen.kommune.no/wsproxy/parkering.json"));
    return parkingClient;
}
```

For at dette skal kompilere må vi også legge til en avhengighet på `parking-rest-client`, men siden vi ikke vil dra inn denne avhengigheten i alle prosjekter som bruker `autoconfig` er den optional. Det gjør at vi kan inkludere `autoconfig` i alle prosjektene våre, uavhengig av om vi ønsker å bruke den eller ikke. Mange av autoconfigene i Spring Boot fungerer på denne måten. 

```xml
<dependency>
    <groupId>no.bouvet</groupId>
    <artifactId>parking-rest-client</artifactId>
    <version>1.0-SNAPSHOT</version>
    <optional>true</optional>
</dependency>
```

Nå får vi automatisk opprettet en ferdig konfigurert `ParkingClient` og den blir autowired i `MyController`. Start webapp og sjekk at http://localhost:8080/parking fungerer.
</details>

# Oppgave 4

Det er jo litt upraktisk at URLen til `ParkingClient` er hardkodet, så vi flytter den ut i ekstern konfigurasjon. På den måten kan den styres med en property.

- [ ] Legg til et URI-parameter på bean-metoden til ParkingClient, og bruk @Value("${propertynavn}") for å få injisert denne fra application.yml
- [ ] Legg til propertyen i `webapp/.../application.yml` 
- [ ] Se at applikasjonen fortsatt virker
- [ ] Legg til `@ConditionalOnProperty` i `ParkingClientAutoConfiguration` for å unngå problemer hvis propertyen ikke er satt


<details>
<summary><b>Klikk her for løsning</b></summary>

## Løsning

Vi endrer ParkingClientAutoconfig slik at parkingClient får injisert en property:


```java
@Bean
public ParkingClient parkingClient(@Value("${parking.endpoint}") URI endpoint) {
    ParkingClient parkingClient = new ParkingClient();
    parkingClient.setEndpoint(endpoint);
    return parkingClient;
}
```

Vi kan nå legge til en property i `webapp/src/main/resources/application.yml`:
```yaml
parking.endpoint: https://www.bergen.kommune.no/wsproxy/parkering.json
```

Så nå kan vi styre hvilken URL som blir brukt med en property i vår egen applikasjon. Men hva hvis vi glemmer å gjøre det? Da får vi en feilmelding fra Spring, og applikasjonen nekter å starte: 
```
IllegalArgumentException: Could not resolve placeholder 'parking.endpoint' in string value "${parking.endpoint}" 
```

Prøv gjerne å kommentere ut linjen i `application.yml` (med #) og se hva som skjer.

Løsningen er å bare aktivere autokonfigurasjonen dersom propertyen `parking.endpoint` faktisk er satt. Det gjøres med annoteringen [`@ConditionalOnProperty`](http://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/autoconfigure/condition/ConditionalOnProperty.html). Legg til annoteringen på `ParkingClientAutoConfig` slik at den ser sånn ut:

```java
@Configuration
@ConditionalOnProperty("parking.endpoint")
public class ParkingClientAutoconfig {
...
```

[`@ConditionalOnProperty`](http://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/autoconfigure/condition/ConditionalOnProperty.html) gjør at konfigurasjonen bare blir aktivert hvis propertyen er satt. Annoteringen har flere valgfrie elementer, bla.a `havingValue` og `matchIfMissing` som lar oss tilpasse oppførselen.
</details>

# Oppgave 5

Litt av poenget med autokonfigurasjon er at den skal kunne være til stede, men ikke bli aktivert før den trengs. Derfor må vi også sørge for at den ikke blir aktivert hvis ikke det den skal konfigurere finnes på classpathen. Det gjøre med annoteringen [`@ConditionalOnClass`](http://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/autoconfigure/condition/ConditionalOnClass.html).

Legg til `@ConditionalOnClass` på `ParkingClientAutoconfig`:

```java
@Configuration
@ConditionalOnClass(ParkingClient.class)
@ConditionalOnProperty("parking.endpoint")
public class ParkingClientAutoconfig {
...
```

Nå kan vi inkludere autoconfig-prosjektet i alle prosjektene våre, men ParkingClientAutoconfig vil bare bli kjørt dersom vi faktisk har ParkingClient på classpathen. Dette blir brukt ekstremt mye i Spring Boot. Omtrent alle autokonfigurasjoner har `@ConditionalOnClass`

Prøv å kommentere ut avhengigheten i pom.xml og det som trengs i MyController og sjekk at applikasjonen starter, men `ParkingClientAutoConfig` ikke lenger blir kjørt (altså at `Configuring parking client` ikke blir skrevet ut i loggen). 

Prøv også å kommentere ut `@ConditionalOnClass`. Da vil Spring forsøke å kjøre autoconfigen til tross for at `ParkingRestClient` ikke er tilgjengelig på classpathen, og du vil få en `ClassNotFoundException`.

Det finnes også en motsatt variant: [`@ConditionalOnMissingClass`](http://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/autoconfigure/condition/ConditionalOnMissingClass.html).

# Oppgave 6

Nå har vi en fungerende autokonfigurasjon for `ParkingClient`. Men hva hvis vi har et prosjekt hvor vi ønsker å bruke ParkingClient, men vi ønsker å instansiere og konfigurere den selv. Vi forsøker å lage vår egen ParkingClient og gi den vår egen konfigurasjon. Det viser seg imidlertid at autoconfiguration-klassene "vinner", og at vår egen bean-definisjon blir overstyrt. 

- [ ] Legg til en bean-metode for ParkingClient i `webapp/.../Main.java`
- [ ] Forsøk å sabotere denne ved å konfigurere den til en ikke-eksisterende url
- [ ] Start prosjektet, og se at det ikke var fullt så enkelt
- [ ] Bruk `@ConditionalOnMissingBean` i autoconfig-prosjektet for å tillate overstyring av bean-definisjonen

<details>
<summary><b>Klikk her for løsning</b></summary>

## Løsning

Legg til følgende i `Main.java`:

```java
@Bean
public ParkingClient parkingClient() {
    ParkingClient parkingClient = new ParkingClient();
    parkingClient.setEndpoint(URI.create("http://www.vg.no"));
    return parkingClient;
}
```

Prøv å starte `webapp` og se hva som skjer når du forsøker å hente http://localhost:8080/parking . Vi ville forventet en feilmelding, siden vg ikke har parkeringsdataene. Hva skjer?

Det er altså viktig å kunne overstyre hele autokonfigurasjonen dersom noen ønsker å gjøre konfigurasjonen selv. Dette gjøres med [`@ConditionalOnMissingBean`](http://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/autoconfigure/condition/ConditionalOnMissingBean.html) som også er også veldig mye brukt i Spring Boot. Den lar oss deaktivere hele konfigurasjonen dersom det allerede finnes en bean av den gitte typen.

Legg til `@ConditionalOnMissingBean` på `ParkingClientAutoconfig`:

```java
@Configuration
@ConditionalOnClass(ParkingClient.class)
@ConditionalOnProperty("parking.endpoint")
@ConditionalOnMissingBean(ParkingClient.class)
public class ParkingClientAutoconfig {
...
```

Nå kan vi lage vår egen @Bean-metode i webapp prosjektet som instansierer og konfigurerer ParkingClient som vi ønsker uten at autokonfigurasjonen blander seg inn lenger. 

</details>

Det finnes også en [`@ConditionalOnBean`](http://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/autoconfigure/condition/ConditionalOnBean.html) som aktiverer konfigurasjonen bare dersom en bean av typen allerede finnes. Begge har flere valgfrie elementer, sjekk dokumentasjonen for detaljer. 

# Oppgave 7
Når du ser på json-outputen fra `http://localhost:8080/parking` ser du at `timestamp`-feltet blir serialisert på en veldig dårlig måte. 
```javascript
 {
      "name" : "Bygarasjen",
      "vacantSpaces" : 1830,
      "timestamp" : {
         "fieldTypes" : [
            {
               "rangeDurationType" : {
                  "name" : "days"
               },
               "durationType" : {
                  "name" : "hours"
               },
               ...
```
Vi ønsker i stedet at timestamp skal serialiseres på formen `HH:mm`. For å få til det kan vi bruke en Jackson-formatter for JodaTime sin `LocalDate`. Kopier inn følgende klasse i autoconfig-prosjektet:

```java 
package no.bouvet.autoconfig;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.joda.time.LocalTime;

import java.io.IOException;

public class JodaTimeSerializer extends JsonSerializer<LocalTime> {
    @Override
    public void serialize(LocalTime localTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(localTime.toString("HH:mm"));
    }
}
```

For å få Jackson til å bruke denne serializeren må vi registrere den. Det gjør man ved å lage en spring bean av typen `com.fasterxml.jackson.databind.Module`. Spring Boot sin autoconfig for Jackson finner nemlig alle beans av denne typen og [registrerer dem med Jackson](https://github.com/spring-projects/spring-boot/blob/v1.4.3.RELEASE/spring-boot-autoconfigure/src/main/java/org/springframework/boot/autoconfigure/jackson/JacksonAutoConfiguration.java#L328). [`SimpleModule`](https://fasterxml.github.io/jackson-databind/javadoc/2.5/com/fasterxml/jackson/databind/module/SimpleModule.html) har en metode for å legge til en serializer for en klasse.


- [ ] Lag en autoconfig for JodaTime. Kall klassen `JodaTimeAutoConfiguration`. Husk `@ConditionalOnClass` som sjekker at du har både JodaTime og Jackson
- [ ] Registrer autoconfigen i `spring.factories`
- [ ] Registrer JodaTimeSerializer i Jackson ved å lage en bean-metode som returnerer en `SimpleModule`
- [ ] Restart webappen og kall servicen på nytt. Nå skal du få timestamp på formen "HH:mm" i stedet for en serialisert klasse.
- [ ] For ekstra bonuspoeng: gjør det mulig å skru av eller på serialisering av `LocalTime` ved hjelp av en property i `application.yml`.


<details>
<summary><b>Klikk her for løsning</b></summary>

## Løsning

Vi må legge til en optional dependency til `joda-time` og `jackson-databind` i `autoconfig/pom.xml`.

```xml
<dependency>
    <groupId>joda-time</groupId>
    <artifactId>joda-time</artifactId>
    <version>2.9.4</version>
    <optional>true</optional>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.6.3</version>
    <optional>true</optional>
</dependency>
```
Lag følgende klasse i `autoconfig`:

```java
package no.bouvet.autoconfig;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.joda.time.LocalTime;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({LocalTime.class, Module.class})
public class JodaTimeAutoConfiguration {
    @Bean
    public Module module() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalTime.class, new JodaTimeSerializer());
        return module;
    }
}
```


Du må også registrere `JodaTimeAutoConfiguration` i `autoconfig/src/main/resources/META-INF/spring.factories`:

```
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
no.bouvet.autoconfig.ParkingClientAutoConfiguration,no.bouvet.autoconfig.JodaTimeAutoConfiguration
```

For å skru av og på autoconfigen kan du bruke `@ConditionalOnProperty` med parameter `havingValue`.
</details>



# Oppgave 8
Spring Boot har innebygde helsesjekker for mange ting i Actuators-prosjektet. Vi skal legge til Actuators og lage autokonfigurasjon for en enkel heslesjekk for ParkingClient.

Begynn med å legge til en avhengighet til `spring-boot-actuator` i `pom.xml` i `webapp`:
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-actuator</artifactId>
    <version>1.4.1.RELEASE</version>
</dependency>
```
Restart `webapp` og sjekk helsesjekken på http://localhost:8080/health 

Det er enkelt å legge til sine egne helsesjekker. Det eneste man trenger er en bean som implementerer HealthIndicator. Vi har laget en veldig enkel HealthIndicator for ParkingClient. Opprett klassen `ParkingClientHealthIndicator` i `autoconfig`:

```java
package no.bouvet.autoconfig;

import no.bouvet.parking.ParkingClient;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;

public class ParkingClientHealthIndicator extends AbstractHealthIndicator {
    
    private  ParkingClient parkingClient;

    public ParkingClientHealthIndicator(ParkingClient parkingClient) {
        this.parkingClient = parkingClient;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        try {
            parkingClient.getData();
            builder.up();
            
        } catch (Exception e) {
            builder.down();
        }
    }
}
```

Lag en autokonfigurasjon for `ParkingClientHealthIndicator`. Husk å legge til det som trengs av avhengigheter og conditionals.

<details>
<summary><b>Klikk her for løsning</b></summary>

## Løsning

Forslag til autokonfigurasjon:
```java
package no.bouvet.autoconfig;

import no.bouvet.parking.ParkingClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({ParkingClient.class, HealthIndicator.class})
@ConditionalOnBean(ParkingClient.class)
public class ParkingClientHealthAutoConfiguration {
    
    @Bean
    @Autowired
    HealthIndicator parkingClientHealthIndicator(ParkingClient parkingClient) {
        return new ParkingClientHealthIndicator(parkingClient);
    }
}
```
Hvorfor har ved med både ParkingClient og HealthIndicator i @ConditionalOnClass. Og hvorfor @ConditionalOnBean(ParkingClient.class)? 

Husk at avhengigheten til `spring-boot-actuator` i `autoconfig` må være optional.
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-actuator</artifactId>
    <version>1.4.1.RELEASE</version>
    <optional>true</optional>
</dependency>
```

Og husk å legge til `ParkingClientHealthAutoConfiguration` i `autoconfig/src/main/resources/META-INF/spring.factories`!

</details>



# Videre lesning

Det er mange gode eksempler på autoconfig i kildekoden til spring boot. Se for eksempel her: 

https://github.com/spring-projects/spring-boot/tree/v1.4.3.RELEASE/spring-boot-autoconfigure/src/main/java/org/springframework/boot/autoconfigure
