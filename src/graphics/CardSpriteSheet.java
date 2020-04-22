package graphics;

import processing.core.PImage;

public class CardSpriteSheet extends SpriteSheet {

    public CardSpriteSheet(PImage img) {
	// A sprite sheet specifically for cards.png
	super(img, 14, 4);
    }

    public PImage getCard(int val, String suit) {
	// Val is 1-14 (Ace - King)
	if (val >= 1 && val <= 13) {
	    if (suit.equalsIgnoreCase("Clubs")) {
		return this.getSprite(val - 1, 0);
	    } else if (suit.equalsIgnoreCase("Spades")) {
		return this.getSprite(val - 1, 1);
	    } else if (suit.equalsIgnoreCase("Hearts")) {
		return this.getSprite(val - 1, 2);
	    } else if (suit.equalsIgnoreCase("Diamonds")) {
		return this.getSprite(val - 1, 3);
	    } else {
		System.out.println("Error: " + suit + " is not a valid suit.");
		return null;
	    }
	} else {
	    System.out.println("Error: " + val + " is not a valid card value.");
	    return null;
	}
    }

    public PImage cardBack(boolean red) {
	if (red)
	    return this.getSprite(13, 0);
	else
	    return this.getSprite(13, 1);
    }

    public PImage joker(boolean red) {
	if (red)
	    return this.getSprite(13, 3);
	else
	    return this.getSprite(13, 2);
    }

}
