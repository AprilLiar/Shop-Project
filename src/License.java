import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class License extends ObjectPlus implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private Long licenseNumber;
    private String type;
    private Integer allowedLevel;

    private static ArrayList<License> licence_coll = new ArrayList<>();

    private License(String type, Integer allowedLevel) {
        super();
        this.licenseNumber = IDManager.generateUniqueId();
        checkType(type);
        checkAllowedLevel(allowedLevel);
        licence_coll.add(this);
    }

    public static void issueLicenceTo(Customer c, String type, Integer allowedLevel){
        License license = new License(type, allowedLevel);
        c.setLicense(license);
    }

    // Getters
    public Long getLicenseNumber() {
        return licenseNumber;
    }

    public String getType() {
        return type;
    }

    public Integer getAllowedLevel() {
        return allowedLevel;
    }

    public static List<License> getLicence_coll() {
        return java.util.Collections.unmodifiableList(licence_coll);
    }

    // Setters

    public void setType(String type) {
        this.type = type;
    }

    public void setAllowedLevel(Integer allowedLevel) {
        this.allowedLevel = allowedLevel;
    }

    // Check functions

    private void checkType(String in) {
        if (in == null || in.isEmpty()) {
            throw new IllegalArgumentException("Invalid type. It cannot be null or empty.");
        } else this.type = in;
    }

    private void checkAllowedLevel(int in) {
        if (in < 0) {
            throw new IllegalArgumentException("Invalid allowed level. It must be a non-negative integer.");
        } else this.allowedLevel = in;
    }

    @Override
    public String toString() {
        return "License{" +
                "licenseNumber=" + licenseNumber +
                ", type='" + type + '\'' +
                ", allowedLevel=" + allowedLevel +
                '}';
    }
}
