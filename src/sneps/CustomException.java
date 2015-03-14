/**
 * @className CustomException.java
 * 
 * @ClassDescription A class that extends Exception.
 * 
 * @author Nourhan Zakaria
 * @version 2.00 18/6/2014
 */
package sneps;

public class CustomException extends Exception {

	/**
	 * The constructor of this class.
	 * 
	 * @param message
	 * 			The message of the custom exception.
	 */
	public CustomException(String message){
		super(message);
	}
}
