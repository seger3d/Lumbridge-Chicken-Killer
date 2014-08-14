package ChickenKillerPackage;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Settings;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

public class Fight extends Node {

	private static final int ADRENALIN_SETTING_ID = 679;

	public static int getCurrentAdrenalinePercent() {
		return Settings.get(ADRENALIN_SETTING_ID) / 10;
	}

	public boolean aCaster() {
		
		if (getCurrentAdrenalinePercent() >= 100) {
			if (Widgets.get(548).getChild(123).click(true)) {
				if (Widgets.get(275).getChild(51).click(true)) {
					if (Widgets.get(275).getChild(60).click(true)) {
						if (Widgets.get(275).getChild(18).getChild(7)
								.click(true)) {
							sleep(600, 800);
							return true;
						}

					}
				}

			}

		}
		return false;
	}

	public void execute() {

		// if (BatKiller.momentum == true) {
		System.out.print("Using Momentum");
		aCaster();
		/*
		 * } else { System.out.print("Not Using Momentum"); int rand =
		 * Random.nextInt(1, 3); Task.sleep(30, 100); switch (rand) { case 1:
		 * Widgets.get(640).getChild(0).interact("Activate"); break; case 2:
		 * Widgets.get(640).getChild(35).interact("Activate"); break; case 3:
		 * Widgets.get(640).getChild(39).interact("Activate"); break; default:
		 * break; } }
		 */
	}

	public boolean activate() {
		return getCurrentAdrenalinePercent() >= 100;
		//return Players.getLocal().isInCombat() && Players.getLocal().getInteracting() != null;
	}
}