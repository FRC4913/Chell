/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.usfirst.frc.team4913.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The Arm subsystem is the pulley mechanism to move the Robot Arm up or down.
 *
 * @author michellephan
 */
public class Arm {
	private static final int ARM_CHANNEL = 5;
	private static final int ENC_SOURCE_1 = 0;
	private static final int ENC_SOURCE_2 = 1;
	private static final int DISTANCE_PER_PULSE = 1;

	// Limit Switches
	private static final int UP_SWITCH_SOURCE = 2;
	private static final int DOWN_SWITCH_SOURCE = 3;

	private static final int ENC_UPPER_LIMIT = 1000;
	private static final int ENC_LOWER_LIMIT = 0;
	private static final int START_PID_DOWN = 200;
	private static final int START_PID_UP = 800;

	private Talon armMotor;
	private Encoder enc;
	private DigitalInput upSwitch, downSwitch;

	private double k = .005; // proportionality constant for PID

	public Arm() {
		armMotor = new Talon(ARM_CHANNEL);
		upSwitch = new DigitalInput(UP_SWITCH_SOURCE);
		downSwitch = new DigitalInput(DOWN_SWITCH_SOURCE);

		enc = new Encoder(ENC_SOURCE_1, ENC_SOURCE_2);
		enc.setDistancePerPulse(DISTANCE_PER_PULSE);
		enc.reset();
	}

	/**
	 * Move the Robot arm up.
	 *
	 * The arm movement is controlled by a Victor motor controller and
	 * restricted by the encoder and (optionally) the limit switch. The arm will
	 * stop when the encoder distance is less than {@link #ENC_LOWER_LIMIT} or
	 * when the up limit switch is set (if added), whichever comes first.
	 *
	 * If pidControl is set to true, the arm movement will also slow down
	 * proportional to the remaining distance.
	 *
	 * @param pidControl
	 *            use PID control to limit the motor speed (boolean)
	 */
	public void armUp(boolean pidControl) {
		double distance = enc.getDistance();
		if (distance > ENC_LOWER_LIMIT && !upSwitch.get()) {
			if (pidControl && distance < START_PID_DOWN) {
				double speed = distance * k;
				armMotor.set(-speed);
			} else
				armMotor.set(-1);
		} else
			armMotor.set(0);
		print();
	}

	/**
	 * Move the Robot arm down.
	 *
	 * The arm movement is controlled by a Victor motor controller and
	 * restricted by the encoder and (optionally) the limit switch. The arm will
	 * stop when the encoder distance is greater than {@link #ENC_UPPER_LIMIT}
	 * or when the down limit switch is set (if added), whichever comes first.
	 *
	 * If pidControl is set to true, the arm movement will also slow down
	 * proportional to the remaining distance.
	 *
	 * @param pidControl
	 *            use PID control to limit the motor speed (boolean)
	 */
	public void armDown(boolean pidControl) {
		double distance = enc.getDistance();
		if (distance < ENC_UPPER_LIMIT && !downSwitch.get()) {
			if (pidControl && distance > START_PID_UP) {
				double speed = (ENC_UPPER_LIMIT - distance) * k;
				armMotor.set(speed);
			} else
				armMotor.set(1);
		} else {
			armMotor.set(0);
		}
		print();
	}

	/**
	 * Stop the arm movement by setting motor speed to 0.
	 */
	public void armStop() {
		armMotor.set(0);
	}

	private void print() {
		SmartDashboard.putNumber("encoder: ", enc.getDistance());
		SmartDashboard.putBoolean("direction: ", enc.getDirection());
		SmartDashboard.putNumber("motor value: ", armMotor.get());

	}
}
