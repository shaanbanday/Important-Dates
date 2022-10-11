/*
Java™ Project: ICS4U
Package: date
Class: ImportantDatesMain
Programmer: Shaan Banday.

Date Created: Monday, 21st March, 2022.
Date Completed: Friday, 25th March, 2022. 

Description: The following program/class has a main method, and tests the DateClass
*/

package date; //Launch the class from this package named "date"

public class DateMain //The name of the class that tests the DateClass
{

	public static void main(String[] args)
	{
		//Declare all variables
		DateClass birthdate = new DateClass(2004, 9, 8); //My birthday
		DateClass today = new DateClass(); //Today's Date
		
		DateClass march1Normal = new DateClass (2019, 3, 1); //Day after February 28th
		DateClass march1Leap = new DateClass (2020, 3, 1); //Day after February 29th
		DateClass jan17 = new DateClass (2020, 1, 17); //17th day of year
		DateClass dec31Normal = new DateClass (2019, 12, 31); //365th day of year
		DateClass dec31Leap = new DateClass (2020, 12, 31); //366th day of year
		
		DateClass bcDate = new DateClass(-53, 2, 3); //Date with a negative, symbolising a date before the common era
		DateClass clone = bcDate; //A copy of bcDate
		DateClass year0 = new DateClass (0, 1, 2); //Date, with the year as 0, which is impossible
		DateClass doubMonthSingDay = new DateClass (2045, 12, 4); //Date with a double digit month and a single digit day (will make sense later)
		DateClass lastDayOfBC = new DateClass (-1, 12, 31); //Last day of before the common era
		DateClass endOfMonth = new DateClass (2023, 6, 30); //A date with the day at the end of a month
		DateClass nd = new DateClass (2022, 5, 2); //A date with a ones digit for the day of 2, that should produce a nd suffix
		DateClass teen = new DateClass (2022, 7, 12); //A date with a ones digit for the day of 2, that should NOT produce a nd suffix
		//This is because we say 12th not 12nd
		
		
		//Get Year method
		System.out.println("I was born in the year " + birthdate .getYear()); //Should be 2004
		System.out.println("The current year is " + today.getYear()); //Should be 2022
		
		//Get month as num and string method
		System.out.println("\nMy birthmonth is " + birthdate .getMonth()); //Should be 9
		System.out.println("My birth month as a String is " + birthdate .getMonthName()); //Should be September
		System.out.println("The current month is " + today .getMonth()); //Should be 3
		System.out.println("The current month as a String is " + today .getMonthName()); //Should be March
		
		//Get day method
		System.out.println("\nThe day of the month I was born is " + birthdate.getDay()); //Should be 8
		System.out.println("The current day of the month is " + today.getDay()); //Should be whatever the date is
		
		//Day of year method
		System.out.println("\nIn a normal year, March 1st is this day of year: " + march1Normal.getDayOfYear()); //Should be 1 less than the next
		System.out.println("In a leap year, March 1st is this day of year: " + march1Leap.getDayOfYear()); //Should be 1 more than the next
		System.out.println("Jan 17th day of year: " + jan17.getDayOfYear()); //Should be 17
		System.out.println("Last day of normal year: " + dec31Normal.getDayOfYear()); //Should be 365
		System.out.println("Last day of leap year: " + dec31Leap.getDayOfYear()); //Should be 366
		
		//Equals method
		System.out.println("\n" + birthdate.equals(today)); //Should be false, unless on my birthday
		System.out.println(bcDate.equals(clone)); //Should be true
		
		//To String method
		System.out.println("\n" + year0.toString()); //Should be error
		System.out.println(bcDate.toString()); //Should show date as positive, and zeros before month and day
		System.out.println(dec31Normal.toString()); //Should show date with no zeros
		System.out.println(jan17.toString()); //Should show zero before month
		System.out.println(doubMonthSingDay); //Should run the same as others and show zero before day
		
		//Switch format method
		birthdate.switchFormat(DateClass.DDMMYY); //Switch the format to DDMMYY
		System.out.println("\n" +birthdate); //Should be 08/09/2004
		birthdate.switchFormat(DateClass.MMDDYY); //Switch the format to MMDDYY
		System.out.println(birthdate); //Should be 09/08/2004
		birthdate.switchFormat(DateClass.YYMMDD); //Switch back to default (YYMMDD)
		System.out.println(birthdate); //Should be 2004/09/08 (default)
		
		//Increase date method
		dec31Normal.increaseDate(); //Increase December 31st by 1 day
		System.out.println("\n" + dec31Normal); //Should be 2020/01/01
		dec31Leap.increaseDate(); //Increase December 31st by 1 day
		System.out.println(dec31Leap);//Should be 2021/01/01
		endOfMonth.increaseDate(); //Increase a day at the end of a month by 1 day
		System.out.println(endOfMonth); //Should be 2023/07/01
		lastDayOfBC.increaseDate(); //Increase the last day before the common era by 1
		System.out.println(lastDayOfBC); //Should be 1/01/01 (new era)
		
		
		//Written date method
		//By default, the era format is secular (CE and BCE)
		System.out.println("\n" + DateClass.writtenDate(birthdate)); //Suffix is th, and era is CE
		birthdate.switchEraFormat(DateClass.NON_SECULAR); //Switch the format to the non-secular one (AD and BC)
		System.out.println(DateClass.writtenDate(birthdate)); //Suffix is th and era is AD
		System.out.println("\n" + DateClass.writtenDate(bcDate)); //Suffix is rd and era is BCE
		bcDate.switchEraFormat(DateClass.NON_SECULAR); //Switch the format to the non-secular one (AD and BC)
		System.out.println(DateClass.writtenDate(bcDate)); //Suffix is rd and era is BC
		System.out.println("\n" + DateClass.writtenDate(nd)); //Suffix is nd
		System.out.println("\n" + DateClass.writtenDate(teen)); //Even though last digit is 2, suffix should be th
		
		System.out.println("\n" + teen.getEraFormat()); //Should be true
		teen.switchEraFormat(DateClass.NON_SECULAR); //Switch the format to the non-secular one (AD and BC)
		System.out.println(teen.getEraFormat()); //Should be false
	}

}
