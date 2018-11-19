package almostNothing;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import com.sun.javafx.geom.Point2D;

public class Util {

  public static Double getMaxQ(HashMap<String, Double> qTable,
                               String comb, int numActions) {

    double max = Integer.MIN_VALUE;

    for(int i = 0; i < numActions; i++) {
      String auxComb = comb;
      auxComb = comb + '~' + i;

      if (qTable.get(auxComb) == null) {
        qTable.put(auxComb, 0.0);
      } else {
        if (qTable.get(auxComb) > max) {
          max = qTable.get(auxComb);
        }
      }
    }

    if (max == Integer.MIN_VALUE) {
      return 0.0;
    }

    return max;

  }

  public static int getBestAction(HashMap<String, Double> qTable,
                                  String comb, int numActions) {

    double max = Integer.MIN_VALUE;
    int action = 0;
    for(int i = 0; i < numActions; i++) {
      String auxComb = comb;
      auxComb = comb + '~' + i;

      if (qTable.get(auxComb) == null) {
        qTable.put(auxComb, 0.0);
      } else {
        if (qTable.get(auxComb) > max) {
          action = i;
          max = qTable.get(auxComb);
        }
      }
    }

    return action;
  }

  public static int getRandom(int numActions) {
    Random rand = new Random();
    int  n = rand.nextInt(numActions) + 0;

    return n;
  }

  public static int quantitazeValue(double value) {
    return (int) (value / 100.0);
  }

  public static String getCurrentComb(int qdistancetoenemy, int q_absbearing, double x, double y) {
    String comb =  String.valueOf(Util.quantitazeValue(x)) + '~'
                   + String.valueOf(Util.quantitazeValue(y))
                   + '~'
                   + String.valueOf(q_absbearing) + '~'
                   + String.valueOf(qdistancetoenemy);
    return comb;
  }

  public static String getCurrentCombWithEnemy(EnemyPosition enemy, double x, double y) {
    String comb =  String.valueOf(Util.quantitazeValue(x)) + '~'
                   + String.valueOf(Util.quantitazeValue(y))
                   + '~'
                   + String.valueOf(Util.quantitazeValue(enemy.getEnemyPositionX())) + '~'
                   + String.valueOf(Util.quantitazeValue(enemy.getEnemyPositionY()));
    return comb;
  }

  public static String getCurrentCombHeading(int qdistancetoenemy, int q_absbearing, double x, double y, int heading) {
    String comb =  String.valueOf(Util.quantitazeValue(x)) + '~'
                   + String.valueOf(Util.quantitazeValue(y))
                   + '~'
                   + String.valueOf(q_absbearing) + '~'
                   + String.valueOf(heading) + '~'
                   + String.valueOf(qdistancetoenemy);
    return comb;
  }

  public static String getCurrentShotComb(double distance, int currentAction, double radarHeading, double x, double y) {
    int radar = (int) (radarHeading / 10);
    String comb = String.valueOf(Util.quantitazeValue(x)) + '~'
                  + String.valueOf(Util.quantitazeValue(y))
                  + '~'
                  + String.valueOf(Util.quantitazeValue(distance)) + '~'
                  + String.valueOf(radar) + '~'
									+ currentAction;
    return comb;
  }

  public static int quantize_distance(double distance2) {
		int qdistancetoenemy = 0;
		if(distance2 > 0 && distance2<=250){
			qdistancetoenemy=1;
		} else if(distance2 > 250 && distance2<=500){
			qdistancetoenemy=2;
		} else if(distance2 > 500 && distance2<=750){
			qdistancetoenemy=3;
		} else if(distance2 > 750 && distance2<=1000){
			qdistancetoenemy=4;
		}

		return qdistancetoenemy;
	}

  public static int quantize_angle(double absbearing2) {
    int q_absbearing = 0;
    if((absbearing2 > 0) && (absbearing2<=90)){
      q_absbearing=1;
      }
    else if((absbearing2 > 90) && (absbearing2<=180)){
      q_absbearing=2;
      }
    else if((absbearing2 > 180) && (absbearing2<=270)){
      q_absbearing=3;
      }
    else if((absbearing2 > 270) && (absbearing2<=360)){
      q_absbearing=4;
      }
    return q_absbearing;
  }

  public static double absoluteBearing(float x1, float y1, EnemyPosition enemyPosition) {


    float x2 = (float) enemyPosition.getEnemyPositionX();
    float y2 = (float) enemyPosition.getEnemyPositionY();
    double xo = x2-x1;
    double yo = y2-y1;
    double hyp = Point2D.distance(x1, y1, x2, y2);
    double arcSin = Math.toDegrees(Math.asin(xo / hyp));
    double bearing = 0;

    if (xo > 0 && yo > 0) {
      bearing = arcSin;
    } else if (xo < 0 && yo > 0) {
      bearing = 360 + arcSin;
    } else if (xo > 0 && yo < 0) {
      bearing = 180 - arcSin;
    } else if (xo < 0 && yo < 0) {
      bearing = 180 - arcSin;
    }

    return bearing;
  }

}
