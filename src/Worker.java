import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

enum jobTitles {Salesman, Assistant, Cleaner}
enum skills {Communicative, TeamPlayer, Positive}

public class Worker extends Person implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final double SalesmanMin = 200.0;
    private static final double AssistantMin = 40.0;
    private static final double CleanerMin = 50.0;
    private static final double AbsoluteMax = 10000.0;

    private double wage;
    private jobTitles title;

    private Building building;

    private EnumSet<skills> skills;

    private Set<Transaction> transactions = new HashSet<>();

    private static ArrayList<Worker> worker_coll = new ArrayList<>();


    public Worker(String name, LocalDate birthDate, double wage, jobTitles title, EnumSet<skills> skills) {
        super(name, birthDate);
        checkTitle(title);
        checkWage(wage);
        checkSkills(skills);
        worker_coll.add(this);
    }

    public Worker(String name, LocalDate birthDate, double wage, jobTitles title, EnumSet<skills> skills, Building store) {
        super(name, birthDate);
        checkTitle(title);
        checkWage(wage);
        checkSkills(skills);
        checkStore(store);
        worker_coll.add(this);
    }

    //getters

    public Building getBuilding() {
        return building;
    }
    public double getWage() {
        return wage;
    }

    public jobTitles getTitle() {
        return title;
    }

    public EnumSet<skills> getSkills() {
        return (EnumSet<skills>) Collections.unmodifiableSet(skills);
    }

    public Set<Transaction> getTransactions() {
        return Collections.unmodifiableSet(transactions);
    }

    public static List<Worker> getWorker_coll() {
        return java.util.Collections.unmodifiableList(worker_coll);
    }

    //setters

    public void setWage(double wage) {
        checkWage(wage);
    }

    public void setTitle(jobTitles title) {
        checkTitle(title);
    }

    public void setBuilding(Building building) {checkStore(building);}

    public void setSkills(EnumSet<skills> skills) {
        checkSkills(skills);
    }

    //checkers

    private void checkWage(double in){
        if ( in < 0.0 ){
            throw new IllegalArgumentException("Negative wage");
        } else if (this.getTitle() == jobTitles.Salesman && in > SalesmanMin && in < AbsoluteMax){
            this.wage = in;
        } else if (this.getTitle() == jobTitles.Assistant && in > AssistantMin && in < SalesmanMin){
            this.wage = in;
        } else if (this.getTitle() == jobTitles.Cleaner && in > CleanerMin && in < AbsoluteMax){
            this.wage = in;
        } else throw new IllegalArgumentException("Wage does not match union demands!");

    }

    private void checkTitle ( jobTitles in){
        if ( in == null){
            throw new IllegalArgumentException("Improper title");
        } else this.title = in;
    }

    public void checkStore(Building in) {
        this.building = in;
    }

    public void checkSkills(EnumSet<skills> in) {
        if (in == null || in.size() == 0) {
            throw new IllegalArgumentException("Skill sett cannot be null.");
        } else {
            for (skills s : in){
                if (s == null){
                    throw new IllegalArgumentException("Skill sett contains a null.");
                }
            }
        }
        this.skills = in;
    }

    public void addSkill(skills in){
        if (in == null){
            throw new IllegalArgumentException("Cannot add a null skill");
        } else if (!this.getSkills().contains(in)){
            this.skills.add(in);
        }
    }

    public void removeSkill(skills in){
        if (in == null){
            throw new IllegalArgumentException("Cannot add a null skill");
        } else if (this.getSkills().contains(in)){
            this.skills.remove(in);
        } else throw new IllegalArgumentException("This skill is not present for this worker");
    }


    //other

    @Override
    public double getMoney() {
        switch (title){
            case Salesman : return wage + 0.2* Loot.getTreasuryWorth();
            case Assistant: return wage + 0.01* Loot.getTreasuryWorth();
            case Cleaner: return wage + 0.05* Loot.getTreasuryWorth();
            default: return 0.0;
        }
    }

    public ArrayList<Transaction> getAllTransactions(){
        ArrayList<Transaction> tmp = new ArrayList<>();
        for (Transaction t : Transaction.getTrans_coll()){
            if(this.getPersId() == t.getWorker().getPersId()){
                tmp.add(t);
            }
        }
        return tmp;
    }

    public void addTransaction(Transaction t){
        if (!this.transactions.contains(t)){
            this.transactions.add(t);
            if (t.getWorker() != this){
                t.setWorker(this);
            }
        }
    }


    @Override
    public String toString() {
        return "Worker{" +
                "wage=" + wage +
                ", title=" + title +
                ", store=" + building +
                '}';
    }

    public static void checkUniqueAndAdd(Worker w){
        if(w.getWage() <= 0 ){
            throw new IllegalArgumentException("Negative wage");
        }
        if(w.getTitle() == null){
            throw new IllegalArgumentException("Improper title");
        }
        if(w.getBuilding() == null){
            throw new IllegalArgumentException("Improper building");
        }
        if(!worker_coll.contains(w)){
            worker_coll.add(w);
        }

    }
}
