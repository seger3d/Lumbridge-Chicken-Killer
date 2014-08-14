package ChickenKillerPackage;

import javax.swing.*;

//import java.awt.event.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import java.awt.event.*;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.Job;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Environment;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.input.Mouse;

import org.powerbot.game.api.methods.widget.Camera;

import org.powerbot.game.api.methods.widget.WidgetCache;
import org.powerbot.game.api.util.Random;

import org.powerbot.game.api.util.Timer;
import org.powerbot.game.bot.Context;
import org.powerbot.game.client.Client;

import ChickenKillerPackage.Attack;
import ChickenKillerPackage.Fight;
import ChickenKillerPackage.Loot;

@Manifest(authors = { "Zerg" }, name = "Lumbridge Chicken Killer", description = "Kills chickens and loots feathers.", version = 1.11, vip = false, website = "http://www.powerbot.org/community/topic/889716-snd-lumbridge-chicken-killer/")
public class LumbridgeChickenKiller extends ActiveScript implements
		PaintListener {

	public static Timer t;
	public static String location;
	boolean guiWait = true;
	chickengui g = new chickengui();
	public int featherID = 314;
	public static boolean pickFeathers;
	public static int loc;
	public static Timer antibanTimer;
	public int featherPrice = 0;
	private Client client = Context.client();
	public long startTime;

	public Image background;

	private final List<Node> jobsCollection = Collections
			.synchronizedList(new ArrayList<Node>());
	private Tree jobContainer = new Tree(new Node[] { new Fight(), new Loot(),
			new Antiban(), new Attack() });

	public synchronized final void provide(final Node... jobs) {
		for (final Node job : jobs) {
			if (!jobsCollection.contains(job)) {
				jobsCollection.add(job);
			}
		}
		jobContainer = new Tree(jobsCollection.toArray(new Node[jobsCollection
				.size()]));
	}

	public synchronized final void revoke(final Node... jobs) {
		for (final Node job : jobs) {
			if (jobsCollection.contains(job)) {
				jobsCollection.remove(job);
			}
		}
		jobContainer = new Tree(jobsCollection.toArray(new Node[jobsCollection
				.size()]));
	}

	public final void submit(final Job... jobs) {
		for (final Job job : jobs) {
			getContainer().submit(job);
		}
	}

	@Override
	public int loop() {
		if (Game.getClientState() != Game.INDEX_MAP_LOADED) {
			return 1000;
		}

		if (client != Context.client()) {
			WidgetCache.purge();
			Context.get().getEventManager().addListener(this);
			client = Context.client();
		}

		while (guiWait == true) {
			sleep(500);
		}
		Camera.setPitch(true);

		if (Walking.isRunEnabled() == false) {
			Walking.setRun(true);
		}

		if (Game.isLoggedIn()) {
			if (jobContainer != null) {
				final Node job = jobContainer.state();
				if (job != null) {
					jobContainer.set(job);
					getContainer().submit(job);
					job.join();
				}
			}
		}
		return Random.nextInt(200, 300);
	}

	@Override
	public void onStart() {
		background = getImage("http://i.imgur.com/4GeHCgR.png");
		g.setVisible(true);
		t = new Timer(0);
		antibanTimer = new Timer(1);
		featherPrice = getPrice(314);
		startTime = System.currentTimeMillis();
		while (guiWait == true) {
			Task.sleep(500);
		}

	}

	private int getPrice(int id) {
		try {
			URLConnection con = new URL(
					"http://open.tip.it/json/ge_single_item?item=" + id)
					.openConnection();
			String line;
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			line = in.readLine();
			if (line.contains("mark_price")) {
				line = line.substring(line.indexOf("mark_price\":\"")
						+ "mark_price\":\"".length());
				line = line.substring(0, line.indexOf("\""));
				return Integer.parseInt(line.replaceAll(",", ""));
			}
			in.close();
		} catch (Exception e) {

		}
		return -1;
	}

	// Method to import
	private Image getImage(String url) {
		try {
			return ImageIO.read(new URL(url));
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	public void onRepaint(Graphics g1) {
		int feathersCollected = Loot.feathersLooted;
		int profit = feathersCollected * featherPrice;
		DecimalFormat f = new DecimalFormat("##.00"); // this will helps you to
														// always keeps in two
														// decimal places

		double profitPerHour = (((profit) * 3600000D / (System
				.currentTimeMillis() - startTime))) / 1000;
		String profitPHS = f.format(profitPerHour);
		Graphics2D g = (Graphics2D) g1;
		g.drawImage(background, 0, 0, null);
		/**
		 * g.setColor(new Color(12, 16, 116)); g.setColor(new Color(22, 49,
		 * 141)); g.fill(new RoundRectangle2D.Double(335, 60, 160, 120, 20,
		 * 20));
		 */

		g.setColor(Color.black); // draw in yellow
		int x, y;
		x = Mouse.getX();
		y = Mouse.getY();

		// g.drawString(Environment.getDisplayName(),20,20);
		g.drawString(t.toElapsedString(), 115, 14);
		g.drawString("" + feathersCollected, 144, 29);
		g.drawString("" + featherPrice, 114, 44);
		g.drawString("" + profit, 225, 14);
		g.drawString(profitPHS + "k", 230, 29);

		// MOUSE
		g.setColor(Color.red);
		Font font = new Font("Arial", Font.PLAIN, 25);
		g.setFont(font);
		g.drawString("+", x - 7, y + 9);

		// Credit to Buccaneer
		// http://www.powerbot.org/community/topic/722969-tut-how-to-make-a-paint-for-beginners/
		while (!mousePath.isEmpty() && mousePath.peek().isUp())
			mousePath.remove();
		Point clientCursor = Mouse.getLocation();
		MousePathPoint mpp = new MousePathPoint(clientCursor.x, clientCursor.y,
				300); // 1000 = lasting time/MS
		if (mousePath.isEmpty() || !mousePath.getLast().equals(mpp))
			mousePath.add(mpp);
		MousePathPoint lastPoint = null;
		for (MousePathPoint a : mousePath) {
			if (lastPoint != null) {
				g.drawLine(a.x, a.y, lastPoint.x, lastPoint.y);
			}
			lastPoint = a;
		}
	}

	// Credit to Buccaneer
	// http://www.powerbot.org/community/topic/722969-tut-how-to-make-a-paint-for-beginners/
	private final LinkedList<MousePathPoint> mousePath = new LinkedList<MousePathPoint>();

	@SuppressWarnings("serial")
	private class MousePathPoint extends Point { // All credits to Enfilade

		private long finishTime;

		public MousePathPoint(int x, int y, int lastingTime) {
			super(x, y);
			finishTime = System.currentTimeMillis() + lastingTime;
		}

		public boolean isUp() {
			return System.currentTimeMillis() > finishTime;
		}
	}

	@SuppressWarnings("serial")
	class chickengui extends JFrame {
		public chickengui() {
			initComponents();
		}

		private void button1ActionPerformed(ActionEvent e) {

			if (Environment.getDisplayName() == "") { //zachb
				Context.get().getScriptHandler().stop();
			}

			if (comboBox1.getSelectedItem().toString()
					.equals((("Lumbridge East Farm")))) {
				location = "Lumbridge";
				loc = 0;
			} else if (comboBox1.getSelectedItem().toString()
					.equals((("Falador South Farm")))) {
				location = "Falador";
				loc = 1;
			} else if (comboBox1.getSelectedItem().toString()
					.equals((("Other")))) {
				location = "Other";
				loc = 2;
			}

			if (checkBox1.isSelected()) {
				pickFeathers = true;
			} else {
				pickFeathers = false;
			}

			guiWait = false;
			g.dispose();
		}

		private void initComponents() {
			// JFormDesigner - Component initialization - DO NOT MODIFY
			// //GEN-BEGIN:initComponents
			// Generated using JFormDesigner Evaluation license - Herpy Derpy
			checkBox1 = new JCheckBox();
			label2 = new JLabel();
			comboBox1 = new JComboBox<>();
			button1 = new JButton();

			// ======== this ========
			Container contentPane = getContentPane();

			// ---- checkBox1 ----
			checkBox1.setText("Pick Feathers");
			checkBox1.setSelected(true);

			// ---- label2 ----
			label2.setText("Location");

			// ---- comboBox1 ----
			comboBox1.setModel(new DefaultComboBoxModel<>(new String[] {
					"Lumbridge East Farm", "Falador South Farm", "Other" }));

			// ---- button1 ----
			button1.setText("Start");
			button1.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					button1ActionPerformed(e);
				}
			});

			GroupLayout contentPaneLayout = new GroupLayout(contentPane);
			contentPane.setLayout(contentPaneLayout);
			contentPaneLayout
					.setHorizontalGroup(contentPaneLayout
							.createParallelGroup()
							.addGroup(
									contentPaneLayout
											.createSequentialGroup()
											.addGroup(
													contentPaneLayout
															.createParallelGroup()
															.addGroup(
																	contentPaneLayout
																			.createSequentialGroup()
																			.addContainerGap()
																			.addComponent(
																					comboBox1,
																					GroupLayout.PREFERRED_SIZE,
																					GroupLayout.DEFAULT_SIZE,
																					GroupLayout.PREFERRED_SIZE))
															.addGroup(
																	contentPaneLayout
																			.createSequentialGroup()
																			.addGap(46,
																					46,
																					46)
																			.addComponent(
																					label2)))
											.addPreferredGap(
													LayoutStyle.ComponentPlacement.RELATED,
													6, Short.MAX_VALUE)
											.addGroup(
													contentPaneLayout
															.createParallelGroup()
															.addComponent(
																	checkBox1,
																	GroupLayout.PREFERRED_SIZE,
																	101,
																	GroupLayout.PREFERRED_SIZE)
															.addGroup(
																	GroupLayout.Alignment.TRAILING,
																	contentPaneLayout
																			.createSequentialGroup()
																			.addComponent(
																					button1)
																			.addGap(22,
																					22,
																					22)))
											.addContainerGap()));
			contentPaneLayout
					.setVerticalGroup(contentPaneLayout
							.createParallelGroup()
							.addGroup(
									contentPaneLayout
											.createSequentialGroup()
											.addContainerGap()
											.addGroup(
													contentPaneLayout
															.createParallelGroup()
															.addGroup(
																	contentPaneLayout
																			.createSequentialGroup()
																			.addComponent(
																					label2)
																			.addGap(10,
																					10,
																					10)
																			.addComponent(
																					comboBox1,
																					GroupLayout.PREFERRED_SIZE,
																					GroupLayout.DEFAULT_SIZE,
																					GroupLayout.PREFERRED_SIZE))
															.addGroup(
																	contentPaneLayout
																			.createSequentialGroup()
																			.addComponent(
																					checkBox1)
																			.addGap(18,
																					18,
																					18)
																			.addComponent(
																					button1)))
											.addContainerGap(8, Short.MAX_VALUE)));
			pack();
			setLocationRelativeTo(getOwner());
			// JFormDesigner - End of component initialization
			// //GEN-END:initComponents
		}

		// JFormDesigner - Variables declaration - DO NOT MODIFY
		// //GEN-BEGIN:variables
		// Generated using JFormDesigner Evaluation license - Herpy Derpy
		private JCheckBox checkBox1;
		private JLabel label2;
		private JComboBox<String> comboBox1;
		private JButton button1;
		// JFormDesigner - End of variables declaration //GEN-END:variables
	}

}
