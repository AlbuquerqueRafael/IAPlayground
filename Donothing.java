package teste;
import robocode.*;
import java.lang.Math;
import java.util.Random;
import java.util.Map;
import java.util.List;
import java.lang.Number;
import java.util.HashMap;
import teste.EnemyPosition;
import teste.SaveAndLoad;
import teste.Util;
import teste.UtilActions;

public class Donothing extends AdvancedRobot {

	private int reward = 0;
	private int rewardShot = 0;
	private int turnValue = 360;
	private int numActions = 8;
	private int numShotActions = 15;
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

	/**
	 * run: Donothing's default behavior
	 */
	public void run() {

		try {
			qShotTable = SaveAndLoad.load(getDataFile("qShotTable.txt").toString());
			qTable = SaveAndLoad.load(getDataFile("qTable.txt").toString());
		} catch(Exception e) {
			System.out.println("error");
		}

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
		double radarHeading = getRadarHeading();
		setTurnRadarLeft(0);
		waitFor(new RadarTurnCompleteCondition(this));

		turnValue = 0;

		double angle = Math.toRadians((getHeading() + e.getBearing()) % 360);
		double x = getX();
		double y = getY();
		EnemyPosition enemyPosition = new EnemyPosition(angle, e.getDistance(), x, y);
		double distance = Math.sqrt(Math.pow(x-enemyPosition.getEnemyPositionX(), 2)
																+Math.pow(y-enemyPosition.getEnemyPositionY(), 2));

		String combShot = Util.getCurrentShotComb(enemyPosition, currentAction, distance);
		String comb = Util.getCurrentComb(enemyPosition, currentAction, x, y);

		if (actionTime) {
			currentShotAction = getAction(combShot, qShotTable, numShotActions);
			currentAction = getAction(comb, qTable, numActions);

			combShot = Util.getCurrentShotComb(enemyPosition, currentAction, distance);
			comb = Util.getCurrentComb(enemyPosition, currentAction, x, y);

			currentQShotValue = getCurrentQValue(qShotTable, combShot);
			currentQValue = getCurrentQValue(qTable, comb);

			rewardShot = 0;
			reward = 0;
			
			this.actionShot = true;
			doShotAction(currentShotAction, radarHeading);
			doAction(currentAction);
			this.actionTime = false;
		} else {
			if (actionShot) {
				calculateQLearning(combShot, currentQShotValue, numShotActions, qShotTable, rewardShot);
			}

			calculateQLearning(comb, currentQValue, numActions, qTable, reward);
			this.actionTime = true;
			turnValue = 360;
		}
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
		SaveAndLoad.save(getDataFile("qShotTable.txt").toString(), qShotTable);
		SaveAndLoad.save(getDataFile("qTable.txt").toString(), qTable);

	}


	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		reward -= 3;
	}

	public void OnHitRobot(HitRobotEvent evnt) {
		reward -= 3;
	}

	public int getAction(String comb, HashMap<String, Double> table, int nA) {
		if (explore) {
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

		if ((radarHeading + nextGunHeading) > 360) { // Ex: Current radar: 355
			 																					 //     Next Gun Heading: 15(Move 15 degrees)
																								 //			New angle = 370 = 10 degrees
			radarHeading = 360 - radarHeading + nextGunHeading;
		} else if ((radarHeading + nextGunHeading) < 0) { // Same example. But imagine this time a next Gun Heading negative
			radarHeading = radarHeading + nextGunHeading + 360;
		} else {
			radarHeading += nextGunHeading;
		}


		double interval = gunHeading - radarHeading;
		executeTurnGun(interval, gunHeading, radarHeading);
		Bullet bullet = setFireBullet(firePower);

		if (bullet == null) {
		//	turnValue = 360;
			this.actionShot = false;
		}

	//
	}

	private void executeTurnGun(double interval, double currentHeading, double nextHeading) {
		if (interval > 0) {
			if (interval > 180) {
				setTurnGunRight((360 - currentHeading + nextHeading));
			} else {
				setTurnGunLeft(interval);
			}
			waitFor(new GunTurnCompleteCondition(this));
		} else {
			interval = interval*(-1);
			if (interval > 180) {
				setTurnGunLeft((360 - nextHeading + currentHeading));
			} else {
				setTurnGunRight(interval);
			}

			waitFor(new GunTurnCompleteCondition(this));
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
		executeTurn(interval, currentHeading, nextHeading);
		setAhead(100);
		waitFor(new MoveCompleteCondition(this));
		turnValue = 360;
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

	public void calculateQLearning(String comb, double currentQValue, int nA,
																 HashMap<String, Double> table, double rew) {
		double max = Util.getMaxQ(table, comb, nA);
		double deltaQ = 0.1 * (rew + 0.9*max - currentQValue);
		double qLearning = currentQValue + deltaQ;
		table.put(comb, qLearning);
	}

	public void onBulletHitBullet(BulletHitBulletEvent event) {
		rewardShot += 3;
	//	turnValue = 360;
	}

	public void onBulletHit(BulletHitEvent event) {
		rewardShot += 3;
	//	turnValue = 360;
	}

	public void onBulletMissed(BulletMissedEvent event) {
		rewardShot -= 3;
	//	turnValue = 360;
	}

	public void onHitWall(HitWallEvent e) {
		reward -= 3;
	}

}
