package teste;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class Util {

  public static Double getMaxQ(ArrayList<Double> defaultList, HashMap<String, Object> qTable,
                               String comb) {
    if (qTable.get(comb) == null) {
      qTable.put(comb, new ArrayList<Double>(defaultList));
      return 0.0;
    } else {
      double max = Integer.MIN_VALUE;
      ArrayList<Double> aux = ((ArrayList<Double>) qTable.get(comb));

      for(int i = 0; i < aux.size(); i++) {
        if (aux.get(i) > max) {
          max = aux.get(i);
        }
      }

      return max;
    }
  }

  public static int getBestAction(List<Double> defaultList, HashMap<String, Object> qTable,
                                  String comb) {
    if (qTable.get(comb) == null) {
      qTable.put(comb, new ArrayList<Double>(defaultList));
      return 0;
    } else {
      double max = Integer.MIN_VALUE;
      int action = 0;
      ArrayList<Double> aux = ((ArrayList<Double>) qTable.get(comb));

      for(int i = 0; i < aux.size(); i++) {
        if (aux.get(i) > max) {
          action = i;
          max = aux.get(i);
        }
      }

      return action;
    }
  }

  public static int getRandom() {
    Random rand = new Random();
    int  n = rand.nextInt(8) + 0;

    return n;
  }

  public static int quantitazeValue(double value) {
    return (int) (value / 100.0);
  }
}
