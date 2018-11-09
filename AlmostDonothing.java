package almostNothing;
import robocode.*;
import java.lang.Math;
import java.util.Random;
import java.util.Map;
import java.util.List;
import java.lang.Number;
import java.util.HashMap;
import almostNothing.EnemyPosition;
import almostNothing.SaveAndLoad;
import almostNothing.Util;
import almostNothing.UtilActions;
import static robocode.util.Utils.normalRelativeAngleDegrees;

public class AlmostDonothing extends AdvancedRobot {

	private int reward = 0;
	private int rewardShot = 0;
	private int turnValue = 360;
	private int numActions = 8;
	private int numShotActions = 18;
	private boolean explore = false;
	private int currentAction = 0;
	private int currentShotAction = 0;
	private HashMap<String, Double> qTable;
	private HashMap<String, Double> qShotTable;
	private boolean firstTime = true;
	private boolean actionTime = true;
	private double currentQValue = 0;
	private double currentQShotValue = 0;
	private boolean actionShot = true;
	private EnemyPosition enemyPosition = new EnemyPosition();
	private double radarHeading = 0;
	private boolean actionFinished = false;
	private String oldCombShot;
	private String oldComb;
	/**
	 * run: Donothing's default behavior
	 */
	public void run() {

		try {
	//		qShotTable = SaveAndLoad.load(getDataFile("qShotTable.txt").toString());
			qTable = SaveAndLoad.load(getDataFile("qTable.txt").toString());
		} catch(Exception e) {
			System.out.println("error");
		}

		// Robot main loop
		while(true) {
			turnRadarRight(turnValue);
			double x = getX();
			double y = getY();
			// double distance = Math.sqrt(Math.pow(x-enemyPosition.getEnemyPositionX(), 2)
			// 														+Math.pow(y-enemyPosition.getEnemyPositionY(), 2));
			// String combShot = Util.getCurrentShotComb(enemyPosition, currentShotAction, distance, radarHeading);
			String comb = Util.getCurrentComb(enemyPosition, currentAction, x, y);

			if (actionTime) {
				this.actionTime = false;
			//	double rH = radarHeading;

				// oldCombShot = Util.getCurrentShotComb(enemyPosition, currentShotAction, distance, rH);
				oldComb = Util.getCurrentComb(enemyPosition, currentAction, x, y);

				// currentShotAction = getAction(oldCombShot, qShotTable, numShotActions);
				currentAction = getAction(oldComb, qTable, numActions);

				// currentQShotValue = getCurrentQValue(qShotTable, oldCombShot);
				currentQValue = getCurrentQValue(qTable, oldComb);

				// rewardShot = 0;
				reward = 0;

				// this.actionShot = true;
				// doShotAction(currentShotAction, rH);
				doAction(currentAction);
			} else if (this.actionFinished){
				if (actionShot) {
					// calculateQLearning(oldCombShot, combShot, currentQShotValue, numShotActions, qShotTable, rewardShot);
				}

				calculateQLearning(oldComb, comb, currentQValue, numActions, qTable, reward);
				this.actionTime = true;
				this.actionFinished = false;
			}
			execute();
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		//radarHeading = getRadarHeading();
		double absoluteBearing = getHeading() + e.getBearing();
		double bearingFromGun = normalRelativeAngleDegrees(absoluteBearing - getGunHeading());
		double x = getX();
		double y = getY();
		double angle = Math.toRadians((getHeading() + e.getBearing()) % 360);
		radarHeading = bearingFromGun;
		this.enemyPosition = new EnemyPosition(angle, e.getDistance(), x, y);
	}

	public double getCurrentQValue(HashMap<String, Double> map, String comb) {
		double currentQValue;

		if (map.get(comb) == null) {
			currentQValue = 0;
			map.put(comb, 0.0);
		} else {
			currentQValue = map.get(comb);
		}

		return currentQValue;
	}


	public void	onRoundEnded(RoundEndedEvent event) {
		//SaveAndLoad.save(getDataFile("qShotTable.txt").toString(), qShotTable);
		SaveAndLoad.save(getDataFile("qTable.txt").toString(), qTable);

	}

	public int getAction(String comb, HashMap<String, Double> table, int nA) {
		if (explore || nA == 8) {
			return Util.getRandom(nA);
		} else {
			return Util.getBestAction(table, comb, nA);
		}
	}

	public void doShotAction(int action, double radarHeading) {
		double gunHeading = getGunHeading();
		double nextGunHeading = 0;
		double firePower = 0;

		double data[] = UtilActions.getHeadingAndFireAction(action);
		firePower = data[0];
		nextGunHeading = data[1];

		double interval = radarHeading + nextGunHeading;
		setTurnGunRight(interval);
		waitFor(new GunTurnCompleteCondition(this));

		Bullet bullet = setFireBullet(firePower);

		if (bullet == null) {
			turnValue = 360;
			this.actionShot = false;
			this.actionFinished = true;
		}

	//
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
		executeTurn(interval, currentHeading, nextHeading);
		setAhead(100);
		waitFor(new MoveCompleteCondition(this));
		this.actionFinished = true;
	}

	private void executeTurn(double interval, double currentHeading, double nextHeading) {
		if (interval > 0) {
			if (interval > 180) {
				setTurnRight((360 - currentHeading + nextHeading));
			} else {
				setTurnLeft(interval);
			}
			waitFor(new TurnCompleteCondition(this));
		} else {
			interval = interval*(-1);
			if (interval > 180) {
				setTurnLeft((360 - nextHeading + currentHeading));
			} else {
				setTurnRight(interval);
			}

			waitFor(new TurnCompleteCondition(this));
		}
	}

	public void calculateQLearning(String oldComb, String comb, double currentQValue, int nA,
																 HashMap<String, Double> table, double rew) {
		double max = Util.getMaxQ(table, comb, nA);
		double deltaQ = 0.1 * (rew + 0.9*max - currentQValue);
		double qLearning = currentQValue + deltaQ;
		table.put(oldComb, qLearning);
	}

	public void onBulletHitBullet(BulletHitBulletEvent event) {
		rewardShot += 3;
		this.actionFinished = true;
	}

	public void onBulletHit(BulletHitEvent event) {
		rewardShot += 3;
		this.actionFinished = true;
	}

	public void onBulletMissed(BulletMissedEvent event) {
		rewardShot -= 3;
		this.actionFinished = true;
	}

	public void onHitWall(HitWallEvent e) {
		reward -= 3;
	}

	public void onHitByBullet(HitByBulletEvent e) {
		reward -= 3;
	}

	public void OnHitRobot(HitRobotEvent evnt) {
		reward -= 3;
	}


}
