package org.usfirst.frc.team4913.robot.subsystems;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class DriveSubsystem extends Subsystem {

    private static final int FRONT_LEFT = 1;
    private static final int REAR_LEFT = 3;
    private static final int FRONT_RIGHT = 4;
    private static final int REAR_RIGHT = 2;

    private CANTalon frontLeftMotor, rearLeftMotor, frontRightMotor,
                    rearRightMotor;
    private RobotDrive drive;

    public DriveSubsystem() {
        super();

        frontLeftMotor = new CANTalon(FRONT_LEFT);
        rearLeftMotor = new CANTalon(REAR_LEFT);
        frontRightMotor = new CANTalon(FRONT_RIGHT);
        rearRightMotor = new CANTalon(REAR_RIGHT);

        rearLeftMotor.changeControlMode(TalonControlMode.Follower);
        rearRightMotor.changeControlMode(TalonControlMode.Follower);
        rearLeftMotor.set(FRONT_LEFT);
        rearRightMotor.set(FRONT_RIGHT);

        drive = new RobotDrive(frontLeftMotor, rearLeftMotor, frontRightMotor,
                        rearRightMotor);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

    public void arcadeDrive(Joystick stick) {
        drive.arcadeDrive(stick);
    }

    public void drive(double output, double curve) {
        drive.drive(output, curve);
    }
}
