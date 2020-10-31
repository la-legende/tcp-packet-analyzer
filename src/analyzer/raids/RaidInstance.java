package analyzer.raids;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RaidInstance {
    private LocalTime raidStart;
    private String bossId;
    private String bossHp;
    private List<String> deaths;

    public RaidInstance(LocalTime time) {
        deaths = new ArrayList<>();
        this.raidStart = time;
        this.bossId = "";
        this.bossHp = "";
    }

    public void setBossId(String bossId) {
        this.bossId = bossId;
    }

    public String getBossId() { return bossId; }

    public void setBossHp(String bossHp) {
        this.bossHp = bossHp;
    }

    public void addDeath(String nick) {
        deaths.add(nick);
    }

    public void displayInfo() {
        StringBuilder stringBuilder;

        stringBuilder = new StringBuilder();

        stringBuilder.append("Raid started at: ").append(raidStart).append("\n");
        stringBuilder.append("Ended at: ").append(java.time.LocalTime.now()).append("\n");
        stringBuilder.append("Boss had: ").append(bossHp).append(" hp.").append("\n");
        stringBuilder.append("Players who died: ").append("\n");
        for (String death : deaths) {
            stringBuilder.append(death).append(" ").append("\n");
        }

        System.out.println(stringBuilder.toString());
    }
}
