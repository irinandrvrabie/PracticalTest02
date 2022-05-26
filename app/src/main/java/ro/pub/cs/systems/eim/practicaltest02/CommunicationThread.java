package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;


public class CommunicationThread extends Thread {

    private Server serverThread;
    private Socket socket;

    public CommunicationThread(Server serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket == null) {
            Log.e("[Practical Test 02]", "[COMMUNICATION THREAD] Socket is null!");
            return;
        }
        try {
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e("[Practical Test 02]", "[COMMUNICATION THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            Log.i("[Practical Test 02]", "[COMMUNICATION THREAD] Waiting for parameters from client");
            String request = bufferedReader.readLine();
            String key = bufferedReader.readLine();
            String value = bufferedReader.readLine();
            if (key == null || key.isEmpty() || request == null || request.isEmpty()) {
                Log.e("[Practical Test 02]", "[COMMUNICATION THREAD] Error receiving parameters from client");
                return;
            }
            HashMap<String, String> data = serverThread.getData();
            String result = null;
            if (request.equals("get") && data.containsKey(key)) {
                Log.i("[Practical Test 02]", "[COMMUNICATION THREAD] Getting the information from the cache...");
                result = data.get(key);
            } else {
                if(request.equals("put"))
                    data.put(key, value);
                printWriter.println(data);
                printWriter.flush();
            }

        } catch (IOException ioException) {
            Log.e("[Practical Test 02]", "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
            ioException.printStackTrace();

        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e("[Practical Test 02]", "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                    ioException.printStackTrace();

                }
            }
        }
    }

}