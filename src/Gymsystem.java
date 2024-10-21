import java.io.*;  // Importerar klasser för filhantering
import java.nio.file.*;  // Importerar klasser för fil- och sökvägshantering
import java.time.LocalDate;  // Importerar klassen för hantering av datum
import java.time.format.DateTimeFormatter;  // Importerar klassen för datumformattering
import java.util.*;  // Importerar klasser för samlingar, som Map och HashMap

public class Gymsystem {

    // Formatter för att hantera datum i "yyyy-MM-dd"-format
    private static final java.time.format.DateTimeFormatter DATE_FORMATTER = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Läser in kunddata från fil och lagrar i en HashMap
    public static java.util.Map<String, Customer> loadCustomerData(String filePath) {
        java.util.Map<String, Customer> customers = new java.util.HashMap<>();
//BufferedReader reader för att läsa filen rad för rad
        try (java.io.BufferedReader reader = java.nio.file.Files.newBufferedReader(java.nio.file.Paths.get(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Vi antar att varje kund består av två rader: personnummer, namn och betalningsdatum.
                String[] customerData = line.split(", ");
                String personNumber = customerData[0];
                String name = customerData[1];

                String paymentDateStr = reader.readLine();
                java.time.LocalDate paymentDate = java.time.LocalDate.parse(paymentDateStr, DATE_FORMATTER);

                // Lägg till kunden i HashMap
                customers.put(personNumber, new Customer(name, personNumber, paymentDate));
            }
        } catch (java.io.IOException e) {
            System.out.println("Kunde inte läsa filen: " + e.getMessage());
        }

        return customers;
    }

    // Kontrollerar medlemsstatus för en given person (personnummer eller namn)
    public static void checkMembership(String input, java.util.Map<String, Customer> customers) {
        java.time.LocalDate currentDate = java.time.LocalDate.now(); // Hämta dagens datum

        // Sök efter kund genom personnummer eller namn
        Customer foundCustomer = null;
        for (Customer customer : customers.values()) {
            if (customer.getPersonNumber().equals(input) || customer.getName().equalsIgnoreCase(input)) {
                foundCustomer = customer;
                break;
            }
        }

        // Kontrollera kundens medlemsstatus
        if (foundCustomer != null) {
            if (foundCustomer.getLastPaymentDate().isAfter(currentDate.minusYears(1))) {
                System.out.println(foundCustomer.getName() + " är en nuvarande medlem.");
                logVisit(foundCustomer); // Logga besöket för personliga tränaren
            } else {
                System.out.println(foundCustomer.getName() + " är en före detta kund.");
            }
        } else {
            System.out.println("Personen finns inte i systemet och är obehörig.");
        }
    }

    // Loggar en kunds besök i en fil för personliga tränaren
    public static void logVisit(Customer customer) {
        String logFilePath = "resources/PT_log.txt"; // Fil för att logga besök
        java.time.LocalDate visitDate = java.time.LocalDate.now(); // Dagens datum

        try (java.io.BufferedWriter writer = java.nio.file.Files.newBufferedWriter(java.nio.file.Paths.get(logFilePath), java.nio.file.StandardOpenOption.APPEND, java.nio.file.StandardOpenOption.CREATE)) {
            writer.write(customer.getPersonNumber() + ", " + customer.getName() + ", " + visitDate + "\n");
        } catch (java.io.IOException e) {
            System.out.println("Kunde inte skriva till filen: " + e.getMessage());
        }
    }

    // Main-metoden som kör programmet
    public static void main(String[] args) {
        String filePath = "resources/customer_data.txt"; // Fil med kunddata
        java.util.Map<String, Customer> customers = loadCustomerData(filePath);

        java.util.Scanner scanner = new java.util.Scanner(System.in);
        System.out.println("Ange personnummer eller namn för att söka medlem:");
        String input = scanner.nextLine();

        checkMembership(input, customers);
    }
}


