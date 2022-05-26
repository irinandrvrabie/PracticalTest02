package ro.pub.cs.systems.eim.practicaltest02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText serverPortEditText = null;
    private Button connectButton = null;

    private EditText clientAddressEditText = null;
    private EditText clientPortEditText = null;
    private EditText key = null;
    private EditText value = null;
    private Spinner informationTypeSpinner = null;
    private Button request = null;
    private TextView result = null;

    private Server serverThread = null;
    private Client clientThread = null;

    private ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = serverPortEditText.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            serverThread = new Server(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() == null) {
                Log.e("[Practical Test 02]", "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }

            serverThread.start();
        }

    }

    private requestClickListener requestClickListener = new requestClickListener();
    private class requestClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String clientAddress = clientAddressEditText.getText().toString();
            String clientPort = clientPortEditText.getText().toString();
            if (clientAddress == null || clientAddress.isEmpty()
                    || clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }
            String key1 = key.getText().toString();
            String value1 = value.getText().toString();

            String request = informationTypeSpinner.getSelectedItem().toString();
            if (key1 == null || value1.isEmpty()
                    || request == null || request.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client should be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            result.setText("");

            clientThread = new Client(
                    clientAddress, Integer.parseInt(clientPort), key1, value1, request, result
            );
            clientThread.start();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("[Practical Test 02]", "[MAIN ACTIVITY] onCreate() callback method has been invoked");
        setContentView(R.layout.activity_main);

        serverPortEditText = (EditText)findViewById(R.id.server_port_edit_text);
        connectButton = (Button)findViewById(R.id.connect_button);
        connectButton.setOnClickListener(connectButtonClickListener);

        clientAddressEditText = (EditText)findViewById(R.id.client_ip_edit_text);
        clientPortEditText = (EditText)findViewById(R.id.client_port_edit_text);
        key = (EditText)findViewById(R.id.key);
        value = (EditText)findViewById(R.id.value);
        informationTypeSpinner = (Spinner)findViewById(R.id.request);
        request = (Button)findViewById(R.id.connect2_button);
        request.setOnClickListener(requestClickListener);
        result = (TextView)findViewById(R.id.info);
    }

    @Override
    protected void onDestroy() {
        Log.i("[Practical Test 02]", "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}