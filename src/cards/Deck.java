package cards;
import java.util.Arrays;

public class Deck extends CardPile {
	public Deck() {
		// Calls Hand class constructor
		super();
		
		// Creates all 52 cards in a deck and adds them into the deck
		for (int val=1; val<=13; val++) {
			for (String suit : Arrays.asList("Clubs", "Spades", "Hearts", "Diamonds")) {
				this.takeTop((new Card(val, suit)));
			}
		}
		
		// Shuffles the cards
		this.shuffle();
	}
}