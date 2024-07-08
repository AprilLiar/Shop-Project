import java.io.Serializable;
import java.util.*;

public class Dungeon extends ObjectPlus implements Serializable {

    private static int counter = 1;

    private int dungeon_id;
    private static Set<Dungeon> allDungeons = new TreeSet<>(Comparator.comparingInt(Dungeon::getLevel));
    private int level;
    private String name;

    private String description;
    private ArrayList<Loot> possibleRewards;

    private Map<Long, DungeonDelve> delveMap = new TreeMap<>();

    public Dungeon (int level, String name, ArrayList<Loot> possibleRewards, String description){
        super();
        checkLevel(level);
        checkName(name);
        checkRewards(possibleRewards);
        checkDescription(description);
        this.dungeon_id = counter++;
        allDungeons.add(this);
    }

    //getters

    public String getName() {
        return name;
    }

    public ArrayList<Loot> getPossibleRewards() {
        return possibleRewards;
    }

    public int getLevel() {
        return level;
    }

    public String getDescription() {
        return description;
    }

    public static Set<Dungeon> getAllDungeonsAsSet() {
        return allDungeons;
    }

    public static List<Dungeon> getDungeons_coll(){
        List<Dungeon> dungeons_coll = new ArrayList<>(allDungeons);
        return java.util.Collections.unmodifiableList(dungeons_coll);
    }

    //setters

    public void setDescription(String description) {
        checkDescription(description);
    }

    public void setLevel(int level) {
        checkLevel(level);
    }

    public void setName(String name) {
        checkName(name);
    }

    public void setPossibleRewards(ArrayList<Loot> possibleRewards) {
        checkRewards(possibleRewards);
    }

    public void addDelve(DungeonDelve dd){
        if (!delveMap.containsKey(dd.getIdDDelve())){
            delveMap.put(dd.getIdDDelve(), dd);
            dd.setDungeon(this);
        }
    }

    //checkers

    private void checkName (String in){
        if ( in == null || in.equals("")){
            throw new IllegalArgumentException("Improper name");
        }
        Set<String> tmp = new HashSet<>();
        for (Dungeon d : allDungeons){
            tmp.add(d.getName());
        }
        if (tmp.contains(in)) {
            throw new IllegalArgumentException("Name already used!");
        } else this.name = in;
    }

    private void checkLevel(int in){
        if ( in < 0) {
            throw new IllegalArgumentException("Improper level");
        } else this.level = in;
    }

    private void checkRewards (ArrayList<Loot> in){
        if ( in == null){
            throw new IllegalArgumentException("Improper rewards");
        } else this.possibleRewards = in;
    }

    private void checkDescription (String in){
        if ( in == null || in.equals("")){
            throw new IllegalArgumentException("Improper description");
        } else this.description = in;
    }

    //other

    public DungeonDelve findDelveByID(Long id) throws Exception {
        if(!this.delveMap.containsKey(id)){
            throw new Exception("Unable to find DungeonDelve: " + id);
        }

        return this.delveMap.get(id);
    }



    @Override
    public String toString() {
        return "Dungeon{" +
                "level=" + level +
                ", name='" + name + '\'' +
                '}';
    }
}
