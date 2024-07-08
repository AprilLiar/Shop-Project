import java.io.*;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.ChronoPeriod;
import java.time.chrono.Chronology;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Transaction extends ObjectPlus implements Serializable{
    @Serial
    private static final long serialVersionUID = 1L;

    private static ArrayList<Transaction> trans_coll = new ArrayList<>();
    private final Long idTransaction;
    private LocalDate date;
    private Double price;
    private Loot loot;
    private Customer customer;
    private Worker worker;

    // Default constructor
    public Transaction(Double price, Loot loot, Customer customer, Worker worker) {
        super();
        this.idTransaction = IDManager.generateUniqueId();
        this.date = LocalDate.now();
        checkAndSetPrice(price);
        checkAndSetLoot(loot);
        checkAndSetCustomer(customer);
        checkAndSetWorker(worker);
        checkUniqueAndAdd(this);
    }

    // Getters
    public Long getIdTransaction() {
        return idTransaction;
    }

    public LocalDate getDate() {
        return date;
    }

    public Double getPrice() {
        return price;
    }

    public Loot getLoot() {
        return loot;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Worker getWorker() {
        return worker;
    }

    public static List<Transaction> getTrans_coll(){
        return java.util.Collections.unmodifiableList(trans_coll);
    }

    // Setters

    public void setPrice(Double price) {
        checkAndSetPrice(price);
    }

    public void setLoot(Loot loot) {
        checkAndSetLoot(loot);
        loot.addTransaction(this);
    }

    public void setCustomer(Customer customer) {
        checkAndSetCustomer(customer);
        customer.addTransaction(this);
    }

    public void setWorker(Worker worker) {
        checkAndSetWorker(worker);
        if(worker.getTransactions().isEmpty() || !worker.getTransactions().contains(this)){
            worker.addTransaction(this);
        }
    }

    private void checkAndSetPrice(Double price) {
        if (price == null || price < 0) {
            throw new IllegalArgumentException("Price must be a non-negative value.");
        }
        this.price = price;
    }

    private void checkAndSetLoot(Loot loot) {
        if (loot == null) {
            throw new IllegalArgumentException("Loot cannot be null.");
        }
        this.loot = loot;
    }

    private void checkAndSetCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }
        this.customer = customer;
    }

    private void checkAndSetWorker(Worker worker) {
        if (worker == null) {
            throw new IllegalArgumentException("Worker cannot be null.");
        }
        this.worker = worker;
    }



    private void checkUniqueAndAdd (Transaction in){
        boolean flagShouldAdd = true;
        if (trans_coll.contains(in)){
            flagShouldAdd = false;
        } else for (Transaction t : trans_coll){
            if (Objects.equals(t.getIdTransaction(), in.getIdTransaction())){
                throw new IllegalArgumentException("Transaction number should be unique");
            }
        }
        if(flagShouldAdd){
            trans_coll.add(in);
        }
    }

    @Override
    public String toString() {
        String lineSeparator = "\n" + "─".repeat(30) + "\n";
        return String.format(
                "\n%s\n" +
                        "Transaction Details\n" +
                        "%s" +
                        "Transaction ID : %s\n" +
                        "Date           : %s\n" +
                        "Price          : %s\n" +
                        "Loot           : %s\n" +
                        "Customer       : %s\n" +
                        "Worker         : %s\n" +
                        "%s",
                "╔════════════════════════════════╗",
                lineSeparator,
                idTransaction,
                date,
                price,
                loot.getName(),
                customer.getName(),
                worker.getName(),
                "╚════════════════════════════════╝"
        );
    }
}
