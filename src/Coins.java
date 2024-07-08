public class Coins extends Valuable {

    private double goldValue;

    private String manufacturer;

    public Coins (String name, String manufacturer, int amount, double goldValue){
        super(name, amount, goldValue);
        checkValueToGold(goldValue);
        checkManufacturer(manufacturer);
    }

    public Coins (Loot prevLoot, double goldValue){
        super(prevLoot.getName(), prevLoot.getAmount(), prevLoot.getSuggestedPrice());
        checkValueToGold(goldValue);
        Loot.removeFromTreasury(prevLoot);
    }

    //getters

    @Override
    public double getAmount() {
        return super.getAmount();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    public double getGoldValue() {
        return goldValue;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    //setters

    public void setGoldValue(double goldValue) {
        checkValueToGold(goldValue);
    }

    public void setManufacturer(String manufacturer) {
        checkManufacturer(manufacturer);
    }

//checkers

    private void checkValueToGold(double in){
        if (in<0.0 || in>30.0){
            throw new IllegalArgumentException("Improper value to gold");
        } else this.goldValue = in;
    }

    private void checkManufacturer(String in){
        if(in.isBlank() || in.isEmpty()){
            throw new IllegalArgumentException("Improper manufacturer");
        } else this.manufacturer = in;
    }



    //other

    public double calculateGoldValue() {
        return goldValue*getAmount();
    }


    @Override
    public String toString() {
        return "These prestige " + name + " coins, made by the famous manufacturer \"" + manufacturer + "\" will cost you " + (int)goldValue + " per coin. Bacause we have " + (int)amount + " coins in stock," + "The whole pile will cost " + (int)(amount*goldValue) + "gp.";
    }
}
