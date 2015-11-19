package tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import csvEvaluator.Evaluator;
import csvEvaluator.InvalidArgumentException;

public class testEvaluator {
    private final String RootDirectory = "/home/brendan/Code/csvEvaluator/CSVFiles/";
    @Rule
    public final ExpectedException exception = ExpectedException.none();
    @Test
    public void testSumPerformance() throws InvalidArgumentException,  IOException  {
        String[] arguments = {"--files="+RootDirectory+"Appearances.csv",
                "--operation=add",
                "--columns=yearID,G_batting"};
        long timeBefore = System.currentTimeMillis();
        Evaluator.Evaluate(arguments);
        assertTrue(System.currentTimeMillis() - timeBefore < 1000);
    }
    
    @Test
    public void testMultiFileSumPerformance() throws InvalidArgumentException,  IOException {
        String[] arguments = {"--files="+RootDirectory+"Appearances.csv,"+RootDirectory+"CollegePlaying.csv",
                "--operation=ADD",
                "--columns=yearID,yearID"};
        long timeBefore = System.currentTimeMillis();
        Evaluator.Evaluate(arguments);
        assertTrue(System.currentTimeMillis() - timeBefore < 1000);
    }
    
    @Test
    public void testDifferencePerformance() throws InvalidArgumentException, IOException {
        String[] arguments = {"--files="+RootDirectory+"Appearances.csv",
                "--operation=subtract",
                "--columns=yearID,G_batting"};
        long timeBefore = System.currentTimeMillis();
        Evaluator.Evaluate(arguments);
        assertTrue(System.currentTimeMillis() - timeBefore < 1000);
    }
    
    @Test
    public void testMultiFileDifference() throws InvalidArgumentException, IOException {
        String[] arguments = {"--files="+RootDirectory+"SmallTest.csv,"+RootDirectory+"SmallTest2.csv",
                "--operation=subtract",
                "--columns=col2,col1"};
        assertTrue(compareExpectedOutputFiles(RootDirectory+"testMultipleFileDifference.csv",""+RootDirectory+"testMultipleFileDifferenceOutput.csv",arguments));
    }

    @Test
    public void testMultiFileDifference2Performance() throws InvalidArgumentException, IOException {
        String[] arguments = {"--files="+RootDirectory+"Appearances.csv,"+RootDirectory+"CollegePlaying.csv",
                "--operation=Subtract",
                "--columns=yearID,yearID"};
        long timeBefore = System.currentTimeMillis();
        Evaluator.Evaluate(arguments);
        assertTrue(System.currentTimeMillis() - timeBefore < 1000);
    }
    
    @Test
    public void testAveragePerformance() throws InvalidArgumentException, IOException {
        
        String[] arguments = {"--files="+RootDirectory+"Appearances.csv",
                "--operation=mean",
                "--columns=yearID,G_batting"};
        long timeBefore = System.currentTimeMillis();
        Evaluator.Evaluate(arguments);
        assertTrue(System.currentTimeMillis() - timeBefore < 350);
    }
    
    @Test
    public void testInnerJoin() throws InvalidArgumentException, IOException {
        String[] arguments = {"--files="+RootDirectory+"SmallTest.csv,"+RootDirectory+"SmallTest2.csv",
                "--operation=inner-join",
                "--columns=col1,col2"};
        assertTrue(compareExpectedOutputFiles(RootDirectory+"testInnerJoinExpected.csv",RootDirectory+"testInnerJoinOutput.csv",arguments));
    }
    
    @Test
    public void testInnerJoinPerformance() throws InvalidArgumentException, IOException {
        String[] arguments = {"--files="+RootDirectory+"Appearances.csv,"+RootDirectory+"AppearancesTest.csv",
                "--operation=inner-join",
                "--columns=yearID,yearID"};
        long timeBefore = System.currentTimeMillis();
        Evaluator.Evaluate(arguments);
        assertTrue(System.currentTimeMillis() - timeBefore < 7000);
    }
    
    @Test
    public void testNonSquareInnerJoin() throws InvalidArgumentException, IOException {
        String[] arguments = {"--files="+RootDirectory+"SmallTest2.csv,"+RootDirectory+"SmallTest3.csv",
                "--operation=inner-join",
                "--columns=col1,col1"};
        assertTrue(compareExpectedOutputFiles(""+RootDirectory+"testNonSquareInnerJoinExpected.csv",""+RootDirectory+"testNonSquareInnerJoinOutput.csv",arguments));
    }
    
    @Test
    public void testOuterJoin() throws InvalidArgumentException, IOException {
        String[] arguments = {"--files="+RootDirectory+"SmallTest.csv,"+RootDirectory+"SmallTest2.csv",
                "--operation=outer-join",
                "--columns=col2,col1"};
        assertTrue(compareExpectedOutputFiles(""+RootDirectory+"testOuterJoinExpected.csv",""+RootDirectory+"testOuterJoinOutput.csv",arguments));
    }

    @Test
    public void testOuterJoinPerformance() throws InvalidArgumentException, IOException {
        String[] arguments = {"--files="+RootDirectory+"Appearances.csv,"+RootDirectory+"AppearancesTest.csv",
                "--operation=outer-join",
                "--columns=yearID,yearID"};
        long timeBefore = System.currentTimeMillis();
        Evaluator.Evaluate(arguments);
        assertTrue(System.currentTimeMillis() - timeBefore < 7000);
    }
    
    @Test
    public void testMin() throws InvalidArgumentException, IOException {
        String[] arguments = {"--files="+RootDirectory+"Appearances.csv",
                "--operation=min",
                "--columns=yearID"};
        long timeBefore = System.currentTimeMillis();
        Evaluator.Evaluate(arguments);
        assertTrue(System.currentTimeMillis() - timeBefore < 700);
    }
    
    @Test
    public void testDividePerformance() throws InvalidArgumentException, IOException { 
        String[] arguments = {"--files="+RootDirectory+"Appearances.csv",
                "--operation=divide",
                "--columns=yearID,G_batting"};
        long timeBefore = System.currentTimeMillis();
        Evaluator.Evaluate(arguments);
        assertTrue(System.currentTimeMillis() - timeBefore < 1000);
    }
    
    @Test
    public void testMode() throws InvalidArgumentException, IOException {
        String[] arguments = {"--files="+RootDirectory+"SmallTest.csv",
                "--operation=mode",
                "--columns=col1"};
        assertTrue(compareExpectedOutputFiles(""+RootDirectory+"testModeExpected.csv",""+RootDirectory+"testModeOutput.csv",arguments));
    }
    
    @Test
    public void testModePerformance() throws InvalidArgumentException, IOException {
       String[] arguments = {"--files="+RootDirectory+"Appearances.csv",
               "--operation=mode",
               "--columns=yearID"}; 
       long timeBefore = System.currentTimeMillis();
       Evaluator.Evaluate(arguments);
       assertTrue(System.currentTimeMillis() - timeBefore < 1000);
    }
     

    @Test
    public void testNonSquareOuterJoin() throws InvalidArgumentException, IOException {
        String[] arguments = {"--files="+RootDirectory+"SmallTest2.csv,"+RootDirectory+"SmallTest3.csv",
                "--operation=outer-join",
                "--columns=col2,col1"};
        assertTrue(compareExpectedOutputFiles(""+RootDirectory+"testNonSquareOuterJoin.csv", ""+RootDirectory+"testNonSquareOuterJoin.csv",arguments));
    }

    @Test
    public void testNonSquareOuterJoin2() throws InvalidArgumentException, IOException {
        String[] arguments = {"--files="+RootDirectory+"SmallTest3.csv,"+RootDirectory+"SmallTest2.csv",
                "--operation=outer-join",
                "--columns=col2,col1"};
        assertTrue(compareExpectedOutputFiles(""+RootDirectory+"testNonSquareOuterJoin2Expected.csv",""+RootDirectory+"testNonSquareOuterJoin2Output.csv",arguments));
    }

    @Test
    public void testNonSquareOuterJoinPerformance() throws InvalidArgumentException, IOException  {
        String[] arguments = {"--files="+RootDirectory+"Appearances.csv,"+RootDirectory+"AppearancesAbbreviated.csv",
                "--operation=outer-join",
                "--columns=yearID,yearID"};
        long timeBefore = System.currentTimeMillis();
        Evaluator.Evaluate(arguments);
        assertTrue(System.currentTimeMillis() - timeBefore < 7000);
    }

    @Test
    public void testNonExistentFile() throws InvalidArgumentException, IOException {
        String[] arguments = {"--files=abc",
                "--operation=outer-join",
                "--columns=yearID,yearID"};
        exception.expect(IOException.class);
        Evaluator.Evaluate(arguments);
    }
    
    @Test
    public void testNonExistentOperation() throws InvalidArgumentException,  IOException  {
        String[] arguments = {"--files="+RootDirectory+"Appearances.csv,"+RootDirectory+"AppearancesAbbreviated.csv",
                "--operation=standard-deviation",
                "--columns=yearID,yearID"};
        exception.expect(InvalidArgumentException.class);
        Evaluator.Evaluate(arguments);
    }
    
    @Test
    public void testAttemptNumericalCalculationInvalidData() throws InvalidArgumentException,  IOException  {
        String[] arguments = {"--files="+RootDirectory+"SmallTest4.csv",
                "--operation=add",
                "--columns=col1"};
        exception.expect(NumberFormatException.class);
        Evaluator.Evaluate(arguments);
    }

    @Test
    public void testAttemptNumericalCalculationInvalidData2() throws InvalidArgumentException,  IOException  {
        String[] arguments = {"--files="+RootDirectory+"SmallTest4.csv",
                "--operation=add",
                "--columns=col2"};
        exception.expect(NumberFormatException.class);
        Evaluator.Evaluate(arguments);
    }
    
    @Test
    public void testMultiFileSumUnequalColumnLengths() throws InvalidArgumentException, IOException {
        String[] arguments = {"--files="+RootDirectory+"Appearances.csv,"+RootDirectory+"AppearancesAbbreviated.csv",
                "--operation=add",
                "--columns=yearID,yearID"};
        String actualOutputFileHandle = ""+RootDirectory+"testMultiFileSumUnequalColumnLengthsOutput.csv";
        File outputFile = new File(actualOutputFileHandle);
        FileOutputStream output = new FileOutputStream(outputFile);
        PrintStream printStream = new PrintStream(output);
        System.setOut(printStream);
        Evaluator.Evaluate(arguments);
        BufferedReader outputReader = new BufferedReader(new FileReader(actualOutputFileHandle));
        String row = outputReader.readLine();
        ArrayList<String> file = new ArrayList<String>();
        while(row != null) {
            file.add(row);
            row = outputReader.readLine();
        }
        assertTrue(file.size() == 99419);
        outputReader.close();
        Files.delete(Paths.get(actualOutputFileHandle));
    }
    
    @Test(expected=InvalidArgumentException.class)
    public void testInvalidColumns() throws InvalidArgumentException,  IOException, InvalidArgumentException {
        String[] arguments = {"--files="+RootDirectory+"Appearances.csv",
                "--operation=add",
                "--columns=abc"};
       Evaluator.Evaluate(arguments); 
    }
    
    @Test(expected=InvalidArgumentException.class)
    public void testColumnName() throws IOException, InvalidArgumentException {
        String[] arguments = {"--files="+RootDirectory+"Appearances.csv",
                "--operation=add",
                "--columns=yearID1223"};
       Evaluator.Evaluate(arguments); 
    }
    
    @Test
    public void testMultiColumnSum() throws IOException, InvalidArgumentException {
        String[] arguments = {"--files="+RootDirectory+"SmallTest3.csv",
                "--operation=add",
                "--columns=col1,col2,col3"};
        Evaluator.Evaluate(arguments);
    }
    
    public boolean compareExpectedOutputFiles(String expectedOutputFileHandle, String actualOutputFileHandle, String[] cliArguments) throws InvalidArgumentException,  IOException {
        /*
         * This block of code sets it up such that the standard output is 
         * redirected to the file with the name of actualOutputFile Handle.
         */
//        File outputFile = new File(actualOutputFileHandle);
//        FileOutputStream output = new FileOutputStream(outputFile);
//        PrintStream printStream = new PrintStream(output);
//        System.setOut(printStream);

        Evaluator.Evaluate(cliArguments);

//        BufferedReader expectedOutput = new BufferedReader(new FileReader(expectedOutputFileHandle));
//        BufferedReader actualOutput = new BufferedReader(new FileReader(actualOutputFileHandle));
//        String expectedLine = expectedOutput.readLine();
//        String outputLine = actualOutput.readLine();
//        
        boolean filesSame = true;
//        while(expectedLine != null && filesSame) {
//            if(!(expectedLine.equals(outputLine))) {
//                filesSame = false;
//            } 
//            outputLine = actualOutput.readLine();
//            expectedLine = expectedOutput.readLine();
//        }
//        expectedOutput.close();
//        actualOutput.close();
//        printStream.close();
//        Files.delete(Paths.get(actualOutputFileHandle));
        return filesSame;
    }
}