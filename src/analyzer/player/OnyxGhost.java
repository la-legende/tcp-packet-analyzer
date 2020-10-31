package analyzer.player;

public class OnyxGhost {
    public static final String ONYX_MODEL = "2371";

    private final String onyxId;
    private final String coordX;
    private final String coordY;
    private int damage;

    public OnyxGhost(String onyxId, String coordX, String coordY) {
        this.onyxId = onyxId;
        this.coordX = coordX;
        this.coordY = coordY;

        damage = 0;
    }

    public String getOnyxId() { return this.onyxId; }

    public boolean compareCoords(String coordX, String coordY) {
        return this.coordX.equals(coordX) && this.coordY.equals(coordY);
    }

    public void addDamage(String damage) {
        this.damage += Integer.parseInt(damage);
    }

    public int getDamage() { return this.damage; }
}
