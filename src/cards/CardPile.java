package cards;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

public class CardPile {
    // Deque to be used as a stack
    // methods are addFirst, removeFirst, peekFirst
    private Deque<Card> cards;

    public CardPile() {
	cards = new LinkedList<>();
    }

    public Card giveTop() {
	Card c = cards.removeFirst();
	if (c != null) {
	    return c;
	} else {
	    System.out.println("Out of cards.");
	    return null;
	}
    }

    public void takeTop(Card c) {
	cards.addFirst(c);
    }

    public void shuffle() {
	// Shuffles all cards in this hand

	// Creates a temporary arraylist to hold all the cards
	ArrayList<Card> tempCards = new ArrayList<>();

	while (!cards.isEmpty()) {
	    tempCards.add(cards.removeFirst());
	}

	// Adds all cards back into the card stack in random order
	while (!tempCards.isEmpty()) {
	    int idx = (int) (Math.random() * tempCards.size());
	    cards.addFirst(tempCards.remove(idx));
	}
    }

    public Card peekTop() {
	// Peeks the last card in the card deque
	return cards.peekFirst();
    }

    public boolean isEmpty() {
	return cards.isEmpty();
    }

    public int stackSize() {
	return cards.size();
    }

    @Override
    public String toString() {
	String str = "Cards: ";
	for (Card c : cards) {
	    str += c + ", ";
	}
	return str;
    }
}
