package org.usfirst.frc.team467.robot;

import org.apache.log4j.Logger;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;

public class Grabber {
	private static final Logger LOGGER = Logger.getLogger(Grabber.class);

	private static Grabber instance;
	private SpeedController left;
	private SpeedController right;
	private boolean hadCube = false;
	private boolean hasCube = false;
	OpticalSensor os;

	private Grabber() {
		if (RobotMap.HAS_GRABBER) {
			left = new Spark(RobotMap.GRABBER_L_CHANNEL);
			right = new Spark(RobotMap.GRABBER_R_CHANNEL);
			os = OpticalSensor.getInstance();
		} else {
			left = new NullSpeedController();
			right = new NullSpeedController();
			os = OpticalSensor.getInstance();
		}
	}

	public static Grabber getInstance() {
		if (instance == null) {
			instance = new Grabber();
		}

		return instance;
	}

	public void grab(double throttle) {
		if (!RobotMap.HAS_GRABBER) {
			return;
		}

		// Save the previous state and check for current state.
		hadCube = hasCube;
		hasCube = os.detectedTarget();

		if (justGotCube()) {
			LOGGER.debug("Just got cube, rumbling");
			DriverStation.getInstance().getNavRumbler().rumble(100, 1.0);
		}

		if (Math.abs(throttle) < RobotMap.MIN_GRAB_SPEED) {
			throttle = 0.0;
		}

		LOGGER.debug("Grabber Throttle=" + throttle);
		hasCube = os.detectedTarget();
		left.set(throttle * RobotMap.MAX_GRAB_SPEED);
		right.set(-throttle * RobotMap.MAX_GRAB_SPEED);
	}

	public boolean justGotCube() {
		return !hadCube && hasCube;
	}
}