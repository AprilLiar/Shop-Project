import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Axe extends Weapon {

    @Serial
    private static final long serialVersionUID = 1L;
    private double additionalCritDamage;
    private static int rareMinAxeDamage = rareMinDamage+5;
    private static int legendaryMinAxeDamage = legendaryMinDamage+10;


    public Axe (String name, int enchantLvl, double price, Rarity rarity, double amount, double suggestedPrice, double additionalCritDamage) throws Exception {
        super(name, enchantLvl, price, rarity, amount, suggestedPrice);
        checkAdditionalCritDamage(additionalCritDamage);
        if (!checkBalance()){
            throw new Exception("Imbalanced axe. Please change it's stats!");
        }
    }

    //getters

    public double getAdditionalCritDamage() {
        return additionalCritDamage;
    }

    //checkers

    private void checkAdditionalCritDamage(double in){
        if ( in < 0.0 || in > 5.0){
            throw new IllegalArgumentException("Improper crit modifier");
            } else this.additionalCritDamage = in;
    }

    //other

    public double getDamage(int str, int dex, int ac) {
        double damage = 0.0;
        int diceRoll = (int)(Math.random()*20) + 1;
        double attackRoll = diceRoll + this.getEnchantLvl() + (0.5*str) + (0.5*dex);
        int damageRoll = (int)(Math.random()*12);
        if (diceRoll == 20) {
            damage = (damageRoll + this.getEnchantLvl() + (0.25*str) + (0.25*dex)) * additionalCritDamage;
        }
        if (attackRoll>ac) {
            damage = damageRoll + this.getEnchantLvl() + (0.25*str) + (0.25*dex);
        }
        return damage;
    }

    public double getDamage() {
        double damage = 0.0;
        int diceRoll = (int)(Math.random()*20) + 1;
        double attackRoll = diceRoll + this.getEnchantLvl() + (0.5*10) + (0.5*10);
        int damageRoll = (int)(Math.random()*12);
        if (diceRoll == 20) {
            damage = (damageRoll + this.getEnchantLvl() + (0.25*10) + (0.25*10)) * additionalCritDamage;
        }
        if (attackRoll>15) {
            damage = damageRoll + this.getEnchantLvl() + (0.25*10) + (0.25*10);
        }
        return damage;
    }


    @Override
    public boolean checkBalance() {
        boolean flag = false;
        double avg = 0.0;
        for (int i = 0; i < balanceChecks; i++){
            avg += this.getDamage(avgStat, avgStat, avgAC);
        }
        avg = avg/100;
        switch(this.getRarity()){
            case Common : if (avg<rareMinAxeDamage) { flag = true;}
            case Rare : if (avg>rareMinAxeDamage && avg < legendaryMinAxeDamage) { flag = true;}
            case Legendary: if (avg>legendaryMinAxeDamage) { flag = true;}
        }
        return flag;
    }

    @Override
    public String toString() {
        return "A fine axe called " + name + ", which deals an avarage of " + (int) getDamage(18, 16, 16) + " damage, while being worth only " + (int)price + "gp.\n" + "Don't forget that it is " + rarity + "!";
    }
}
