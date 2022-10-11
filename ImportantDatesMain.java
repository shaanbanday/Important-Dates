/*
Java™ Project: ICS4U
Package: date
Class: ImportantDatesMain
Programmer: Shaan Banday.

Date Created: Monday, 21st March, 2022.
Date Completed: Friday, 25th March, 2022. 

Description: The following program/class has a main method, and creates the GUI object by calling the ImportantDates class. Since the main method is
always ran first, and since the ImportantDates class does not have a main method, then there needs to be one to create the object. Otherwise, the code
would run, but not do anything, since Eclipse wouldn't know which method to run first. So this main method has to be here.
*/

package date; //Launch the class from this package named "date"

public class ImportantDatesMain //The name of the class that will reference the "ImportantDates" class.
{
	@SuppressWarnings("unused") //Suppress any warnings of unused objects
	public static void main(String[] args) //Main method which creates the ImportantDates object as a GUI
	{
		ImportantDates datesGUI = new ImportantDates(); //Create the ImportantDates object as a new GUI
	}
} //End of class