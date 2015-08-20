package csvEvaluator;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		try {
		    Evaluator.Evaluate(args);
		}
		catch(IOException e) {
		    System.out.println("IO exception either file does not exist or" +
		    		"there was an error with opening or closing the file;" +
		    		" aborting calculation now.");
		} catch(NumberFormatException e) {
		    System.out.println("Tried to do a numerical calculation on " +
		    		"non-numerical data; aborting calculation now.");
		} catch (InvalidArgumentException e) {
            System.out.println("An invalid argument was supplied; possible " +
            		"problems include column does not exist in requested " +
            		"table or operation does not exist.");
        }
	}

}
