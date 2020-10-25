package analyzer.tcpClient;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class TCPClient {
    private Socket clientSocket;
    private int serverPort;
    private InputStream inputStream;
    private OutputStream outputStream;
    private BufferedReader bufferedReader;

    public TCPClient() throws Exception{
        parseServerPort();
        startConnection();
        initializeDataReader();
    }

    public void parseServerPort() throws Exception{
        PortParser portParser = new PortParser();
        this.serverPort = Integer.parseInt(portParser.getServerPort());
    }

    public void startConnection() throws IOException {
        try {
            System.out.println("Trying to connect to server...");
            clientSocket = new Socket("127.0.0.1", serverPort);
            inputStream = clientSocket.getInputStream();
            outputStream = clientSocket.getOutputStream();
            System.out.println("Gracefully connected to server on port: " + serverPort + ".");
        } catch (IOException e) {
            throw new IOException("Couldn't connect with server.");
        }
    }

    public void closeConnection() throws IOException {
        try {
            clientSocket.close();
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            throw new IOException("Couldn't close connection with server.");
        }
    }

    public void initializeDataReader() {
        System.out.println("Initialized data reader...");
        bufferedReader =  new BufferedReader(new InputStreamReader(inputStream));
    }

    public String readDataLine() throws SocketException{
        try {
            return bufferedReader.readLine();
        } catch (SocketException ex) {
            throw new SocketException("Connection reset");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public BufferedReader getDataReader() { return bufferedReader; }
}
