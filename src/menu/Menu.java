package menu;

import java.io.IOException;

import event.EventCoordinator;
import graphics.SpitGameGraphics;
import processing.core.PApplet;
import processing.core.PFont;

public class Menu {
    PApplet p;
    PFont bigText;
    PFont littleText;

    public boolean buttonPressed;

    private long windowStart;
    private int windowDuration;

    private EventCoordinator ec;

    public Menu(PApplet p, EventCoordinator ec) {
	this.p = p;
	this.bigText = this.loadFont("resources/NewsGothicMT-Bold-42.vlw");
	this.littleText = this.loadFont("resources/NewsGothicMT-Bold-18.vlw");
	this.buttonPressed = false;
	this.ec = ec;
    }

    public PFont loadFont(String url) {
	try {
	    return new PFont(this.getClass().getClassLoader().getResourceAsStream(url));
	} catch (IOException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public boolean checkForStartClick(int mouseX, int mouseY) {
	if (mouseX >= 150 && mouseX < 450 && mouseY >= 120 && mouseY < 240) {
	    return true;
	} else {
	    return false;
	}
    }

    public void displayBackground(SpitGameGraphics sg) {
	// The width of the line around the button
	p.strokeWeight(6);
	// Displays the background
	sg.displayTable();
	// Transparent grayish fill for rectangle
	p.fill(50, 150);
	p.noStroke();
	p.rect(300, 300, 600, 600);
    }

    public void displayMainMenu(SpitGameGraphics sg) {
	this.displayBackground(sg);
	// White stroke for button rectangle
	p.stroke(255);
	// Dark or brighter red fill for button rectangle if pressed
	if (buttonPressed) {
	    p.fill(120, 0, 0);
	} else {
	    p.fill(100, 0, 0);
	}
	p.rect(300, 180, 300, 120);
	// Draw START text
	p.fill(255);
	p.textFont(bigText);
	p.textAlign(PApplet.CENTER, PApplet.CENTER);
	p.text("START", 300, 180);
	// Draw controls text
	p.textFont(littleText);
	p.textAlign(PApplet.LEFT, PApplet.TOP);
	p.fill(255);
	p.text("Controls:", 20, 300);
	p.text("L-Click and R-Click to move cards from stacks to L/R piles.\n"
		+ "'R' to check if the round is over (in console).\n" + "'S' to flip new cards from draw piles.\n"
		+ "'1' and '2' to slap left and right piles respectively.", 30, 325);
	p.text("Credits:", 20, 450);
	p.text("Cards - Dav @ https://kibernetik.pro/forum/viewtopic.php?t=2064\n"
		+ "Wood - Eric @ https://soundimage.org/txr-wood/\n" + "Font - NewsGothicMT\n", 30, 475);
    }

    public void displayWin(SpitGameGraphics sg) {
	this.displayBackground(sg);
	// Draw win text
	p.fill(255);
	p.textFont(bigText);
	p.textAlign(PApplet.CENTER, PApplet.CENTER);
	p.text("You win!!!", 300, 300);
    }

    public void displayLose(SpitGameGraphics sg) {
	this.displayBackground(sg);
	// Draw lose text
	p.fill(255);
	p.textFont(bigText);
	p.textAlign(PApplet.CENTER, PApplet.CENTER);
	p.text("You lose.", 300, 300);
    }

    public void displaySlap(SpitGameGraphics sg) {
	this.displayBackground(sg);
	// Display slap text
	p.fill(255);
	p.textFont(bigText);
	p.textAlign(PApplet.CENTER, PApplet.CENTER);
	// Figures out the details of the slap from event coordinator
	String slapper = ec.didPlayerSlap() ? "Player" : "AI";
	String pile = ec.leftPileSlapped() ? "left" : "right";
	// Displays text
	p.text(slapper + " slapped " + pile + " pile.", 300, 300);
    }

    public void displayStatusIndicator(String msg) {
	// Display message text
	p.fill(255);
	p.textFont(littleText);
	p.textAlign(PApplet.CENTER, PApplet.CENTER);
	p.text(msg, 300, 550);
    }

    public void startTimedWindow(int duration) {
	this.windowStart = System.currentTimeMillis();
	this.windowDuration = duration;
    }

    public int timedWindow(int beforeState, int afterState) {
	if ((System.currentTimeMillis() - windowStart) < this.windowDuration) {
	    return beforeState;
	} else {
	    return afterState;
	}
    }
}
