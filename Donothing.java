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

public class Donothing extends AdvancedRobot {

	private int reward = 0;
	private int turnValue = 360;
	private int numActions = 8;
	private boolean explore = true;
	private int currentAction = 0;
	private HashMap<String, Double> qTable;
	private boolean firstTime = true;
	private boolean actionTime = true;
	private double currentQValue = 0;


	/**
	 * run: Donothing's default behavior
	 */
	public void run() {

		try {
			qTable = SaveAndLoad.load(getDataFile("t.txt").toString());
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
									+ enemyPosition.getEnemyPositionY() + '-'
									+ currentAction;


		if (actionTime) {
			 currentAction = getAction(comb);

			 comb = String.valueOf(Util.quantitazeValue(x)) + '-'
							+ String.valueOf(Util.quantitazeValue(y))
							+ '-'
							+ enemyPosition.getEnemyPositionX() + '-'
							+ enemyPosition.getEnemyPositionY() + '-'
							+ currentAction;

			if (qTable.get(comb) == null) {
				currentQValue = 0;
				qTable.put(comb, 0.0);
			} else {
				currentQValue = qTable.get(comb);
			}

			reward = 0;
			doAction(currentAction);
			this.actionTime = false;
		} else {
			calculateQLearning(comb, currentQValue);
			this.actionTime = true;
			turnValue = 360;
		}
	}


	public void	onRoundEnded(RoundEndedEvent event) {
		SaveAndLoad.save(getDataFile("t.txt").toString(), qTable);
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
			return Util.getBestAction(qTable, comb, numActions);
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

	public void calculateQLearning(String comb, double currentQValue) {
		double max = Util.getMaxQ(qTable, comb, numActions);
		double deltaQ = 0.1 * (reward + 0.9*max - currentQValue);
		double qLearning = currentQValue + deltaQ;
		qTable.put(comb, qLearning);
	}


	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		reward -= 3;
	}

}
