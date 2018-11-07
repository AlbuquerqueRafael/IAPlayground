package teste;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class UtilActions {

  // public static int[] getHeadingAndFireAction(int action) {
  //   int firePower = 1;
  //   int nextGunHeading = 0;
  //   int[] data = {1,0};
  //
  //   if (action == 0) { // just shoot/fire 1
	// 		firePower = 1;
	// 		nextGunHeading = 0;
	// 	} else if (action == 1) { // just shoot 15 degrees/fire 1
	// 		firePower = 1;
	// 		nextGunHeading = 15;
	// 	} else if (action == 2) { // just shoot 30 degrees/fire 1
	// 		firePower = 1;
	// 		nextGunHeading = 30;
	// 	} else if (action == 3) { // just shoot/fire 2
	// 		firePower = 2;
	// 		nextGunHeading = 0;
	// 	} else if (action == 4) { // just shoot 15 degrees/fire 2
	// 		firePower = 2;
	// 		nextGunHeading = 15;
	// 	} else if (action == 5) { // just shoot 30 degrees/fire 2
	// 		firePower = 2;
	// 		nextGunHeading = 30;
	// 	} else if (action == 6) { // just shoot/fire 3
	// 		firePower = 3;
	// 		nextGunHeading = 0;
	// 	} else if (action == 7) { // just shoot 15 degrees/fire 3
	// 		firePower = 3;
	// 		nextGunHeading = 15;
	// 	} else if (action == 8) { // just shoot 30 degrees/fire 3
	// 		firePower = 3;
	// 		nextGunHeading = 30;
	// 	} else if (action == 9) { // just shoot -15 degrees/fire 1
	// 		firePower = 1;
	// 		nextGunHeading = -15;
	// 	} else if (action == 10) { // just shoot -30 degrees/fire 1
	// 		firePower = 1;
	// 		nextGunHeading = -30;
	// 	} else if (action == 11) { // just shoot -15 degrees/fire 1
	// 		firePower = 2;
	// 		nextGunHeading = -15;
	// 	} else if (action == 12) { // just shoot -30 degrees/fire 2
	// 		firePower = 2;
	// 		nextGunHeading = -30;
	// 	} else if (action == 13) { // just shoot -15 degrees/fire 3
	// 		firePower = 3;
	// 		nextGunHeading = -15;
	// 	} else if (action == 14) { // just shoot -30 degrees/fire 3
	// 		firePower = 3;
	// 		nextGunHeading = -30;
	// 	}
  //
  //   data[0] = firePower;
  //   data[1] = nextGunHeading;
  //
  //   return data;
  // }

  public static double[] getHeadingAndFireAction(int action) {
    double firePower = 1;
    double nextGunHeading = 0;
    double[] data = {1,0};

    if (action == 0) { // just shoot/fire 1
			firePower = 1;
			nextGunHeading = 0;
		} else if (action == 1) { // just shoot 15 degrees/fire 1
			firePower = 1;
			nextGunHeading = 7.5;
		} else if (action == 2) { // just shoot 30 degrees/fire 1
			firePower = 1;
			nextGunHeading = 15;
		} else if (action == 3) { // just shoot/fire 2
			firePower = 1;
			nextGunHeading = 22.5;
		} else if (action == 4) { // just shoot 15 degrees/fire 2
			firePower = 1;
			nextGunHeading = 30;
		} else if (action == 5) { // just shoot 30 degrees/fire 2
			firePower = 1;
			nextGunHeading = 37.5;
		} else if (action == 6) { // just shoot/fire 3
			firePower = 1;
			nextGunHeading = 45;
		} else if (action == 7) { // just shoot/fire 3
			firePower = 1;
			nextGunHeading = 52.5;
		} else if (action == 8) { // just shoot 15 degrees/fire 3
			firePower = 1;
			nextGunHeading = -7.5;
		} else if (action == 9) { // just shoot 30 degrees/fire 3
			firePower = 1;
			nextGunHeading = -15;
		} else if (action == 10) { // just shoot -15 degrees/fire 1
			firePower = 1;
			nextGunHeading = -22.5;
		} else if (action == 11) { // just shoot -30 degrees/fire 1
			firePower = 1;
			nextGunHeading = -30;
		} else if (action == 12) { // just shoot -15 degrees/fire 1
			firePower = 1;
			nextGunHeading = -37.5;
		} else if (action == 13) { // just shoot -30 degrees/fire 2
			firePower = 1;
			nextGunHeading = -45;
		} else if (action == 14) { // just shoot -30 degrees/fire 2
			firePower = 1;
			nextGunHeading = 52.5;
		}

    data[0] = firePower;
    data[1] = nextGunHeading;

    return data;
  }

}
