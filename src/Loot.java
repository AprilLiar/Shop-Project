import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public abstract class Loot extends ObjectPlus implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static ArrayList<Loot> loot_coll = new ArrayList<>();
    public String name;
    public double amount;
    public double suggestedPrice;
    public Person isInPossessionOf;

    private Set<Transaction> transactions = new HashSet<>();

    public Loot(String name, double amount, double suggestedPrice){
        super();
        checkName(name);
        checkAmount(amount);
        checkSuggestedPrice(suggestedPrice);
        loot_coll.add(this);
    }

    public Loot(){
    }

    //getters

    public Set<Transaction> getTransactions() {
        return Collections.unmodifiableSet(transactions);
    }
    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public double getSuggestedPrice() {
        return suggestedPrice;
    }

    public Person getIsInPossessionOf() {
        return isInPossessionOf;
    }

    public static List<Loot> getLoot_coll() {
        return java.util.Collections.unmodifiableList(loot_coll);
    }

    //setters

    public void setName(String name) {
        checkName(name);
    }

    public void setAmount(int amount) {
        checkAmount(amount);
    }

    public void setSuggestedPrice(double suggestedPrice) {
        checkSuggestedPrice(suggestedPrice);
    }

    public void setIsInPossessionOf(Person p) throws Exception {
            this.isInPossessionOf = p;
            if(p instanceof Customer){
                ((Customer) p).getInventory().add(this);
            }

    }

    public void addTransaction(Transaction t){
        if(this.transactions.isEmpty() || !this.transactions.contains(t)){
            this.transactions.add(t);
            t.setLoot(this);
        }
    }

    public void removeFromPossessions() throws Exception {
        Person p = this.isInPossessionOf;
        if(p instanceof Worker){
            ((Worker) p).getBuilding().removeItem(this);
            this.isInPossessionOf = null;
        } else if (p instanceof Customer){
            ((Customer) p).removeFromInventory(this);
            this.isInPossessionOf = null;
        }
    }

    //checkers

    private void checkName(String in){
        if ( in == null || in.equals("")){
            throw new IllegalArgumentException("Improper trasure name");
        } else this.name = in;
    }

    private void checkAmount(double in){
        if ( in < 0){
            throw new IllegalArgumentException("Improper trasure amount");
        } else this.amount = in;
    }

    private void checkSuggestedPrice(double in){
        if ( in < 0){
            throw new IllegalArgumentException("Improper suggested price");
        } else this.suggestedPrice = in;
    }

    //other

    public double calculateGoldValue(){
        return(0);
    };

    public static void removeFromTreasury(Loot t){
        if ( loot_coll.contains(t)){
            loot_coll.remove(t);
        }
    }

    public static double getTreasuryWorth(){
        double tmp = 0.0;
        for (Loot t : loot_coll){
            tmp += t.calculateGoldValue();
        }
        return tmp;
    }

    public boolean enoughFunds (Person p){
        return p.getMoney() > this.getSuggestedPrice();
    }

    public String toString2() {
        final StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("-------------------").append("<br>");
        sb.append("<b>Name:</b> ").append(name).append("<br>");
        sb.append("<b>Type:</b> ").append(this.getClass().getCanonicalName()).append("<br>");
        sb.append("<b>Bought at:</b> ").append((int)this.calculateGoldValue()).append("gp").append("<br>");
        sb.append("</html>");
        return sb.toString();
    }

}
