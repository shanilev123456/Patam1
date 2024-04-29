package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class BookScrabbleHandler implements ClientHandler {

    private final DictionaryManager dictionaryManager;

    public BookScrabbleHandler(){
        this.dictionaryManager= DictionaryManager.get();
    }

    @Override
    public void handleClient(InputStream inFromClient, OutputStream outToClient) {
        Scanner scanner = new Scanner(inFromClient);
        PrintWriter out = new PrintWriter(outToClient);

        if (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            String[] parts = input.split(",");

            if (parts.length < 3) { 
                out.flush();
                return;
            }

            String action = parts[0]; 
            String bookId = parts[1]; 
            String queryWord = parts[parts.length - 1]; 

            boolean result;
            if ("Q".equals(action)) { 
                result = dictionaryManager.query(Arrays.copyOfRange(parts, 1, parts.length));
            } else if ("C".equals(action)) { 
                result = dictionaryManager.challenge(Arrays.copyOfRange(parts, 1, parts.length));
            } else {
                out.flush();
                return;
            }

            out.println(result);
            out.flush();
        }

        scanner.close();
        out.close();
    }

    @Override
    public void close() {
       
    }
}
