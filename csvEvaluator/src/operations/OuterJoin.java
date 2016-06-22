package operations;
import csvEvaluator.Evaluator;
import csvEvaluator.InvalidArgumentException;

import java.io.IOException;
import java.util.ArrayList;

public class OuterJoin implements Operation {

    public Mean() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void evaluate() throws IOException, InvalidArgumentException {
        join(csvFile.get(0), csvFile.get(1));
    }
}
