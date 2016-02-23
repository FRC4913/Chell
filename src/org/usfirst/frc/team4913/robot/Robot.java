package org.usfirst.frc.team4913.robot;

import org.usfirst.frc.team4913.robot.commands.AutonomousDrive;
import org.usfirst.frc.team4913.robot.commands.TeleOpDrive;
import org.usfirst.frc.team4913.robot.subsystems.DriveSubsystem;
import org.usfirst.frc.team4913.robot.subsystems.PulleySubsystem;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

    public static final PulleySubsystem pulley = new PulleySubsystem();
    public static final DriveSubsystem drivetrain = new DriveSubsystem();

    private static final int CAMERA_QUALITY = 50; // can be set to 0 - 100

    private CameraServer server;
    private TeleOpDrive teleopDrive;
    private AutonomousDrive autonomousDrive;
    private OI oi;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        teleopDrive = new TeleOpDrive();
        autonomousDrive = new AutonomousDrive();
        oi = new OI();

        server = CameraServer.getInstance();
        server.setQuality(CAMERA_QUALITY);
        server.startAutomaticCapture("cam0");
    }

    /**
     * This function is run once each time the robot enters autonomous mode
     */
    public void autonomousInit() {
        if (autonomousDrive != null)
            autonomousDrive.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
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
        Scheduler.getInstance().run();
        teleopDrive.start();
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }

}
