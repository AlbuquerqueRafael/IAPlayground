package teste;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import robocode.*;


public class SaveAndLoad {

  public static Map<String, List<Double>> load(String filename) {
    Map<String, List<Double>> auxQTable;

    try {
      FileInputStream fos = new FileInputStream(filename);
      ObjectInputStream ois = new ObjectInputStream(fos);
      System.out.println(ois.readObject());
      auxQTable = (Map<String, List<Double>>) ois.readObject();
      ois.close();
    } catch(Exception e) {
      System.out.println("vem");
      System.out.println(e.getStackTrace());

      auxQTable = new HashMap<String, List<Double>>();
    }

    return auxQTable;
  }

  public static void save(Map<String, List<Double>> qTable, String filename) {
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
