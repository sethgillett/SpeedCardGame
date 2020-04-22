package graphics;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

import cards.Card;
import cards.CardPile;
import player.Player;
import processing.core.PApplet;
import processing.core.PImage;

public class SpitGameGraphics {
    PApplet p;
    CardSpriteSheet cs;
    PImage tabletop;

    public SpitGameGraphics(PApplet p) {
	this.p = p;
	this.cs = new CardSpriteSheet(this.loadImage("resources/cards.png"));
	this.tabletop = loadImage("resources/wood.jpg");
    }

    private PImage loadImage(String url) {
	try {
	    Image image = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(url));
	    return new PImage(image);
	} catch (IOException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public void displayTable() {
	// Sets the table surface as the background
	p.background(tabletop);
    }

    public void displayPiles(CardPile left, CardPile right) {
	if (left.peekTop() != null) {
	    this.displayStackBottoms(left.stackSize(), 250, 300, 2);
	    p.image(left.peekTop().cardImage(cs), 250, 300);
	} else {
	    p.image(cs.joker(false), 250, 300);
	}
	if (right.peekTop() != null) {
	    this.displayStackBottoms(right.stackSize(), 350, 300, 2);
	    p.image(right.peekTop().cardImage(cs), 350, 300);
	} else {
	    p.image(cs.joker(false), 350, 300);
	}
    }

    public void displayDrawPiles(boolean leftDrawEmpty, boolean rightDrawEmpty, int leftDrawSize, int rightDrawSize) {
	if (!leftDrawEmpty) {
	    this.displayStackBottoms(leftDrawSize, 150, 300, 3);
	    p.image(cs.cardBack(true), 150, 300);
	}
	if (!rightDrawEmpty) {
	    this.displayStackBottoms(rightDrawSize, 450, 300, 3);
	    p.image(cs.cardBack(true), 450, 300);
	}
    }

    private void displayPlayerStackBottoms(Player player, int yHeight) {
	// Displays the bottom cards for a player's card stacks

	final int stackSeparator = 5;

	int x = (p.width - 4 * cs.getCellW()) / 2;

	for (int i = 0; i < player.topStacks().length; i++) {
	    int stackSize = player.stackSize(i);

	    displayStackBottoms(stackSize, x, yHeight, stackSeparator);

	    x += cs.getCellW();
	}
    }

    private void displayStackBottoms(int n, int x, int y, int cardSeparator) {
	// Draw cards stacked underneath the top card of a hand separated by
	// a distance of cardSeparator

	int yHeight = y + (cardSeparator * (n - 1));

	for (int i = 0; i < n - 1; i++) {
	    p.image(cs.cardBack(true), x, yHeight);
	    yHeight -= cardSeparator;
	}
    }

    public void displayPlayerStacks(Player player, int y) {
	// Displays the top card of every card pile for a certain player
	Card[] cardTops = player.topStacks();

	displayPlayerStackBottoms(player, y);

	// Tiles the card stack tops across the screen
	int x = (p.width - 4 * cs.getCellW()) / 2;
	for (Card c : cardTops) {

	    // Skip the current stack if it is empty
	    if (c == null) {
		x += cs.getCellW();
		continue;
	    }

	    p.image(c.cardImage(cs), x, y);
	    x += cs.getCellW();
	}

    }

    public int getClickedPile(int pileHeight, int clickX, int clickY) {
	// Finds out the index of the card pile that was potentially clicked
	// returns -1 if invalid

	// The farthest left boundary where a click is possible
	int xMin = (p.width - 5 * cs.getCellW()) / 2;
	// The left edge of the farthest right card where a click is possible
	int xMax = p.width - xMin;

	// Click is in the right y-coordinate range
	if ((clickY >= pileHeight - cs.getCellH() / 2) && (clickY <= pileHeight + cs.getCellH() / 2)) {
	    // Click is in the right x-coordinate range
	    if (clickX >= xMin && clickX < xMax) {
		int clicked = clickX - xMin;
		clicked /= cs.getCellW();

		return clicked;
	    } else {
		return -1;
	    }
	} else {
	    return -1;
	}
    }
}
