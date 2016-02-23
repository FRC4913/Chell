package org.usfirst.frc.team4913.robot;

import org.usfirst.frc.team4913.robot.commands.PulleyDown;
import org.usfirst.frc.team4913.robot.commands.PulleyUp;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {

	private static final int JOYSTICK_PORT = 0;

	public static Joystick stick = new Joystick(JOYSTICK_PORT);
	Button button1 = new JoystickButton(stick, 1);
	Button button2 = new JoystickButton(stick, 2);

	public OI() {
		button1.whileHeld(new PulleyUp());
		button2.whileHeld(new PulleyDown());
	}
}
