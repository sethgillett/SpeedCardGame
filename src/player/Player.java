package player;

import cards.Card;
import cards.CardPile;

public class Player {
    private CardPile[] cardStacks;

    public void stackCards(Card[] cards) {
	// Stacks 15 or less cards as the spit rules dictate into 5 piles
	// of 1 2 3 4 5 cards or as many as can fit
	if (cards.length > 15) {
	    System.out.println("Player was dealt too many (" + cards.length + ") cards.");
	    return;
	}

	cardStacks = new CardPile[5];

	int curCard = 0;

	for (int handIdx = 0; handIdx < cardStacks.length; handIdx++) {
	    cardStacks[handIdx] = new CardPile();

	    for (int cardCount = 0; cardCount < handIdx + 1; cardCount++) {
		// Once cards run out, only empty piles will be instantiated
		if (curCard < cards.length) {
		    cardStacks[handIdx].takeTop(cards[curCard]);
		    curCard++;
		} else {
		    break;
		}
	    }
	}
    }

    public boolean playCard(CardPile spitPile, int cardIdx) {
	// Attempts to play a card from one of the player's stacks into
	// one of the spit piles
	if (cardIdx >= 0 && cardIdx < cardStacks.length && !cardStacks[cardIdx].isEmpty()) {

	    Card playing = cardStacks[cardIdx].peekTop();
	    Card receiving = spitPile.peekTop();
	    if (playing.validStack(receiving)) {
		System.out.println("Playing " + playing + " on " + receiving + ".");
		spitPile.takeTop(cardStacks[cardIdx].giveTop());
		spreadCards();
		return true;
	    } else {
		System.out.println("Can't play " + playing + " on " + receiving + ".");
		return false;
	    }
	} else {
	    System.out.println("Error: " + cardIdx + " is not a valid pile.");
	    return false;
	}
    }

    public boolean canMakeMoves(SpitGame sc) {
	// Returns whether this player can make moves given current game state
	for (Card c : this.topStacks()) {
	    // Skips the current card if null
	    if (c == null) {
		continue;
	    }
	    if (c.validStack(sc.leftPile.peekTop()) || c.validStack(sc.rightPile.peekTop())) {
		return true;
	    }
	}

	return false;
    }

    public void printHand() {
	for (int i = 0; i < cardStacks.length; i++) {
	    System.out.println(cardStacks[i]);
	}
    }

    public void spreadCards() {
	// If less than 5 piles are present, spreads cards to empty piles

	for (CardPile h : cardStacks) {

	    if (h.stackSize() == 0) {

		for (CardPile full : cardStacks) {

		    if (full.stackSize() > 1) {
			h.takeTop(full.giveTop());
			break;
		    }
		}
	    }
	}
    }

    public boolean stacksEmpty() {
	// Checks whether there are any cards left in the player's card stacks
	for (CardPile h : cardStacks) {
	    if (h.isEmpty())
		continue;
	    else
		return false;
	}
	return true;
    }

    public Card[] topStacks() {
	// Returns the top card of every stack
	Card[] tops = new Card[5];
	for (int i = 0; i < cardStacks.length; i++) {
	    tops[i] = cardStacks[i].peekTop();
	}
	return tops;
    }

    public CardPile gatherCards() {
	// Returns all cards in player's stacks
	CardPile allCards = new CardPile();

	// Loops through player stacks and takes out all cards
	for (CardPile h : cardStacks) {
	    while (!h.isEmpty()) {
		allCards.takeTop(h.giveTop());
	    }
	}

	return allCards;
    }

    public int stackSize(int stackIdx) {
	// Returns the size of a single player stack
	return cardStacks[stackIdx].stackSize();
    }
}
