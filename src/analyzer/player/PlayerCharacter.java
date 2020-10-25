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
}
