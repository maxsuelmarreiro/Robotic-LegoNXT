import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import lejos.nxt.Button;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Waypoint;
import lejos.util.PilotProps;

public class WaypointNav {
	Navigator nav;

	public static void main(String[] args) throws IOException {

		// X0 Y0 X1 Y1 X2 Y2 X3 Y3...
		String caminho = "0.0 0.0 16.0 8.0 22.0 16.0 25.0 16.0 28.0 18.0 35.0 23.0 40.0 29.0";
		ArrayList<String> dados = new ArrayList<>();
		StringTokenizer st = new StringTokenizer(caminho);

		PilotProps pp = new PilotProps();
		pp.loadPersistentValues();
		float RAIO = Float.parseFloat(pp.getProperty(PilotProps.KEY_WHEELDIAMETER, "4.5")); //0.021 -- 4.32 -- 28000
		float EIXO = Float.parseFloat(pp.getProperty(PilotProps.KEY_TRACKWIDTH, "12.8")); //140mm 16.35
		RegulatedMotor leftMotor = PilotProps.getMotor(pp.getProperty(PilotProps.KEY_LEFTMOTOR, "A"));
		RegulatedMotor rightMotor = PilotProps.getMotor(pp.getProperty(PilotProps.KEY_RIGHTMOTOR, "C"));
		boolean reverse = Boolean.parseBoolean(pp.getProperty(PilotProps.KEY_REVERSE, "true"));

		System.out.println("start");
		Button.waitForAnyPress();

		DifferentialPilot pilot = new DifferentialPilot(RAIO, EIXO, leftMotor, rightMotor, reverse);
		Navigator nav = new Navigator(pilot);

		while (st.hasMoreTokens()) {
			dados.add(st.nextToken());
		}

		for (int i = 0; i < dados.size(); i = i + 2) {
			Double x , y;
			x = Double.parseDouble(dados.get(i));
			y = Double.parseDouble(dados.get(i+1));
			nav.addWaypoint(new Waypoint(x,y));
		}

		nav.followPath();
		System.out.println("Any button to halt");
		Button.waitForAnyPress();
	}
}
