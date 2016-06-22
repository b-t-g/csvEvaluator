package operations;
import csvEvaluator.Evaluator;
import csvEvaluator.InvalidArgumentException;

import java.io.IOException;
import java.util.ArrayList;

public class Mean implements Operation, Combinable {

    public Mean() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void evaluate() throws IOException, InvalidArgumentException {
        totalColumn = collectDataSingleFile();
        float maximum = totalColumn.get(0);
        for(int i = 1; i < totalColumn.size(); i++) {
            float current = totalColumn.get(i);
            if(current > maximum) {
                maximum = current;
            }
        }
        System.out.println(maximum);
    }

    @Override
    public float combine() throws IOException, InvalidArgumentException {
        return value.get(0);
    }

}
