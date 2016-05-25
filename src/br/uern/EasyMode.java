package br.uern;

import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.MoveController;
import lejos.robotics.navigation.Navigator;

public class EasyMode {
	static Navigator navigator;
	public static void main(String[] args) {
		PoseProvider poseProvider;
		MoveController pilot;
		navigator = new Navigator(pilot, poseProvider);
		navigator.goTo(0, 0);
	}
}
