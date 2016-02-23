package org.usfirst.frc.team4913.robot.commands;

import org.usfirst.frc.team4913.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class PulleyUp extends Command {

    public PulleyUp() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.pulley);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        Robot.pulley.moveUp(true);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.pulley.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        Robot.pulley.stop();
    }
}
