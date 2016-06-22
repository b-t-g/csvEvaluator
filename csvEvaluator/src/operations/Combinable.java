package csvEvaluator.src.operations;

import java.io.IOException;
import csvEvaluator.InvalidArgumentException;
import java.util.ArrayList;

public interface Combinable {

    public float combine(ArrayList<Float> value) throws IOException, InvalidArgumentException;
}
