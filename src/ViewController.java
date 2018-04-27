import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.util.ArrayList;
import java.util.List;

/**
 * This class encapsulates a GUI, a component of which is a view-controller
 * delegate. This object is an observer of the Model.
 * 
 * This GUI presents views that are tailored to the current state of the Model.
 * Changes to the view are driven by state changes to the Model. View
 * initializes with 3 button labeled "Easy", "Medium" or "Hard". This action
 * launches the first state change to the Model.
 * 
 * @author Yiwei Wang
 *
 */
public class ViewController extends JFrame implements Observer, ActionListener {

	private static final long serialVersionUID = 2L; 
	private PrintStream output = System.out;
	
	private static final Color COLOR_DEFAULT = new Color(12824808);
	private static final Color COLOR_TARGET = new Color(8306936);
	private static final Color COLOR_NOISE = new Color(4173299);

	private static String TARGET;
	private static String DEFAULT;
	private static String NOISE;
	private static String ACCURACY;
	
	private Model theModel;

	private JPanel leftEmptyPanel, rightEmptyPanel,infoPanel, startButnPanel,butnPanel, finalPanel;
	private JLabel intro,prompt, finalLabel;
	private JButton easy_Butn, medium_Butn, hard_Butn;
	private List<JButton> btn;
	private List<String> btn_State;
	private List<String> noise_List;
	private List<String> default_List;

	private String MODE;
	public static final int rowButton = 10;
	public static final int 	columnButton = 10;
	
	/**
	 * Creates the view-controller delegate
	 * 
	 * @param model
	 *            the Model
	 */
	public ViewController(Model model) {
		theModel = model;
		
		/*
		 * Set up some basic aspects of the frame
		 */
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension thisScreen = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize((int) thisScreen.getWidth() / 2, (int) thisScreen.getHeight() / 2);
		this.setTitle("Yiwei Wang EECS3461 Assignment1");
		this.setLocationByPlatform(true);
		this.setLocationRelativeTo(null);

		/*
		 * Here we set up the GUI. The first panel is for the prompt and the
		 * second panel is for the button.
		 */
		leftEmptyPanel = new JPanel();
		rightEmptyPanel = new JPanel();
		leftEmptyPanel.setPreferredSize(new Dimension(50,500));
		rightEmptyPanel.setPreferredSize(new Dimension(50,500));
		
		infoPanel = new JPanel();
		infoPanel.setLayout(new BorderLayout());
		infoPanel.setPreferredSize(new Dimension(500, 200));
		
		intro = new JLabel();
		String introd = "<html><div style=\"font-family:verdana;text-align:center;color:#C3B0E8;font-size:20px\">Welcome</div><br>"
				+ "<p style=\"color:#7EC0F8;font-size:15px;\">Introduction: </p>"
				+ "In the 10x10 filed, there are button lighting up. "
				+ "There is only one Target button, that is different from the rest. "
				+ "There are many Nosie button appear with the Target button. "
				+ "Select the Target button and select it as soon as possible. "
				+ "You will be scored based on the time taken to select and the overall correctness.\n" + 
				"</html>";
		
		intro.setText(introd);
		prompt = new JLabel();
		String p = "";
		prompt.setText(p);
		infoPanel.add(leftEmptyPanel, BorderLayout.WEST);
		infoPanel.add(rightEmptyPanel, BorderLayout.EAST);
		infoPanel.add(intro, BorderLayout.CENTER);
		
		finalPanel = new JPanel();
		finalLabel = new JLabel();
		finalPanel.add(finalLabel, BorderLayout.CENTER);
		
		startButnPanel = new JPanel();
		
		easy_Butn = new JButton("EASY");
		easy_Butn.setEnabled(true);
		medium_Butn = new JButton("MEDIUM");
		medium_Butn.setEnabled(true);
		hard_Butn = new JButton("HARD");
		hard_Butn.setEnabled(true);
		startButnPanel.add(easy_Butn);
		startButnPanel.add(medium_Butn);
		startButnPanel.add(hard_Butn);
		startButnPanel.setPreferredSize(new Dimension(500, 100));
		
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(infoPanel, BorderLayout.NORTH);
		this.getContentPane().add(startButnPanel, BorderLayout.SOUTH);

		easy_Butn.addActionListener(this);
		medium_Butn.addActionListener(this);
		hard_Butn.addActionListener(this);
		
		butnPanel = new JPanel();
		
		//create rowButton*columnButton buttons in an array list
		btn = new ArrayList<JButton>();
		for(int i=0;i<(rowButton*columnButton);i++) {
			btn.add(new JButton());
		}
		//make the panel display like rowButton*columnButton grid
		butnPanel.setLayout(new GridLayout(rowButton,columnButton));
		//add the buttons into the grid panel
		for(int i = 0; i <(rowButton*columnButton); i++) {
			btn.get(i).setEnabled(true);
			butnPanel.add(btn.get(i));
		}
		//listen to those button
		for(int i = 0;i<(rowButton*columnButton);i++) {
			btn.get(i).addActionListener(this);
		}
		
		this.setVisible(true);

		// this method asks the frame layout manager to size the frame so that
		// all its contents are at or above their preferred sizes
		this.pack();
		// make this component visible (do not assume that it will be visible by
		// default)
		this.setVisible(true);
	}

	@Override
	public Dimension getPreferredSize() {
		// find the dimensions of the screen and a dimension that is derive one
		// quarter of the size
		Dimension thisScreen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension targetSize = new Dimension((int) thisScreen.getWidth() / 2, (int) thisScreen.getHeight() / 2);
		return targetSize;
	}

	@Override
	public void update(Observable o, Object arg) {		
		if (theModel.getCurrentState() == Model.SELECTED_STATE) {
			if(prompt.getText() == theModel.getEndMsg()) { //if the current page is the last page
				this.getContentPane().removeAll();
				this.repaint();
				prompt.setText(theModel.getCurrentPosition() + " Current Level: " +MODE);
				infoPanel.setPreferredSize(new Dimension(500, 50));
				infoPanel.add(prompt, BorderLayout.CENTER);
				this.getContentPane().add(infoPanel, BorderLayout.NORTH);
			}
			else {
				infoPanel.removeAll();
				infoPanel.repaint();
				infoPanel.setPreferredSize(new Dimension(500, 50));
				infoPanel.add(prompt, BorderLayout.CENTER);
				prompt.setText(theModel.getCurrentPosition() + " Current Level: " +MODE);
			}
			
			this.getContentPane().remove(startButnPanel);
			this.getContentPane().add(butnPanel, BorderLayout.CENTER);
			
			//draw the buttons 
			noise_List = new ArrayList<String>();
			default_List = new ArrayList<String>();
			
			for(int i=0;i<btn.size();i++) {
				
				btn.get(i).setOpaque(true);
				btn.get(i).setBorderPainted(true); 
				
				if(btn_State.get(i) == "DEFAULT") {
					DEFAULT = String.valueOf(i);
					btn.get(i).setBackground(COLOR_DEFAULT);
					default_List.add(DEFAULT);
				}
				else if(btn_State.get(i) == "TARGET") {
					TARGET = String.valueOf(i);
					btn.get(i).setBackground(COLOR_TARGET);
				}
				else if(btn_State.get(i) == "NOISE") {
					NOISE = String.valueOf(i);
					btn.get(i).setBackground(COLOR_NOISE);
					noise_List.add(NOISE);
				}
				
				btn.get(i).setActionCommand((String.valueOf(i)));
			}	
			
		}
	
		if(theModel.getCurrentState() == Model.END_STATE) {
			String last_page = "<html>You Finished the Game! Conguratulations! <br><br><br>Total Time: "+ theModel.totalTime()+ " Seconds "
								+"<br><br><br>Correctness with mode "+MODE+":<br>"+ACCURACY+"<br><br><br><br><br><br><br><br><div style=\"color:#7EC0F8;font-size:20px;\">"
										+ "Restart the Game:</div> <html>";
			prompt.setText(theModel.getEndMsg());
	
			this.getContentPane().remove(butnPanel);
			this.getContentPane().repaint();
			finalLabel.setText(last_page);
			finalPanel.setOpaque(true);
			
			this.getContentPane().add(finalPanel, BorderLayout.CENTER);
			this.getContentPane().add(startButnPanel, BorderLayout.SOUTH);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	if (e.getSource().equals(easy_Butn)) { //easy mode is selected
		if(theModel.getCurrentState()== Model.END_STATE ) {
			theModel.restartInitial();
		}
		MODE = e.getActionCommand();
		btn_State = theModel.buttonState(MODE);
		theModel.setState(Model.SELECTED_STATE);
		output.print("User select Mode: ");
		output.println(MODE);
		ACCURACY = theModel.correctness_Initial().toString();
		output.print("Correctness is: ");
		output.println(ACCURACY);
		theModel.recordStartTimeStamp();
		} 
	else if(e.getSource().equals(medium_Butn)) {	//medium mode
		if(theModel.getCurrentState() == Model.END_STATE ) {
			theModel.restartInitial();
		}
		MODE = e.getActionCommand();
		btn_State = theModel.buttonState(MODE);
		theModel.setState(Model.SELECTED_STATE);
		output.print("User select Mode: ");
		output.println(MODE);
		ACCURACY = theModel.correctness_Initial().toString();
		output.print("Correctness is: ");
		output.println(ACCURACY);
		theModel.recordStartTimeStamp();
	}
	else if(e.getSource().equals(hard_Butn)) { //hard mode
		if(theModel.getCurrentState()== Model.END_STATE ) {
			theModel.restartInitial();
		}
		MODE = e.getActionCommand();
		btn_State = theModel.buttonState(MODE);
		theModel.setState(Model.SELECTED_STATE);
		output.print("User select Mode: ");
		output.println(MODE);
		ACCURACY= theModel.correctness_Initial().toString();
		output.print("Correctness is: ");
		output.println(ACCURACY);
		theModel.recordStartTimeStamp();
	}
	//if the user hit the target button
	if(e.getActionCommand().equals(TARGET)) {
		theModel.correctness(btn_State.get(Integer.valueOf(TARGET)));
		btn_State = theModel.buttonState(MODE);
		
		ACCURACY = theModel.correctness_Initial.toString();
		
		output.print("Correctness is: ");
		output.println(ACCURACY);
		if (theModel.getCurrentState() == Model.SELECTED_STATE) {
			if (theModel.ismsgRemaining()) {
				theModel.setPromptToNext();
				
				theModel.setState(Model.SELECTED_STATE);
			} else {
				theModel.recordStopTimeStamp();
				theModel.setState(Model.END_STATE);
				theModel.totalTime();
			}
		} 
	}
	//hit the noise button
	else if (noise_List.contains(e.getActionCommand()))
	{
		int i=0;
		if(noise_List.contains(e.getActionCommand())) {//action is noise
			while(! e.getActionCommand().equals(noise_List.get(i))) {
				i++;
			}
			theModel.correctness(btn_State.get(Integer.valueOf(noise_List.get(i))));
			btn_State = theModel.buttonState(MODE);
			ACCURACY = theModel.correctness_Initial.toString();
			output.print("Correctness is: ");
			output.println(ACCURACY);
			if (theModel.getCurrentState() == Model.SELECTED_STATE) {
				if (theModel.ismsgRemaining()) {
					theModel.setPromptToNext();
					theModel.setState(Model.SELECTED_STATE);
				} else {
					theModel.recordStopTimeStamp();
					theModel.setState(Model.END_STATE);
					theModel.totalTime();
				}
			} 
		}
	}
		//default
		else if(default_List.contains(e.getActionCommand())) {
			int j=0;
			while(!e.getActionCommand().equals(default_List.get(j))) {
				j++;
			}
			theModel.correctness(btn_State.get(Integer.valueOf(DEFAULT)));
			btn_State = theModel.buttonState(MODE);
			ACCURACY = theModel.correctness_Initial.toString();
			output.print("Correctness is: ");
			output.println(ACCURACY);
			if (theModel.getCurrentState() == Model.SELECTED_STATE) {
				if (theModel.ismsgRemaining()) {
					theModel.setPromptToNext();
					theModel.setState(Model.SELECTED_STATE);
				} else {
					theModel.recordStopTimeStamp();
					theModel.setState(Model.END_STATE);
					theModel.totalTime();
				}
			} 
		}
	}
}
