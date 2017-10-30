package ifpb.gpes.filter;

import ifpb.gpes.Call;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 *
 * @author juan
 */
public class FilterByMethod implements Predicate<Call> {

    private String methodName;
    private List<String> nameList;

    //estrategia para receber varios nomes de metodos possiveis
    public FilterByMethod(String... methods) {
        this.nameList = Arrays.asList(methods);
    }

    public FilterByMethod(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public boolean test(Call t) {
        return t.getMethodName().contains(methodName);
    }

//    @Override
//    public boolean test(Call t) {
//        return nameList.stream().anyMatch((name) -> (t.getMethodName().contains(name)));
//    }
}
