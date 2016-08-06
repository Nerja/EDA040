package todo;

public class ClockUpdater extends Thread {
	private ClockMonitor clockMonitor;
	
	public ClockUpdater(ClockMonitor clockMonitor) {
		this.clockMonitor = clockMonitor;
	}
	
	@Override
	public void run() {
		long t = System.currentTimeMillis(), diff = 0;
		try{
			while(clockMonitor.isAlive()) {
				clockMonitor.tick();
				t += 1000;
				diff = t - System.currentTimeMillis();
				if(diff > 0)
					sleep(diff);
			}
			System.out.println("Clock thread stopped because monitor told it to");
		} catch (InterruptedException e) {
			System.out.println("Terminated clockupdater");
		}
	}
}
