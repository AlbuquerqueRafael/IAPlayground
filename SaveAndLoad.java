package teste;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import robocode.*;


public class SaveAndLoad {

  public static HashMap<String, Double> load(String filename) throws IOException {
    HashMap<String, Double> qTable = new HashMap<String, Double>();
    BufferedReader reader = null;

    try {
      reader = new BufferedReader(new FileReader(filename));
      String line = reader.readLine();

      while (line != null) {
        String splitLine[] = line.split("/");
        qTable.put(splitLine[0], Double.parseDouble(splitLine[1]));
        line= reader.readLine();
      }

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      reader.close();
    }

    return qTable;

  }

  public static void save(String filename, HashMap<String, Double> qTable) {
    PrintStream ps = null;
    try {
      ps = new PrintStream(new RobocodeFileOutputStream(filename));
      for (Map.Entry<String, Double> entry : qTable.entrySet()) {
        ps.println(entry.getKey()+"/"+entry.getValue());
      }
    } catch (IOException e) {
      System.out.println("error");
    } finally {
      ps.flush();
      ps.close();
    }
  }
}
