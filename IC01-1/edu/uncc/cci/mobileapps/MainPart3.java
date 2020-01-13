package edu.uncc.cci.mobileapps;

import java.util.ArrayList;
import java.util.List;

public class MainPart3 {
    /*
    * Question 3:
    * - In this question you will use the Data.users and Data.otherUsers array that includes
    * a list of users. Formatted as : firstname,lastname,age,email,gender,city,state
    * - Create a User class that should parse all the parameters for each user.
    * - The goal is to print out the users that are exist in both the Data.users and Data.otherUsers.
    * Two users are equal if all their attributes are equal.
    * - Print out the list of users which exist in both Data.users and Data.otherUsers.
    * */

    public static void main(String[] args) {
	    List<User> users = new ArrayList<User>();
	    List<User> otherUsers = new ArrayList<User>();

	    for (String str : Data.users) {
		    users.add(new User(str));
	    }

	    for (String str : Data.otherUsers){
	    	otherUsers.add(new User(str));
	    }
    }
}