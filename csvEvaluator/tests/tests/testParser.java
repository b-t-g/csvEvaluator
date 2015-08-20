package tests;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import csvEvaluator.InvalidArgumentException;
import csvEvaluator.Parser;
import csvEvaluator.Parser.Operation;

public class testParser {
    private final String RootDirectory = "/home/brendan/Code/HRT/CSVFiles/";
    @Test
    public void testFindArgument1() throws IOException, InvalidArgumentException {
        String[] arguments = {RootDirectory+"Appearances.csv",
                "--operation=inner-join",
                "--columns=yearID,G_batting"};
        ArrayList<Integer> hash = Parser.parseColumnNameToIndex(arguments, new BufferedReader(new FileReader(RootDirectory+"Appearances.csv")));
        assertTrue(hash.get(0) == 0);
        assertTrue(hash.get(1) == 6);
    }

    @Test
    public void testFindOperation1() throws Throwable {
        String[] arguments = {RootDirectory+"Appearances.csv",
                "--operation=inner_join",
                "--columns=yearID,G_batting"};
        Parser.Operation operation = Parser.parseOperation(arguments);
        assertTrue(operation.equals(Operation.INNER_JOIN));
    }

    @Test
    public void testFindArgument2() throws IOException, InvalidArgumentException {
        String[] arguments = {RootDirectory+"Appearances.csv",
                "--operation=outer-join",
                "--columns=G_all,G_defense"};
        ArrayList<Integer> hash = Parser.parseColumnNameToIndex(arguments, new BufferedReader(new FileReader(RootDirectory+"Appearances.csv")));
        assertTrue(hash.get(0) == 4);
        assertTrue(hash.get(1) == 7);
    }

    @Test
    public void testFindOperation2() throws Throwable {
        String[] arguments = {RootDirectory+"Appearances.csv",
                "--operation=outer-join",
                "--columns=G_all,G_defense"};
        Parser.Operation operation = Parser.parseOperation(arguments);
        assertTrue(operation.equals(Operation.OUTER_JOIN));
    }

    @Test
    public void testfindOperation3() throws Throwable {
        String[] arguments = {RootDirectory+"Appearances.csv",
                "--operation=ADD",
                "--columns=G_all,G_defense"};
        Parser.Operation operation = Parser.parseOperation(arguments);
        assertTrue(operation.equals(Operation.ADD));
    }

    @Test
    public void testFindOperation4() throws Throwable {
        String[] arguments = {RootDirectory+"Appearances.csv",
                "--operation=inner-join",
                "--columns=yearID,G_batting"};
        Parser.Operation operation = Parser.parseOperation(arguments);
        assertTrue(operation.equals(Operation.INNER_JOIN));
    }

    @Test
    public void testFindArgument3() throws Throwable {
        String[] arguments = {RootDirectory+"Appearances.csv",
                "--operation=outer-join",
                "--columns=G_defense,G_all"};
        ArrayList<Integer> hash = Parser.parseColumnNameToIndex(arguments, new BufferedReader(new FileReader(RootDirectory+"Appearances.csv")));
        assertTrue(hash.get(0) == 7);
        assertTrue(hash.get(1) == 4);
    }
}
