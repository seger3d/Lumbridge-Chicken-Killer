package ChickenKillerPackage;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.GroundItems;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.GroundItem;

public class Loot extends Node {
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

	public int loot =  314 ;
	public final int spinTicket = 24154;
	public static int feathersLooted = 0;
	public int startFeathers = -1;

	@Override
	public void execute() {

		GroundItem item = GroundItems.getNearest(new Filter<GroundItem>() {
			public boolean accept(final GroundItem item) {
				if (LumbridgeChickenKiller.location.equals("Lumbridge")) {
					return (item.getId() == loot && lumbridgeArea
							.contains(item.getLocation()));
				} else if (LumbridgeChickenKiller.location.equals("Falador")) {
					return (item.getId() == loot && faladorArea
							.contains(item.getLocation()));
				} else {
					return (item.getId() == loot);
				}

			}

		});

		if (startFeathers == -1) {
			if (Inventory.getItem(loot).getStackSize() == 0){
				startFeathers = 0;
			} else {
				startFeathers = Inventory.getItem(loot).getStackSize();
			}
			
		}
		
		if (!item.equals(null)) {
			if (item.isOnScreen()) {
				item.interact("Take", "Feather");
				int featherCount = Inventory.getItem(loot).getStackSize();
				Task.sleep(600, 800);
				while (Players.getLocal().isMoving()
						&& Inventory.getItem(loot).getStackSize() == featherCount) {
					sleep(200, 300);
				}

				feathersLooted = Inventory.getItem(loot).getStackSize()
						- startFeathers;

			} else {
				Camera.turnTo(item);
				if (!item.isOnScreen() & !Players.getLocal().isMoving()) {
					Walking.walk(item);
				}
			}
		}

	}

	@Override
	public boolean activate() {

		if (LumbridgeChickenKiller.pickFeathers == true) {
			// System.out.print("Looting Feathers");
			GroundItem item = GroundItems.getNearest(new Filter<GroundItem>() {
				public boolean accept(final GroundItem item) {
					if (LumbridgeChickenKiller.location.equals("Lumbridge")) {
						return (item.getId() == loot && lumbridgeArea
								.contains(item.getLocation()));
					} else if (LumbridgeChickenKiller.location
							.equals("Falador")) {
						return (item.getId() == loot && faladorArea
								.contains(item.getLocation()));
					} else {
						return (item.getId() == loot);
					}

				}

			});
			return (item != null);
		}
		return false;

	}
}