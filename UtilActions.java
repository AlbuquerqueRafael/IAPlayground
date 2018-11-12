package almostNothing;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class UtilActions {

  public static double[] getHeadingAndFireAction(int action) {
    double firePower = 1;
    double nextGunHeading = 0;
    double[] data = {1,0};

    nextGunHeading = -15 + (action/3)*5;
    firePower = (int) (action / 7);
    data[0] = firePower + 1;
    data[1] = nextGunHeading;

    return data;
  }

}
