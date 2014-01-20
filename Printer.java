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
import lejos.nxt.Button;
import lejos.nxt.LCD;

public class Printer extends Thread {
	
	private UltrasonicController cont;
	private final int option;
	
	public Printer(int option, UltrasonicController cont) {
		this.cont = cont;
		this.option = option;
	}
	
	public void run() {
		while (true) {
			LCD.clear();
			LCD.drawString("Controller Type is... ", 0, 0);
			if (this.option == Button.ID_LEFT)
				LCD.drawString("BangBang", 0, 1);
			else if (this.option == Button.ID_RIGHT)
				LCD.drawString("P type", 0, 1);
			LCD.drawString("US Distance: " + cont.readUSDistance(), 0, 2 );
			
			
			//Display error proprional to speed adjustment
			int error = cont.readUSDistance() - 20;
			LCD.drawString("ERROR: " + error, 0, 3 );
			LCD.drawString("Left speed: " + cont.getLeftSpeed(), 0, 4 );
			LCD.drawString("Right speed: " + cont.getRightSpeed(), 0, 5 );
					
			
			try {
				Thread.sleep(200);
			} catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
			}
		}
	}
	
	public static void printMainMenu() {
		LCD.clear();
		LCD.drawString("left = bangbang",  0, 0);
		LCD.drawString("right = p type", 0, 1);
	}
}

