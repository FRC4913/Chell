package org.usfirst.frc.team4913.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Arm {
	private static final int MASTER_CHANNEL = 5;
	private static final int FOLLOWER_CHANNEL = 6;

	private CANTalon masterMotor;
	private CANTalon followerMotor;

	public Arm() {
		masterMotor = new CANTalon(MASTER_CHANNEL);
		// masterMotor.changeControlMode(TalonControlMode.Position);
		// masterMotor.set(0);
		masterMotor.setPID(0.1, 0.0, 0.0);
		masterMotor.enableBrakeMode(true);
		masterMotor.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);

		followerMotor = new CANTalon(FOLLOWER_CHANNEL);
		followerMotor.setInverted(true);
		followerMotor.changeControlMode(TalonControlMode.Follower);
		followerMotor.set(masterMotor.getDeviceID());
		followerMotor.enableBrakeMode(true);
	}

	public void motorUp() {
		masterMotor.set(.4);
		print();
	}

	public void motorDown() {
		masterMotor.set(-.4);
		print();
	}

	/**
	 * Stop the arm movement by setting motor speed to 0.
	 */
	public void armStop() {
		masterMotor.set(0);

	}

	private void print() {
		SmartDashboard.putNumber("enc position: ", masterMotor.getEncPosition());
		SmartDashboard.putNumber("enc velocity: ", masterMotor.getEncVelocity());
		SmartDashboard.putNumber("motor value: ", masterMotor.get());
	}

}
