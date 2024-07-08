enum barType {Platinum, Gold, Silver}

public class PreciousMetalBars extends Valuable {
    private double length;
    private double width;
    private double height;
    private barType type;

    private double goldValue;

    public PreciousMetalBars (String name, double amount, double length, double width, double height, barType type, double suggestedPrice){
        super(name, amount, suggestedPrice);
        checkLength(length);
        checkWidth(width);
        checkHeight(height);
        checkType(type);
        this.goldValue = calculateGoldValue();
    }

    public PreciousMetalBars (Loot prevLoot, double length, double width, double height, barType type){
        super(prevLoot.getName(), prevLoot.getAmount(), prevLoot.getSuggestedPrice());
        checkLength(length);
        checkWidth(width);
        checkHeight(height);
        checkType(type);
        Loot.removeFromTreasury(prevLoot);
    }

    //getters

    public barType getType() {
        return type;
    }

    public double getHeight() {
        return height;
    }

    public double getLength() {
        return length;
    }

    public double getWidth() {
        return width;
    }

    //setters

    public void setHeight(double height) {
        checkHeight(height);
    }

    public void setLength(double length) {
        checkLength(length);
    }

    public void setWidth(double width) {
        checkWidth(width);
    }

    public void setType(barType type) {
        checkType(type);
    }

    //checkers

    private void checkLength(double in){
        if ( in < 0 || in > 200){
            throw new IllegalArgumentException("Incorrect dimension");
        } else this.length = in;
    }

    private void checkWidth(double in){
        if ( in < 0 || in > 200){
            throw new IllegalArgumentException("Incorrect dimension");
        } else this.width = in;
    }

    private void checkHeight(double in){
        if ( in < 0 || in > 200){
            throw new IllegalArgumentException("Incorrect dimension");
        } else this.height = in;
    }

    private void checkType(barType in){
        if ( in == null){
            throw new IllegalArgumentException("Improper type");
        } else this.type = in;
    }

    //other

    public double calculateGoldValue(){
        return switch (type) {
            case Platinum -> length * width * height * 3;
            case Gold -> length * width * height * 2;
            case Silver -> length * width * height;
        };
    }

    @Override
    public String toString() {
        return "A very special lot: \"" + name + "\" will cost you " + (int)goldValue + "gp per bar. Because we have " + (int)amount + " bars in stock," + "The whole collection will cost " + (int)(amount*goldValue) + "gp.";
    }
}
