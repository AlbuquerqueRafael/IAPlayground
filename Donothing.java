package teste;
import robocode.*;
import java.lang.Math;
import java.util.Random;
import java.util.Map;
import java.util.List;
import java.lang.Number;
import java.util.HashMap;
import java.util.ArrayList;
import teste.EnemyPosition;
import teste.SaveAndLoad;
import teste.Util;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import robocode.util.Utils;

public class Donothing extends AdvancedRobot {

	private int reward = 0;
	private int maxXValue = 800;
	private int turnValue = 360;
	private int numActions = 8;
	private int moveDirection = 1;
	private ArrayList<Double> defaultList;
	private boolean explore = true;
	private int currentAction = 0;
	private HashMap<String, Object> qTable;
	private boolean firstTime = true;
	private boolean actionTime = true;
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

		load(getDataFile("t.txt").toString());

		// Robot main loop
		while(true) {
			setTurnRadarLeft(turnValue);
			execute();
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		setTurnRadarLeft(0);
		waitFor(new RadarTurnCompleteCondition(this));

		turnValue = 0;

		double angle = Math.toRadians((getHeading() + e.getBearing()) % 360);
		double x = getX();
		double y = getY();
		EnemyPosition enemyPosition = new EnemyPosition(angle, e.getDistance(), x, y);

		String comb = String.valueOf(Util.quantitazeValue(x)) + '-'
									+ String.valueOf(Util.quantitazeValue(y))
									+ '-'
									+ enemyPosition.getEnemyPositionX() + '-'
									+ enemyPosition.getEnemyPositionY();

		if (actionTime) {
			if (qTable.get(comb) == null) {
				currentQValue = 0;
				qTable.put(comb, new ArrayList<Double>(defaultList));
			} else {
				currentQValue = ((ArrayList<Double>) qTable.get(comb)).get(currentAction);
			}

			reward = 0;
			currentAction = getAction(comb);
			doAction(currentAction);
			this.actionTime = false;
		} else {
			calculateQLearning(comb, currentQValue);
			this.actionTime = true;
			turnValue = 360;
		}
	}


	public void	onRoundEnded(RoundEndedEvent event) {
		save(getDataFile("t.txt").toString());
	}


	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		reward -= 3;
	}

	public void onBulletHit(BulletHitEvent e) {
		//reward += 3;
	}

	public void OnHitRobot(HitRobotEvent evnt) {
		reward -= 3;
	}

	public int getAction(String comb) {
		if (explore) {
			return Util.getRandom();
		} else {
			return Util.getBestAction(defaultList, qTable, comb);
		}

	}

	public void doAction(int action) {
		double currentHeading = getHeading();
		double nextHeading = 0;

		if (action == 0) { // go 0 degrees
			nextHeading = 0;
		} else if (action == 1) { // go 45 degrees
			nextHeading = 45;
		} else if (action == 2) { // go 90 degrees
			nextHeading = 90;
		} else if (action == 3) { // go 135 degrees
			nextHeading = 135;
		} else if (action == 4) { // go 180 degrees
			nextHeading = 180;
		} else if (action == 5) { // go 225 degrees
			nextHeading = 225;
		} else if (action == 6) { // go 270 degrees
			nextHeading = 270;
		} else if (action == 7) { //go 315 degrees
			nextHeading = 315;
		}

		double interval = currentHeading - nextHeading;
		double radians = interval * Math.PI / 180;

		if (interval > 0) {
			setTurnLeft(interval);
			waitFor(new TurnCompleteCondition(this));
		} else {
			setTurnRight(interval*(-1));
			waitFor(new TurnCompleteCondition(this));
		}

		setAhead(100);
		waitFor(new MoveCompleteCondition(this));
		turnValue = 360;
	}

	public void calculateQLearning(String comb, double currentQValue) {
		double max = Util.getMaxQ(defaultList, qTable, comb);
		double deltaQ = 0.1 * (reward + 0.9*max - currentQValue);
		double qLearning = currentQValue + deltaQ;
		((ArrayList<Double>) qTable.get(comb)).set(currentAction, qLearning);
	}


	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		reward -= 3;
	}

	public void load(String filename) {

    try {
      FileInputStream fos = new FileInputStream(filename);
      ObjectInputStream ois = new ObjectInputStream(fos);
      System.out.println(ois.readObject().getClass());
      qTable = (HashMap<String, Object>) ois.readObject();
			System.out.println(ois.readObject().getClass());
      ois.close();
    } catch(Exception e) {
      System.out.println("vem");
      System.out.println(e.toString());
			System.out.println(e.getMessage());

      qTable = new HashMap<String, Object>();
    }

  }

  public void save(String filename) {
    try {
      RobocodeFileOutputStream fos = new RobocodeFileOutputStream(filename);
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(qTable);
      System.out.println("vem");
      oos.close();
    } catch(Exception e) {
      System.out.println("save");
      System.out.println(e.getMessage());
    }
  }


}
