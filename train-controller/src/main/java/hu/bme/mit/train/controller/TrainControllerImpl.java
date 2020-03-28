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
	private int speedLimit = 100;
	private static final int TIME_INTERVAL = 1000;
	private final Timer timer = new Timer();

	public Table<String, String, Long> tachograph = HashBasedTable.create();

	public TrainControllerImpl() {
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				followSpeed();
			}
		}, TIME_INTERVAL, TIME_INTERVAL);
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
