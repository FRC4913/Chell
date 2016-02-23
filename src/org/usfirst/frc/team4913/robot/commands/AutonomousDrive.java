package org.usfirst.frc.team4913.robot.commands;

import org.usfirst.frc.team4913.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutonomousDrive extends Command {
    private int autoCounter;

    public AutonomousDrive() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.drivetrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        autoCounter = 0;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        Robot.drivetrain.drive(-0.5, 0.0); // drive forwards half speed
        autoCounter++;
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        // Check if we've completed 100 loops
        // (approximately 2 seconds)
        return autoCounter > 100 ? true : false;
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.drivetrain.drive(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        Robot.drivetrain.drive(0, 0);
    }
}
