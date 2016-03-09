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
	private static final int RETRACTED_SWITCH_SOURCE = 2;// plate hits switch
															// when plate is all
															// the way up
	private static final int DEPLOYED_SWITCH_SOURCE = 3;// plate hits switch
														// when plate is all the
														// way down

	private static int ENC_UPPER_LIMIT = 1000;
	private static int ENC_LOWER_LIMIT = 0;

	// start applying PID 1/4th of the way
	private static final int PID_THRESHOLD = (ENC_UPPER_LIMIT - ENC_LOWER_LIMIT) / 4;
	private static final int START_PID_DOWN = ENC_UPPER_LIMIT - PID_THRESHOLD;
    private static final int START_PID_UP = ENC_LOWER_LIMIT + PID_THRESHOLD;

	private double motorMinSpeed = 0.1;

	private Talon armMotor;
	private Encoder enc;
	private DigitalInput retractedSwitch, deployedSwitch;

	private double k = 1 / PID_THRESHOLD; // proportionality constant for PID

	public Arm() {
		armMotor = new Talon(ARM_CHANNEL);
		retractedSwitch = new DigitalInput(RETRACTED_SWITCH_SOURCE);
		deployedSwitch = new DigitalInput(DEPLOYED_SWITCH_SOURCE);

		enc = new Encoder(ENC_SOURCE_1, ENC_SOURCE_2);
		enc.setDistancePerPulse(DISTANCE_PER_PULSE);
		enc.reset();
		// try this if it makes more sense logically to have the encoder values
		// go from higher to lower as the arm moves from up to down
		// enc.setReverseDirection(true);
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
		if (distance > ENC_LOWER_LIMIT/* && !upSwitch.get()*/) {
			if (pidControl && distance < START_PID_UP) {
				double speed = distance * k;
				//speed = speed > motorMinSpeed ? speed : motorMinSpeed;
				armMotor.set(speed);
			} else
				armMotor.set(1);
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
		if (distance < ENC_UPPER_LIMIT /*&& !downSwitch.get()*/) {
			if (pidControl && distance > START_PID_DOWN) {
				double speed = (ENC_UPPER_LIMIT - distance) * k;
				//speed = speed > motorMinSpeed ? speed : motorMinSpeed;
				armMotor.set(-speed);
			} else
				armMotor.set(-1);
		} else {
			armMotor.set(0);
		}
		print();
	}

	public void motorUp() {
			armMotor.set(.5);
	}

	public void motorDown() {
			armMotor.set(-.5);
	}
	
	public void autoUp() {
		if (retractedSwitch.get()) {
			armMotor.set(.3);
			print();
		}
	}
	
	public void autoDown() {
		if (deployedSwitch.get()) {
			armMotor.set(-.3);
			print();
		}
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
		SmartDashboard.putBoolean("retracted limit ", retractedSwitch.get());
		SmartDashboard.putBoolean("deployed limit ", deployedSwitch.get());
		SmartDashboard.putNumber("motor value: ", armMotor.get());

	}

}
