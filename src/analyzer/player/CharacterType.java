package analyzer.player;

public enum CharacterType {
    ADVENTURER(0),
    WARRIOR(1),
    ARCHER(2),
    MAGE(3),
    MARTIAL_ARTIST(4);

    int value;

    CharacterType(int value) {
        this.value = value;
    }
}
