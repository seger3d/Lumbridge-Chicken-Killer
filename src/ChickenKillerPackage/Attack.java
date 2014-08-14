package ChickenKillerPackage;

import ChickenKillerPackage.LumbridgeChickenKiller;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.Menu;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.NPC;

public class Attack extends Node {

	Area lumbridgeArea = new Area(new Tile[] { new Tile(3224, 3302, 0),
			new Tile(3223, 3301, 0), new Tile(3223, 3293, 0),
			new Tile(3230, 3293, 0), new Tile(3230, 3286, 0),
			new Tile(3236, 3286, 0), new Tile(3236, 3290, 0),
			new Tile(3237, 3290, 0), new Tile(3237, 3293, 0),
			new Tile(3236, 3294, 0), new Tile(3236, 3298, 0),
			new Tile(3237, 3298, 0), new Tile(3237, 3301, 0),
			new Tile(3236, 3302, 0) });

	Area faladorArea = new Area(new Tile[] { new Tile(3012, 3299, 0),
			new Tile(3020, 3299, 0), new Tile(3020, 3296, 0),
			new Tile(3020, 3280, 0), new Tile(3012, 3280, 0),
			new Tile(3012, 3299, 0) });

	public int NPCMonster[] = { 1017, 41, 2314, 2315, 2313 };

	/**
	public boolean interact(NPC npc, String action, String option) {
		if (!npc.getModel().contains(Mouse.getLocation()))
			Mouse.move(npc.getNextViewportPoint());
		return Menu.contains(action, option) && Menu.select(action, option);
	}
	
	*/
	@Override
	public void execute() {
		System.out.print("Attack");
		

		NPC monster = NPCs.getNearest(new Filter<NPC>() {
			public boolean accept(NPC npc) {

				if (LumbridgeChickenKiller.location.equals("Lumbridge")) {
					//System.out.print("Location: Lumbridge");
					return (npc.getName().equals("Chicken")
							&& npc.getInteracting() == null && lumbridgeArea
							.contains(npc));
				} else if (LumbridgeChickenKiller.location.equals("Falador")) {
					//System.out.print("Location: Falador");
					return (npc.getName().equals("Chicken")
							&& npc.getInteracting() == null && faladorArea
							.contains(npc));
				} else {
					//System.out.print("Location: Other");
					return (npc.getName().equals("Chicken") && npc
							.getInteracting() == null);
				}

			}
		});

		if (monster != null) {
			if (monster.isOnScreen()) {
				if (!monster.isInCombat()) {
					//interact(monster, "Attack", "Chicken");
					monster.interact("Attack", "Chicken");
					sleep(1000, 1200);
				}
				while (Players.getLocal().isMoving()) {
					sleep(100, 200);
				}

			} else {
				Camera.turnTo(monster);
				if (!monster.isOnScreen()) {
					Walking.walk(monster);
				}

			}

		}
	}

	@Override
	public boolean activate() {

		if (Players.getLocal().isMoving()){
			return false;
		}
		if (Players.getLocal().getInteracting() != null && Players.getLocal().getInteracting().getHpPercent() == 0){
			return true;
		}
		if (Players.getLocal().getInteracting() != null || Players.getLocal().getAnimation() != -1
				) {
			return false;

		}
		return true;
	}
}