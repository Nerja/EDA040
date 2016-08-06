package todo;

import done.ClockOutput;
import se.lth.cs.realtime.semaphore.MutexSem;
import se.lth.cs.realtime.semaphore.Semaphore;

public class ClockMonitor {
	
	private Semaphore lock;
	private ClockOutput	output;
	private int sec, min, hour, alarmTime, alarmCounter;
	private boolean alarmEnabled, alarmTriggered, alive;
	
	public ClockMonitor(ClockOutput	output) {
		alive = true;
		this.output = output;
		lock = new MutexSem();
		min = 59;
		sec = 55;
		hour = 23;
	}
	
	public void turnOff() {
		lock.take();
		alive = false;
		lock.give();
	}
	
	public boolean isAlive() {
		lock.take();
		boolean alive = this.alive;
		lock.give();
		return alive;
	}
	
	public void setTime(int time) {
		lock.take();
		hour = (int)(time/10000.0);
		min = (int)((time-hour*10000)/100.0);
		sec = (int)(time%60);
		lock.give();
	}
	
	public void setAlarmTime(int alarmTime) {
		lock.take();
		this.alarmTime = alarmTime;
		lock.give();
	}
	
	public void setAlarmEnabled(boolean alarmEnabled) {
		lock.take();
		this.alarmEnabled = alarmEnabled;
		lock.give();
	}

	public void tick() {
		lock.take();
		if(sec == 59) {
			if(min == 59) {
				hour = (hour+1)%24;
				min = 0;
				sec = 0;
			} else {
				min += 1;
				sec = 0;
			}
		} else {
			sec += 1;
		}
		int time = 10000*hour + 100*min + sec;
		if(alarmEnabled && time == alarmTime && !alarmTriggered) {
			alarmTriggered = true;
			alarmCounter=0;
		}
		if(alarmTriggered) {
			if(alarmCounter == 0) {
				output.doAlarm();
			}
			alarmCounter = alarmCounter == 19 ? 0 : alarmCounter+1;
		}
		output.showTime(time);
		lock.give();
	}

	public void turnOffAlarm() {
		lock.take();
		alarmTriggered = false;
		lock.give();
	}
}
