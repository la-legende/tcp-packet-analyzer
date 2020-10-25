package analyzer.packet;

import java.util.ArrayList;
import java.util.List;

public class Packet {
    private final String packet;
    private int argumentsAmount;
    private final List<String> packetFields;
    private PacketType packetType;

    public Packet(String packet) {
        this.packet = packet;
        packetFields = new ArrayList<>();
        serializeArguments(packet);
    }

    private void serializeArguments(String packet) {
        StringBuilder args = new StringBuilder();
        int argumentCounter = 0;

        packetType = packet.charAt(0) == '0' ? PacketType.RECEIVE : PacketType.SEND;

        for (int i = 2; i < packet.length(); i++) {
            if (packet.charAt(i) == ' ' || i == packet.length() - 1) {
                packetFields.add(args.toString());
                args = new StringBuilder();
                argumentCounter++;
            } else {
                args.append(packet.charAt(i));
            }
        }
        packetFields.add(args.toString());

        argumentsAmount = argumentCounter;
    }

    public String getRawPacket() { return packet; }

    public int getArgumentsAmount() { return argumentsAmount; }

    public List<String> getPacketFields() { return packetFields; }

    public PacketType getPacketType() { return packetType; }

    public String getHeader() { return packetFields.get(0); }

    public String getArgument(int index) {
        if(index < argumentsAmount) {
            return packetFields.get(index);
        }

        return "";
    }
}
