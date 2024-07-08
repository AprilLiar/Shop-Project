import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

public class Customer extends Person implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static ArrayList<Customer> customer_coll = new ArrayList<>();
    private double pocketMoney;
    public static int counter = 1;
    private int customerId;
    private preferences preference;
    private License license;
    private ArrayList<Loot> inventory = new ArrayList<>();

    private Set<Transaction> transactions = new HashSet<>();

    private Building building;

    public Customer(String name, LocalDate birthDate, preferences preference, double pocketMoney, String type, Integer allowedLevel){
        super(name, birthDate);
        this.customerId = counter++;
        checkPreference(preference);
        checkMoney(pocketMoney);
        checkUniqueAndAdd(this);
        issueLicense(type, allowedLevel);
    }

    //getters

    public Set<Transaction> getTransactions() {
        return Collections.unmodifiableSet(transactions);
    }
    public int getCustomerId() {
        return customerId;
    }

    public preferences getPreferences() {
        return preference;
    }

    public double getMoney() {
        return pocketMoney;
    }

    public License getLicense() {
        return license;
    }
    public static List<Customer> getCustomer_coll() {
        return java.util.Collections.unmodifiableList(customer_coll);
    }

    public ArrayList<Loot> getInventory() {
        return inventory;
    }

    public Building getBuilding() {
        return building;
    }

    //setters

    public void setPreference(preferences preference) {
        checkPreference(preference);
    }

    public void setPocketMoney(double pocketMoney) {
        checkMoney(pocketMoney);
    }

    public void setLicense(License license) {
        checkLicense(license);
    }

    public void setInventory(ArrayList<Loot> inventory) {
        checkInventory(inventory);
    }

    public void setBuilding(Building building) {
        if (building == null){
            throw new IllegalArgumentException("Improper building");
        } else this.building = building;
    }

    public void addToInventory(Loot loot){
        this.inventory.add(loot);
    }

    public void removeFromInventory(Loot loot){
        this.inventory.remove(loot);
    }

    //checkers

    private void checkMoney(double in){
        if ( in < 0) {
            throw new IllegalArgumentException("Improper amount of money");
        } else this.pocketMoney = in;
    }

    private void checkInventory ( ArrayList<Loot> in){
        if ( in == null){
            throw new IllegalArgumentException("Improper inventory");
        } else this.inventory = in;
    }
    private void issueLicense (String type, Integer allowedLevel){
        if ( type == null || type.equals("") || allowedLevel == null || allowedLevel <=0){
            throw new IllegalArgumentException("Improper license");
        } else License.issueLicenceTo(this, type, allowedLevel);
    }

    private void checkLicense ( License in){
        if ( in == null){
            throw new IllegalArgumentException("Improper license");
        } else this.license = in;
    }

    private void checkPreference (preferences in){
        if (in == null){
            throw new IllegalArgumentException("Improper preference");
        } else this.preference = in;
    }


    //other

    public void decreaseMoney(int in){
        this.pocketMoney -= in;
    }

    public static void checkUniqueAndAdd (Customer in){
        boolean flagShouldAdd = true;
        if (customer_coll.contains(in)){
            flagShouldAdd = false;
        } else for (Customer c : customer_coll){
            if (c.getBirthDate() == in.getBirthDate() && c.getName().equals(in.getName())){
                flagShouldAdd = false;
            }
            if (c.getCustomerId() == in.getCustomerId()){
                throw new IllegalArgumentException("Customer number should be unique");
            }
        }
        if(flagShouldAdd){
            customer_coll.add(in);
        }
    }

    public Customer getCustomer(int number){
        Customer tmp = null;
        for (Customer c : customer_coll){
            if (number == c.getCustomerId()){
                tmp =  new Customer(c.getName(), c.getBirthDate(), c.getPreferences(), c.getMoney(), c.getLicense().getType(), c.getLicense().getAllowedLevel());
            }
        }
        if (tmp != null){
            return tmp;
        } else throw new IllegalArgumentException("Could not find a customer with this number");
    }

    public void addTransaction(Transaction t){
        if(this.transactions.isEmpty() || !this.transactions.contains(t)){
            this.transactions.add(t);
            t.setCustomer(this);
        }
    }
    public ArrayList<Transaction> getAllTransactions(){
        ArrayList<Transaction> tmp = new ArrayList<>();
        for (Transaction t : Transaction.getTrans_coll()){
            if(this.getCustomerId() == t.getCustomer().getCustomerId()){
                tmp.add(t);
            }
        }
        return tmp;
    }


    @Override
    public String toString() {
        return "Customer{" +
                "pocketMoney=" + pocketMoney +
                ", number=" + customerId +
                ", preference=" + preference +
                '}';
    }

}
