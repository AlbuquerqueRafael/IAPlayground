package analysis;

import robocode.control.*;
import robocode.control.events.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class BattleMaster {
	
	static StringBuilder sb = initFile();
    static int id = 0;
	
    public static void main(String[] args) {
    	
    	for(int count = 0; count < 10; count++){
    		id = count;
//        	copyBaseToData();
            RobocodeEngine.setLogMessagesEnabled(false);
            RobocodeEngine engine = new RobocodeEngine(new java.io.File("C:/Robocode")); // Run from C:/Robocode
            engine.addBattleListener(new BattleObserver());
            int numberOfRounds = 100;
            BattlefieldSpecification battlefield = new BattlefieldSpecification(800, 600); // 800x600
            RobotSpecification[] selectedRobots = engine.getLocalRepository("sample.Crazy,almostNothing.AlmostDonothing");

            BattleSpecification battleSpec = new BattleSpecification(numberOfRounds, battlefield, selectedRobots);
            engine.runBattle(battleSpec, true); // waits till the battle finishes
            engine.close();
            System.out.println(count + " Completed");
      //      copyBaseToData(count);
    	}
    	
//    	copyBaseToData();
    	saveCSV();
    	

    }
    
    public static void saveCSV() {
    	try {
            PrintWriter pw = new PrintWriter(new File("C:\\Users\\rbrun\\eclipse-workspace\\teste\\src\\analysis\\data.csv"));
            System.out.println(sb.toString());
            pw.write(sb.toString());
            pw.close();
            System.out.println("File SAVED");
    	} catch(Exception e) {
    		System.out.println("error");
    	}
    }
    
    public static StringBuilder initFile() {
        StringBuilder sb = new StringBuilder();
        sb.append("id");
        sb.append(',');
        sb.append("Survival");
        sb.append(',');
        sb.append("Vitorias");
        sb.append('\n');
        
        return sb;
    }
    
    //Necessary to restart the file to make data analysis.
    public static void copyBaseToData(int fileNumber) {
    	FileInputStream instream = null;
    	FileOutputStream outstream = null;
     
	try{
	    File outfile;
	    
	    if (fileNumber == 1) {
	    	outfile = new File("C:\\Users\\rbrun\\eclipse-workspace\\teste\\src\\analysis\\5k.txt");
	    } else if(fileNumber == 2) {
	    	outfile = new File("C:\\Users\\rbrun\\eclipse-workspace\\teste\\src\\analysis\\10k.txt");
	    } else {
	    	outfile = new File("C:\\Users\\rbrun\\eclipse-workspace\\teste\\src\\analysis\\15k.txt");
	    }
	    
	    File infile =new File("C:\\robocode\\robots\\almostNothing\\AlmostDonothing.data\\qTable.txt");
 
    	    instream = new FileInputStream(infile);
    	    outstream = new FileOutputStream(outfile);
 
    	    byte[] buffer = new byte[1024];
 
    	    int length;
    	    /*copying the contents from input stream to
	     * output stream using read and write methods
	     */
	    while ((length = instream.read(buffer)) > 0){
	    	outstream.write(buffer, 0, length);
	    }

	    //Closing the input/output file streams
	    instream.close();
	    outstream.close();

	    System.out.println("File copied successfully!!");
 
      } catch(IOException ioe){
    	  ioe.printStackTrace();
       }
    }
}

//
// Our private battle listener for handling the battle event we are interested in.
//
class BattleObserver extends BattleAdaptor {

    // Called when the battle is completed successfully with battle results
    public void onBattleCompleted(BattleCompletedEvent e) {
        System.out.println("-- Battle has completed --");

        // Print out the sorted results with the robot names
        System.out.println("Battle results:");
        for (robocode.BattleResults result : e.getSortedResults()) {
        	if (result.getTeamLeaderName().equals("almostNothing.AlmostDonothing*")) {
        		BattleMaster.sb.append(BattleMaster.id);
            	BattleMaster.sb.append(',');
            	BattleMaster.sb.append(result.getSurvival());
            	BattleMaster.sb.append(',');
            	BattleMaster.sb.append(result.getFirsts());
            	BattleMaster.sb.append('\n');
        	}
        
            System.out.println("Firsts  " + result.getTeamLeaderName() + ": " + result.getFirsts());
            System.out.println("Survival  " + result.getTeamLeaderName() + ": " + result.getSurvival());
        }
    }

    // Called when the game sends out an information message during the battle
    public void onBattleMessage(BattleMessageEvent e) {
        System.out.println("Msg> " + e.getMessage());
    }

    // Called when the game sends out an error message during the battle
    public void onBattleError(BattleErrorEvent e) {
        System.out.println("Err> " + e.getError());
    }
}