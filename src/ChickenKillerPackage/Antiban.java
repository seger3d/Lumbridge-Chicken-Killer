package ChickenKillerPackage;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

public class Antiban extends Node {

	@Override
	public boolean activate() {
		return(!LumbridgeChickenKiller.antibanTimer.isRunning());
	}

	@Override
	public void execute() {
		random();
		LumbridgeChickenKiller.antibanTimer.setEndIn(Random.nextInt(10000, 60000));

	}
	
	public static void rotateScreen() {

		if (Players.getLocal().getInteracting() != null) {

			Camera.turnTo(Players.getLocal().getInteracting()); // Turns camera
																// to the object
																// you're
																// interacting
																// with
			Camera.setPitch(Camera.getPitch() + Random.nextInt(-10, 10)); // Rotate
																			// up
																			// or
																			// down
																			// a
																			// random
																			// amount.
			Camera.setAngle(Camera.getYaw() + Random.nextInt(-22, 22)); // Rotate
																		// left
																		// or
																		// right
																		// a
																		// random
																		// amount.

		} else {

			// Since you're not interacting with anything, allow a larger
			// rotation.
			Camera.setPitch(Camera.getPitch() + Random.nextInt(-40, 40)); // Rotate
																			// up
																			// or
																			// down
																			// a
																			// random
																			// amount.
			Camera.setAngle(Camera.getYaw() + Random.nextInt(-83, 83)); // Rotate
																		// left
																		// or
																		// right
																		// a
																		// random
																		// amount.

		}

	}

	public static void checkSkill(int index) {

		Tabs.STATS.open(); // Self explanatory - opens STATS tab.
		Skills.getWidgetChild(index).hover(); // Hovers the specified stat

		Task.sleep(500);

		Tabs.INVENTORY.open();

	}

	public static boolean random() {

		int randomAnti = Random.nextInt(1, 2);

		switch (randomAnti) {

		case 1:
			rotateScreen();
			return true;
		case 2:
			checkSkill(Random.nextInt(0, 24));
			return true;
		}
		return false;

	}

	public static boolean moveMouse(WidgetChild w) {

		int x = w.getAbsoluteX() + Random.nextInt(0, w.getWidth()), y = w
				.getAbsoluteY() + Random.nextInt(0, w.getHeight());

		int startX = Mouse.getX(), startY = Mouse.getY(), startDX = Mouse
				.getX(), startDY = Mouse.getY(); // where mouse started
		double distX = Math.abs((double) startX - x), distY = Math
				.abs((double) startY - y), dist = Math.sqrt(distX * distX
				+ distY * distY); // Distance from start to finish

		double width = 2.0 * dist
				* ((0.5 * (double) w.getWidth()) / Math.max(distX, distY));

		double time = Random.nextDouble(125, 150) + Random.nextDouble(90, 100)
				* (Math.log(2.0 * dist / width) / Math.log(2)); // fitts law
																// brah

		double bPoint = 0;
		if (dist > 100)
			bPoint = dist / 20; // Last 20% of the distance

		double friction = Random.nextDouble(-1.00, 1.00); // Random friction, to
															// be safe

		for (int i = 0; i < (int) dist; i++) {
			if (startDX < x)
				startDX++;
			else if (startDX > x)
				startDX--;

			if (startDY < y)
				startDY++;
			else if (startDY > y)
				startDY--;

			// TODO: save points, randomize using friction, find different
			// friction for diff surfaces
		}

		if (Mouse.getX() == x && Mouse.getY() == y)
			return true;
		return false;
	}



}
