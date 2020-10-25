package analyzer.tcpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PortParser {
    public String getServerPort() throws Exception {
        try {
            String processId = findProcess();

            System.out.println("Searching for server port...");

            Process process = Runtime.getRuntime().exec("netstat -ano");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if(line.contains(processId)) {
                    System.out.println("Found server port!");
                    return parsePort(line);
                }
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        throw new Exception("Couldn't find server port!");
    }

    public String parsePort(String line) {
        System.out.println("Trying to parse server port...");

        line = line.replaceAll("( +)", " ");

        StringBuilder port = new StringBuilder();

        for(int i = line.indexOf(':') + 1; i < line.length(); i++) {
            if(line.charAt(i) == ' ') {
                break;
            }
            port.append(line.charAt(i));
        }

        System.out.println("Successfully parsed server port: " + port.toString() + "!");
        return port.toString();
    }

    public String findProcess() throws Exception {
        System.out.println("Searching for NostaleClientX process...");

        try {
            Process process = Runtime.getRuntime().exec("tasklist");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String readLine;
            while ((readLine = reader.readLine()) != null) {
                if(readLine.contains("NostaleClientX.exe")) {
                    System.out.println("Found NostaleClientX process!");
                    return parseProcessId(readLine);
                }
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        throw new Exception("Couldn't found NostaleClientX process!");
    }
    
    public String parseProcessId(String line) {
        System.out.println("Trying to parse process id...");

        line = line.replaceAll("( +)", " ");

        StringBuilder processId = new StringBuilder();

        boolean canStartParsing = false;
        for (char c : line.toCharArray()) {
            if (c == ' ') {
                canStartParsing = !canStartParsing;

                if(!canStartParsing) break;
            } else if (canStartParsing) {
                processId.append(c);
            }
        }

        System.out.println("Successfully parsed process id: " + processId.toString() + "!");
        return processId.toString();
    }
}
