# MP2 – urząd pocztowy

Prosty projekt w Javie przygotowany na przedmiot MAS. Modeluje wybrane elementy działania urzędu pocztowego i prezentuje konstrukcje wymagane w MP2: asocjację zwykłą, asocjację z atrybutem, asocjację kwalifikowaną oraz kompozycję.

## Zaimplementowane konstrukcje

### Asocjacja zwykła wiele–wiele

Klasy `MailOriginator` i `MailAddresses` reprezentują nadawców oraz adresatów przesyłek. Obie strony przechowują zbiory powiązanych obiektów. Metody `sendTo(...)`, `stopSendingTo(...)`, `addOriginator(...)` i `removeOriginator(...)` automatycznie aktualizują oba końce asocjacji. Gettery zwracają niemodyfikowalne kolekcje, dzięki czemu relacji nie można zmienić z pominięciem tej logiki.

### Asocjacja z atrybutem

Relacja wiele–wiele między `DeliveryAgent` i `DeliveryCustomer` została zastąpiona klasą pośrednią `DeliveryRecord`. Obiekt tej klasy opisuje pojedynczą dostawę i przechowuje jej atrybuty: datę dostarczenia oraz numer śledzenia. Konstruktor dodaje rekord po obu stronach relacji, a `remove()` usuwa go z obu kolekcji. Dla jednej pary kurier–klient może istnieć tylko jeden aktywny rekord.

### Asocjacja kwalifikowana

`ZipDirectory` przechowuje obiekty `DeliveryZone` w mapie, w której kwalifikatorem jest kod pocztowy. Metoda `findZone(postalCode)` pozwala odnaleźć strefę bez przeglądania całej kolekcji. Dodanie, usunięcie i zmiana kodu aktualizują równocześnie katalog oraz odpowiadający mu obiekt strefy. Nie można przypisać dwóch stref do tego samego kodu.

### Kompozycja

`MailOffice` jest właścicielem obiektów `Clerk`. Pracownik może zostać utworzony wyłącznie przez metodę `hireClerk(...)`, ponieważ konstruktor `Clerk` nie jest publiczny. Usunięcie pracownika przez `fireClerk(...)` kończy jego aktywność, a zamknięcie urzędu przez `closeOffice()` dezaktywuje wszystkich należących do niego pracowników. Nieaktywny pracownik nie udostępnia już referencji do urzędu.

## Struktura

- `MailOriginator`, `MailAddresses` – asocjacja zwykła wiele–wiele,
- `DeliveryAgent`, `DeliveryCustomer`, `DeliveryRecord` – asocjacja z atrybutem,
- `ZipDirectory`, `DeliveryZone` – asocjacja kwalifikowana kodem pocztowym,
- `MailOffice`, `Clerk` – kompozycja urzędu i jego pracowników,
- `Main` – przykłady utworzenia, modyfikacji i usuwania wszystkich relacji oraz sprawdzenie ograniczeń.

## Uruchomienie

Wymagana jest Java 17 lub nowsza. Z katalogu głównego projektu w PowerShellu należy wykonać:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
javac -encoding UTF-8 -d out src/*.java
java -cp out Main
```

Program wypisuje w konsoli wyniki operacji na wszystkich czterech rodzajach relacji, w tym sprawdzenia spójności obu stron, ograniczeń unikalności oraz zachowania po usunięciu powiązań.
