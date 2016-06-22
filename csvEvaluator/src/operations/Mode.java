package operations;
import csvEvaluator.Evaluator;
import csvEvaluator.InvalidArgumentException;

import java.io.IOException;
import java.util.ArrayList;

public class Mode implements Operation {

    public Mode() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void evaluate() throws IOException, InvalidArgumentException {
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

}
