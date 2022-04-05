package com.boolb;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import top.defaults.colorpicker.ColorObserver;
import top.defaults.colorpicker.ColorPickerPopup;
import top.defaults.colorpicker.ColorPickerView;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter myBluetooth = null;
    private Set<BluetoothDevice> pairedDevices;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    String address = null;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    ProgressBar progress;
    TextView status;
    Button reconnect;
    Button reset;
    Switch switchh;
    ColorPickerView picker;
    int red = 255, green = 255, blue = 255;

    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... devices) {
            try {
                if (btSocket == null || !isBtConnected) {
                    BluetoothDevice device = myBluetooth.getRemoteDevice(address);
                    btSocket = device.createInsecureRfcommSocketToServiceRecord(myUUID);
                    btSocket.connect();
                }
            } catch (IOException e) {
                ConnectSuccess = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (!ConnectSuccess) {
                status.setText("status: failed");
                progress.setVisibility(View.INVISIBLE);
                reconnect.setEnabled(true);
            } else {
                status.setText("status: connected");
                progress.setVisibility(View.INVISIBLE);
                reconnect.setEnabled(true);
                reset.setEnabled(true);
                switchh.setEnabled(true);
                isBtConnected = true;
            }
        }
    }

    private void connect() {
        status = (TextView) findViewById(R.id.status);
        progress = (ProgressBar) findViewById(R.id.progress);
        reconnect = (Button) findViewById(R.id.reconnect);
        reset = (Button) findViewById(R.id.reset);
        switchh = (Switch) findViewById(R.id.switchh);
        status.setText("status: connecting");
        progress.setVisibility(View.VISIBLE);
        reconnect.setEnabled(false);
        reset.setEnabled(false);
        switchh.setEnabled(false);
        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        if (myBluetooth == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();
            status.setText("status: failed");
            progress.setVisibility(View.INVISIBLE);
            reconnect.setEnabled(true);
        } else {
            if (!myBluetooth.isEnabled()) {
                status.setText("status: failed");
                progress.setVisibility(View.INVISIBLE);
                reconnect.setEnabled(true);
                Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(turnBTon, 1);
            } else {
                pairedDevices = myBluetooth.getBondedDevices();
                boolean found = false;
                for (BluetoothDevice bt : pairedDevices) {
                    if (bt.getName().equals("HC-05")) {
                        address = bt.getAddress();
                        ConnectBT connect = new ConnectBT();
                        connect.execute();
                        found = true;
                    }
                }
                if (found == false) {
                    Toast.makeText(getApplicationContext(), "Device not found", Toast.LENGTH_LONG).show();
                    status.setText("status: failed");
                    progress.setVisibility(View.INVISIBLE);
                    reconnect.setEnabled(true);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        picker = (ColorPickerView) findViewById(R.id.colorPicker);
        picker.setInitialColor((int) Long.parseLong("ffffffff", 16));
        picker.subscribe(new ColorObserver() {
            @Override
            public void onColor(int color, boolean fromUser, boolean shouldPropagate) {
                if (btSocket != null) {
                    try {
                        red = red(color);
                        green = green(color);
                        blue = blue(color);
                        String msg = String.valueOf(red) + "." + String.valueOf(green) + "." + String.valueOf(blue) + ";";
                        btSocket.getOutputStream().write(String.valueOf(msg).getBytes());
                        btSocket.getOutputStream().flush();
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), "Device not available", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        connect();
    }

    public void reconnect(android.view.View view) throws IOException {
        if (isBtConnected == true) {
            btSocket.close();
            btSocket = null;
            isBtConnected = false;
        }
        connect();
    }

    public int red(int color) {
        String hex = Integer.toHexString(color);
        return Integer.valueOf(hex.substring(2, 4), 16);
    }

    public int green(int color) {
        String hex = Integer.toHexString(color);
        return Integer.valueOf(hex.substring(4, 6), 16);
    }

    public int blue(int color) {
        String hex = Integer.toHexString(color);
        return Integer.valueOf(hex.substring(6, 8), 16);
    }

    public void onoff(android.view.View view) {
        switchh = (Switch) findViewById(R.id.switchh);
        if (switchh.isChecked()) {
            if (btSocket != null) {
                try {
                    String msg = String.valueOf(red) + "." + String.valueOf(green) + "." + String.valueOf(blue) + ";";
                    btSocket.getOutputStream().write(String.valueOf(msg).getBytes());
                    btSocket.getOutputStream().flush();
                } catch (IOException e) {

                    Toast.makeText(getApplicationContext(), "Device not available", Toast.LENGTH_LONG).show();
                }
            }
            Toast.makeText(getApplicationContext(), "on", Toast.LENGTH_LONG).show();
        } else {
            if (btSocket != null) {
                try {
                    String msg = String.valueOf(0) + "." + String.valueOf(0) + "." + String.valueOf(0) + ";";
                    btSocket.getOutputStream().write(String.valueOf(msg).getBytes());
                    btSocket.getOutputStream().flush();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Device not available", Toast.LENGTH_LONG).show();
                }
            }
            Toast.makeText(getApplicationContext(), "off", Toast.LENGTH_LONG).show();
        }
    }

    public void changeRed(android.view.View view) {
        picker = (ColorPickerView) findViewById(R.id.colorPicker);
        picker.reset();
        if (btSocket != null) {
            try {
                red = 255;
                green = 0;
                blue = 0;
                String msg = String.valueOf(red) + "." + String.valueOf(green) + "." + String.valueOf(blue) + ";";
                btSocket.getOutputStream().write(String.valueOf(msg).getBytes());
                btSocket.getOutputStream().flush();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Device not available", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void changeGreen(android.view.View view) {
        picker = (ColorPickerView) findViewById(R.id.colorPicker);
        picker.reset();
        if (btSocket != null) {
            try {
                red = 0;
                green = 255;
                blue = 0;
                String msg = String.valueOf(red) + "." + String.valueOf(green) + "." + String.valueOf(blue) + ";";
                btSocket.getOutputStream().write(String.valueOf(msg).getBytes());
                btSocket.getOutputStream().flush();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Device not available", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void changeBlue(android.view.View view) {
        picker = (ColorPickerView) findViewById(R.id.colorPicker);
        picker.reset();
        if (btSocket != null) {
            try {
                red = 0;
                green = 0;
                blue = 255;
                String msg = String.valueOf(red) + "." + String.valueOf(green) + "." + String.valueOf(blue) + ";";
                btSocket.getOutputStream().write(String.valueOf(msg).getBytes());
                btSocket.getOutputStream().flush();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Device not available", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void reset(android.view.View view) {
        picker = (ColorPickerView) findViewById(R.id.colorPicker);
        picker.reset();
        if (btSocket != null) {
            try {
                red = 255;
                green = 255;
                blue = 255;
                String msg = String.valueOf(red) + "." + String.valueOf(green) + "." + String.valueOf(blue) + ";";
                btSocket.getOutputStream().write(String.valueOf(msg).getBytes());
                btSocket.getOutputStream().flush();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Device not available", Toast.LENGTH_LONG).show();
            }
        }

    }
}

