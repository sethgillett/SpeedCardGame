package player;

import cards.Card;
import event.EventCoordinator;

public class AIController {
    private SpitGame sc;
    private EventCoordinator ec;

    private boolean roundOver;

    private int minDelay, maxDelay;

    private long sleepStart;
    private int sleepDuration;

    public AIController(SpitGame sc, EventCoordinator ec, double minDelay, double maxDelay) {
	// The game instance
	this.sc = sc;
	// The event coordinator to register shared events
	this.ec = ec;
	// The min and max delays (in ms) between moves
	this.minDelay = (int) (minDelay * 1000);
	this.maxDelay = (int) (maxDelay * 1000);
	// Used to control whether or not the AI can play
	this.sleepStart = System.currentTimeMillis();
	this.sleepDuration = 0;
    }

    public void play() {
	// The AI makes all possible moves on a timed delay

	// If the AI is sleeping, return immediately
	if (this.sleeping()) {
	    return;
	}
	// Otherwise, attempt to make a move
	else {
	    makeMove();
	    this.roundOverCheck();
	    this.sleep(this.amountToSleep());
	}

    }

    public int amountToSleep() {
	// If round isn't over and player can make moves, long delay
	if (!roundOver && sc.p.canMakeMoves(sc)) {
	    return ((int) (Math.random() * (maxDelay - minDelay)) + minDelay);
	}
	// If it is a valid slap time, a shorter delay
	else if (sc.validSlapTime()) {
	    // If either player's stacks are empty the delay will be small
	    if (sc.p.stacksEmpty() || sc.ai.stacksEmpty()) {
		return 500;
	    }
	    // If the round is over because no further plays are possible, delay will be
	    // larger
	    else {
		return minDelay;
	    }
	}
	// If the player is waiting for the AI to play, moves are quicker
	else {
	    return 200;
	}
    }

    public boolean makeMove() {
	// Attempts to make a move: returns false if a move was made, true if a move
	// wasn't made

	// If the round isn't over, attempt to play a card
	if (!roundOver) {
	    for (int cardIdx = 0; cardIdx < sc.ai.topStacks().length; cardIdx++) {
		// Gets all the cards on top of the AI's piles in sequence
		Card c = sc.ai.topStacks()[cardIdx];

		// If the card stack is empty, move on
		if (c == null)
		    continue;

		// If a card can go somewhere, put it there
		if (c.validStack(sc.leftPile.peekTop())) {
		    sc.ai.playCard(sc.leftPile, cardIdx);
		    return false;
		}
		// If a card can go somewhere, put it there
		if (c.validStack(sc.rightPile.peekTop())) {
		    sc.ai.playCard(sc.rightPile, cardIdx);
		    return false;
		}
	    }
	}

	// If it is a valid point to slap, the AI will slap the smaller pile
	if (sc.validSlapTime()) {
	    if (sc.leftPile.stackSize() <= sc.rightPile.stackSize()) {
		System.out.println("AI slapped left pile!");
		ec.eventPileSlapped(false, true);
		sc.reDealCards(sc.rightPile, sc.leftPile);
		return false;
	    } else {
		System.out.println("AI slapped right pile!");
		ec.eventPileSlapped(false, false);
		sc.reDealCards(sc.leftPile, sc.rightPile);
		return false;
	    }
	}

	return true;
    }

    public void forceSleep() {
	this.sleep(this.amountToSleep());
    }

    public void roundOverCheck() {
	roundOver = sc.isRoundOver();
	ec.roundOverEvent(roundOver);
    }

    private boolean sleeping() {
	return !((System.currentTimeMillis() - sleepStart) > sleepDuration);
    }

    private void sleep(int millis) {
	this.sleepStart = System.currentTimeMillis();
	this.sleepDuration = millis;
    }
}
