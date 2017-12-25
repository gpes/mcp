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
    private List<String> nameList = Arrays.asList(
            "add", "addAll", "clear",
            "remove", "removeAll", "retainAll", 
            "pollFirst", "pollLast", "replaceAll", 
            "set", "sort", "offer",
            "poll", "addFirst", "addLast", 
            "offerFirst", "offerLast", "removeFirst", 
            "removeLast", "removeFirstOccurrence", "removeLastOccurrence", 
            "push", "pop", "put",
            "replace", "putAll", "putIfAbsent",
            "computeIfAbsent", "computeIfPresent", "compute",
            "merge", "pollFirstEntry", "pollLastEntry");

    //estrategia para receber varios nomes de metodos possiveis
    public FilterByMethod() {
    }

    public FilterByMethod(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public boolean test(Call t) {
        return nameList.contains(t.getMethodName().split("\\[")[0]);
    }

//    @Override
//    public boolean test(Call t) {
//        return nameList.stream().anyMatch((name) -> (t.getMethodName().contains(name)));
//    }
}
