/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;
//import edu.wpi.first.wpilibj.templates.RobotTemplate;
//import edu.wpi.first.wpilibj.SimpleRobot;
//import edu.wpi.first.wpilibj.Joystick;
//import edu.wpi.first.wpilibj.RobotDrive;
//import edu.wpi.first.wpilibj.GenericHID;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends SimpleRobot 
{
    private static final int FRONT_LEFT_MOTOR = 3;
    private static final int BACK_LEFT_MOTOR = 1;
    private static final int FRONT_RIGHT_MOTOR = 4;
    private static final int BACK_RIGHT_MOTOR = 2;
    
    private static final int PRESSURE_SWITCH_CHANNEL = 1;
    private static final int COMPRESSOR_RELAY_CHANNEL = 1;
    
    private double m_sensitivity = .5;
    
    private RobotDrive m_drive = null;
    private Joystick m_joystick = null;
    private Compressor m_compressor;
    private Solenoid m_solenoidWheelUp = null;
    private Solenoid m_solenoidWheelDown = null;

    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    
    public RobotTemplate()
    {
        super();
        m_drive = new RobotDrive(FRONT_LEFT_MOTOR, BACK_LEFT_MOTOR, FRONT_RIGHT_MOTOR, BACK_RIGHT_MOTOR);
        m_joystick = new Joystick(1);
        m_compressor = new Compressor(PRESSURE_SWITCH_CHANNEL, COMPRESSOR_RELAY_CHANNEL);
        
        m_solenoidWheelUp = new Solenoid(1);//slot, channel
        m_solenoidWheelDown = new Solenoid(2);
        
        m_drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
        m_drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
        m_compressor.start();
    
    }
    
    public void autonomous() 
    {
        
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    
    public void operatorControl() 
    {       
        getWatchdog().setEnabled(true);
        while(isOperatorControl() && isEnabled())
        {
            getWatchdog().feed();
            m_drive.mecanumDrive_Cartesian(rampSpeed(m_joystick.getX(), m_sensitivity),
                    rampSpeed(m_joystick.getY(), m_sensitivity), -1 * m_joystick.getZ(), 0);
            
            
            if(m_joystick.getRawButton(5))
            {
                 m_solenoidWheelUp.set(false);
                 m_solenoidWheelDown.set(true);
            }
            
            if(m_joystick.getRawButton(6))           
            {
                m_solenoidWheelUp.set(true);
                m_solenoidWheelDown.set(false);
            }
            
            
            Timer.delay(0.005);
        }        
    }
    
    private double rampSpeed( double input, double sensitivity)
    {
        
        if( (input > 0 && input < .1) || (input < 0 && input > -.1 )) // dead band of -.1 to .1
            return 0;
       
        //formula for ramping: f(x) = ax^3 + (1-a)x where a is the sensitivity and x is the input
        return (sensitivity * input * input * input + (1-sensitivity)*input);
        
    }
    
    private double rampSpeed( double input)
    {
        return rampSpeed( input, .5);  //auto set sensitivity to .5
    }
    
}