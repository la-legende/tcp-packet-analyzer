package analyzer.player;

import java.util.ArrayList;
import java.util.List;

public class PlayerCharacter {
    private int playerId;
    private String name;
    private int characterType;
    private int level;
    private int heroLevel;
    private String family;
    private int damage;
    private int amountOfHits;
    private int bonCritics;
    private int critics;
    private int misses;
    private boolean wasSoftDamage;
    private int softDamageAmount;
    private int onyxCounter;
    private int lastHitDamage;
    private int lowestHit;
    private int biggestHit;
    private List<Integer> usedSkills;
    private List<PlayerCharacter> killedPlayers;


    public PlayerCharacter(int playerId, String name, int characterType, int level, int heroLevel, String family) {
        this.playerId = playerId;
        this.name = name;
        this.characterType = characterType;
        this.level = level;
        this.heroLevel = heroLevel;
        this.family = family;

        damage = 0;
        amountOfHits = 0;
        bonCritics = 0;
        critics = 0;
        misses = 0;
        wasSoftDamage = false;
        softDamageAmount = 0;
        onyxCounter = 0;
        lastHitDamage = 0;
        lowestHit = 0;
        biggestHit = 0;
        usedSkills = new ArrayList<>();
        killedPlayers = new ArrayList<>();
    }

    public void updateCharacter(int playerId, String name, int characterType, int level, int heroLevel, String family) {
        this.playerId = playerId;
        this.name = name;
        this.characterType = characterType;
        this.level = level;
        this.heroLevel = heroLevel;
        this.family = family;
    }

    public int getId() { return playerId; }

    public void onDamageDealt(int damage) {
        if(damage != 0) {
            softDamageAmount++;
            lastHitDamage = damage;

            this.damage += damage;
            lowestHit = Math.min(damage, lowestHit);
            biggestHit = Math.max(damage, biggestHit);
        }
    }

    public void addHitType(int hitType) {
        amountOfHits++;

        switch (hitType) {
            case 3, 6 -> {
                if(wasSoftDamage) {
                    bonCritics++;
                    wasSoftDamage = false;
                } else {
                    critics++;
                }
            }
            case 4, 7 -> {
                misses++;
                wasSoftDamage = false;
            }
            default -> {
                if(wasSoftDamage) {
                    softDamageAmount++;
                    wasSoftDamage = false;
                }
            }
        }
    }

    public void addUsedSkill(int usedSkillVnum) {
        usedSkills.add(usedSkillVnum);
    }

    public void addKilledPlayers(PlayerCharacter playerCharacter) {
        killedPlayers.add(playerCharacter);
    }

    public void setWasSoftDamage() {
        wasSoftDamage = true;
    }

    public int getPlayerId() {
        return playerId;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getHeroLevel() {
        return heroLevel;
    }

    public String getFamily() {
        return family;
    }

    public int getDamage() {
        return damage;
    }

    public int getAmountOfHits() {
        return amountOfHits;
    }

    public int getBonCritics() {
        return bonCritics;
    }

    public int getCritics() {
        return critics;
    }

    public int getMisses() {
        return misses;
    }

    public int getSoftDamageAmount() {
        return softDamageAmount;
    }

    public int getOnyxCounter() {
        return onyxCounter;
    }

    public int getLowestHit() {
        return lowestHit;
    }

    public int getBiggestHit() {
        return biggestHit;
    }

    public void displayFullInfo() {
        StringBuilder stringBuilder;

        stringBuilder = new StringBuilder();
        stringBuilder.append("Player ID: ").append(getId()).append("\n");
        stringBuilder.append("Nick: ").append(getName()).append("\n");
        stringBuilder.append("Family: ").append(getFamily()).append("\n");
        stringBuilder.append("Full damage done: ").append(getDamage()).append("\n");
        stringBuilder.append("Full amount of hits: ").append(getAmountOfHits()).append("\n");
        stringBuilder.append("BonCritics hit: ").append(getBonCritics()).append("\n");
        stringBuilder.append("Critical hits: ").append(getCritics()).append("\n");
        stringBuilder.append("Missed hit: ").append(getMisses()).append("\n");
        stringBuilder.append("Normal attack soft damage hits: ").append(getSoftDamageAmount()).append("\n");
        stringBuilder.append("Number of summoned onyx: : ").append(getOnyxCounter()).append("\n");
        stringBuilder.append("Damage done by onyx shadow: ").append(0).append("\n");        //TODO
        stringBuilder.append("Lowest hit: ").append(getLowestHit()).append("\n");
        stringBuilder.append("Biggest hit: ").append(getBiggestHit()).append("\n");
        stringBuilder.append("Skilled used: ");
        usedSkills.forEach(s -> {
            stringBuilder.append(s).append(" -> ");
        });
        //killedPlayers.forEach(p -> {
        //    stringBuilder.append("Killed player id: ").append(p.getId()).append("  |  ").append("Killed player name: ").append(p.getName()).append("\n");
        //});
        stringBuilder.append("\n");

        System.out.println(stringBuilder.toString());
    }
}
