/*
 * ECSE211 - Lab1
 * Wall Follower
 * 
 * Elitsa Asenova - 260481980
 * Andrea Cabral - 260465023
 * 
 * Implementing a wall follower using the bang-bang and the p controller.
 * 
 */
public interface UltrasonicController {
	
	public void processUSData(int distance);
	
	public int readUSDistance();
	
	public int getLeftSpeed();
	public int getRightSpeed();
}

