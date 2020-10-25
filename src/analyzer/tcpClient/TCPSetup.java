package analyzer.tcpClient;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class TCPSetup {
    TCPClient tcpClient;

    public TCPSetup(){
        while(true) {
            try {
                tcpClient = new TCPClient();
                break;
            } catch (Exception e) {
                System.out.println('\n' + e.toString());
                System.out.println("Retry in 3seconds... \n");

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        }
    }

    public TCPClient getTcpClient() { return tcpClient; }
}
