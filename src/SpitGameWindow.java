
import cards.CardPile;
import event.EventCoordinator;
import graphics.CardSpriteSheet;
import graphics.SpitGameGraphics;
import menu.Menu;
import player.AIController;
import player.PlayerController;
import player.SpitGame;
import processing.core.PApplet;

public class SpitGameWindow extends PApplet {
    CardSpriteSheet cs;
    SpitGame sc;
    SpitGameGraphics sg;
    Menu menu;

    AIController aiController;
    PlayerController pController;
    EventCoordinator ec;

    int state;
    final int MENU = 0, GAME = 1, WIN = 2, LOSE = 3, SLAP = 4;

    public static void main(String[] args) {
	PApplet.main(SpitGameWindow.class);
    }

    @Override
    public void settings() {
	size(600, 600);
    }

    @Override
    public void setup() {
	state = MENU;

	imageMode(CENTER);
	rectMode(CENTER);

	ec = new EventCoordinator();

	menu = new Menu(this, ec);

	sg = new SpitGameGraphics(this);

	sc = new SpitGame();

	aiController = new AIController(sc, ec, 2, 3);

	pController = new PlayerController(sc.p);

	sc.firstRoundDeal();
    }

    @Override
    public void draw() {
	// Updates state based on win conditions
	if (sc.playerWon())
	    state = WIN;
	else if (sc.aiWon())
	    state = LOSE;

	if (state == MENU) {
	    menu.displayMainMenu(sg);
	} else if (state == WIN) {
	    menu.displayWin(sg);
	} else if (state == LOSE) {
	    menu.displayLose(sg);
	} else if (state == SLAP) {
	    if (menu.timedWindow(SLAP, GAME) == GAME) {
		ec.resetSlapEvent();
		state = GAME;
	    }
	    menu.displaySlap(sg);
	} else if (state == GAME) {
	    // Checks if a slap event has been registered
	    if (ec.isSlapEventRegistered()) {
		menu.startTimedWindow(1500);
		state = SLAP;
		// Flags that draw() needs to be called again
		redraw();
		// Prevents the board from being drawn
		return;
	    }

	    sg.displayTable();

	    sg.displayDrawPiles(sc.leftDrawEmpty(), sc.rightDrawEmpty(), sc.leftDrawSize(), sc.rightDrawSize());
	    sg.displayPiles(sc.leftPile, sc.rightPile);
	    sg.displayPlayerStacks(sc.p, 450);
	    sg.displayPlayerStacks(sc.ai, 150);

	    if (ec.isRoundOver()) {
		menu.displayStatusIndicator("No further plays possible");
	    }

	    // AI controller attempts to move every draw cycle, fewer errors than
	    // multithreaded approach
	    aiController.play();
	}
    }

    @Override
    public void keyPressed() {
	if (key == 'r') {
	    // Prints whether any more moves can be made
	    System.out.println("Round " + (sc.isRoundOver() ? "is " : "isn't ") + "over.");
	} else if (key == 'e') {
	    // Resets the game
	    setup();
	} else if (key == 's') {
	    // Flips new cards from draw piles into spit piles
	    if (sc.isRoundOver()) {
		System.out.println("Spit!");
		sc.spit();
		// Forces the ai to begin sleeping once cards are flipped
		aiController.forceSleep();
	    } else {
		System.out.println("Round isn't over, can't spit yet.");
	    }
	    // Lets the ai controller know that new actions may be possible
	    aiController.roundOverCheck();
	} else if (key == '1' || key == '2') {
	    // Player slaps the left pile (1) or the right pile (2)
	    if (sc.validSlapTime()) {
		if (key == '1') {
		    System.out.println("Player slapped left pile!");
		    this.ec.eventPileSlapped(true, true);
		    sc.reDealCards(sc.leftPile, sc.rightPile);
		} else {
		    System.out.println("Player slapped right pile!");
		    this.ec.eventPileSlapped(true, false);
		    sc.reDealCards(sc.rightPile, sc.leftPile);
		}
	    } else {
		System.out.println("Can't slap yet");
	    }
	}
    }

    @Override
    public void mousePressed() {
	if (state == MENU) {
	    if (menu.checkForStartClick(mouseX, mouseY)) {
		menu.buttonPressed = true;
	    }
	} else if (state == GAME) {
	    // Figures out which pile the intended card is meant to go into
	    CardPile whichPile;
	    if (mouseButton == LEFT) {
		whichPile = sc.leftPile;
	    } else if (mouseButton == RIGHT) {
		whichPile = sc.rightPile;
	    } else {
		System.out.println("Invalid mouse button.");
		return;
	    }

	    // Determines which of the player's card stacks was clicked
	    int clicked = sg.getClickedPile(450, mouseX, mouseY);

	    if (clicked != -1) {
		// Attempts to perform a player action based on the click
		pController.playClick(whichPile, clicked);
		// Lets the ai controller know new actions may be possible
		aiController.roundOverCheck();
	    } else {
		System.out.printf("Invalid click: (%d, %d)\n", mouseX, mouseY);
	    }
	}
    }

    @Override
    public void mouseReleased() {
	if (state == MENU) {
	    if (menu.checkForStartClick(mouseX, mouseY)) {
		state = GAME;
	    }
	}
    }

}
