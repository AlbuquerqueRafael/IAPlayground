package teste;
import robocode.*;
import java.lang.Math;
import java.util.Random;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import teste.EnemyPosition;
import teste.SaveAndLoad;
import teste.Util;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;

public class Donothing extends AdvancedRobot {

	private int reward = 0;
	private int maxXValue = 800;
	private int turnValue = 360;
	private int numActions = 3;
	private int moveDirection = 1;
	private List<Double> defaultList;
	private boolean explore = false;
	private int currentAction = 0;
	private HashMap<String, ArrayList<Double>> qTable;
	private boolean firstTime = true;
	private double currentQValue = 0;
	private int count = 0;


	public Donothing() {
		defaultList = new ArrayList<Double>();

		for(int i = 0; i < numActions; i++) {
			defaultList.add(0.0);
		}
	}

	/**
	 * run: Donothing's default behavior
	 */
	public void run() {

		qTable = SaveAndLoad.load(getDataFile("t.txt").toString());


		// Robot main loop
		while(true) {
			turnRadarLeft(turnValue);
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		turnValue = 0;

		double angle = Math.toRadians((getHeading() + e.getBearing()) % 360);
		double x = getX();
		double y = getY();
		EnemyPosition enemyPosition = new EnemyPosition(angle, e.getDistance(), x, y);

		String comb = String.valueOf((int) x) + '-'
									+ String.valueOf((int) y)
									+ '-'
									+ enemyPosition.getEnemyPositionX() + '-'
									+ enemyPosition.getEnemyPositionY();

		if (firstTime) {
			if (qTable.get(comb) == null) {
				currentQValue = 0;
				qTable.put(comb, new ArrayList<Double>(defaultList));
			} else {
				currentQValue = qTable.get(comb).get(currentAction);
			}

			reward = 0;
			currentAction = getAction(comb);
			System.out.println(currentAction);
			doAction(currentAction, e.getBearing());
			this.firstTime = false;
		} else {
			calculateQLearning(enemyPosition.getEnemyPositionX(), enemyPosition.getEnemyPositionY(),
												 String.valueOf((int) x), String.valueOf((int) y), 0, currentQValue);
			this.firstTime = true;
		}

		turnValue = 360;
	}


	public void	onRoundEnded(RoundEndedEvent event) {
		SaveAndLoad.save(qTable, getDataFile("t.txt").toString());
	}


	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		reward -= 3;
	}

	public void onBulletHit(BulletHitEvent e) {
		reward += 3;
	}

	public int getAction(String comb) {
		if (explore) {
			return Util.getRandom();
		} else {
			return Util.getBestAction(defaultList, qTable, comb);
		}

	}

	public void doAction(int action, Double bearing, double gunTurnAmt;) {
		if (action == 0) {
			int moveDirection=+1;  //moves in anticlockwise direction

			// circle our enemy
			setTurnRight(getBearing + 90);
			setAhead(150 * moveDirection);
		} else if (action == 1){
			int moveDirection1=-1;  //moves in clockwise direction

			// circle our enemy
			setTurnRight(getBearing + 90);
			setAhead(150 * moveDirection1);
		} else if (action == 2) {
			turnGunRight(gunTurnAmt); // Try changing these to setTurnGunRight,
			turnRight(getBearing-25); // and see how much Tracker improves...
			// (you'll have to make Tracker an AdvancedRobot)
			ahead(150);
		} else if (action == 3) {
			turnGunRight(gunTurnAmt); // Try changing these to setTurnGunRight,
			turnRight(getBearing-25); // and see how much Tracker improves...
			// (you'll have to make Tracker an AdvancedRobot)
			back(150);
		}
	}

	public void calculateQLearning(String enemyPositionX, String enemyPositionY, String x,
																 String y, int action, double currentQValue) {
		double qLearning = currentQValue;
		String comb = x + '-' + y + '-' + enemyPositionY + '-' + enemyPositionY;
		double max = Util.getMaxQ(defaultList, qTable, comb);
		double deltaQ = 0.1 * (reward + 0.9*max - currentQValue);
		qTable.get(comb).set(currentAction, deltaQ);
	}


	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		reward -= 2;
	}

}
