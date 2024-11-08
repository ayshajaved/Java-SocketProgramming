package Practise;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientServer {
    public static void main(String[] args) throws Exception {
        System.out.println("CLIENT IS CONNECTED TO SERVER!\n");
        // Setting the client socket connection to the server
            Socket socket = new Socket("localhost", 1234);
            Scanner sc = new Scanner(System.in);
            //printwriter is used to send data to the server
            PrintWriter p = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            //bufferedreader is used to receive data from the server
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //printing the menu to the server
            String serverResponse;
                while ((serverResponse = br.readLine()) != null) {
                    System.out.println(serverResponse);
                }
    }
}
