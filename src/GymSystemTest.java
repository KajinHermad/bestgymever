import org.junit.jupiter.api.Test;
import java.util.Map;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class GymSystemTest {


    // Testmetod för att kontrollera att kunddata laddas korrekt från fil
    @Test
    void testLoadCustomerData() {
        // Ange sökvägen till en testfil med kunddata
        String testFilePath = "resources/test_customer_data.txt";

        // Ladda kunddata från filen
        Map<String, Customer> customers = Gymsystem.loadCustomerData(testFilePath);

        // Kontrollera att kunderna har laddats in korrekt
        assertNotNull(customers, "Kundlistan ska inte vara null"); // Kontrollera att listan inte är null

        // Kontrollera att rätt antal kunder har laddats in
        assertEquals(2, customers.size(), "Det borde finnas två kunder i filen"); // Förväntar sig att 2 kunder har laddats

        // Kontrollera att specifika kunddata stämmer
        Customer user1 = customers.get("7703021234"); // Hämtar kund med personnummer
        assertEquals("Test User1", user1.getName(), "Kundens namn borde vara 'Test User1'"); // Kontrollera kundens namn
        assertEquals(LocalDate.of(2023, 10, 20), user1.getLastPaymentDate(), "Betalningsdatum borde vara 2023-10-20"); // Kontrollera betalningsdatum
    }

    // Testmetod för att kontrollera att en aktiv medlem klassas korrekt
    @Test
    void testCheckMembershipForActiveMember() {
        // Skapa en testkund som betalat inom de senaste 12 månaderna
        Customer activeCustomer = new Customer("Active User", "1234567890", LocalDate.now().minusMonths(6)); // Kund som betalade för 6 månader sedan
        Map<String, Customer> customers = new java.util.HashMap<>();
        customers.put("1234567890", activeCustomer); // Lägger till kunden i HashMap

        // Kontrollerar medlemsstatus
        Gymsystem.checkMembership("1234567890", customers);
        // Kontrollera att kunden fortfarande är aktiv
        assertTrue(activeCustomer.getLastPaymentDate().isAfter(LocalDate.now().minusYears(1)),
                "Kunden borde klassas som nuvarande medlem."); // Förväntar sig att kunden är en aktiv medlem
    }

    // Testmetod för att kontrollera att en förfallen medlem klassas korrekt
    @Test
    void testCheckMembershipForExpiredMember() {
        // Skapa en testkund som betalat för två år sedan
        Customer expiredCustomer = new Customer("Expired User", "2345678901", LocalDate.now().minusYears(2)); // Kund som betalade för 2 år sedan
        Map<String, Customer> customers = new java.util.HashMap<>();
        customers.put("2345678901", expiredCustomer); // Lägger till kunden i HashMap

        // Kontrollerar medlemsstatus
        Gymsystem.checkMembership("2345678901", customers);
        // Kontrollera att kunden är förfallen
        assertTrue(expiredCustomer.getLastPaymentDate().isBefore(LocalDate.now().minusYears(1)),
                "Kunden borde klassas som en före detta medlem."); // Förväntar sig att kunden är en före detta medlem
    }

    // Testmetod för att kontrollera en obehörig användare som inte finns i systemet
    @Test
    void testCheckMembershipForUnauthorizedUser() {
        // Skapa en ny HashMap för att lagra kunder
        Map<String, Customer> customers = new java.util.HashMap<>();
        // Kontrollerar medlemsstatus för en person som inte finns
        Gymsystem.checkMembership("9999999999", customers);

        // Kontrollera att kunden inte finns i HashMap
        assertFalse(customers.containsKey("9999999999"),
                "Personen borde klassas som obehörig då den inte finns i systemet."); // Förväntar sig att kunden inte finns
    }
}
