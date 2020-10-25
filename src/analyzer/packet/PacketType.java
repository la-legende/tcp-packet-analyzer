package analyzer.packet;

public enum PacketType {
    RECEIVE(0),
    SEND(1);

    int value;

    PacketType(int value) {
        this.value = value;
    }
}
