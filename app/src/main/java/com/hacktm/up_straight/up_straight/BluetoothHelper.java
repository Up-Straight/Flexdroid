package com.hacktm.up_straight.up_straight;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Created by Bogdan on 2017-05-27.
 */
public class BluetoothHelper {
    BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice mDevice;
    BluetoothSocket mBluetoothSocket;
    BluetoothGatt mBluetoothGatt;
    BluetoothManager mBluetoothManager;
    InputStream mInputStream;
    Context context;
    boolean connected = false;
    private final static int REQUEST_ENABLE_BT = 1;
    private static final String DEVICE_ADDRESS = "98:4F:EE:0F:B5:78"; //TO DO
    private static final UUID PORT_UUID = UUID.fromString("19B10001-E8F2-537E-4F6C-D104768A1214");

    public BluetoothHelper(Context context) throws IOException {
        this.context=context;

        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager =
                (BluetoothManager)context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            Toast.makeText(context,"Device doesnt Support Bluetooth", Toast.LENGTH_SHORT).show();
        }
        if(!mBluetoothAdapter.isEnabled()) {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            Toast.makeText(context,"You might want to activate bluetooth first", Toast.LENGTH_SHORT).show();
            //startActivityForResult(enableAdapter, 0);
        }

        //pair device
        System.out.println("Before pairing devicessssssssssssssss");
        pairDevice();
        System.out.println("After pairing devicessssssssssssssss");

        //create socket
        //mBluetoothGatt = mDevice.connectGatt(this, false, mGattCallback);
    }



    public void connectSocket(){
        mBluetoothAdapter.cancelDiscovery();
        new ConnectBluetoothTask().execute(mBluetoothSocket);
    }

    public void startReading(){
        new ReadInputTask().execute(mInputStream);
    }

    private void pairDevice(){
        Set<BluetoothDevice> bondedDevices = mBluetoothAdapter.getBondedDevices();

        if(bondedDevices.isEmpty()) {
            Toast.makeText(context,"Please Pair the Device first",Toast.LENGTH_SHORT).show();
        } else {

            for (BluetoothDevice iterator : bondedDevices) {
                if(iterator.getAddress().equals(DEVICE_ADDRESS)) //Replace with iterator.getName() if comparing Device names.{
                    System.out.println("Found device!!!!!!!!!!!!!");
                    mDevice=iterator; //device is an object of type BluetoothDevice
                    break;
            }
        }
    }



    private class ReadInputTask extends AsyncTask<InputStream, Integer, Void>{

        @Override
        protected Void doInBackground(InputStream... params) {
            InputStream inputStream = params[0];

            byte[] mmBuffer = new byte[1024];
            int numBytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                int byteCount = 0;
                try {
                    byteCount = inputStream.available();
                    if(byteCount > 0)
                    {
                        byte[] rawBytes = new byte[byteCount];
                        inputStream.read(rawBytes);
                        final String string=new String(rawBytes,"UTF-8");
                        //TO DO
                        Toast.makeText(context,string,Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onPostExecute(Void ds) {
            Toast.makeText(context,"The reading task has finished",Toast.LENGTH_SHORT).show();
        }
    }

    private class ConnectBluetoothTask extends AsyncTask<BluetoothSocket, Integer, InputStream>{

        @Override
        protected InputStream doInBackground(BluetoothSocket... params) {
            BluetoothSocket bluetoothSocket = params[0];
            //this call blocks !!!
            try {
                bluetoothSocket.connect();
                //get inputStream
                mInputStream=bluetoothSocket.getInputStream();
                return mInputStream;
            } catch (IOException e) {
                //e.printStackTrace();
                System.out.println("CEVAAAAA");
                try {
                    bluetoothSocket = (BluetoothSocket) mDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(mDevice,1);
                    bluetoothSocket.connect();
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                } catch (InvocationTargetException e1) {
                    e1.printStackTrace();
                } catch (NoSuchMethodException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            //TO DO here
            //setProgressPercent(progress[0]);
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            mInputStream = inputStream;
            connected = mInputStream != null;
        }
    }

}
