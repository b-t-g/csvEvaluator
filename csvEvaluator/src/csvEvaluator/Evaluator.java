package csvEvaluator;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class Evaluator {
    private static ArrayList<Integer> columnNameToIndex;
    private static ArrayList<BufferedReader> csvFile;
    private static Parser.Operation operation;

    /**
     * 
     * @param cliArguments the arguments supplied to the program from the
     * command line.
     * @return the columnNameToIndex field for all of the columns for all of the
     * files given in the command line arguments.
     * @throws NullPointerException
     * @throws IOException 
     * @throws InvalidArgumentException 
     */
    private static ArrayList<Integer> getColumnNameToIndex (String[] cliArguments)
            throws NullPointerException, IOException, InvalidArgumentException {
        if(csvFile.size() == 1) {
            return Parser.parseColumnNameToIndex(cliArguments, csvFile.get(0));
        }
        else {
            ArrayList<Integer> columnNameToIndex = new ArrayList<Integer>();
            String columns = Parser.getIndexAndSplit(cliArguments,"--columns=")[1];
            for(int i = 0; i < csvFile.size(); i++) {
                String[] column = {columns.split(",")[i]};
                String[] columnArgument = {"--columns="+column[0]};
                int columnNameToIndexForFile =
                        Parser.parseColumnNameToIndex(columnArgument, csvFile.get(i)).get(0);
                columnNameToIndex.add(columnNameToIndexForFile);
            }
            return columnNameToIndex;
        }
    }

    /**
     * Evaluates the given operation (given by the field operation) on the 
     * given columns on the given files.
     * @throws IOException
     * @throws InvalidArgumentException 
     */
    public  static void Evaluate(String[] cliArguments) throws IOException,
                                                               InvalidArgumentException {
        csvFile = Parser.parseFiles(cliArguments);
        columnNameToIndex = getColumnNameToIndex(cliArguments);
        operation = Parser.parseOperation(cliArguments);
        if(csvFile.size() == 0 || columnNameToIndex.size() == 0) {
            throw new InvalidArgumentException();
        }
        ArrayList<Float> totalColumn;
        boolean singleFile = csvFile.size() == 1;
        switch(operation) {
        case MEAN: 
            totalColumn = collectDataSingleFile();
            float sum = 0;
            for(float f : totalColumn) {
                sum += f;
            }
            System.out.println(sum/totalColumn.size());
            break;

        case MIN:
            totalColumn = collectDataSingleFile();
            float minimum = totalColumn.get(0);
            for(int i = 1; i < totalColumn.size(); i++) {
                float current = totalColumn.get(i);
                if(current < minimum) {
                    minimum = current;
                }
            }
            System.out.println(minimum);
            break;

        case MAX:
            totalColumn = collectDataSingleFile();
            float maximum = totalColumn.get(0);
            for(int i = 1; i < totalColumn.size(); i++) {
                float current = totalColumn.get(i);
                if(current > maximum) {
                    maximum = current;
                }
            }
            System.out.println(maximum);

        case MODE:
            totalColumn = collectDataSingleFile();
            HashMap<Float, Integer> frequency = new HashMap<Float, Integer>();
            for(float f : totalColumn) {
                if(frequency.get(f) == null) {
                    frequency.put(f,1);
                } else {
                    frequency.put(f,frequency.get(f)+1);
                }
            }
            System.out.println(calculateMostFrequent(frequency));
            break;

        case INNER_JOIN:
            join(csvFile.get(0), csvFile.get(1));
            break;

        case OUTER_JOIN:
            join(csvFile.get(0), csvFile.get(1));
            break;

        default:
            if(singleFile) {
                totalColumn = collectDataSingleFile();
            } else {
                totalColumn = collectDataMultiFile();
            }
            for(float f : totalColumn) {
                System.out.println(f);
            }
        }
    }

    /**
     * This function reads the data from multiple tables and returns the result
     * of the computation given by the operation field.
     * @return
     * @throws IOException
     */
    private static ArrayList<Float> collectDataMultiFile() throws IOException,
                                                                  NumberFormatException {
        ArrayList<Float> operatedColumn = new ArrayList<Float>();
        boolean haveEncounteredNullColumn = false;
        while(!haveEncounteredNullColumn) {
            ArrayList<Float> columnValues = new ArrayList<Float>();
            for(int i = 0; i < csvFile.size(); i++) {
                String line = csvFile.get(i).readLine();
                if(line == null) {
                    haveEncounteredNullColumn = true;
                    break;
                }
                columnValues.add(Float.parseFloat(line.split(",")[columnNameToIndex.get(i)]));
            }
            if(haveEncounteredNullColumn) {
                break;
            }
            float aggregateTotal = columnValues.get(0);
            for(int i = 1; i < columnValues.size(); i++) {
                ArrayList<Float> toBeCombined = new ArrayList<Float>();
                toBeCombined.add(aggregateTotal);
                toBeCombined.add(columnValues.get(i));
                aggregateTotal = combineFloats(toBeCombined);
            }
            operatedColumn.add(aggregateTotal);
        }
        for(BufferedReader b : csvFile) {
            b.close();
        }
        return operatedColumn; 
    }

    /**
     * This function reads the data from one tables (and possibly multiple
     * columns) and returns either the result of the computation given by the
     * operation field or the columns specified by the command line arguments.
     * @return either
     * @throws InvalidArgumentException 
     * @throws IOException 
     */
    public static ArrayList<Float> collectDataSingleFile() throws InvalidArgumentException,
                                                                  IOException {
        BufferedReader file = csvFile.get(0);
        ArrayList<Float> aggregateTotal = new ArrayList<Float>();
        String values;
        try {
            values = file.readLine();
        } catch (IOException e) {
            return aggregateTotal;
        }
        while(values != null) {
            String[] splitValues = values.split(",");
            float intermediateValue = getIntermediateValue(0, splitValues);
            for(int i = 1; i < columnNameToIndex.size(); i++) {
                ArrayList<Float> toBeCombined = new ArrayList<Float>();
                toBeCombined.add(intermediateValue);
                toBeCombined.add(getIntermediateValue(i, splitValues));
                intermediateValue = combineFloats(toBeCombined);
            }
            aggregateTotal.add(intermediateValue);
            values = file.readLine();
        }
        file.close();
        return aggregateTotal;
    }

    private static float getIntermediateValue(int index, String[] splitValues)
            throws NumberFormatException {
        return Float.parseFloat(splitValues[columnNameToIndex.get(index)]);
    }

    /**
     * This function takes any number of values and returns a single value 
     * depending on which operation is specified.
     * @param value values collected from the specified file and column.
     * @return a single float whose value is determined by the operation and
     * the input values.
     */
    public static float combineFloats(ArrayList<Float> value) {
        switch(operation) {
        case ADD: return value.get(0) + value.get(1);
        case SUBTRACT: return value.get(0) - value.get(1);
        case MULTIPLY: return value.get(0) * value.get(1);
        case DIVIDE: return value.get(0) / value.get(1);
        case MEAN: return value.get(0); 
        case MIN: return value.get(0);
        case MAX: return value.get(0);
        default: return 0;
        }
    } 

    /**
     * This function performs an inner or outer join depending on the operation
     * specified via the command line (the operation variable).
     * @param file1 this is a reader for the file containing the first table.
     * @param file2 this is a reader for the file containing the second table
     * @return the join of the two tables.
     * @throws IOException
     */
    private static ArrayList<String[][]> join(BufferedReader file1, BufferedReader file2)
            throws IOException {
        ArrayList<String[][]> joinedTable = new ArrayList<String[][]>();
        boolean endOfFile1 = false;
        boolean endOfFile2 = false;
        String[] rowInTable1;
        String[] rowInTable2;
        int columnsInRow1 = 0;
        int columnsInRow2 = 0;
        while(!(endOfFile1 && endOfFile2)) {
            String file1Row = file1.readLine();
            String file2Row = file2.readLine();
            if(endOfFile1 || file1Row == null) {
                rowInTable1 = null;
                endOfFile1 = true;
            } else {
                rowInTable1 = file1Row.split(",");
                columnsInRow1 = rowInTable1.length;
            }
            if(endOfFile2 || file2Row == null) {
                rowInTable2 = null;
                endOfFile2 = true;
            } else {
                rowInTable2 = file2Row.split(",");
                columnsInRow2 = rowInTable2.length;
            }
            if(combineColumns(rowInTable1, rowInTable2)){
                if(rowInTable1 == null && !endOfFile2) {
                    file1Row = createNullString(columnsInRow1);
                }else if(rowInTable2 == null && !endOfFile1) {
                    file2Row = createNullString(columnsInRow2);
                }
                String[][] joinedColumn = {rowInTable1, rowInTable2};
                joinedTable.add(joinedColumn);
                System.out.println(file1Row+","+file2Row);
            }
        }
        return joinedTable;
    }

    /**
     * Creates a string containing a number of instances of "null" specified 
     * by numberOfNulls.
     * @param numberOfNulls the number of times the string "null" is to appear
     * @return a string of the form "null,null,null,...,null" with the number
     * of appearances of "null" being exactly numberOfNulls.
     */
    private static String createNullString(int numberOfNulls) {
        StringBuffer nullRows = new StringBuffer("");
        for(int i = 0; i < numberOfNulls-1; i++) {
            nullRows.append("null,");
        }
        nullRows.append("null");
        return new String(nullRows);
    }

    /**
     * Takes two columns and determine whether they are to be included
     * depending on whether the join is inner or outer.
     * @param row1
     * @param row2
     * @return
     */
    private static boolean combineColumns(String[] row1, String[] row2) {
        if(operation.equals(Parser.Operation.OUTER_JOIN)) {
            return outerJoinColumns(row1, row2);
        } else {
            return innerJoinColumns(row1, row2);
        }
    }

    /**
     * Takes in two rows and determines whether they are to be included in an
     * inner join
     * @param row1 a row from the first table.
     * @param row2 a row from the second table.
     * @return a boolean which is true only if row1 and row2 are to be included
     * in an inner join.
     */
    private static boolean innerJoinColumns(String[] row1, String[] row2) {
        boolean join = false;
        if(row1 != null && row2 != null) {
            String joiningColumn1 = row1[columnNameToIndex.get(0)];
            String joiningColumn2 = row2[columnNameToIndex.get(1)];
            if(joiningColumn1.equals(joiningColumn2)) {
                join = true;
            }
        }
        return join;
    }

    /**
     * Takes in two rows and determines whether they are to be included in an
     * outer join
     * @param row1 a row from the first table.
     * @param row2 a row from the second table.
     * @return a boolean which is true only if row1 and row2 are to be included
     * in an outer join.
     */
    private static boolean outerJoinColumns(String[] row1, String[] row2) {
        return (!(row1 == null) || !(row2 == null));
    }

    /**
     * Takes in a hashmap and determines which key has the highest hash value.
     * @param frequency a hashmap from floats to integers.
     * @return the float whose hash has the highest value.
     */
    private static Float calculateMostFrequent(HashMap<Float, Integer> frequency) {
        Float mostFrequent = null;
        for(float f : frequency.keySet()) {
            if(mostFrequent == null) {
                mostFrequent = f;
            }
            if(frequency.get(f) > frequency.get(mostFrequent)) {
                mostFrequent = f;
            }
        }
        return mostFrequent;
    }
}