package hu.bme.mit.train.controller;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import hu.bme.mit.train.interfaces.TrainController;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TrainControllerImpl implements TrainController {

	private int step = 0;
	private int referenceSpeed = 0;
	private int speedLimit = 99; //in order to make defense from Covid-19
	public Table<String, String, Long> tachograph = HashBasedTable.create();
	private Thread t; //i ve chosen thread as the descreption of task suggested
	private int interval = 3000; //3sec periodic

	public TrainControllerImpl() {
		t = new Thread() {
			public void run() {
				t.run();
				try {
					followSpeed();
					t.sleep(interval);
				}
				catch (Exception err) { err.printStackTrace(); }
			}
		};
	}

	public void recordData() {
		String cTime = new Date().toString();
		tachograph.put(cTime, "Time", new Date().getTime());
		tachograph.put(cTime, "Speed", (long) referenceSpeed);
		tachograph.put(cTime, "JoyPos", (long) step);
	}

	@Override
	public void followSpeed() {
		if (referenceSpeed < 0) {
			referenceSpeed = 0;
		} else {
		    if(referenceSpeed+step > 0) {
                referenceSpeed += step;
            } else {
		        referenceSpeed = 0;
            }
		}

		enforceSpeedLimit();
		recordData();
	}

	@Override
	public int getReferenceSpeed() {
		return referenceSpeed;
	}

	@Override
	public void setSpeedLimit(int speedLimit) {
		this.speedLimit = speedLimit;
		enforceSpeedLimit();
		recordData();
		
	}

	private void enforceSpeedLimit() {
		if (referenceSpeed > speedLimit) {
			referenceSpeed = speedLimit;
		}
	}

	@Override
	public void setJoystickPosition(int joystickPosition) {
		this.step = joystickPosition;
		recordData();
	}

}
