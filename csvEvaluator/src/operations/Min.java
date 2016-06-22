package operations;
import csvEvaluator.Evaluator;
import csvEvaluator.InvalidArgumentException;

import java.io.IOException;
import java.util.ArrayList;

public class Min implements Operation {

    public Mean() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void evaluate() throws IOException, InvalidArgumentException {
        totalColumn = collectDataSingleFile();
        float minimum = totalColumn.get(0);
        for(int i = 1; i < totalColumn.size(); i++) {
            float current = totalColumn.get(i);
            if(current < minimum) {
                minimum = current;
            }
        }
        System.out.println(minimum);

}
