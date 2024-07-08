import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

enum buildingType {Storage, Store, Home}

public class Building extends ObjectPlus implements Serializable {

    private String name;
    private int building_id;
    @Serial
    private static final long serialVersionUID = 1L;
    private List<Person> listOfInhabitants = new ArrayList<>();
    private String adress;
    private EnumSet<buildingType> purpouse;
    private List<Loot> storedInside = new ArrayList<>();
    private static ArrayList<Building> build_coll = new ArrayList<>();

    public Building (String name, String adress, EnumSet<buildingType> purpose, List<Person> listOfInhabitants, List<Loot> storedInside) throws Exception {
        super();
        checkName(name);
        checkAdress(adress);
        checkPurpouse(purpose);
        this.building_id = (int) IDManager.generateUniqueId();
        if (purpose.contains(buildingType.Home)){
            checkListOfInhabitants(listOfInhabitants, purpose);
        }

        if (purpose.contains(buildingType.Storage)){
            checkStoredInside(storedInside);
        }

        if (purpose.contains(buildingType.Store)){
            checkListOfInhabitants(listOfInhabitants, purpose);
            checkStoredInside(storedInside);
        }
        build_coll.add(this);
    }

    //getters

    public static List<Building> getBuild_coll() {
        return java.util.Collections.unmodifiableList(build_coll);
    }

    public String getAdress() {
        return adress;
    }

    public List<Loot> getStoredInside() {
        return storedInside;
    }

    public List<Person> getListOfInhabitants() {
        return listOfInhabitants;
    }

    public EnumSet<buildingType> getPurpouse() {
        return purpouse;
    }

    public String getName() {
        return name;
    }

    //setters

    public void setAdress(String adress) {
        checkAdress(adress);
    }


    public void setStoredInside(ArrayList<Loot> storedInside) {
        checkStoredInside(storedInside);
    }

    public void setListOfInhabitants(List<Person> listOfInhabitants) throws Exception {
        if (this.purpouse.contains(buildingType.Store) || this.purpouse.contains(buildingType.Home)){
            checkListOfInhabitants(listOfInhabitants, this.purpouse);
        } else throw new Exception("Method not avaliable for such buildings");
    }

    public void setName(String name) {
        checkName(name);
    }

    //checkers

    private void checkAdress(String in){
        if ( in == null || in.equals("")){
            throw new IllegalArgumentException("Improper adress");
        } else this.adress = in;
    }

    private void checkName (String in){
        if ( in == null || in.equals("")){
            throw new IllegalArgumentException("Improper name");
        } else this.name = in;
    }

    private void checkPurpouse(EnumSet<buildingType> in){
        if ( in == null){
            throw new IllegalArgumentException("Improper building type");
        } else if (in.contains(null)){
            throw new IllegalArgumentException("Improper building type");
        } else this.purpouse = in;
    }

    private void checkStoredInside(List<Loot> in){
        if ( in == null){
            throw new IllegalArgumentException("Improper building type");
        } else this.storedInside = in;
    }

    public void checkListOfInhabitants(List<Person> listOfInhabitants, EnumSet<buildingType> purposes) throws Exception {
        if (listOfInhabitants == null) {
            throw new IllegalArgumentException("List of inhabitants cannot be null.");
        }
        for (Person person : listOfInhabitants) {
            if (person == null) {
                throw new IllegalArgumentException("Person in the list cannot be null.");
            }
            addPerson(person);
        }
    }

    //other


    public void addItem(Loot w) throws Exception {
        if (this.purpouse.contains(buildingType.Store) || this.purpouse.contains(buildingType.Storage)){
            this.storedInside.add(w);
        } else throw new Exception("Not a proper building!");
    }

    public void removeItem(Loot w) throws Exception {
        if (this.storedInside.isEmpty()){
            throw new Exception("Cannot remove an item from empty storage/shop");
        }
        if (this.purpouse.contains(buildingType.Store) || this.purpouse.contains(buildingType.Storage)){
            this.storedInside.remove(w);
        } else throw new Exception("Not a proper building!");
    }

    public void addPerson(Person p) throws Exception {
        p.setBuilding(this);
        this.listOfInhabitants.add(p);
    }

    public void removePerson(Person p) throws Exception {
        if (this.purpouse.contains(buildingType.Store) || this.purpouse.contains(buildingType.Home)){
            try {
                int tmp = this.listOfInhabitants.indexOf(p);
                listOfInhabitants.remove(tmp);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else throw new Exception("Not a proper building!");
    }

    @Override
    public String toString() {
        return "Building{" +
                "building_id=" + building_id +
                ", numberOfInhabitants=" + listOfInhabitants.size() +
                ", adress='" + adress + '\'' +
                ", purpouse=" + purpouse +
                ", storedInside=" + storedInside +
                '}';
    }

    public static void checkUniqueAndAdd(Building b){
        if (b.listOfInhabitants == null) {
            throw new IllegalArgumentException("List of inhabitants cannot be null.");
        }
        for (Person person : b.listOfInhabitants) {
            if (person == null) {
                throw new IllegalArgumentException("Person in the list cannot be null.");
            }
        }
        if ( b.getStoredInside() == null){
            throw new IllegalArgumentException("Improper building type");
        }
        if ( b.getPurpouse() == null){
            throw new IllegalArgumentException("Improper building type");
        } else if (b.getPurpouse().contains(null)){
            throw new IllegalArgumentException("Improper building type");
        }
        if ( b.getName() == null || b.getName().equals("")){
            throw new IllegalArgumentException("Improper name");
        }
        if ( b.getAdress() == null || b.getAdress().equals("")){
            throw new IllegalArgumentException("Improper adress");
        }
        if (!build_coll.contains(b)){
            build_coll.add(b);
        }
    }
}
