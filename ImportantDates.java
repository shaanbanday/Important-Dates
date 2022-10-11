/*
Java™ Project: ICS4U
Package: date
Class: ImportantDates
Programmer: Shaan Banday.

Date Created: Monday, 21st March, 2022.
Date Completed: Friday, 25th March, 2022. 

Description: The following program/class does not have a main method, but it constructs all the graphical elements for the that the ImportantDatesMain
class calls to create the GUI. The constructor itself (this first method) is spilt into many constructors since this class is very big and has many
variables it needs to initialise. This class also implements an ActionListener method which is called whenever a button is pressed within the program.
To draw the date as a number and in a written format, the class interacts with the paint method, which makes it easier to update frequently, compared
to just using JLabels which are always the same text throughout the program's run. Finally, this class takes care of reading the file of dates, which
it only does once before storing all the values in the arrays. This class also interacts with the DateClass to create the actual date objects.

As for the function of the class, it creates a visual graphic which takes a day as a number and a month as a string and checks the file if anything
important happened on that day. It there's a match, then the program will display the date, along with a description. If the date entered is not in 
the file, than the program displays an appropriate message. At the bottom, after a date is displayed or not, there are buttons which the user can use 
to change the format of the date (switch between MMDDYY, YYMMDD, DDMMYY), and the format of the era and whether it is a secular format or not (CE and
BCE vs AD and BC). Lastly, at the bottom, the user can press a button to go back and check another date.
*/

package date; //Launch the class from this package named "date"

//Import input and output elements
import java.io.*; //Import the input and output class, which is necessary for accessing the file
import java.io.IOException; //Import the class to handle input output exceptions

//Import graphical elements
import java.awt.*; //Import the package with all the graphical objects
import javax.swing.*; //Import the package with more graphical objects
import java.awt.event.*; //Import the Action Listener Class
import javax.swing.border.EtchedBorder; //Import the border class
import javax.swing.text.*; //Import the text editor class

public class ImportantDates extends JFrame implements ActionListener //The name of the class that is being referencing by "ImportantDatesMain" class
{ //This class implements an ActionListener to respond to button clicks
	/**
	 * 
	 */
	private static final long serialVersionUID = -6825735090640950609L;
	
	//Declare all graphical objects
	private JPanel datesPanel; //Panel to hold everything
	private JButton checkDateButton, changeToMMDDYY, changeToYYMMDD, changeToDDMMYY, changeEra, goBack; //The various buttons do different things
	private JLabel title, subheading1, subheading2, dayPrompt, monthPrompt; //All the JLabels which display text
	private JTextField dayTextBox, monthTextBox; //The text boxes to take the user input
	private JTextPane dateMessage; //The text pane to display the message. These are more versatile than JLabels
	private StyledDocument dateMessageTextSyle; //The style for the TextPane to centre the text always, no matter the size
	private SimpleAttributeSet centerAlignment; //The aforementioned centre alignment
	private Rectangle dateAsNumSize, dateAsWrittenSize; //Rectangles to hold the dates at the top, to be drawn by the paint method, but always centred
	private Image cursorImage; //Image of the cursor
	private Cursor dateCursor; //Cursor inside the JFrame
	
	//Declare all fonts
	private Font titleFont, subheadingFont, textBoxFont, checkButtonFont, messageFont, otherButtonFont; //All of the fonts in the program
	
	//Declare all Colours
	private Color darkEmerald, lightEmerald, offWhite; //These three colours are custom colours which enhance the overall aesthetic of the GUI
	
	//Declare all arrays to hold date data
	private int [] yearBank; //Array for the years. It is an integer because technically speaking, in the future, years could be infinite
	private byte [] monthBank, dayBank; //Array for the months and days of the important date. They are bytes because both can't go over 12 and 31 
	private byte monthInput, dayInput; //Input for the day and the month to take from the text boxes
	private String [] messageBank; //Array of strings to hold the corresponding methods for the important dates
	private DateClass [] dates; //Array of DateClass objects to hold the dates
	
	//Declare other variables
	private int arraySize; //The size of all arrays, which is the first line of the text file
	private int trackIndex; //A way to track where in the array the index is when a match is found between a user input and one of the dates
	private boolean mainMenu; //Boolean for the state of the program. True means on the main menu, false means it is showing a date
	
	//Declare all constants
	private static final short F_WIDTH = 1150, F_HEIGHT = 700; //Width and Height of the JFrame
	private static final byte GAP = 30; //Gap between date input and month input graphical elements
	
	public ImportantDates() //Constructor which initialises the object, and displays all the proper graphical components.
	{		
		super("Important Dates"); //Name of JFrame/window
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Terminate the program, and close the window if the close button is hit, 
        this.setSize(F_WIDTH, F_HEIGHT); //Set the size of the window (JFrame) in pixels to the constants initialised outside the constructor
        
        //Initialise JPanel
        this.datesPanel = (JPanel)this.getContentPane();  //Create a JPanel to organise contents in the JFrame/Window  
        this.datesPanel.setLayout(null); //Assign no layout (null) to the JPanel
        
        //Initialise Cursor elements
        this.cursorImage = Toolkit.getDefaultToolkit().getImage("cursor.png"); //Set Image for the cursor
        this.dateCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage, new Point (0,0), "dateCursor"); //Set the cursor
        this.datesPanel.setCursor(this.dateCursor); //Add the cursor to the JPanel
        constructColours(); //Construct the colours
        this.datesPanel.setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, this.darkEmerald));
        this.datesPanel.setBackground(this.offWhite); //Set the colour of the background as black.
        
        //Construct the rest of the graphical elements 
        constructRectangles(); //Construct the rectangles
        constructFonts(); //Construct the fonts
        constructLabels(); //Construct the JLabel
        constructPrompts(); //Construct the fonts
        constructButtons(); //Construct the buttons
        
        this.setResizable(false); //Window is unable to be resized.
        this.setVisible(true); //Everything in the JFrame is visible, unless otherwise specified  
        
        readFile(); //Execute the readFile method, and set the values to the arrays				
		addDates(); //Add the dates to the dates array
		
		this.mainMenu = true; //Set the state of the program to true, which means the main menu is displayed
		
		this.repaint(); //Invoke paint method, and update the screen
	}
	
	//If any method below is private, it is because other classes do not need access to it
	//If any method below is public, it is because it's visibility cannot be reduced because it is an inherited method (e.g. ActionListener and Paint)
	
	private void constructColours() //Private method which is an extension of the constructor to set the colours
	{
		this.darkEmerald = new Color (18, 118, 120); //Colour of some of the text and the buttons
		this.lightEmerald = new Color (48, 181, 164); //Colour of other text
		this.offWhite = new Color (180, 217, 210); //Colour of the background
	}
	
	private void constructRectangles() //Private method which is an extension of the constructor to set the rectangles
	{
		this.dateAsNumSize = new Rectangle (0, 40, 1130, 50); //Rectangle to hold the date
        this.dateAsWrittenSize = new Rectangle (0, 100, 1130, 50); //Rectangle to hold the date as written
	}
	
	private void constructFonts() //Private method which is an extension of the constructor to set the fonts
	{
		this.titleFont = new Font("Century Schoolbook", Font.BOLD, 100); //Font for the programs title
		this.subheadingFont = new Font("Century Schoolbook", Font.BOLD, 40); //Font for the sub-heading text on the main menu
		this.textBoxFont = new Font("Century Schoolbook", Font.BOLD, 70); //Font for the text boxes and the prompts for the text boxes
		this.checkButtonFont = new Font("Century Schoolbook", Font.ITALIC, 60); //Font for the button to check the date
		this.messageFont = new Font("Century Schoolbook", Font.BOLD, 30); //Font for the message associated with a date
		this.otherButtonFont = new Font("Century Schoolbook", Font.ITALIC, 30); //Font for the other buttons
	}
	
	private void constructLabels() //Private method which is an extension of the constructor to set the JLabels
	{
		//Title Label
		this.title = new JLabel("Important Dates"); //Set string for the title label
		this.title.setHorizontalAlignment(SwingConstants.CENTER); //Make the title centre aligned
		this.title.setBounds(new Rectangle(0, 10, 1130, 120)); //Set the bounds of the title within the JFrame
		this.title.setForeground(this.lightEmerald); //Set the colour of the title to light emerald
		this.title.setFont(this.titleFont); //Set the font style of the title
		this.datesPanel.add(this.title); //Add the title to the JPanel
		
		//First Line of Sub-heading Label
		this.subheading1 = new JLabel("Enter a Month and a Day to See if Anything"); //Set string for the first line of the sub-heading
		this.subheading1.setHorizontalAlignment(SwingConstants.CENTER); //Make the first line centre aligned
		this.subheading1.setBounds(new Rectangle(0, 150, 1130, 50)); //Set the bounds of the first line within the JFrame
		this.subheading1.setForeground(this.darkEmerald); //Set the colour of the first line to dark emerald
		this.subheading1.setFont(this.subheadingFont);  //Set the font of the first line
		this.datesPanel.add(this.subheading1); //Add the first line to the JPanel
		
		//Second Line of Sub-heading Label
		this.subheading2 = new JLabel("Important Happened on that Date"); //Set string for the second line of the sub-heading
		this.subheading2.setHorizontalAlignment(SwingConstants.CENTER); //Make the second line centre aligned
		this.subheading2.setBounds(new Rectangle(this.subheading1.getX(), this.subheading1.getY() + 50, this.subheading1.getWidth(), 
				this.subheading1.getHeight())); //Set the bounds of the second line within the JFrame, which is relative to the first line
		this.subheading2.setForeground(this.subheading1.getForeground()); //Set the colour of the second line to the same as the first line
		this.subheading2.setFont(this.subheading1.getFont()); //Set the font of the second line to the same as the first line
		this.datesPanel.add(this.subheading2); //Add the second line to the panel
	}
	
	private void constructPrompts() //Private method which is an extension of the constructor to set the prompt graphical elements
	{
		//Day Prompt Label
		this.dayPrompt = new JLabel("Day:"); //Set the string for the day prompt
		this.dayPrompt.setHorizontalAlignment(SwingConstants.CENTER); //Make it centre aligned
		this.dayPrompt.setBounds(new Rectangle(0, 300, 193, 100)); //Set the bounds of the day prompt within the JFrame
		this.dayPrompt.setForeground(this.lightEmerald); //Set the colour to light emerald
		this.dayPrompt.setFont(this.textBoxFont); //Set the font to the text box font
		this.datesPanel.add(this.dayPrompt); //Add the JLabel to the panel
		
		//Day Text Box
		this.dayTextBox = new JTextField(); //Create a new text field to take in the input day (as a number)
		this.dayTextBox.setHorizontalAlignment(JTextField.CENTER); //Make it centre aligned
		this.dayTextBox.setBounds(new Rectangle(this.dayPrompt.getX() + this.dayPrompt.getWidth(), this.dayPrompt.getY(), 
				90, this.dayPrompt.getHeight())); //Set the bounds of the day text box within the JFrame
		this.dayTextBox.setForeground(this.darkEmerald); //Set the text colour to dark emerald
		this.dayTextBox.setFont(this.textBoxFont); //Set the font to the text box font
		this.dayTextBox.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, this.lightEmerald)); //Add a border to the TextField
		this.datesPanel.add(this.dayTextBox); //Add the text box to the JPanel
		
		//Month Prompt Label
		this.monthPrompt = new JLabel("Month:"); //Set the string for the month prompt
		this.monthPrompt.setHorizontalAlignment(SwingConstants.CENTER); //Make it centre aligned
		this.monthPrompt.setBounds(new Rectangle(this.dayTextBox.getX() + this.dayTextBox.getWidth() + GAP, this.dayTextBox.getY(), 
				300, this.dayTextBox.getHeight())); //Set the bounds of the month prompt within the JFrame
		this.monthPrompt.setForeground(this.lightEmerald); //Set the colour to light emerald
		this.monthPrompt.setFont(this.textBoxFont); //Set the font to the text box font
		this.datesPanel.add(this.monthPrompt); //Add the JLabel to the panel
		
		//Month Text Box
		this.monthTextBox = new JTextField(); //Create a new text field to take in the input month (as a string)
		this.monthTextBox.setHorizontalAlignment(JTextField.CENTER); //Make it centre aligned
		this.monthTextBox.setBounds(new Rectangle(this.monthPrompt.getX() + this.monthPrompt.getWidth(), this.monthPrompt.getY(), 
				500, this.monthPrompt.getHeight())); //Set the bounds of the day text box within the JFrame
		this.monthTextBox.setForeground(this.darkEmerald); //Set the text colour to dark emerald
		this.monthTextBox.setFont(this.textBoxFont); //Set the font to the text box font
		this.monthTextBox.setBorder(this.dayTextBox.getBorder()); //Add a border to the TextField
		this.datesPanel.add(this.monthTextBox); //Add the text box to the JPanel
	}
	
	private void constructButtons() //Private method which is an extension of the constructor to set the buttons
	{		
		//Date Checker Button
		this.checkDateButton = new JButton("Check The Date"); //Set the name of the button to check the dates
        this.checkDateButton.setBounds(new Rectangle (325, 450, 500, 100)); //Set the bounds of the button within the JFrame
        this.checkDateButton.addActionListener(this); //Add an action listener to respond to a button click
        this.checkDateButton.setFocusable(false); //Set the button to not be focusable with a tab press
        this.checkDateButton.setBackground(this.darkEmerald); //Set the background of the button to darkEmerald
        this.checkDateButton.setForeground(Color.BLACK); //Set the text colour to black to contrast with the emerald
        this.checkDateButton.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.BLACK, Color.BLACK)); //Add a border to the button
        this.checkDateButton.setFont(this.checkButtonFont); //Set the font of the button to a specific one
        this.datesPanel.add(this.checkDateButton); //Add the button to the panel
        
        //Changing to MMDDYY Button
		this.changeToMMDDYY = new JButton("Change to MMDDYY"); //Set the name of the button to change the date format to MMDDYY
		this.changeToMMDDYY.setBounds(new Rectangle (43, 450, 325, 80)); //Set the bounds of the button within the JFrame
        this.changeToMMDDYY.addActionListener(this); //Add an action listener to respond to a button click
        this.changeToMMDDYY.setFocusable(false); //Set the button to not be focusable with a tab press
        this.changeToMMDDYY.setBackground(this.darkEmerald); //Set the background of the button to darkEmerald
        this.changeToMMDDYY.setForeground(Color.BLACK); //Set the text colour to black to contrast with the emerald
        this.changeToMMDDYY.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.BLACK, Color.BLACK)); //Add a border to the button
        this.changeToMMDDYY.setFont(this.otherButtonFont); //Set the font of the button to a specific one
        this.changeToMMDDYY.setEnabled(false); //For now, disable the button
        this.changeToMMDDYY.setVisible(false); //For now, make the button invisible
        this.datesPanel.add(this.changeToMMDDYY); //Add the button to the panel
        
        //Changing to YYMMDD Button
        this.changeToYYMMDD = new JButton("Change to YYMMDD"); //Set the name of the button to change the date format to YYMMDD
		this.changeToYYMMDD.setBounds(new Rectangle (406, 450, 325, 80)); //Set the bounds of the button within the JFrame
        this.changeToYYMMDD.addActionListener(this);  //Add an action listener to respond to a button click
        this.changeToYYMMDD.setFocusable(false); //Set the button to not be focusable with a tab press
        this.changeToYYMMDD.setBackground(this.darkEmerald); //Set the background of the button to darkEmerald
        this.changeToYYMMDD.setForeground(Color.BLACK); //Set the text colour to black to contrast with the emerald
        this.changeToYYMMDD.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.BLACK, Color.BLACK)); //Add a border to the button
        this.changeToYYMMDD.setFont(this.otherButtonFont);//Set the font of the button to a specific one
        this.changeToYYMMDD.setEnabled(false); //For now, disable the button
        this.changeToYYMMDD.setVisible(false); //For now, make the button invisible
        this.datesPanel.add(this.changeToYYMMDD); //Add the button to the panel
        
        //Changing to DDMMYY Button
        this.changeToDDMMYY = new JButton("Change to DDMMYY"); //Set the name of the button to change the date format to DDMMYY
		this.changeToDDMMYY.setBounds(new Rectangle (769, 450, 325, 80)); //Set the bounds of the button within the JFrame
        this.changeToDDMMYY.addActionListener(this);  //Add an action listener to respond to a button click
        this.changeToDDMMYY.setFocusable(false); //Set the button to not be focusable with a tab press
        this.changeToDDMMYY.setBackground(this.darkEmerald); //Set the background of the button to darkEmerald
        this.changeToDDMMYY.setForeground(Color.BLACK); //Set the text colour to black to contrast with the emerald
        this.changeToDDMMYY.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.BLACK, Color.BLACK)); //Add a border to the button
        this.changeToDDMMYY.setFont(this.otherButtonFont);//Set the font of the button to a specific one
        this.changeToDDMMYY.setEnabled(false); //For now, disable the button
        this.changeToDDMMYY.setVisible(false); //For now, make the button invisible
        this.datesPanel.add(this.changeToDDMMYY); //Add the button to the panel
        
        //Changing the Written Date Era Button
        this.changeEra = new JButton("Change Era Format"); //Set the name of the button to change the written date era format (BC and AD vs CE and BCE)
		this.changeEra.setBounds(new Rectangle (43, 550, 503, 80)); //Set the bounds of the button within the JFrame
        this.changeEra.addActionListener(this); //Add an action listener to respond to a button click
        this.changeEra.setFocusable(false); //After the button is pressed, remove the focus
        this.changeEra.setBackground(this.darkEmerald); //Set the background of the button to darkEmerald
        this.changeEra.setForeground(Color.BLACK); //Set the text colour to black to contrast with the emerald
        this.changeEra.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.BLACK, Color.BLACK)); //Add a border to the button
        this.changeEra.setFont(this.otherButtonFont); //Set the font of the button to a specific one
        this.changeEra.setEnabled(false); //For now, disable the button
        this.changeEra.setVisible(false); //For now, make the button invisible
        this.datesPanel.add(this.changeEra); //Add the button to the panel
        
        //Checking Another Date Button
        this.goBack = new JButton("Check Another Date"); //Set the name of the button to go back to the main menu and check another date
		this.goBack.setBounds(new Rectangle (590, 550, 504, 80)); //Set the bounds of the button within the JFrame
		this.goBack.addActionListener(this); //Add an action listener to respond to a button click
        this.goBack.setFocusable(false); //After the button is pressed, remove the focus
        this.goBack.setBackground(this.darkEmerald); //Set the background of the button to darkEmerald
        this.goBack.setForeground(Color.BLACK); //Set the text colour to black to contrast with the emerald
        this.goBack.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED, Color.BLACK, Color.BLACK)); //Add a border to the button
        this.goBack.setFont(this.otherButtonFont); //Set the font of the button to a specific one
        this.goBack.setEnabled(false); //For now, disable the button
        this.goBack.setVisible(false); //For now, make the button invisible
        this.datesPanel.add(this.goBack); //Add the button to the panel
	}
	
	public void actionPerformed(ActionEvent e) //Create void ActionListener method, to be invoked when a button is pressed
	{
		//Declare all Variables
		String buttonName = e.getActionCommand(); //String variable that replicates the button name. 
		
		//Decisions
		switch (buttonName) //Switch statement based on what the button name is
		{
			case "Check The Date": //If Check the Date button is pressed
			
				//Try and catch statement
				try //Try to parse the input day and input month to bytes
				{
					this.dayInput = Byte.parseByte(this.dayTextBox.getText().trim()); //Parse the input to a byte
					this.monthInput = parseMonthToByte(this.monthTextBox.getText()); //Invoke a specific parsing method for the month
					checkDates(); //Check the dates to see if there is a match
				}
			
				catch (NumberFormatException ex) //Catch a number format exception if parsing cannot occur
				{
					this.trackIndex = this.arraySize; //Set index to array size, which means loop to check date does not run and error is displayed
					/*Throughout this entire program, the way it keeps track if a date is not found is it is set to the size of the array.
				  	  In the messages array, it is one size bigger than the rest, and the last index has the error message.
				  	  So when a date is not found, the index in that array = array size, and error is displayed instead of the message of a date*/
				}
			
				toSecondScreen(); //Regardless of whether the date is parsed, and after it is checked, move to the second screen
			
				break; 
			
			case "Change to MMDDYY": //If the user wants to change the format of the date and this button is pressed
			
				//Nested Decisions
				if (this.trackIndex != this.arraySize) //If the date is valid (READ ABOVE FOR EXPLANATION)
				{
					this.dates[this.trackIndex].switchFormat(DateClass.MMDDYY); //Call the date class and switch the format
				}
				
				else {} //Otherwise, do nothing if there is no date being displayed, because it can't be changed anyways
				
				break;
			
			case "Change to YYMMDD": //If the user wants to change the format of the date and this button is pressed
				
				//Nested Decisions
				if (this.trackIndex != this.arraySize) //If the date is valid (READ ABOVE FOR EXPLANATION)
				{
					this.dates[this.trackIndex].switchFormat(DateClass.YYMMDD); //Call the date class and switch the format
				}
			
				else {} //Otherwise, do nothing if there is no date being displayed, because it can't be changed anyways
				
				break;
			
			case "Change to DDMMYY": //If the user wants to change the format of the date and this button is pressed
				
				//Nested Decisions
				if (this.trackIndex != this.arraySize) //If the date is valid (READ ABOVE FOR EXPLANATION)
				{
					this.dates[this.trackIndex].switchFormat(DateClass.DDMMYY); //Call the date class and switch the format
				}
			
				else {} //Otherwise, do nothing if there is no date being displayed, because it can't be changed anyways
		
				break;
			
			case "Change Era Format": //If the user wants to change the format of the era of the written date and this button is pressed
				
				//Nested Decisions
				if (this.trackIndex != this.arraySize) //If the date is valid (READ ABOVE FOR EXPLANATION)
				{
					this.dates[this.trackIndex].switchEraFormat(!(this.dates
					[this.trackIndex].getEraFormat())); //Call the getEraFormat() method and switch to the opposite of whatever it currently is
				}
			
				else {} //Otherwise, do nothing if there is no date being displayed, because it can't be changed anyways
			
				break;
			
			case "Check Another Date": //If the user wants to go back and check another date
				toFirstScreen(); //Call the toFirstScreen method, which takes care of disabling and enabling everything
				
				break;
			
			default: //If anything else is somehow pressed
				//Do nothing
				break;
		}
		repaint(); //After a button is pressed, update the screen and repaint 
	}
	
	private byte parseMonthToByte(String month) //Private method which parses the month as a string to a byte and returns it to the action
	{
		//Declare all variables
		byte result; //Byte that will be returned
		
		//Decisions
		switch (month.toLowerCase().trim()) //Switch statement which takes the month as all lower case and with extra space trimmed off
		{
			case "january": //If the month that the user inputs is January
				result = 1; //Set the return value to 1
				break;
				
			case "february": //If the month that the user inputs is February
				result = 2; //Set the return value to 2
				break;
			
			case "march": //If the month that the user inputs is March
				result = 3; //Set the return value to 3
				break;
			
			case "april": //If the month that the user inputs is April
				result = 4; //Set the return value to 4
				break;
			
			case "may": //If the month that the user inputs is May
				result = 5; //Set the return value to 5
				break;
			
			case "june": //If the month that the user inputs is June
				result = 6; //Set the return value to 6
				break;
			
			case "july": //If the month that the user inputs is July
				result = 7; //Set the return value to 7
				break;
				
			case "august": //If the month that the user inputs is August
				result = 8; //Set the return value to 8
				break;
				
			case "september": //If the month that the user inputs is September
				result = 9; //Set the return value to 9
				break;
			
			case "october": //If the month that the user inputs is October
				result = 10; //Set the return value to 10
				break;
			
			case "november": //If the month that the user inputs is November
				result = 11; //Set the return value to 11
				break;
			
			case "december": //If the month that the user inputs is December
				result = 12; //Set the return value to 12
				break;
			
			default: //If the month that the user inputs is anything else other than a valid month
				result = 0; //Set the return value to 0, which is not a valid month
				break;
		}
		
		return result; //Return the result back to the action listener
	}
	
	private void checkDates() //Private method which checks the dates by comparing the month and day input with any of the combinations in the array
	{
		this.trackIndex = this.arraySize; //Set the index to the size of the array by default
		
		//Loops
		for (int i = 0; i < this.arraySize; i++) //Start the loop at 0 and iterate through every element of the array
		{
			if ((this.monthInput == this.monthBank[i]) && (this.dayInput == this.dayBank[i])) //If both day and month match an index in the arrays
			{
				this.trackIndex = i; //If a match is found, keep track of the index where it is found.
				//All of the arrays for the important months, days, and years are the same size, and their indices match for the important dates
				i = this.arraySize; //When a match is found, break the loop
			}
			else {} //Otherwise, if a match is not found, continue with the loop
		}
		
		/*By the end, if no match is found, then trackIndex is still arraySize. In the message array, it is one index bigger than the rest. 
		  The last index is an error message saying no date was found, and if this loop does not find anything, than that message is displayed*/
	}
	
	private void toFirstScreen() //Private method which is an extension of the action listener. It handles all the logic to go to the first screen
	{
		this.mainMenu = true; //Set the state of the program to true, which means the main menu is visible
		
		changeMenus(); //Handle all the logic to change the menus
		this.dateMessage.setVisible(false); //Set the message as invisible
		this.dayTextBox.setText(null); //Clear the day text box
		this.monthTextBox.setText(null); //Clear the month text box
	}
	
	private void toSecondScreen() //Private method which is an extension of the action listener. It handles all the logic to go to the second screen
	{	
		this.mainMenu = false; //Set the state of the program as false, which means the date or error message is visible
		
		changeMenus(); //Handle all the logic to change the menus
		
		//Date Message
		this.dateMessage = new JTextPane(); //Declare a new text pane to hold the message
		this.dateMessage.setText(this.messageBank[this.trackIndex]); //Set the text pane to whatever index of the message bank
		/*If there was a match when finding the date, then the index will just be that, and the message will be displayed correspondingly.
		  If there was no match when finding the date, then the last index is set to the text pane, which is the error message */
		
		this.dateMessageTextSyle = this.dateMessage.getStyledDocument(); //Declare the text style for the text pane
		this.centerAlignment = new SimpleAttributeSet(); //Declare the attribute for the text pane
		StyleConstants.setAlignment(centerAlignment, StyleConstants.ALIGN_CENTER); //Set the attribute to a centre alignment
		this.dateMessageTextSyle.setParagraphAttributes(0, this.dateMessageTextSyle.getLength(), 
				this.centerAlignment, false); //Apply this attribute to the text style for the text pane
		this.dateMessage.setBounds(new Rectangle(11, 120, 1112, 320)); //Set the bounds for the message within the JFrame
		this.dateMessage.setForeground(this.darkEmerald); //Set the colour of the message to dark emerald
		this.dateMessage.setBackground(this.datesPanel.getBackground()); //Set it's background to match whatever the JPanel has
		this.dateMessage.setHighlighter(null); //Do not allow the user to highlight the text
		this.dateMessage.setEditable(false); //Do not allow the user to edit the text
		this.dateMessage.setFont(this.messageFont); //Set the font of the message to be displayed as a custom one
		this.datesPanel.add(this.dateMessage); //Add the message text to the JPanel
	}
	
	private void changeMenus() //Private method which handles all the logic to switch screens
	{
		/*Set the visibility of the main menu graphical elements to whatever the state of mainMenu is. If mainMenu is true, then all of these will 
		  match the value and be set to visible. If mainMenu is false, then all of these will match the value and be set to invisible*/
		
		this.title.setVisible(this.mainMenu); //Set the visibility of the title to whatever mainMenu is
		this.subheading1.setVisible(this.mainMenu); //Set the visibility of the first sub-heading to whatever mainMenu is
		this.subheading2.setVisible(this.mainMenu); //Set the visibility of the second sub-heading to whatever mainMenu is
		this.dayPrompt.setVisible(this.mainMenu); //Set the visibility of the day prompt to whatever mainMenu is
		this.dayTextBox.setVisible(this.mainMenu); //Set the visibility of the day text box to whatever mainMenu is
		this.dayTextBox.setEnabled(this.mainMenu); //Set whether the day text box is enable to whatever mainMenu is
		this.dayTextBox.setEditable(this.mainMenu); //Set whether the day text box is editable to whatever mainMenu is
		this.monthPrompt.setVisible(this.mainMenu); //Set whether the month prompt is enable to whatever mainMenu is
		this.monthTextBox.setVisible(this.mainMenu); //Set the visibility of the month text box to whatever mainMenu is
		this.monthTextBox.setEnabled(this.mainMenu); //Set whether the month text box is enable to whatever mainMenu is
		this.monthTextBox.setEditable(this.mainMenu); //Set whether the month text box is editable to whatever mainMenu is
		this.checkDateButton.setVisible(this.mainMenu); //Set the visibility of the date checker button to whatever mainMenu is
		
		/*Set the visibility of the date menu graphical elements to the opposite of the state of mainMenu. If mainMenu is true, then all of these 
		  will be the opposite be set to invisible. If mainMenu is false, then all of these will be the opposite and be set to visible*/
		
		this.changeToMMDDYY.setEnabled(!(this.mainMenu)); //Set whether the button to change to MMDDYY is enable to the opposite of mainMenu
		this.changeToMMDDYY.setVisible(!(this.mainMenu)); //Set the visibility of the button to change to MMDDYY to the opposite of mainMenu
		this.changeToYYMMDD.setEnabled(!(this.mainMenu)); //Set whether the button to change to YYMMDD is enable to the opposite of mainMenu
		this.changeToYYMMDD.setVisible(!(this.mainMenu)); //Set the visibility of the button to change to YYMMDD to the opposite of mainMenu
        this.changeToDDMMYY.setEnabled(!(this.mainMenu)); //Set whether the button to change to YYMMDD is enable to the opposite of mainMenu
		this.changeToDDMMYY.setVisible(!(this.mainMenu)); //Set the visibility of the button to change to YYMMDD to the opposite of mainMenu
        this.changeEra.setEnabled(!(this.mainMenu)); //Set whether the button to change the era is enable to the opposite of mainMenu
		this.changeEra.setVisible(!(this.mainMenu)); //Set the visibility of the button to change the era to the opposite of mainMenu
        this.goBack.setEnabled(!(this.mainMenu)); //Set whether the button to check another date is enable to the opposite of mainMenu
		this.goBack.setVisible(!(this.mainMenu)); //Set the visibility of the button to check another date the opposite of mainMenu
	}
	
	public void paint(Graphics g) //Create void paint method to paint and update the JPanel. This method is invoked numerous times when relevant
	{
		super.paint(g); //Enable panel to be painted
		
		//Decisions
		if (!mainMenu) //If the main menu is not displayed
		{
			g.setFont(this.subheadingFont); //Set the font of the painter to the sub-heading font
    		g.setColor(this.lightEmerald); //Set the colour of the painter to light emerald
            
    		//Nested Decisions
            if (this.trackIndex != this.arraySize) //If the error message is not going displayed and there is a match with the dates
            {
            	FontMetrics datePaintArea = g.getFontMetrics(this.subheadingFont); //Set the font metric
                int xNum = this.dateAsNumSize.x + (this.dateAsNumSize.width - 
                		datePaintArea.stringWidth(this.dates[this.trackIndex].toString())) / 2; //Find x-coordinate based on the size of the string
                int yNum = this.dateAsNumSize.y + ((this.dateAsNumSize.height - 
                		datePaintArea.getHeight()) / 2) + datePaintArea.getAscent(); //Find the y-coordinate based on the size of the string
            	g.drawString(this.dates[this.trackIndex].toString(), 
            			xNum, yNum); //Paint the date as a number, which will now always be centred, based on the use of font metrics 
            	
            	FontMetrics writtenMetrics = g.getFontMetrics(this.subheadingFont); //Set the font metric
                int xWrit = this.dateAsWrittenSize.x + (this.dateAsWrittenSize.width - writtenMetrics.stringWidth(DateClass.writtenDate
                		(this.dates[this.trackIndex]))) / 2; //Find x-coordinate based on the size of the string 
                int yWrit = this.dateAsWrittenSize.y + ((this.dateAsWrittenSize.height - writtenMetrics.getHeight()) / 2) + 
                		writtenMetrics.getAscent(); //Find the y-coordinate based on the size of the string
                // Draw the String
            	g.drawString(DateClass.writtenDate(this.dates[this.trackIndex]), 
            			xWrit, yWrit); //Paint the date as written, which will now always be centred, based on the use of font metrics 
            }
            
            else {} //Otherwise, if there is no date match and the error is displayed, do not paint anything
		}
	}
	
	private void readFile() throws NumberFormatException //Private method reads the file and throws a NumberFormatException error to handle parsing
	{		
		
		//Try and catch statement
		try //Try to open the file
		{
			//Declare all objects
			BufferedReader fileInput = new BufferedReader (new FileReader("important dates.txt")); //Initialise Buffered reader to read the text file
			
			 //Set the string line to the first line the scanner reads
			this.arraySize = Byte.parseByte(fileInput.readLine()); //Set the size of all the arrays to the first line read
			
			this.yearBank = new int [this.arraySize]; //Set the size of the array of years
			this.monthBank = new byte [this.arraySize]; //Set the size of the array of months
			this.dayBank = new byte [this.arraySize]; //Set the size of the array of days
			this.messageBank = new String[this.arraySize + 1]; //Set the size of the array of messages to one more than arraySize
			this.dates = new DateClass[this.arraySize]; //Set the size of the array of dates
			
			//Loops
			for (int i = 0; i < this.arraySize; i++) //Run the loop and iterate through each index of the all the arrays
			{
				this.yearBank [i] = Integer.parseInt(fileInput.readLine()); //Read the next line  and set it to the index in the years array
				
				this.monthBank [i] = Byte.parseByte(fileInput.readLine()); //Read the next line  and set it to the index in the months array
				
				this.dayBank [i] = Byte.parseByte(fileInput.readLine()); //Read the next line  and set it to the index in the days array
				
				this.messageBank [i] = fileInput.readLine(); //Read the next line  and set it to the index in the messages array
				}
			//When this point is reached, all the data from the text file is stored
			fileInput.close(); //Close the scanner, since the file reading is now done, and to prevent a resource leak
			
			//Set the message for when no date is found
			this.messageBank[this.arraySize] = "Sorry! Nothing important happened on that day as per the text file of important days submitted by"
					+ " the ICS3U and ICS4U classes."
					+ "\n\nClick the button below that says "
					+ "\"Check Another Date\" to try another date."; //Set the last extra index in the message array to an error message
			
			fileInput.close(); //Close the scanner, since the file reading is now done, and to prevent a resource leak
		}
		
		catch (IOException e) //If the file cannot be opened, catch the exception
		{
			//Print the message
			System.out.println("There was an error in reading the file. The file may be missing, or it may have the wrong name. "
					+ "Ensure the file has the name \"important dates.txt\""); //Tell user that the file cannot be opened
		}
	}
	
	private void addDates() //Private method which adds all the dates to the dates array
	{
		//Loops
		for (int j = 0; j < this.arraySize; j++) //Start loop at 0 and iterate through all the elements of the dates array 
		{
			this.dates[j] = new DateClass(this.yearBank[j], this.monthBank[j], this.dayBank[j]); //Create a new date each time for each index
		}
	}
} //End of class
