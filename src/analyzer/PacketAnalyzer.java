package analyzer;

import analyzer.packet.Packet;
import analyzer.packet.PacketType;
import analyzer.player.PlayerCharacter;
import analyzer.tcpClient.TCPSetup;

import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class PacketAnalyzer {
    private Map<String, PlayerCharacter> playerCharacters;
    private boolean isFrozen;

    public PacketAnalyzer() {
        playerCharacters = new HashMap<>();
        isFrozen = false;
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
                    if(packet.getPacketType() == PacketType.RECEIVE && !packetAnalyzer.isFrozen) {
                        packetAnalyzer.handleReceive(packet);
                    } else if(packet.getPacketType() == PacketType.SEND){
                        packetAnalyzer.handlerSend(packet);
                    }
                }
            } catch (SocketException e) {
                System.out.println(e.toString());
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleReceive(Packet packet) {
        String packetHeader = packet.getHeader();

        if(packetHeader.equals("c_info")) {
            String playerId = packet.getArgument(6);

            if(playerCharacters.containsKey(playerId)) {
                playerCharacters.get(playerId).updateCharacter(
                        Integer.parseInt(playerId),
                        packet.getArgument(1),
                        Integer.parseInt(packet.getArgument(11)),
                        0,
                        0,
                        packet.getArgument(5));
            } else {
                playerCharacters.put(playerId,
                        new PlayerCharacter(
                                Integer.parseInt(playerId),
                                packet.getArgument(1),
                                Integer.parseInt(packet.getArgument(11)),
                                0,
                                0,
                                packet.getArgument(5)
                        ));
            }
        } else if(packetHeader.equals("in")) {
            if (packet.getArgument(1).equals("1")) {
                String playerId = packet.getArgument(4);

                if(playerCharacters.containsKey(playerId)) {
                    playerCharacters.get(playerId).updateCharacter(
                            Integer.parseInt(playerId),
                            packet.getArgument(2),
                            Integer.parseInt(packet.getArgument(12)),
                            Integer.parseInt(packet.getArgument(33)),
                            Integer.parseInt(packet.getArgument(39)),
                            packet.getArgument(27));
                } else {
                    playerCharacters.put(playerId,
                            new PlayerCharacter(
                                    Integer.parseInt(playerId),
                                    packet.getArgument(2),
                                    Integer.parseInt(packet.getArgument(12)),
                                    Integer.parseInt(packet.getArgument(33)),
                                    Integer.parseInt(packet.getArgument(39)),
                                    packet.getArgument(27)
                            ));
                }

                //System.out.println("Registered character!");
            }
        }else if(packetHeader.equals("su")) {
            if(packet.getArgument(1).equals("1")) {
                String playerId = packet.getArgument(2);

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

                if(packet.getArgument(3).equals("1") && packet.getArgument(11).equals("0")) {
                    String targetId = packet.getArgument(4);
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

                playerCharacter.addUsedSkill(Integer.parseInt(packet.getArgument(5)));
                playerCharacter.onDamageDealt(Integer.parseInt(packet.getArgument(13)));
                playerCharacter.addHitType(Integer.parseInt(packet.getArgument(14)));

                displayCharacter();
                //System.out.println("Registered damage");
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

                //System.out.println("Registered softDamage.");
            }
        }
    }

    public void handlerSend(Packet packet) throws IOException {
        String packetHeader = packet.getHeader();

         if(packetHeader.equals("say")) {
            if(packet.getArgument(1).charAt(0) == '!') {
                String pack = packet.getPacketFields().toString();
                String command = packet.getArgument(1).substring(1);

                if(command.equals("help")) {
                    try {
                        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Available commands: ");
                    System.out.println("Every command freezes counter! Remember that!");
                    System.out.println("!stop -> you can stop calculating damage with that.");
                    System.out.println("!info <player_name> -> you can display more info about specified player if his nickname was registered. ");
                    System.out.println("!resume -> unfreezes counter.");
                    System.out.println("!clear -> clear all info about everyone ever registered.");

                    isFrozen = true;
                } else if(command.equals("info")) {
                    try {
                        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                    }

                    String name = packet.getArgument(1);

                    for (Map.Entry<String, PlayerCharacter> player : playerCharacters.entrySet()) {
                        if(player.getValue().getName().equals(name)) {
                            player.getValue().displayFullInfo();
                            isFrozen = true;
                            break;
                        }
                    }

                } else if(command.equals("resume")) {
                    if(isFrozen) {
                        try {
                            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                        } catch (InterruptedException | IOException e) {
                            e.printStackTrace();
                        }
                        isFrozen = false;
                    }
                } else if(command.equals("clear")) {
                    try {
                        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                    }

                    playerCharacters = new HashMap<>();
                }
            }
        }
    }


    public void displayCharacter() {
        StringBuilder stringBuilder;

        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

        for (Map.Entry<String, PlayerCharacter> player : playerCharacters.entrySet()) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Player ID: ").append(player.getValue().getId()).append(" ");
            stringBuilder.append("Player Name: ").append(player.getValue().getName()).append(" ");
            stringBuilder.append("Full damage done: ").append(player.getValue().getDamage()).append(" ");
            stringBuilder.append("Full amount of hits: ").append(player.getValue().getAmountOfHits()).append(" ");
            stringBuilder.append("BonCritics hit: ").append(player.getValue().getBonCritics()).append(" ");
            stringBuilder.append("Critical hits: ").append(player.getValue().getCritics()).append(" ");
            stringBuilder.append("Missed hit: ").append(player.getValue().getMisses()).append(" ");
            stringBuilder.append("Normal attack soft damage hits: ").append(player.getValue().getSoftDamageAmount()).append(" ");
            stringBuilder.append("Number of summoned onyx: : ").append(player.getValue().getOnyxCounter()).append(" ");
            stringBuilder.append("Damage done by onyx shadow: ").append(0).append(" ");        //TODO
            stringBuilder.append("Lowest hit: ").append(player.getValue().getLowestHit()).append(" ");
            stringBuilder.append("Biggest hit: ").append(player.getValue().getBiggestHit()).append(" ");
            System.out.println(stringBuilder.toString() + "\n");
        }
    }
}
