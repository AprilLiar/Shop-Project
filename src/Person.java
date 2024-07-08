import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
enum preferences { Weapon, Valuable}
public abstract class Person extends ObjectPlus implements Serializable {

    private int persId;
    private static int counter = 1;
    @Serial
    private static final long serialVersionUID = 1L;
    private String name;
    private LocalDate birthDate;

    private Building building;

    public Person(String name, LocalDate birthDate) {
        super();
        checkName(name);
        checkDate(birthDate);
        this.persId = counter++;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        checkName(name);
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public int getPersId() {
        return persId;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBirthDate(LocalDate birthDate) {
        checkDate(birthDate);
    }

    public void setBuilding(Building building) {}

    //checkers

    private void checkName (String in){
        if ( in == null || in.equals("")){
            throw new IllegalArgumentException("Improper name");
        } else this.name = in;
    }

    private void checkDate ( LocalDate in){
        if ( in.isAfter(LocalDate.now())){
            throw new IllegalArgumentException("Improper date");
        } else this.birthDate = in;
    }



    //other

    public abstract double getMoney();
}