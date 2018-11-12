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
import com.sun.javafx.geom.Point2D;
import static robocode.util.Utils.normalRelativeAngleDegrees;

public class AlmostDonothing extends AdvancedRobot {

	private double reward = 0;
	private double rewardShot = 0;
	private int turnValue = 360;
	private int numActions = 4;
	private int numShotActions = 7;
	private boolean explore = false;
	private int currentAction = 0;
	private boolean turnComplete = false;
	private int currentShotAction = 0;
	private HashMap<String, Double> qTable;
	private HashMap<String, Double> qShotTable;
	private boolean firstTime = true;
	private boolean actionTime = true;
	private boolean actionShotTime = true;
	private double currentQValue = 0;
	private double distance = 0;
	private double currentQShotValue = 0;
	private boolean actionShot = true;
	private EnemyPosition enemyPosition = new EnemyPosition();
	private double absoluteBearing = 0;
	private double bearingFromGun = 0;
	private int q_absbearing = 0;
	private double gunTurnAmt = 0;
	private boolean actionFinished = false;
	private boolean actionShotFinished = false;
	private String oldCombShot;
	private String oldComb;
	private double getVelocity = 0;
	private double getBearing = 0;
	private int qdistancetoenemy;
	// private List<String> combShot = new ArrayList<String>();
	// private List<String> futureCombShot = new ArrayList<String>();
	private double x = 0;
	private double y = 0;
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

		while(true) {
			turnGunRight(360);
			String comb = Util.getCurrentComb(qdistancetoenemy, q_absbearing, x, y);
		//	AIShot(comb);
			AIMoviment(comb);
		}
	}

	public void AIMoviment(String comb) {
		boolean movimentFinished = (new MoveCompleteCondition(this)).test();

		if (this.actionTime) {
			this.actionTime = false;
			oldComb = Util.getCurrentComb(qdistancetoenemy, q_absbearing, x, y);
			currentAction = getAction(oldComb, qTable, numActions);
			currentQValue = getCurrentQValue(qTable, oldComb, currentAction);
			doAction(currentAction);
		} else if (this.actionFinished && (movimentFinished)) {
			this.actionFinished = false;
			calculateQLearning(oldComb, comb, currentQValue, numActions, qTable, reward);
			this.actionTime = true;
			reward = 0;
		}
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		absoluteBearing = getHeading() + e.getBearing();
		bearingFromGun = normalRelativeAngleDegrees(absoluteBearing - getGunHeading());
		this.getVelocity = e.getVelocity();
		qdistancetoenemy = Util.quantize_distance(e.getDistance());
		x = getX();
		y = getY();
		this.getBearing=e.getBearing();
		double angle = Math.toRadians((getHeading() + e.getBearing()) % 360);
		this.enemyPosition = new EnemyPosition(angle, e.getDistance(), x, y);
		double absbearing=Util.absoluteBearing((float)x,(float)y,this.enemyPosition);
		q_absbearing=Util.quantize_angle(absbearing);
		// if(qdistancetoenemy==1){firePower = 3;}
		// if(qdistancetoenemy==2){firePower = 2;}
		// if(qdistancetoenemy>2){firePower = 1;}
	}

	public double getCurrentQValue(HashMap<String, Double> map, String comb, int currentAction) {
		double currentQValue;
		comb = comb + '~' + currentAction;

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
		if (explore) {
			return Util.getRandom(nA);
		} else {
			return Util.getBestAction(table, comb, nA);
		}
	}

	public void doShotAction(int action) {
		double nextGunHeading = 0;
		// double firePower = 0;

		double data[] = UtilActions.getHeadingAndFireAction(action);
		// firePower = data[0];
		nextGunHeading = data[1];
		int firePower = 0;


		setTurnGunRight(bearingFromGun);

		Bullet bullet = setFireBullet(firePower);

		if (bullet == null) {
			rewardShot -= 1;
			this.actionShotFinished = true;
		}


		waitFor((new MoveCompleteCondition(this)));
		this.actionShot = true;
	}

	public void doAction(int action) {
		double currentHeading = getHeading();
		double nextHeading = 0;

		switch(action){
			case 0: moveAntiClockWise();
				break;
			case 1: moveClockWise();
				break;
			case 2: moveAntiClockWiseSoftly();
				break;
			case 3: moveClockWiseSoftly();
				break;
		}

		this.actionFinished = true;
	}

	private void moveAntiClockWise() {
		int moveDirection=+1;
		if (getVelocity == 0)
			moveDirection *= 1;

		setTurnRight(getBearing + 90);
		setAhead(300 * moveDirection);
	}

	private void moveClockWise() {
		int moveDirection1=-1;
		if (getVelocity == 0)
			moveDirection1 *= 1;

		setTurnRight(getBearing + 90);
		setAhead(200 * moveDirection1);
	}

	private void moveAntiClockWiseSoftly() {
		int moveDirection1=+1;
		if (getVelocity == 0)
			moveDirection1 *= 1;

		setTurnRight(getBearing + 90);
		setAhead(150 * moveDirection1);
	}

	private void moveClockWiseSoftly() {
		int moveDirection1=-1;
		if (getVelocity == 0)
			moveDirection1 *= 1;

		setTurnRight(getBearing + 90);
		setAhead(150 * moveDirection1);
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
		oldComb = oldComb + '~' + currentAction;
		table.put(oldComb, qLearning);
	}

	public void onBulletHitBullet(BulletHitBulletEvent event) {
		// reward += 3;
		this.actionShotFinished = true;
	}

	public void onBulletHit(BulletHitEvent event) {
		double change = event.getBullet().getPower() * 3 ;
		rewardShot = rewardShot + (int)change;;
		this.actionShotFinished = true;
	}

	public void onBulletMissed(BulletMissedEvent event) {
		double change = -event.getBullet().getPower();
		rewardShot += (int)change;
		this.actionShotFinished = true;
	}

	public void onHitWall(HitWallEvent e) {
		reward -=3.5;
		wallSmoothing();
	}

	public void onHitByBullet(HitByBulletEvent e) {
		double power = e.getBullet().getPower();
		double change = -3 * power;

		reward -= 3;
	}

	public void OnHitRobot(HitRobotEvent evnt) {
		reward -= 2;
	}

	public void wallSmoothing() {
		double xPos=this.getX();
		double yPos=this.getY();
		double width=this.getBattleFieldWidth();
		double height=this.getBattleFieldHeight();
		if(yPos<80) {
			turnLeft(getHeading() % 90);
			if(getHeading()==0){turnLeft(0);}
			if(getHeading()==90){turnLeft(90);}
			if(getHeading()==180){turnLeft(180);}
			if(getHeading()==270){turnRight(90);}
			ahead(150);

			if ((this.getHeading()<180)&&(this.getHeading()>90)) {
				this.setTurnLeft(90);
			} else if((this.getHeading()<270)&&(this.getHeading()>180)) {
				this.setTurnRight(90);
			}


		} else if(yPos>height-80) {
			if((this.getHeading()<90)&&(this.getHeading()>0)){this.setTurnRight(90);}
			else if((this.getHeading()<360)&&(this.getHeading()>270)){this.setTurnLeft(90);}
			turnLeft(getHeading() % 90);

			if(getHeading()==0){turnRight(180);}
			if(getHeading()==90){turnRight(90);}
			if(getHeading()==180){turnLeft(0);}
			if(getHeading()==270){turnLeft(90);}
			ahead(150);
		} else if(xPos<80) {
			turnLeft(getHeading() % 90);
			if(getHeading()==0){turnRight(90);}
			if(getHeading()==90){turnLeft(0);}
			if(getHeading()==180){turnLeft(90);}
			if(getHeading()==270){turnRight(180);}
			ahead(150);
		} else if(xPos>width-80){
			turnLeft(getHeading() % 90);
			if(getHeading()==0){turnLeft(90);}
			if(getHeading()==90){turnLeft(180);}
			if(getHeading()==180){turnRight(90);}
			if(getHeading()==270){turnRight(0);}
			ahead(150);
		}
	}

}
