package csvEvaluator.src.csvEvaluator;

import csvEvaluator.src.operations.Operation;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Parser {
    public enum Operation {
        INNER_JOIN, OUTER_JOIN, ADD, SUBTRACT, MULTIPLY, DIVIDE,
        MIN, MAX, MEAN, MODE
    }

    /**
     * Takes in the arguments supplied from the command line and returns an arraylist of readers
     * for the supplied files.
     * @param cliArguments arguments supplied from the command line
     * @return arraylist of buffered readers for the supplied files
     * @throws FileNotFoundException
     * @throws IllegalArgumentException
     */
    public static ArrayList<BufferedReader> parseFiles(String[] cliArguments)
            throws FileNotFoundException, IllegalArgumentException {
        ArrayList<BufferedReader> files = new ArrayList<BufferedReader>();
        String[] filesArgument = getIndexAndSplit(cliArguments, "--files=")[1].split(",");
        for(String fileName : filesArgument) {
            files.add(new BufferedReader(new FileReader(fileName)));
        }
       return files; 
    }

    /**
     * Takes in the arguments supplied from the command line and returns the operation supplied
     * @param cliArguments arguments supplied from the command line
     * @return the supplied operation
     * @throws IllegalArgumentException
     */
    public static Operation parseOperation(String[] cliArguments) throws IllegalArgumentException {
        String[] operationArgument = getIndexAndSplit(cliArguments, "--operation=");
        if(isJoin(operationArgument[1])) {
            return returnJoin(operationArgument[1]);
        }
        for(Operation op : Operation.values()) {
            for(String argument : operationArgument) {
                if(argument.equalsIgnoreCase(op.toString())) {
                    return op;
                }
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * Takes in arguments from the command line and returns the index of the supplied columns
     * from the intended files.
     * @param cliArguments arguments from the command line
     * @param csvFile supplied files
     * @return an array list of the index of the columns in the intended files.
     * @throws IOException
     * @throws IllegalArgumentException
     */
    public static ArrayList<Integer> parseColumnNameToIndex(
            String[] cliArguments,
            BufferedReader csvFile) throws IOException, IllegalArgumentException{
        String[] columnsArgument = getIndexAndSplit(cliArguments, "--columns=")[1].split(",");
        ArrayList<Integer> columnNameToIndex = new ArrayList<Integer>();
        String[] columnNames = csvFile.readLine().split(",");
        for(String column : columnsArgument) {
            for(int i = 0; i < columnNames.length; i++) {
                if(column.equals(columnNames[i])) {
                    columnNameToIndex.add(i);
                }
            }
        }
        return columnNameToIndex;
    }

    /**
     * Takes in the command line arguments and the name of an argument
     *  and returns an array list of all of the parameters of the desired argument.
     * @param cliArguments the arguments supplied via the command line.
     * @param argument the argument to be split.
     * @return an array list of all of the parameters of the desired argument.
     * @throws IllegalArgumentException
     */
    public static String[] getIndexAndSplit(String[] cliArguments, String argument)
            throws IllegalArgumentException {
        for(int i = 0; i < cliArguments.length; i++) {
            if(cliArguments[i].lastIndexOf(argument) >= 0) {
                return cliArguments[i].split(argument);
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * Takes in a string and returns whether it is of the form "inner-join" or "outer-join"
     * @param operation a string which has determined to have been supplied by the "--operation="
     * argument
     * @return a boolean which determines whether the operation is a join operation.
     */
    private static boolean isJoin(String operation) {
        return operation.equals("inner-join") || operation.equals("outer-join");
    }

    /**
     * Takes in a string representing an operation and returns an inner-join or outer-join depending
     * on which string the string represents
     * @param operation a string which has determined to have been supplied by the "--operation="
     * argument
     * @return the operation represented by operation.
     */
    private static Operation returnJoin(String operation) {
        return operation.charAt(0) == 'i' ? Operation.INNER_JOIN : Operation.OUTER_JOIN; 
    }
}