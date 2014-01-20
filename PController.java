/*
 * ECSE211 - Lab1
 * Wall Follower
 * 
 * Elitsa Asenova - 260481980
 * Andrea Cabral - 
 * 
 * Implementing a wall follower using the bang-bang and the p controller.
 * 
 */
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.*;

public class PController implements UltrasonicController {

	private final int bandCenter, bandWidth;
	private final int motorStraight = 200, FILTER_OUT = 20;				// 250,60
	private final NXTRegulatedMotor leftMotor = Motor.A, rightMotor = Motor.C;	
	private int distance;
	private int currentLeftSpeed;
	private int filterControl;		
	private int leftSpeed, rightSpeed;
	private double turn; 
	
	public PController(int bandCenter, int bandWidth) {
		//Default Constructor
		this.bandCenter = bandCenter;
		this.bandWidth = bandWidth;
		leftMotor.setSpeed(motorStraight);
		rightMotor.setSpeed(motorStraight);
		leftMotor.forward();
		rightMotor.forward();
		currentLeftSpeed = 0;
		filterControl = 0;
		turn = 5;	
	}

	//This method process a movement based on the us distance passed in (P style)
	//The turning of the wheels and the speed of the follower will be determined by the error
	@Override
	public void processUSData(int distance) {

		// rudimentary filter
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
		
		
		// Wall on the left
		int error = distance - this.bandCenter;
		
		
		//Within tolerance level (bandWidt), turn is 0
		if (Math.abs(error) <= bandWidth){ 	
			this.turn = 0;
			LCD.drawString("Within Bandwith", 0, 6);
		}
		
		//Too far from wall, set the turn to 5
		else if(error > 20){		
			
			//this.turn = (error > 150) ?  0.2 : 5;  
			//If it's too far, to make sharper turns, set turn to 0.2
			if(error > 150){
				this.turn = 0.2;
			}else{
				this.turn = 5;
			}
			
			LCD.drawString("Too far", 0, 6);
		}
		
		//Too close to wall, set the turn to 20
		else if(error < -8){			
			this.turn = 20;	
			LCD.drawString("Too close", 0, 6);
		}
		
		//adjust the speed according to the error  
		leftSpeed = this.fixSpeed(error);
		leftMotor.setSpeed(leftSpeed);
		rightSpeed = this.fixSpeed(-error);
		rightMotor.setSpeed(rightSpeed);
		
		
		
	}
	
	// Compute Adjusted speed: for positive error:  new_speed < motorStraight
	// 						  for negative error:  new_speed > motorStraight
	// make sure  Lab1.getMotorLow() <= new_speed <= Lab1.getMotorHigh()
	private int fixSpeed(int error){
		int new_speed = Math.max(Lab1.getMotorLow(),  motorStraight - (int)(turn*error));
		new_speed = Math.min(new_speed,  Lab1.getMotorHigh());
		return new_speed;
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

/*
 
 import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.*;

public class PController implements UltrasonicController {
	
	private final int bandCenter, bandwith;
	private final int motorStraight = 200, FILTER_OUT = 20;
	private final NXTRegulatedMotor leftMotor = Motor.A, rightMotor = Motor.C;	
	private int distance;
	private int currentLeftSpeed;
	private int filterControl;
	
	public PController(int bandCenter, int bandwith) {
		//Default Constructor
		this.bandCenter = bandCenter;
		this.bandwith = bandwith;
		leftMotor.setSpeed(motorStraight);
		rightMotor.setSpeed(motorStraight);
		leftMotor.forward();
		rightMotor.forward();
		currentLeftSpeed = 0;
		filterControl = 0;
	}
	
	@Override
	public void processUSData(int distance) {
		
		// rudimentary filter
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
		// TODO: process a movement based on the us distance passed in (P style)
		
	}

	
	@Override
	public int readUSDistance() {
		return this.distance;
	}

}

*/
