package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import ro.pub.cs.systems.eim.practicaltest02.Utilities;

public class Client extends Thread {

    private String address;
    private int port;
    private String key;
    private String value;
    private String request;
    private TextView response;

    private Socket socket;

    public Client(String address, int port, String key, String value, String request, TextView result) {
        this.address = address;
        this.port = port;
        this.key = key;
        this.value = value;
        this.request = request;
        this.response = result;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            if (socket == null) {
                Log.e("[Practical Test 02]", "[CLIENT THREAD] Could not create socket!");
                return;
            }
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e("[Practical Test 02]", "[CLIENT THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            printWriter.println(key);
            printWriter.flush();
            printWriter.println(value);
            printWriter.flush();
            String result;
            while ((result = bufferedReader.readLine()) != null) {
                final String finalizedWeateherInformation = result;
                response.post(new Runnable() {
                    @Override
                    public void run() {
                        response.setText(finalizedWeateherInformation);
                    }
                });
            }
        } catch (IOException ioException) {
            Log.e("[Practical Test 02]", "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            ioException.printStackTrace();

        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e("[Practical Test 02]", "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                    ioException.printStackTrace();

                }
            }
        }
    }

}