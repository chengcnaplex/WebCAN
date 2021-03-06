package com.android.socketcan;

import java.io.IOException;
import java.util.Arrays;

import android.R.integer;
import android.util.Log;

import com.android.socketcan.CanSocket.CanFrame;
import com.android.socketcan.CanSocket.CanId;
import com.android.socketcan.CanSocket.CanInterface;
import com.android.socketcan.CanSocket.Mode;

public class CanUtils {

	public static final int EFF = 0;

	public static final int SFF = 1;

	public static final int DATA = 0;

	public static final int REMOTE = 1;

	private static String TAG = "CanUtils";

	private static CanSocket socket;
	private static CanInterface canif;

	//triple-sampling on 
	private static String cmdList[] = { "su 0 netcfg can0 down",
//			"su 0 ip link set can0 type can bitrate 1000000 loopback off listen-only on  triple-sampling on",
			"su 0 ip link set can0 type can bitrate 1000000",
			"su 0 netcfg can0 up"};
	
	private static void setCmdList(String can0bitrate,String can1bitrate){
		cmdList[1] = "su 0 ip link set can0 type can bitrate " +can0bitrate+ "  triple-sampling on";
	}

	public static void configCan0Device(String can0bitrate,String can1bitrate) {
		setCmdList(can0bitrate, can1bitrate);
		for (String cmd : cmdList) {
			try {
				Log.e(TAG, cmd);
				Log.e(TAG, ShellExecute.execute(cmd));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void initCan0() {
		try {
			socket = new CanSocket(Mode.RAW);
			canif = new CanInterface(socket, "can0");
			socket.bind(canif);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void closeCan() {
		try {
			socket.close();
			socket = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static CanFrame revCan0Data() {
		try {
			return socket.recv();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static int sendCan0Data(int Type, int Format,int address, byte[] data) {
		try {
			CanId id = new CanId(address);
			switch (Type) {
			case EFF:
				id.setEFFSFF();
				break;
			case SFF:
				id.clearEFFSFF();
				break;
			default:
				break;
			}
			
			switch (Format) {
			case DATA:
				id.clearRTR();
				break;
			case REMOTE:
				id.setRTR();
				break;
			default:
				break;
			}
			int i = 0;
			byte currentData[] = null;
			for (; i * 8 < data.length - 8; i++) {
				currentData = Arrays.copyOfRange(data, i * 8, (i + 1) * 8);
				socket.send(new CanFrame(canif, id, currentData));
			}
			currentData = Arrays.copyOfRange(data, i * 8, data.length);
			return	socket.send(new CanFrame(canif, id, currentData));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("error", "error");
			e.printStackTrace();
			return -1;
		}
	}

}
