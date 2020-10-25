package analyzer.raids;

public class RaidInstance {
    private final int raidId;
    private final String raidName;
    private int bossId;
    private String bossName;

    public RaidInstance(int raidId, String raidName, int bossId, String bossName) {
        this.raidId = raidId;
        this.raidName = raidName;
        this.bossId = bossId;
        this.bossName = bossName;
    }
}
