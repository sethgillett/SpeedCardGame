package event;

public class EventCoordinator {
	
	private boolean playerSlapped, leftPileSlapped;
	
	private boolean roundOver;
	
	private boolean slapEventRegistered;
	
	public EventCoordinator() {
		this.slapEventRegistered = false;
	}
	
	public void eventPileSlapped(boolean playerSlapped, boolean leftPile) {
		this.playerSlapped = playerSlapped;
		this.leftPileSlapped = leftPile;
		this.registerSlapEvent();
	}
	
	public void roundOverEvent(boolean roundOver) {
		// Toggles whether the round is over or not
		this.roundOver = roundOver;
	}
	
	public boolean isRoundOver() {
		return roundOver;
	}
	
	public void registerSlapEvent() {
		slapEventRegistered = true;
	}
	
	public void resetSlapEvent() {
		slapEventRegistered = false;
	}
	
	public boolean didPlayerSlap() {
		return playerSlapped;
	}
	
	public boolean leftPileSlapped() {
		return leftPileSlapped;
	}
	
	public boolean isSlapEventRegistered() {
		return slapEventRegistered;
	}
}
