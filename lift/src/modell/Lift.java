package modell;

import lift.LiftView;

public class Lift extends Thread {
	private LiftMonitor liftMonitor;
	private LiftView liftView;

	public Lift(LiftView liftView, LiftMonitor liftMonitor) {
		this.liftView = liftView;
		this.liftMonitor = liftMonitor;
	}

	@Override
	public void run() {
		int currentLvl = 0;
		try {
			while (!isInterrupted()) {
				int nextLvl = liftMonitor.getNextLvl();
				liftView.moveLift(currentLvl, nextLvl);
				currentLvl = nextLvl;
			}
		} catch (InterruptedException e) {
			System.out.println("Killed lift thread!");
		}
	}

}
