enum Rarity {Common, Rare, Legendary}

public abstract class Weapon extends Loot {

    private static int counter = 1;

    public int weapon_id;
    public int enchantLvl;
    public double price;
    public Rarity rarity;

    public static int rareMinDamage = 10;
    public static int legendaryMinDamage = 30;

    public Weapon (String name, int enchantLvl, double price, Rarity rarity, double amount, double suggestedPrice){
        super(name, amount, suggestedPrice);
        checkEnchantLvl(enchantLvl);
        checkPrice(price);
        checkRarity(rarity);
        this.weapon_id = counter++;
    }

    //getters

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getEnchantLvl() {
        return enchantLvl;
    }

    public Rarity getRarity() {
        return rarity;
    }


    //checkers

    private void checkEnchantLvl(int enchantLvl){
        if (enchantLvl<0){
            throw new IllegalArgumentException("Negative enchant level");
        } else this.enchantLvl = enchantLvl;
    }

    private void checkPrice(double price){
        if (price<0 || price>200000){
            throw new IllegalArgumentException("Improper price");
        } else this.price = price;
    }

    private void checkRarity(Rarity in){
        if (in == null){
            throw new IllegalArgumentException("Improper rarity");
        } else this.rarity = in;
    }

    public abstract double getDamage(int str, int dex, int ac);

    public abstract double getDamage();

    @Override
    public double calculateGoldValue(){
        return price;
    };

    public abstract boolean checkBalance();

    public final static int balanceChecks = 100;

    public final static int avgStat = 10;

    public final static int avgAC = 13;

}
