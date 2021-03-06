package org.usfirst.frc.team4913.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	RobotDrive myRobot;
	Joystick stick;
	int autoLoopCounter;
	int autoPlateCounter;
	CANTalon frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor;
	CameraServer server;

	int autoMode;
	boolean autoModeEnabled;

	private static final int FRONT_LEFT = 1;
	private static final int REAR_LEFT = 2;
	private static final int FRONT_RIGHT = 4;
	private static final int REAR_RIGHT = 3;
	private static final int CAMERA_QUALITY = 50; // can be set to 0 - 100

	private static final boolean PID_ENABLED = true;
	Arm arm;
	Preferences prefs;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		stick = new Joystick(0);
		prefs = Preferences.getInstance();

		arm = new Arm();
		arm.setEncUpperLimit(prefs.getInt("Upper Limit", 1000));
		arm.setK(prefs.getDouble("K Constant", 0.005));
		arm.setMotorMinSpeed(prefs.getDouble("Motor Min Speed", 0.1));

		frontLeftMotor = new CANTalon(FRONT_LEFT);
		rearLeftMotor = new CANTalon(REAR_LEFT);
		frontRightMotor = new CANTalon(FRONT_RIGHT);
		rearRightMotor = new CANTalon(REAR_RIGHT);

		/*rearLeftMotor.changeControlMode(TalonControlMode.Follower);
		rearRightMotor.changeControlMode(TalonControlMode.Follower);
		rearLeftMotor.set(FRONT_LEFT);
		rearRightMotor.set(FRONT_RIGHT);*/

		server = CameraServer.getInstance();
		server.setQuality(CAMERA_QUALITY);
		server.startAutomaticCapture("cam0");

		// myRobot = new RobotDrive(frontLeftMotor, frontRightMotor);
		myRobot = new RobotDrive(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);
	}

	/**
	 * This function is run once each time the robot enters autonomous mode
	 */
	public void autonomousInit() {
		autoMode = prefs.getInt("auto mode", 1);
		autoModeEnabled = prefs.getBoolean("auto mode enabled", true);
		autoLoopCounter = 0;
		autoPlateCounter = 0;
	}

	public void fastAuto() {
		myRobot.drive(-1, 0);
	}

	public void deployAuto() {
		arm.autoDown();
		myRobot.drive(-.5, 0);
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		if (!autoModeEnabled){
			return;
		}

		if (autoMode == 0) { // low bar, portcullis
			if (autoPlateCounter < 40) {
				arm.motorUp();
				autoPlateCounter++;
			} else if (autoLoopCounter < 140 && autoPlateCounter >= 40) {
				arm.armStop();
				myRobot.drive(-0.4, 0.0); // drive forwards half speed
				autoLoopCounter++;
			} else {
				arm.armStop();
				myRobot.drive(0.0, 0.0); // stop robot
			}

		} else if (autoMode == 1)// rocky terrain
		{
			if (autoLoopCounter < 120 && autoLoopCounter >= 1) {
				myRobot.drive(-0.5, 0.0);
				autoLoopCounter++;
			} else {
				myRobot.drive(0, 0);
				arm.armStop();
				autoLoopCounter++;
			}
		} else if (autoMode == 2)// high wall
		{
			if (autoLoopCounter < 120) {
				myRobot.drive(-0.95, 0);
				autoLoopCounter++;
			} else {
				myRobot.drive(0, 0);
			}
		} else if (autoMode == 3)// moat
		{
			if (autoLoopCounter < 100) {
				myRobot.drive(0.95, 0);
				autoLoopCounter++;
			} else {
				myRobot.drive(0, 0);
			}
		}
		else if (autoMode == 4){ // arm down, drive backward half speed
			if (autoPlateCounter < 40) {
				arm.motorUp();
				autoPlateCounter++;
			} else if (autoLoopCounter < 140 && autoPlateCounter >= 40) {
				arm.armStop();
				myRobot.drive(0.4, 0.0); // drive backwards half speed
				autoLoopCounter++;
			} else {
				arm.armStop();
				myRobot.drive(0.0, 0.0); // stop robot
			}


		}
	}

	/**
	 * This function is called once each time the robot enters tele-operated
	 * mode
	 */
	public void teleopInit() {
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		if (stick.getRawButton(3)) {
			arm.armUp(PID_ENABLED);
		} else if (stick.getRawButton(1)) {
			arm.armStop();
		} else if (stick.getRawButton(2)) {
			arm.armDown(PID_ENABLED);
		} else if (stick.getRawButton(5)) {
			arm.motorUp(); // run without encoder shutoff
		} else if (stick.getRawButton(4)) {
			arm.motorDown(); // run without encoder shutoff
		} else if (stick.getRawButton(11)) {
			arm.autoUp();
		} else if (stick.getRawButton(6)) {
			arm.autoDown();
		} else
			arm.armStop();
		// myRobot.arcadeDrive(stick);
		myRobot.arcadeDrive(stick.getRawAxis(1)/1.2, -stick.getRawAxis(0)/1.2);
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
		LiveWindow.run();
	}

}
