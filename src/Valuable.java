public abstract class Valuable extends Loot {
    private double goldValue;

    public Valuable(String name, double amount, double suggestedPrice){
        super(name, amount, suggestedPrice);
        setGoldValue();
    }

    public double getGoldValue() {
        return goldValue;
    }

    private void setGoldValue() {
        this.goldValue = getSuggestedPrice();
    }
    @Override
    public abstract double calculateGoldValue();

}
