import java.util.Date;

public class Main {
    public static void main(String[] args) {
        MailOriginator originatorJacek = new MailOriginator("Jacek");
        MailOriginator originatorKasia = new MailOriginator("Kasia");

        MailAddresses addressA = new MailAddresses("Agnieszka");
        MailAddresses addressB = new MailAddresses("Piotr");
        MailAddresses addressC = new MailAddresses("Marek");

        originatorJacek.sendTo(addressA);
        originatorJacek.sendTo(addressB);

        addressC.addOriginator(originatorJacek);
        addressA.addOriginator(originatorKasia);

        originatorJacek.sendTo(addressA);

        System.out.println("Adresaci nadawcy Jacek: " + originatorJacek.getAddressees().size());
        for (MailAddresses address : originatorJacek.getAddressees()) {
            System.out.println(address.getName());
        }

        System.out.println("Nadawcy adresu Agnieszka: " + addressA.getOriginators().size());
        for (MailOriginator originator : addressA.getOriginators()) {
            System.out.println(originator.getName());
        }

        System.out.println("Czy Agnieszka zna Jacka: "
                + addressA.getOriginators().contains(originatorJacek));
        System.out.println("Czy Jacek zna Marka po dodaniu od strony adresu: "
                + originatorJacek.getAddressees().contains(addressC));

        originatorJacek.stopSendingTo(addressB);
        System.out.println("Po usunieciu od strony MailOriginator:");
        System.out.println(" -> Jacek zna Piotra: "
                + originatorJacek.getAddressees().contains(addressB));
        System.out.println(" -> Piotr zna Jacka: "
                + addressB.getOriginators().contains(originatorJacek));

        addressC.removeOriginator(originatorJacek);
        System.out.println("Po usunieciu od strony MailAddresses:");
        System.out.println(" -> Marek zna Jacka: "
                + addressC.getOriginators().contains(originatorJacek));
        System.out.println(" -> Jacek zna Marka: "
                + originatorJacek.getAddressees().contains(addressC));

        try {
            originatorJacek.getAddressees().add(addressB);
        } catch (UnsupportedOperationException ex) {
            System.out.println("Getter addressees zwraca niemodyfikowalna kolekcje");
        }

        System.out.println();

        DeliveryAgent agent = new DeliveryAgent("AGENT_1");
        DeliveryCustomer customer = new DeliveryCustomer("CUSTOMER_1");
        DeliveryCustomer customer2 = new DeliveryCustomer("CUSTOMER_2");

        DeliveryRecord record1 = agent.deliver(customer, new Date(), "TRACKING_1");

        DeliveryRecord record2 = agent.deliver(customer2, new Date(), "TRACKING_2");

        System.out.println("Po utworzeniu record1:");
        System.out.println(" -> record1 zna agenta: " + (record1.getAgent() == agent));
        System.out.println(" -> record1 zna klienta: " + (record1.getCustomer() == customer));
        System.out.println(" -> agent zna record1: " + agent.getRecords().contains(record1));
        System.out.println(" -> klient zna record1: " + customer.getDeliveries().contains(record1));

        try {
            agent.deliver(
                    customer,
                    new Date(),
                    "TRACKING_DUPLICATE"
            );
        } catch (IllegalStateException ex) {
            System.out.println("Nie mozna utworzyc drugiego rekordu dla tej samej pary agent-klient: "
                    + ex.getMessage());
        }

        System.out.println("Dostawy kuriera " + agent.getAgentId() + ":");
        for (DeliveryRecord record : agent.getRecords()) {
            System.out.println(" -> klient=" + record.getCustomer().getCustomerId()
                    + ", data=" + record.getDateDelivered()
                    + ", nr=" + record.getTrackingNumber());
        }

        agent.cancelDelivery(record1);

        System.out.println("Po anulowaniu pierwszej dostawy od strony agenta:");
        System.out.println(" -> liczba dostaw kuriera: " + agent.getRecords().size());
        System.out.println(" -> liczba dostaw klienta: " + customer.getDeliveries().size());
        System.out.println(" -> record1 aktywny: " + record1.isActive());
        System.out.println(" -> agent w record1: " + record1.getAgent());
        System.out.println(" -> klient w record1: " + record1.getCustomer());

        customer2.cancelDelivery(record2);

        System.out.println("Po anulowaniu drugiej dostawy od strony klienta:");
        System.out.println(" -> liczba dostaw kuriera: " + agent.getRecords().size());
        System.out.println(" -> liczba dostaw klienta 2: " + customer2.getDeliveries().size());
        System.out.println(" -> record2 aktywny: " + record2.isActive());

        try {
            agent.getRecords().add(record1);
        } catch (UnsupportedOperationException ex) {
            System.out.println("Getter records zwraca niemodyfikowalna kolekcje");
        }

        System.out.println();

        ZipDirectory directory = new ZipDirectory("Warsaw Directory");

        DeliveryZone zoneNorth = new DeliveryZone("North Zone");
        DeliveryZone zoneSouth = new DeliveryZone("South Zone");
        DeliveryZone zoneWest = new DeliveryZone("West Zone");
        DeliveryZone zoneDuplicate = new DeliveryZone("Duplicate Zone");

        directory.registerZone("00-001", zoneNorth);
        directory.registerZone("00-200", zoneSouth);

        DeliveryZone found = directory.findZone("00-001");

        System.out.println("Strefa dla kodu 00-001: " + found.getZoneName());
        System.out.println("Strefa zna swoj katalog: " + found.getDirectory().getName());
        System.out.println("Strefa zna swoj kod: " + found.getPostalCode());

        try {
            directory.registerZone("00-001", zoneDuplicate);
        } catch (IllegalArgumentException ex) {
            System.out.println("Nie mozna zdublowac postalCode: " + ex.getMessage());
        }

        zoneNorth.changePostalCode("00-999");

        System.out.println("Po zmianie kodu North Zone z 00-001 na 00-999 od strony DeliveryZone:");
        System.out.println(" -> findZone stary kod 00-001: " + directory.findZone("00-001"));
        System.out.println(" -> findZone nowy kod 00-999: "
                + directory.findZone("00-999").getZoneName());
        System.out.println(" -> kod zapisany w zoneNorth: " + zoneNorth.getPostalCode());

        directory.changePostalCode(zoneNorth, "00-998");

        System.out.println("Po zmianie kodu North Zone z 00-999 na 00-998 od strony ZipDirectory:");
        System.out.println(" -> findZone stary kod 00-999: " + directory.findZone("00-999"));
        System.out.println(" -> findZone nowy kod 00-998: "
                + directory.findZone("00-998").getZoneName());
        System.out.println(" -> kod zapisany w zoneNorth: " + zoneNorth.getPostalCode());

        zoneNorth.changePostalCode("00-998");
        System.out.println("Zmiana na ten sam postalCode nie powoduje bledu");

        directory.removeZone("00-200");

        System.out.println("Po usunieciu strefy 00-200 od strony ZipDirectory:");
        System.out.println(" -> findZone dla 00-200: " + directory.findZone("00-200"));
        System.out.println(" -> katalog zapisany w zoneSouth: " + zoneSouth.getDirectory());
        System.out.println(" -> kod zapisany w zoneSouth: " + zoneSouth.getPostalCode());

        zoneWest.attachToDirectory(directory, "00-500");

        System.out.println("Po dodaniu West Zone od strony DeliveryZone:");
        System.out.println(" -> strefa dla kodu 00-500: "
                + directory.findZone("00-500").getZoneName());
        System.out.println(" -> katalog zapisany w zoneWest: "
                + zoneWest.getDirectory().getName());

        zoneWest.detachFromDirectory();

        System.out.println("Po odpieciu West Zone od strony DeliveryZone:");
        System.out.println(" -> findZone dla 00-500: " + directory.findZone("00-500"));
        System.out.println(" -> katalog zapisany w zoneWest: " + zoneWest.getDirectory());
        System.out.println(" -> kod zapisany w zoneWest: " + zoneWest.getPostalCode());

        try {
            directory.getZonesByCode().put("00-777", zoneDuplicate);
        } catch (UnsupportedOperationException ex) {
            System.out.println("Getter mapy zwraca niemodyfikowalna mape");
        }

        System.out.println();

        MailOffice office = new MailOffice("Urzad Pocztowy Warszawa");

        Clerk clerkAnna = office.hireClerk("Anna");
        Clerk clerkBartek = office.hireClerk("Bartek");

        System.out.println("Stan urzedu po zatrudnieniu pracownikow:");
        System.out.println(office);

        System.out.println("Biuro Anny: " + clerkAnna.getOffice().getOfficeName());
        System.out.println("Biuro Bartka: " + clerkBartek.getOffice().getOfficeName());

        office.fireClerk(clerkBartek);

        System.out.println("Stan urzedu po zwolnieniu Bartka od strony MailOffice:");
        System.out.println(office);
        System.out.println("Czy Bartek jest aktywny: " + clerkBartek.isActive());

        try {
            System.out.println(clerkBartek.getOffice().getOfficeName());
        } catch (IllegalStateException ex) {
            System.out.println("Nie mozna pobrac biura zwolnionego pracownika: "
                    + ex.getMessage());
        }

        office.closeOffice();

        System.out.println("Stan urzedu po zamknieciu:");
        System.out.println(office);
        System.out.println("Czy urzad jest aktywny: " + office.isActive());
        System.out.println("Czy Anna jest aktywna: " + clerkAnna.isActive());

        try {
            office.hireClerk("Dawid");
        } catch (IllegalStateException ex) {
            System.out.println("Nie mozna zatrudnic pracownika w zamknietym urzedzie: "
                    + ex.getMessage());
        }

        try {
            System.out.println(clerkAnna.getOffice().getOfficeName());
        } catch (IllegalStateException ex) {
            System.out.println("Nie mozna pobrac biura pracownika po zamknieciu urzedu: "
                    + ex.getMessage());
        }
    }
}
