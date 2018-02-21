package org.usfirst.frc.team467.robot;

import org.apache.log4j.Logger;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class uses a state machine with two states: UP <-> DOWN.
 * Each function checks that it's in the appropriate precondition before it starts,
 * and then sets the appropriate state when it finishes.
 */
public class Ramp {
	private static final Logger LOGGER = Logger.getLogger(Ramp.class);

	// Compressor automatically set to closedLoopControl when Solenoid is declared
	private DoubleSolenoid solenoid;

	private String name;
	private State state;

	public enum State {
		UP,
		DOWN;
	}

	public Ramp(String name, int forwardChannel, int reverseChannel) {
		if (!RobotMap.HAS_RAMPS) {
			LOGGER.info("No ramps");
			return;
		}

		solenoid = new DoubleSolenoid(forwardChannel, reverseChannel);
		this.name = name;
		state = State.DOWN;
	}

	public State getState() {
		return state;
	}

	public void toggle() {
		if (!RobotMap.HAS_RAMPS) {
			return;
		}

		switch (state) {
		case DOWN:
			lift();
			break;
		case UP:
			drop();
			break;
		}
	}

	public void lift() {
		if (!RobotMap.HAS_RAMPS) {
			return;
		}

		if (state == State.DOWN) {
			solenoid.set(DoubleSolenoid.Value.kForward);
			LOGGER.info(name + " lifting");
			state = State.UP;
		}
	}

	public void drop() {
		if (!RobotMap.HAS_RAMPS) {
			return;
		}

		if (state == State.UP) {
			solenoid.set(DoubleSolenoid.Value.kReverse);
			LOGGER.info(name + " dropping");
			state = State.DOWN;
		}
	}

	public void telemetry() {
		SmartDashboard.putString("Ramps/" + name + "/State", state.name());
	}
}