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

	private int encUpperLimit = 1000;
	private int encLowerLimit = 0;

	// start applying PID 1/4th of the way
	private int pidThreshold = (encUpperLimit - encLowerLimit) / 4;
	private int startPIDDown = encUpperLimit - pidThreshold;
	private int startPIDUp = encLowerLimit + pidThreshold;

	private double motorMinSpeed = 0.1;

	private Talon armMotor;
	private Encoder enc;
	private DigitalInput retractedSwitch, deployedSwitch;

	private double k = 1 / pidThreshold; // proportionality constant for PID

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
		if (distance > encLowerLimit/* && retractedSwitch.get() */) {
			if (pidControl && distance < startPIDUp) {
				double speed = distance * k;
				speed = speed > motorMinSpeed ? speed : motorMinSpeed;
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
		if (distance < encUpperLimit /* && deployedSwitch.get() */) {
			if (pidControl && distance > startPIDDown) {
				double speed = (encUpperLimit - distance) * k;
				speed = speed > motorMinSpeed ? speed : motorMinSpeed;
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
		while (!retractedSwitch.get()) {
			armMotor.set(.5);
		}
		armMotor.set(0);
	}
	
	public void autoDown() {
		while (!deployedSwitch.get()) {
			armMotor.set(-.5);
		}
			armMotor.set(0);
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

	public int getEncUpperLimit() {
		return encUpperLimit;
	}

	public void setEncUpperLimit(int encUpperLimit) {
		this.encUpperLimit = encUpperLimit;
	}

	public double getK() {
		return k;
	}

	public void setK(double k) {
		this.k = k;
	}

	public double getMotorMinSpeed() {
		return motorMinSpeed;
	}

	public void setMotorMinSpeed(double motorMinSpeed) {
		this.motorMinSpeed = motorMinSpeed;
	}

}
