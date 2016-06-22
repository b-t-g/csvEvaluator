package csvEvaluator.src.operations;
import csvEvaluator.Evaluator;
import csvEvaluator.InvalidArgumentException;

import java.io.IOException;
import java.util.ArrayList;

public class Mean implements Operation {

    public Mean() {
    }

    @Override
    public void evaluate() throws IOException, InvalidArgumentException {
        ArrayList<Float> totalColumn = Evaluator.collectDataSingleFile();
        float sum = 0;
        for(float f : totalColumn) {
            sum += f;
        }
        System.out.println(sum/totalColumn.size());
    }

}
