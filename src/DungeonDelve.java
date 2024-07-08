import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DungeonDelve extends ObjectPlus implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Long idDDelve;
    private static ArrayList<DungeonDelve> ddelv_coll = new ArrayList<>();
    private Customer adventurer;
    private Dungeon dungeon;
    private ArrayList<Loot> actualTreasure = new ArrayList<>();
    private LocalDate delveDate;

    public DungeonDelve (Customer adventurer, Dungeon dungeon, ArrayList<Loot> actualTreasure, LocalDate delveDate){
        super();
        this.idDDelve = IDManager.generateUniqueId();
        checkPerson(adventurer);
        checkdungeon(dungeon);
        checkLoot(actualTreasure);
        checkDate(delveDate);
        ddelv_coll.add(this);
    }

    //getters


    public Long getIdDDelve() {
        return idDDelve;
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    public Customer getAdventurer() {
        return adventurer;
    }

    public ArrayList<Loot> getActualTreasure() {
        return actualTreasure;
    }

    public LocalDate getDelveDate() {
        return delveDate;
    }

    public static List<DungeonDelve> getDdelv_coll() {
        return java.util.Collections.unmodifiableList(ddelv_coll);
    }

    //setters

    public void setDungeon(Dungeon dungeon) {
        checkdungeon(dungeon);
    }


    //checkers

    private void checkPerson ( Customer in){
        if (in == null) {
            throw new IllegalArgumentException("Improper adventurer");
        } else if (in.getLicense().getAllowedLevel()<=this.getDungeon().getLevel()){
            throw new IllegalArgumentException("Adventurer too weak for this dungeon!");
        } else this.adventurer = in;
    }

    private void checkdungeon ( Dungeon in){
        if (in == null) {
            throw new IllegalArgumentException("Improper dungeon");
        } else {
            this.dungeon = in;
            in.addDelve(this);
        }
    }

    private void checkDate ( LocalDate in){
        if (in == null || in.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Improper time");
        } else this.delveDate = in;
    }

    private void checkLoot ( ArrayList<Loot> in){
        if (in == null){
            throw new IllegalArgumentException("Improper list");
        } else {
            for (Loot t : in){
                if (t == null && this.getDungeon().getPossibleRewards().contains(t)){
                    throw new IllegalArgumentException("List contains improper loot");
                }
            }
        } this.actualTreasure = in;
    }

    public ArrayList<Loot> getAllTreasureOf (Customer p){
        ArrayList<Loot> tmp = new ArrayList<>();
        for(DungeonDelve dd : ddelv_coll){
            if (dd.getAdventurer().getCustomerId() ==  p.getCustomerId()){
                tmp.addAll(dd.getActualTreasure());
            }
        }
        return tmp;
    }


    //other

    public double getOverallLootValue(){
        double value = 0;
        for (Loot l : actualTreasure){
            value += l.suggestedPrice;
        }
        return value;
    }
    public static DungeonDelve mostSuccessfulDDelve (){
        DungeonDelve current = ddelv_coll.get(0);
        for (DungeonDelve dd : ddelv_coll){
            if (dd.getOverallLootValue()>current.getOverallLootValue()){
                current = dd;
            }
        }
        return current;
    }

    @Override
    public String toString() {
        return "DungeonDelve{" +
                "adventurer=" + adventurer +
                ", dungeon=" + dungeon +
                ", actualLoot=" + actualTreasure +
                ", delveDate=" + delveDate +
                '}';
    }
}
