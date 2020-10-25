package analyzer;

import analyzer.packet.Packet;
import analyzer.packet.PacketType;
import analyzer.player.PlayerCharacter;
import analyzer.tcpClient.TCPSetup;

import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class PacketAnalyzer {
    private final UserInterface userInterface;
    private final Map<String, PlayerCharacter> playerCharacters;

    public PacketAnalyzer() {
        playerCharacters = new HashMap<>();
        userInterface = new UserInterface();
    }

    public static void main(String[] args) {
        System.out.println("Damage calculator for NosTale. Fully made by LaLegende. \n");

        PacketAnalyzer packetAnalyzer = new PacketAnalyzer();
        TCPSetup tcpSetup = new TCPSetup();
        Packet packet;

        for(;;) {
            try {
                String line;
                if((line = tcpSetup.getTcpClient().readDataLine()) != null) {
                    packet = new Packet(line);
                    if(packet.getPacketType() == PacketType.RECEIVE) {
                        packetAnalyzer.handleReceive(packet);
                    }
                }
            } catch (SocketException e) {
                System.out.println(e.toString());
                return;
            }
        }
    }

    public void handleReceive(Packet packet) {
        String packetHeader = packet.getHeader();

        if(packetHeader.equals("in")) {
            if (packet.getArgument(1).equals("1")) {
                String playerId = packet.getArgument(2);

                if(playerCharacters.containsKey(playerId)) {
                    playerCharacters.get(playerId).updateCharacter(
                            Integer.parseInt(packet.getArgument(4)),
                            playerId,
                            Integer.parseInt(packet.getArgument(12)),
                            Integer.parseInt(packet.getArgument(33)),
                            Integer.parseInt(packet.getArgument(39)),
                            packet.getArgument(27));
                } else {
                    playerCharacters.put(playerId,
                            new PlayerCharacter(
                                    Integer.parseInt(packet.getArgument(4)),
                                    playerId,
                                    Integer.parseInt(packet.getArgument(12)),
                                    Integer.parseInt(packet.getArgument(33)),
                                    Integer.parseInt(packet.getArgument(39)),
                                    packet.getArgument(27)
                            ));
                }

                System.out.println("Registered character!");
            }
        }else if(packetHeader.equals("su")) {
            if(packet.getArgument(1).equals("1")) {
                String playerId = packet.getArgument(3);

                if(!playerCharacters.containsKey(playerId)) {
                    playerCharacters.put( playerId,
                            new PlayerCharacter(
                                    Integer.parseInt(playerId),
                                    null,
                                    0,
                                    0,
                                    0,
                                    null
                            ));
                }
                PlayerCharacter playerCharacter = playerCharacters.get(playerId);

                if(packet.getArgument(2).equals("1") && packet.getArgument(10).equals("0")) {
                    String targetId = packet.getArgument(3);
                    if(playerCharacters.containsKey(targetId)) {
                        playerCharacter.addKilledPlayers(playerCharacters.get(targetId));
                    } else {
                        playerCharacter.addKilledPlayers(new PlayerCharacter(
                                Integer.parseInt(playerId),
                                null,
                                0,
                                0,
                                0,
                                null));
                    }
                }

                playerCharacter.addUsedSkill(Integer.parseInt(packet.getArgument(4)));
                playerCharacter.onDamageDealt(Integer.parseInt(packet.getArgument(12)));
                playerCharacter.addHitType(Integer.parseInt(packet.getArgument(13)));

                System.out.println("Registered damage");
            }
        } else if(packetHeader.equals("eff")) {
            if (packet.getArgument(3).equals("15")) {
                String playerId = packet.getArgument(2);

                if (!playerCharacters.containsKey(playerId)) {
                    playerCharacters.put(playerId,
                            new PlayerCharacter(
                                    Integer.parseInt(playerId),
                                    null,
                                    0,
                                    0,
                                    0,
                                    null
                            ));
                } else {
                    playerCharacters.get(playerId).setWasSoftDamage();
                }

                System.out.println("Registered softDamage.");
            }
        }
    }
}
