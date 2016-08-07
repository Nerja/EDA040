package modell;

import lift.LiftView;

public class LiftMonitorNaive {

	private static final int MAX_LOAD = 4;
	private static final int NBR_FLOORS = 7;
	private LiftView liftView;
	private int currentLvl, nextLvl, load;
	private int[] waitEntry, waitExit;

	public LiftMonitorNaive(LiftView liftView) {
		this.liftView = liftView;
		waitEntry = new int[NBR_FLOORS];
		waitExit = new int[NBR_FLOORS];
		currentLvl = 0;
		nextLvl = 1;
	}

	public synchronized void travel(int currentLvl, int targetLvl) throws InterruptedException {
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
		boolean goingUp = currentLvl < nextLvl;
		currentLvl = nextLvl;
		notifyAll();
		while (waitExit[currentLvl] > 0) {
			wait();
		}
		while (waitEntry[currentLvl] > 0 && load < MAX_LOAD) {
			wait();
		}
		if (goingUp) {
			if (nextLvl < 6)
				nextLvl++;
			else
				nextLvl--;
		} else {
			if (nextLvl > 0)
				nextLvl--;
			else
				nextLvl++;
		}
		return nextLvl;
	}

}
