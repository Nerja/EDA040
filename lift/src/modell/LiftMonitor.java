package modell;

import lift.LiftView;

public class LiftMonitor {

	private static final int MAX_LOAD = 4;
	private static final int NBR_FLOORS = 7;
	private LiftView liftView;
	private int currentLvl, nextLvl, load;
	private int[] waitEntry, waitExit;

	public LiftMonitor(LiftView liftView) {
		this.liftView = liftView;
		waitEntry = new int[NBR_FLOORS];
		waitExit = new int[NBR_FLOORS];
		currentLvl = 0;
		nextLvl = 0;
	}

	public synchronized void travel(int currentLvl, int targetLvl) throws InterruptedException {
		if (currentLvl == targetLvl)
			throw new IllegalArgumentException("Can't be same!");
		waitEntry[currentLvl]++;
		liftView.drawLevel(currentLvl, waitEntry[currentLvl]);
		while (this.currentLvl != this.nextLvl || this.currentLvl != currentLvl || load >= MAX_LOAD) {
			wait();
		}
		load++;
		waitEntry[currentLvl]--;
		waitExit[targetLvl]++;
		notifyAll();
		liftView.drawLevel(currentLvl, waitEntry[currentLvl]);
		liftView.drawLift(currentLvl, load);
		while (this.currentLvl != targetLvl || this.currentLvl != this.nextLvl) {
			wait();
		}
		load--;
		waitExit[targetLvl]--;
		notifyAll();
		liftView.drawLift(targetLvl, load);
	}

	public synchronized int getNextLvl() throws InterruptedException {
		if (currentLvl == 0 && nextLvl == 0) { // Kickstart it
			nextLvl = 1;
			return nextLvl;
		}
		boolean isGoingUp = currentLvl < nextLvl;
		currentLvl = nextLvl;
		notifyAll();
		while (waitExit[currentLvl] > 0)
			wait();

		while (waitEntry[currentLvl] > 0 && load < MAX_LOAD)
			wait();

		boolean waitingAbove = waitingAbove();
		boolean waitingBelow = waitingBelow();
		if (isGoingUp) {
			if (nextLvl < 6 && waitingAbove)
				nextLvl++;
			else
				nextLvl--;
		} else {
			if (nextLvl > 0 && waitingBelow)
				nextLvl--;
			else
				nextLvl++;
		}
		return nextLvl;
	}

	private boolean waitingBelow() {
		boolean below = false;
		for (int lvl = currentLvl - 1; lvl >= 0 && !below; lvl--)
			if (waitEntry[lvl] > 0 || waitExit[lvl] > 0)
				below = true;
		return below;
	}

	private boolean waitingAbove() {
		boolean above = false;
		for (int lvl = currentLvl + 1; lvl < NBR_FLOORS && !above; lvl++)
			if (waitEntry[lvl] > 0 || waitExit[lvl] > 0)
				above = true;
		return above;
	}

}
