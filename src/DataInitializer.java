import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class DataInitializer {

    public static Customer currentCustomer;
    public static Worker overseeingWorker;

    public static List<Building> initializeData() throws Exception {

        Loot coins_1 = new Coins("Elven Coins", "Calisto", 12, 0.8);
        Loot coins_2 = new Coins("Dwarven Coins", "St. Luis", 24, 0.6);
        Loot pmb_1 = new PreciousMetalBars("Gold bar from Ostenwald", 1.0, 12.0, 35.0, 12.0, barType.Gold, 35.0);
        Loot pmb_2 = new PreciousMetalBars("Silver bar from Morrowind", 1.0, 12.0, 35.0, 12.0, barType.Silver, 27.0);
        Axe axe_1 = new Axe("Slasher", 1, 12.0, Rarity.Common, 1.0, 55.0, 1.6);
        Sword sword_1 = new Sword("Sword of Light", 3, 25000.0, Rarity.Legendary, 1.0, 1210.0, 3);
        Sword sword_2 = new Sword("Sword of Darkness", 3, 15000.0, Rarity.Rare, 1.0, 1210.0, 3);


        Worker w1 = new Worker("Bohdan1", LocalDate.now(), 211.0, jobTitles.Salesman, EnumSet.of(skills.TeamPlayer));
        Worker w2 = new Worker("Bohdan2", LocalDate.now(), 212.0, jobTitles.Salesman, EnumSet.of(skills.TeamPlayer));
        Worker w3 = new Worker("Bohdan3", LocalDate.now(), 213.0, jobTitles.Salesman, EnumSet.of(skills.TeamPlayer));

        currentCustomer = new Customer("BillyBob", LocalDate.now(), preferences.Weapon, 10000.0 , "Adventurer", 5);;
        overseeingWorker = w1;


        Building b1 = new Building("House of coins", "Somewhere", EnumSet.of(buildingType.Store), new ArrayList<>(List.of(w1)), new ArrayList<>(Arrays.asList(coins_1, coins_2)));
        w1.setBuilding(b1);
        Building b2 = new Building("The Royal Jewelerly", "Elsewhere", EnumSet.of(buildingType.Store), new ArrayList<>(List.of(w2)), new ArrayList<>(Arrays.asList(pmb_1, axe_1, pmb_2)));
        w2.setBuilding(b2);
        Building b3 = new Building("The Boar's Armory", "Elsewhere", EnumSet.of(buildingType.Store), new ArrayList<>(List.of(w3)), new ArrayList<>(Arrays.asList(sword_1, sword_2)));
        w3.setBuilding(b3);
        coins_1.setIsInPossessionOf(w1);
        coins_2.setIsInPossessionOf(w1);

        pmb_2.setIsInPossessionOf(w2);
        axe_1.setIsInPossessionOf(w2);
        pmb_1.setIsInPossessionOf(w2);

        sword_1.setIsInPossessionOf(w3);
        sword_2.setIsInPossessionOf(w3);

        ArrayList<Loot> lootList = new ArrayList<>();
        lootList.add(axe_1);

        Dungeon d1 = new Dungeon(9, "Aincrad", lootList, "Pretty good");
        Dungeon d2 = new Dungeon(7, "Aincrad2", lootList, "Pretty good");
        Dungeon d3 = new Dungeon(12, "Aincrad3", lootList, "Pretty good");

        System.out.println(Dungeon.getAllDungeonsAsSet());


        return new ArrayList<>(Arrays.asList(b1, b2, b3));
    }

    public static <T extends Serializable> void writeTo(String path, List<T> extent) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path))) {
            for (T obj : extent) {
                out.writeObject(obj);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void writeToFile() {
        writeTo("DB/Customer.ser", Customer.getCustomer_coll());
        writeTo("DB/Worker.ser", Worker.getWorker_coll());
        writeTo("DB/Loot.ser", Loot.getLoot_coll());
        writeTo("DB/Dungeon.ser", Dungeon.getDungeons_coll());
        writeTo("DB/DungeonDelve.ser", DungeonDelve.getDdelv_coll());
        writeTo("DB/License.ser", License.getLicence_coll());
        if(!Transaction.getTrans_coll().isEmpty()){
        List<Transaction> transList = DataInitializer.getFrom("DB/Transaction.ser", Transaction.class);
        transList.add(Transaction.getTrans_coll().get(Transaction.getTrans_coll().size()-1));
        writeTo("DB/Transaction.ser", transList);
        }
        writeTo("DB/Building.ser", Building.getBuild_coll());
    }
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> List<T> getFrom(String filename, Class<T> myClass) {
        File file = new File(filename);
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }

        List<T> objects = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            while (true) {
                try {
                    T obj = (T) ois.readObject();
                    objects.add(obj);
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return objects;
    }
}
