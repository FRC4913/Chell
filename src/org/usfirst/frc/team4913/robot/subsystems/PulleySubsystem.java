package org.usfirst.frc.team4913.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class PulleySubsystem extends Subsystem {

    private static final int MOTOR_CHANNEL = 5;
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

    private Victor motor;
    private Encoder enc;
    private DigitalInput upSwitch, downSwitch;

    private double k = .005; // proportionality constant for PID

    public PulleySubsystem() {
        super();

        motor = new Victor(MOTOR_CHANNEL);
        upSwitch = new DigitalInput(UP_SWITCH_SOURCE);
        downSwitch = new DigitalInput(DOWN_SWITCH_SOURCE);

        enc = new Encoder(ENC_SOURCE_1, ENC_SOURCE_2);
        enc.setDistancePerPulse(DISTANCE_PER_PULSE);
        enc.reset();
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

    /**
     * Move the pulley system up.
     *
     * The pulley is controlled by a Victor motor controller and restricted by
     * the encoder and (optionally) the limit switch. The pulley will stop when
     * the encoder distance is less than {@link #ENC_LOWER_LIMIT} or when the up
     * limit switch is set (if added), whichever comes first.
     *
     * If pidControl is set to true, the pulley movement will also slow down
     * proportional to the remaining distance.
     *
     * @param pidControl
     *            use PID control to limit the motor speed (boolean)
     */
    public void moveUp(boolean pidControl) {
        double distance = enc.getDistance();
        if (distance > ENC_LOWER_LIMIT && !upSwitch.get()) {
            if (pidControl && distance < START_PID_DOWN) {
                double speed = distance * k;
                motor.set(-speed);
            } else
                motor.set(-1);
        } else
            motor.set(0);
        print();
    }

    /**
     * Move the pulley system down.
     *
     * The pulley is controlled by a Victor motor controller and restricted by
     * the encoder and (optionally) the limit switch. The pulley will stop when
     * the encoder distance is greater than {@link #ENC_UPPER_LIMIT} or when the
     * down limit switch is set (if added), whichever comes first.
     *
     * If pidControl is set to true, the pulley movement will also slow down
     * proportional to the remaining distance.
     *
     * @param pidControl
     *            use PID control to limit the motor speed (boolean)
     */
    public void moveDown(boolean pidControl) {
        double distance = enc.getDistance();
        if (distance < ENC_UPPER_LIMIT && !downSwitch.get()) {
            if (pidControl && distance > START_PID_UP) {
                double speed = (ENC_UPPER_LIMIT - distance) * k;
                motor.set(speed);
            } else
                motor.set(1);
        } else {
            motor.set(0);
        }
        print();
    }

    /**
     * Stop the arm movement by setting motor speed to 0.
     */
    public void stop() {
        motor.set(0);
    }

    private void print() {
        SmartDashboard.putNumber("encoder: ", enc.getDistance());
        SmartDashboard.putBoolean("direction: ", enc.getDirection());
        SmartDashboard.putNumber("motor value: ", motor.get());

    }

}
