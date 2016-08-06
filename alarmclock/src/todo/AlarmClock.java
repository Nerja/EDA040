package todo;
import done.ClockInput;
import done.ClockOutput;
import se.lth.cs.realtime.semaphore.Semaphore;

public class AlarmClock extends Thread {

	private static ClockInput	input;
	private static ClockOutput	output;
	private static Semaphore	sem; 
	private static ClockMonitor monitor;
	private Thread clUp;

	public AlarmClock(ClockInput i, ClockOutput o) {
		input = i;
		output = o;
		sem = input.getSemaphoreInstance();
		monitor = new ClockMonitor(output);
		clUp = new ClockUpdater(monitor);
	}

	// The AlarmClock thread is started by the simulator. No
	// need to start it by yourself, if you do you will get
	// an IllegalThreadStateException. The implementation
	// below is a simple alarmclock thread that beeps upon 
	// each keypress. To be modified in the lab.
	public void run() {
		clUp.start();
		int ch = input.getChoice();
		try{
			while (monitor.isAlive()) {
				sem.take();
				int nextCh = input.getChoice();
				monitor.setAlarmEnabled(input.getAlarmFlag());
				monitor.turnOffAlarm();
				if(nextCh == 0) 
					if(ch == 2)
						monitor.setTime(input.getValue());
					else if(ch == 1)
						monitor.setAlarmTime(input.getValue());
				
				ch = nextCh;
			}
			System.out.println("Input thread stopped because monitor told it to");
		} catch(Exception e) {
			System.out.println("Terminated input thread");
		}
	}
}
