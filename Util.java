package teste;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class Util {

  public static Double getMaxQ(HashMap<String, Double> qTable,
                               String comb, int numActions) {

    double max = Integer.MIN_VALUE;

    for(int i = 0; i < numActions; i++) {
      String combi[] = comb.split("-");
      combi[combi.length-1] = new Integer(i).toString();
      String joined1 = String.join("-", combi);

      if (qTable.get(joined1) == null) {
        qTable.put(joined1, 0.0);
      } else {
        if (qTable.get(joined1) > max) {
          max = qTable.get(joined1);
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
      String combi[] = comb.split("-");
      combi[combi.length-1] = new Integer(i).toString();
      String joined1 = String.join("-", combi);

      if (qTable.get(joined1) == null) {
        qTable.put(joined1, 0.0);
      } else {
        if (qTable.get(joined1) > max) {
          action = i;
          max = qTable.get(joined1);
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

  public static String getCurrentComb(EnemyPosition ep, int currentAction, double x, double y) {
    String comb =  String.valueOf(Util.quantitazeValue(x)) + '-'
                   + String.valueOf(Util.quantitazeValue(y))
                   + '-'
                   + ep.getEnemyPositionXToString() + '-'
                   + ep.getEnemyPositionYToString() + '-'
                   + currentAction;
    return comb;
  }

  public static String getCurrentShotComb(EnemyPosition ep, int currentAction, double distance) {
    String comb =  ep.getEnemyPositionXToString() + '-'
									+ ep.getEnemyPositionYToString() + '-'
                  + String.valueOf(Util.quantitazeValue(distance)) + '-'
									+ currentAction;
    return comb;
  }
}
