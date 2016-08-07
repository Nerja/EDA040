import lift.LiftView;
import modell.Lift;
import modell.LiftMonitor;
import modell.Person;

public class Launcher {

	public static void main(String[] args) {
		LiftView liftView = new LiftView();
		LiftMonitor liftMonitor = new LiftMonitor(liftView);
		for (int i = 0; i < 10; i++)
			(new Person(liftMonitor)).start();
		Lift lift = new Lift(liftView, liftMonitor);
		lift.start();
	}

}
