public class Sword extends Weapon{
    private int additionalAttacks;
    private static int rareMinSwordDamage = rareMinDamage-5;
    private static int legendaryMinSwordDamage = legendaryMinDamage-10;

    public Sword(String name, int enchantLvl, double price, Rarity rarity, double amount, double suggestedPrice, int additionalAttacks) throws Exception {
        super(name, enchantLvl, price, rarity, amount, suggestedPrice);
        checkAdditionalAttacks(additionalAttacks);
        if (!checkBalance()){
            throw new Exception("Imbalanced sword. Please change it's stats!");
        }
    }

    //getters

    public int getAdditionalAttacks() {
        return additionalAttacks;
    }


    //checkers

    private void checkAdditionalAttacks(int in){
        if ( in < 0 || in > 3){
            throw new IllegalArgumentException("Improper number of additional attacks");
        } else this.additionalAttacks = in;
    }

    //others

    public double getDamage(int str, int dex, int ac){
        double damage = 0.0;
        int tmp = additionalAttacks;
        while (tmp>0){
            damage += calculateAttack(str, dex, ac);
            tmp --;
        }
        return damage;
    }

    public double getDamage(){
        double damage = 0.0;
        int tmp = additionalAttacks;
        while (tmp>0){
            damage += calculateAttack(10, 10, 15);
            tmp --;
        }
        return damage;
    }



    private double calculateAttack(int str, int dex, int ac){
        double damage = 0.0;
        int diceRoll = (int)(Math.random()*20) + 1;
        double attackRoll = diceRoll + this.getEnchantLvl() + (0.1*str) + (0.9*dex);
        int damageRoll = (int)(Math.random()*4);
        if (attackRoll>ac) {
            damage = damageRoll + this.getEnchantLvl() + (0.05*str) + (0.45*dex);
        }
        return damage;
    }

    @Override
    public boolean checkBalance() {
        boolean flag = false;
        double avg = 0.0;
        for (int i = 0; i < balanceChecks; i++) {
            avg += this.getDamage(avgStat, avgStat, avgAC);
        }
        avg = avg / 100;
        switch (this.getRarity()) {
            case Common:
                if (avg < rareMinSwordDamage) {
                    flag = true;
                }
            case Rare:
                if (avg > rareMinSwordDamage && avg < legendaryMinSwordDamage) {
                    flag = true;
                }
            case Legendary:
                if (avg > legendaryMinSwordDamage) {
                    flag = true;
                }
        }
        return flag;
    }
    @Override
    public String toString() {
        return "A fine sword called " + name + ", which deals an avarage of " + (int) getDamage(18, 16, 16) + " damage, while being worth only " + (int)price + "gp.\n" + "Don't forget that it is " + rarity + "!";
    }
}
