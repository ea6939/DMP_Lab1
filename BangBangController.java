/*
 * ECSE211 - Lab1
 * Wall Follower
 * BangBangController.java
 * 
 * Elitsa Asenova - 260481980
 * Andrea Cabral - 260465023
 * 
 * Implementing a wall follower using the bang-bang controller.
 * The bang-bang is an on/off controller that controlls the speed
 * of the wheels (inner and outer) depending on the distance btw
 * the wall and the follower. The following extra details were implemented
 * along with regular wall and object detection :
 * 			- avoid confusion when a gap is in the wall
 * 			- recognize concave corners and avoids collision
 * 			- make turns at convex corners sharper
 * 
 */
import lejos.nxt.*;

public class BangBangController implements UltrasonicController {
	private final int bandCenter, bandWidth;
	private final int motorLow, motorHigh;
	private final int motorStraight = 300;   
	private final int FILTER_OUT = 20;  	
	private int delta = 60;  				
	private final NXTRegulatedMotor leftMotor = Motor.A, rightMotor = Motor.B;
	private int distance;
	private int filterControl, leftSpeed, rightSpeed;  

	public BangBangController(int bandCenter, int bandWidth, int motorLow,
			int motorHigh) {
		// Default Constructor
		this.bandCenter = bandCenter;
		this.bandWidth = bandWidth;
		this.motorLow = motorLow;
		this.motorHigh = motorHigh;
		this.leftSpeed = motorStraight; 	//setting the start speed for left and right to straight speed
		this.rightSpeed = motorStraight;	
		leftMotor.setSpeed(leftSpeed);
		rightMotor.setSpeed(rightSpeed);
		leftMotor.forward();
		rightMotor.forward();

	}
	
	//Process a movement based on the us distance passed in (BANG-BANG style)
	@Override
	public void processUSData(int distance) {

		//Avoid getting confused by gaps
		// rudimentary filter -> helps the robot deal woth gaps
		if (distance == 255 && filterControl < FILTER_OUT) {
			// bad value, do not set the distance var, however do increment the filter value
			filterControl ++;
		} else if (distance == 255){
			// true 255, therefore set distance to 255
			this.distance = distance;
		} else {
			// distance went below 255, therefore reset everything.
			filterControl = 0;
			this.distance = distance;
		}
		

		int error = distance - this.bandCenter;
		
		//Within tolerance level (bandWidth), going straight
		if (Math.abs(error) <= bandWidth) {  
			System.out.println(bandWidth);
			rightSpeed = motorStraight;
			leftSpeed = motorStraight;
			rightMotor.setSpeed(rightSpeed);
			leftMotor.setSpeed(leftSpeed);
			LCD.drawString("Within Tolerance - going forward", 0, 6);
		
		//Too close to wall
		} else if (error < 0) {
		
		 	//Very close to wall, increase delta
		 	//Done to avoid running into concave corners
			if (error < -10 ){			
				delta = 150;   
			}
			
			// increase speed of left(inner) wheel and decrease speed of right(outer) wheel 
			leftSpeed = motorStraight + delta;
			rightSpeed = motorStraight - delta;
			leftMotor.setSpeed(leftSpeed);
			rightMotor.setSpeed(rightSpeed);
			LCD.drawString("Too close ", 0, 6);
		
		//Too far from wall
		} else { 	
			
			//Very far from wall, decrease delta
			//To turn convex corners sharply
			if (error > 20 ){ 			
				delta = 85;   
			}
			
			// increase speed of right(outer) wheel and decrease speed of left(inner) wheel
			leftSpeed = motorStraight - delta;
			rightSpeed = motorStraight + delta;
			leftMotor.setSpeed(leftSpeed);
			rightMotor.setSpeed(rightSpeed);
			LCD.drawString("Too far", 0, 6);
		}
	}

	public int getLeftSpeed() {
		return leftSpeed;
	}

	public int getRightSpeed() {
		return rightSpeed;
	}

	@Override
	public int readUSDistance() {
		return this.distance;
	}
}
