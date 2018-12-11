package cards;
import graphics.CardSpriteSheet;
import processing.core.PImage;

public class Card {
	public final int val;
	public final String suit;
	
	public Card(int val, String suit) {
		this.val = val;
		this.suit = suit;
	}
	
	public PImage cardImage(CardSpriteSheet cs) {
		return cs.getCard(val, suit);
	}
	
	public boolean validStack(Card other) {
		// If one card is null, fails immediately
		if (other == null) return false;
		
		// Returns true if the either card can be stacked on the other (commutative)
		
		int minDist;
		minDist = Math.min(
				Math.min(Math.abs(this.val - other.val), Math.abs((this.val % 13) - other.val)),
				Math.min(Math.abs(other.val - this.val), Math.abs((other.val % 13) - this.val))
				);
		
		return minDist == 1;
	}
	
	@Override
	public String toString() {
		if (val >= 2 && val <= 10) {
			return "" + val + " of " + suit;
		} else {
			switch(val) {
			case 1:
				return "Ace of " + suit; 
			case 11:
				return "Jack of " + suit;
			case 12:
				return "Queen of " + suit;
			case 13:
				return "King of " + suit;
			default:
				return "Error (" + val + ") of " + suit;
			}
		}
	}
}
