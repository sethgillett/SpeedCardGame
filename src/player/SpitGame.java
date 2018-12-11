package player;
import cards.Card;
import cards.Deck;
import cards.CardPile;

public class SpitGame {
	// Main deck of cards
	private Deck mainDeck;
	// Left (player) and right (AI) draw piles
	private CardPile leftDraw, rightDraw;
	// Visible card piles on left and right
	public CardPile leftPile, rightPile;
	// Player and AI
	public Player p, ai;
	// Booleans that will be used to check if a player has won
	private boolean playerWon;
	private boolean aiWon;
	
	public SpitGame() {
		mainDeck = new Deck();
		leftPile = new CardPile();
		rightPile = new CardPile();
		leftDraw = new CardPile();
		rightDraw = new CardPile();
		
		p = new Player();
		ai = new Player();
		
		playerWon = false;
		aiWon = false;
	}
	
	public void deal(CardPile toPlayer, CardPile toAI) {
		// Shuffles cards and distributes them to players
		
		// Shuffles cards
		toPlayer.shuffle();
		toAI.shuffle();
		
		// Distributes cards to player stacks
		Card[] toPlayerStacks = new Card[Math.min(toPlayer.stackSize(), 15)];
		for (int i = 0; i < toPlayerStacks.length; i++) {
			toPlayerStacks[i] = toPlayer.giveTop();
		}
		p.stackCards(toPlayerStacks);
		
		// Distributes cards to left draw pile (player's)
		while (!toPlayer.isEmpty()) {
			leftDraw.takeTop(toPlayer.giveTop());
		}
		
		// Distributes cards to AI stacks
		Card[] toAIStacks = new Card[Math.min(toAI.stackSize(), 15)];
		for (int i = 0; i < toAIStacks.length; i++) {
			toAIStacks[i] = toAI.giveTop();
		}
		ai.stackCards(toAIStacks);
		
		// Distributes cards to right draw pile (AI's)
		while (!toAI.isEmpty()) {
			rightDraw.takeTop(toAI.giveTop());
		}
		
	}
	
	public void firstRoundDeal() {
		// Default deal at very first round
		
		// 26 cards to each player
		CardPile toPlayer = new CardPile();
		for (int i = 0; i < 26; i++) {
			toPlayer.takeTop(mainDeck.giveTop());
		}
		
		CardPile toAI = new CardPile();
		for (int i = 0; i < 26; i++) {
			toAI.takeTop(mainDeck.giveTop());
		}
		
		deal(toPlayer, toAI);
	}
	
	public void spit() {
		if (!leftDraw.isEmpty()) {
			leftPile.takeTop(leftDraw.giveTop());
		}
		if (!rightDraw.isEmpty()) {
			rightPile.takeTop(rightDraw.giveTop());
		}
	}
	
	public boolean validSlapTime() {
		// Checks whether it is a valid time to slap
		// True if either player's stacks are empty or the
		//draw piles are empty and no further plays are possible
		return p.stacksEmpty() || ai.stacksEmpty() ||
				(leftDraw.isEmpty() && rightDraw.isEmpty() && this.isRoundOver());
	}
	
	private void checkWins(CardPile pileToPlayer, CardPile pileToAI) {
		// Updates booleans if either player won
		if (leftDraw.isEmpty() && pileToPlayer.isEmpty() && p.stacksEmpty()) {
			playerWon = true;
		}
		if (rightDraw.isEmpty() && pileToAI.isEmpty() && ai.stacksEmpty()) {
			aiWon = true;
		}
	}
	
	public void reDealCards(CardPile pileToPlayer, CardPile pileToAI) {
		// Updates booleans if either player has won
		this.checkWins(pileToPlayer, pileToAI);
		// Gathers and redeals cards to players
		CardPile[] allPlayerCards = this.gatherCards(pileToPlayer, pileToAI);
		this.deal(allPlayerCards[0], allPlayerCards[1]);
	}
	
	public CardPile[] gatherCards(CardPile pileToPlayer, CardPile pileToAI) {
		// Gathers each player's cards, player's hand first and AI's second
		CardPile[] hands = new CardPile[2];
		
		// Player's
		hands[0] = new CardPile();
		// AI's
		hands[1] = new CardPile();
		
		// TO PLAYER
		while (!pileToPlayer.isEmpty()) {
			hands[0].takeTop(pileToPlayer.giveTop());
		}
		while (!leftDraw.isEmpty()) {
			hands[0].takeTop(leftDraw.giveTop());
		}
		CardPile playerStacks = p.gatherCards();
		while (!playerStacks.isEmpty()) {
			hands[0].takeTop(playerStacks.giveTop());
		}
		
		// TO AI
		while (!pileToAI.isEmpty()) {
			hands[1].takeTop(pileToAI.giveTop());
		}
		while (!rightDraw.isEmpty()) {
			hands[1].takeTop(rightDraw.giveTop());
		}
		CardPile aiStacks = ai.gatherCards();
		while (!aiStacks.isEmpty()) {
			hands[1].takeTop(aiStacks.giveTop());
		}
		
		return hands;
	}
	
	public boolean isRoundOver() {
		// Checks if either player's cards can be placed on top of the current spit piles
		for (Card c : p.topStacks()) {
			// If the current card is null, skip it
			if (c == null) continue;

			if (c.validStack(leftPile.peekTop())|| c.validStack(rightPile.peekTop())) {
				return false;
			}
		}
		
		for (Card c : ai.topStacks()) {
			// If the current card is null, skip it
			if (c == null) continue;
			
			if (c.validStack(leftPile.peekTop()) || c.validStack(rightPile.peekTop())) {
				return false;
			}
		}
		
		return true;
	}
	
	public int leftDrawSize() {
		return leftDraw.stackSize();
	}
	
	public boolean leftDrawEmpty() {
		return leftDraw.isEmpty();
	}
	
	public int rightDrawSize() {
		return rightDraw.stackSize();
	}
	
	public boolean rightDrawEmpty() {
		return rightDraw.isEmpty();
	}
	
	public boolean playerWon() {
		return playerWon;
	}
	
	public boolean aiWon() {
		return aiWon;
	}
}

