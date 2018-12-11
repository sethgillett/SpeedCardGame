package player;

import cards.Card;
import cards.CardPile;

public class PlayerController {
	private Player p;
	
	public PlayerController(Player p) {
		this.p = p;
	}
	
	public void playClick(CardPile whichPile, int stackIdxClicked) {
		Card playCard = p.topStacks()[stackIdxClicked];
		Card pileTop = whichPile.peekTop();
		// Checks if the clicked card can go on top of the intended pile
		if (playCard != null && playCard.validStack(pileTop)) {
			// If so plays it there
			p.playCard(whichPile, stackIdxClicked);
		}
		else {
			System.out.println(p.topStacks()[stackIdxClicked] + " won't go on that pile.");
		}
	}
}
