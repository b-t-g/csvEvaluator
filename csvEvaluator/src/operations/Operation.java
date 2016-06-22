package csvEvaluator.src.operations;

import java.io.IOException;
import csvEvaluator.InvalidArgumentException;

public interface Operation {
    
    public void evaluate() throws IOException, InvalidArgumentException;
}