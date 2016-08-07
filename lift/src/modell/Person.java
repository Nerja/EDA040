package modell;

public class Person extends Thread {

	private static final int MAX_WAIT = 30;
	private static final int MAX_LEVEL = 6;
	private LiftMonitor monitor;

	public Person(LiftMonitor monitor) {
		this.monitor = monitor;
		;
	}

	@Override
	public void run() {
		try {
			while (!isInterrupted()) {
				sleep((int) (Math.random() * 1000 * MAX_WAIT));
				int currentLvl = randomLevel(-1);
				int targetLvl = randomLevel(currentLvl);
				monitor.travel(currentLvl, targetLvl);
			}
		} catch (InterruptedException e) {
			System.out.println("Personthread killed!");
		}
	}

	private int randomLevel(int avoid) {
		int randLvl = (int) Math.round(Math.random() * MAX_LEVEL);
		while (randLvl == avoid)
			randLvl = (int) Math.round(Math.random() * MAX_LEVEL);
		return randLvl;
	}

}
