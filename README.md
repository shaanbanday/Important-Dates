# Important-Dates
a visual graphic which takes a day as a number and a month as a string and checks the file if anything important happened on that day. It there's a match, then the program will display the date, along with a description. If the date entered is not in  the file, than the program displays an appropriate message.
At the bottom, after a date is displayed or not, there are buttons which the user can use 
to change the format of the date (switch between MMDDYY, YYMMDD, DDMMYY), and the format of the era and whether it is a secular format or not (CE and
BCE vs AD and BC). Lastly, at the bottom, the user can press a button to go back and check another date.

The following program/class does not have a main method, but it constructs all the graphical elements for the that the ImportantDatesMain
class calls to create the GUI. The constructor itself (this first method) is spilt into many constructors since this class is very big and has many
variables it needs to initialise. This class also implements an ActionListener method which is called whenever a button is pressed within the program.
To draw the date as a number and in a written format, the class interacts with the paint method, which makes it easier to update frequently, compared
to just using JLabels which are always the same text throughout the program's run. Finally, this class takes care of reading the file of dates, which
it only does once before storing all the values in the arrays. This class also interacts with the DateClass to create the actual date objects.
