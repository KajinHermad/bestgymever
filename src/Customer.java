import java.time.LocalDate;

// Kundklass som representerar en gymmedlem
public class Customer {
    private String name;
    private String personNumber;
    private LocalDate lastPaymentDate;

    // Konstruktor för att skapa en ny kund
    public Customer(String name, String personNumber, LocalDate lastPaymentDate) {
        this.name = name;
        this.personNumber = personNumber;
        this.lastPaymentDate = lastPaymentDate;
    }

    // Getter-metoder för att komma åt kundens data
    public String getName() {
        return name;
    }

    public String getPersonNumber() {
        return personNumber;
    }

    public LocalDate getLastPaymentDate() {
        return lastPaymentDate;
    }
}
