package ifpb.gpes.filter;

import ifpb.gpes.AssignVerifier;
import ifpb.gpes.Call;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 *
 * @author juan
 */
public class FilterClassType implements Predicate<Call> {

    private AssignVerifier verifier;

    public FilterClassType(String type) {
        verifier = new AssignVerifier(type);
    }

    @Override
    public boolean test(Call t) {
        if (t.getClassType() == null) {
            return false;
        }
        return t.getClassType().contains("java.util.") && verifier.isAssignable(t.getClassType().split("<")[0]);
    }
}
