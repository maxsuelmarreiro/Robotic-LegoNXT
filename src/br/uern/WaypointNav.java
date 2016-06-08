import java.io.IOException;
import java.util.ArrayList;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Waypoint;
import lejos.util.PilotProps;

public class WaypointNav {
	Navigator nav;

	public static void main(String[] args) throws IOException {
		String caminho = "0.0 0.0 30.0 30.0 180 90";
		String[] dados = split(" ", caminho);

		PilotProps pp = new PilotProps();
		pp.loadPersistentValues();


		System.out.println("Any button to start");
		//Button.waitForAnyPress();
		
		//leftMotor.setSpeed(720);
		//rightMotor.setSpeed(720);

		DifferentialPilot p = new DifferentialPilot(4.4f, 13f, Motor.B, Motor.A, false);
		
		Navigator nav = new Navigator(p);

		
		p.setRotateSpeed(360/8);
		p.setAcceleration(30);

		for (int i = 0; i < dados.length; i = i + 2) {
			Double x, y;
			x = Double.parseDouble(dados[i]);
			y = Double.parseDouble(dados[i + 1]);
			nav.addWaypoint(new Waypoint(x, y));
		}

		 //nav.addWaypoint(new Waypoint(90, 0));
		 //nav.addWaypoint(new Waypoint(0, 0));

		nav.followPath();

		System.out.println("Any button to halt");
		Button.waitForAnyPress();
	}

	private static String[] split(String s, String S) {
		ArrayList<String> list = new ArrayList<String>();
		int start = 0;
		while (start < S.length()) {
			int end = S.indexOf(s, start);
			if (end < 0)
				break;

			list.add(S.substring(start, end));
			start = end + s.length();
		}
		if (start < S.length())
			list.add(S.substring(start));
		return list.toArray(new String[list.size()]);
	}
}
