import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;


/**
 * 
 * This class encapsulates the data model of a very simple data collection
 * protocol. The model has three possible states: INIT_STATE (has an associated
 * sting containing initial instructions), SELECTED_STATE (has an associated
 * ordered set of prompts), and END_STATE (has an associated final message).
 * This class provides services for iterating over the ordered set of prompts
 * and generate the random "Target", "Noise" and "Default" buttons.
 * 
 * This Model is an Observable.
 * 
 * @author Yiwei Wang
 *
 */

public class Model extends Observable {

	public static final int INIT_STATE = 0;
	public static final int SELECTED_STATE = 1;
	public static final int END_STATE = 2;
	public HashMap<String, Integer> correctness_Initial;
	
	private static final int EASY = 6;
	private static final int MEDIUM = 12;
	private static final int HARD = 24;
	
	
	private static final int BUTTON_SIZE = ViewController.rowButton * ViewController.columnButton;
	private static final int Max_Indx_Button = ViewController.rowButton * ViewController.columnButton -1;
	private static final int Min_Indx_Button = 0;

	private String[] msg = new String[10];
	private String endMsg = "***************************************************Task Complete****************************************************";	
	private int currMsgIdx;
	private int currentState;

	private double startTimestamp;
	private double stopTimestamp;
	private double totaltime;

	/**
	 * Create an instance of this model. The iterator over the msg has not
	 * been initialized.
	 */
	public Model() {
		currMsgIdx = 1;
		this.setState(Model.INIT_STATE);
	}
	/**
	 * 
	 * @param o
	 * 
	 * set change, and notify all observers with given parameter
	 */
	private void modelNotify(Object o) {
	
		setChanged();
		notifyObservers(o);
	}

	/**
	 * Advances the iterator to the next page. Should ensure first that the
	 * iterator has not reached past the end of the set of msg.
	 */
	public void setPromptToNext() {
		currMsgIdx++;
		modelNotify(currMsgIdx);
	}

	/**
	 * Check if the iterator reach the end of msg
	 * @return whether the iterator has reached past the end of the set of msg
	 *         
	 */
	public boolean ismsgRemaining() {
		return currMsgIdx < msg.length;
	}

	/**
	 * @param modelState
	 *            the state for the model. Passed parameter must be one of the
	 *            class fields.
	 * 
	 *            Mutate the current state of this model.
	 */
	public void setState(int modelState) {
		currentState = modelState;
		modelNotify(currentState);
	}

	/**
	 * @return the current state of this model (value will be one of the class
	 *         fields).
	 */
	public int getCurrentState() {
		return currentState;
	}

	/**
	 * @return the initialization message associated with the INIT_STATE of this
	 *         model.
	 */
	public String getCurrentPosition() {
		return "You are at Page: " + (currMsgIdx) + "/" + (msg.length);
	}
	/**
	 * @return the finish message associated with the END_STATE of this model.
	 */
	public String getEndMsg() {
		return endMsg;
	}

	/**
	 * Tell this model to stop recording time.
	 */
	public void recordStopTimeStamp() {
		stopTimestamp = System.currentTimeMillis();	
	}

	/**
	 * Tell this model to start recording time.
	 */
	public void recordStartTimeStamp() {
		startTimestamp = System.currentTimeMillis();
	}
	
	/**
	 * Randomly generated the target button and noise button for grid filed
	 * @param mode
	 * 			current mode user selected, easy, medium or hard
	 * @return an ArrayList with an random assigned state (default, noise or target sate)of all the buttons
	 * 
	 */
	public ArrayList<String> buttonState(String mode){
		int n = 0;
		if(mode.equals("EASY")) {
			n = EASY;
		}
		else if(mode.equals("MEDIUM")) {
			n = MEDIUM;
		}
		else if(mode.equals("HARD")) {
			n = HARD;
		}
		
		List<String> button_color= new ArrayList<String>();
		for(int i=0; i<BUTTON_SIZE;i++) {
			button_color.add("DEFAULT");
		}
		int[] result; 
		    result = new int[n];  
		    int count = 0;  
		    while(count < n) {  
		        int num = (int) (Math.random() * (Max_Indx_Button - Min_Indx_Button)) + Min_Indx_Button;  
		        boolean flag = true;  
		        for (int j = 0; j < n; j++) {  
		            if(num == result[j]){  
		                flag = false;  
		                break;  
		            }  
		        }  
		        if(flag){  
		            result[count] = num;  
		            count++;  
		        }  
		    }
		    	button_color.set(result[0], "TARGET");
		    	for(int i = 1; i < result.length;i++) {
		    		button_color.set(result[i], "NOISE");
		    	}
		    return (ArrayList<String>) button_color;   
	}
	
	/**
	 * Initial the Correctness
	 * @return the HashMap with initial ("Default", 0),("Noise", 0),("Target", 0)
	 */
	public HashMap<String, Integer> correctness_Initial(){
		correctness_Initial = new HashMap<String, Integer>();
		correctness_Initial.put("Default", 0);
		correctness_Initial.put("Noise", 0);
		correctness_Initial.put("Target", 0);
		
		return correctness_Initial;
	}
	/**
	 * Increase the value with corresponding key
	 * @param button_type
	 * @return the HashMap with the current s
	 */
	public HashMap<String, Integer> correctness(String button_type){
		if(button_type.equals("TARGET")) {
			correctness_Initial.put("Target", correctness_Initial.get("Target") + 1);
		}
		else if(button_type.equals("NOISE")){
			correctness_Initial.put("Noise", correctness_Initial.get("Noise") + 1);
		}
		else if(button_type.equals("DEFAULT")){
			correctness_Initial.put("Default", correctness_Initial.get("Default") + 1);
		}
		return correctness_Initial;
	}
	/**
	 * return the total time 
	 * @return the total time user takes in this game
	 */
	public String totalTime() {
		DecimalFormat df = new DecimalFormat("#.##");
		totaltime = (stopTimestamp - startTimestamp) / 1000;
		return df.format(totaltime);
	}
	
	/**
	 * reset all the Model state and index
	 * and notify all the observers with parameter
	 */
	public void restartInitial() {
		currMsgIdx = 1;
		this.setState(Model.SELECTED_STATE);
		modelNotify(Model.SELECTED_STATE);
	}

}
