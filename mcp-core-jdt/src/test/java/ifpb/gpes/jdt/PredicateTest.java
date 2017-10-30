package ifpb.gpes.jdt;

import ifpb.gpes.Call;
import ifpb.gpes.Parse;
import ifpb.gpes.Project;
import ifpb.gpes.VerificationException;
import ifpb.gpes.jdt.ParseStrategies;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import static org.hamcrest.CoreMatchers.hasItems;
import org.junit.Assert;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 17/08/2017, 15:32:22
 */
public class PredicateTest {

    private static final String sources = "../mcp-samples/src/main/java/";

    @Test
    public void captureException() {

    }

    @Test
    public void returnSubListTest() {
//        Predicate<Call> and = DecoratorPredicate.and(new TypePredicate("java.util.Collection"));
        Predicate<Call> and = DecoratorPredicate.and(new TypePredicate("java.util.List"));
        List<Call> coletado = calls().stream().filter(and).collect(Collectors.toList());
        calls().forEach(System.out::println);
        Assert.assertEquals(coletado.size(), 5);
//        Assert.assertEquals(coletado.size(), 6);
        assertThat(coletado, hasItems(
                Call.of("ifpb.gpes.domain.HasJCFObject", "getElements[]", "java.util.List<ifpb.gpes.domain.HasJCFObject>", "ifpb.gpes.domain.LambdaWithArguments", "m1[]", "void", "forEach[java.util.function.Consumer<? super ifpb.gpes.domain.HasJCFObject>]"),
                Call.of("ifpb.gpes.domain.HasJCFObject", "getElements[]", "java.util.List<ifpb.gpes.domain.HasJCFObject>", "ifpb.gpes.domain.LambdaWithArguments", "m2[]", "void", "forEach[java.util.function.Consumer<? super ifpb.gpes.domain.HasJCFObject>]"),
                Call.of("ifpb.gpes.domain.HasJCFObject", "getElements[]", "java.util.List<ifpb.gpes.domain.HasJCFObject>", "ifpb.gpes.domain.LambdaWithArguments", "m3[]", "void", "toArray[]"),
                Call.of("ifpb.gpes.domain.HasJCFObject", "getElements[]", "java.util.List<ifpb.gpes.domain.HasJCFObject>", "ifpb.gpes.domain.LambdaWithArguments", "m3[]", "void", "stream[]"),
                Call.of("ifpb.gpes.domain.HasJCFObject", "getElements[]", "java.util.List<ifpb.gpes.domain.HasJCFObject>", "java.util.function.Consumer<? super ifpb.gpes.domain.HasJCFObject>", "accept[? super ifpb.gpes.domain.HasJCFObject]", "void", "add[ifpb.gpes.domain.HasJCFObject]")
        //                Call.of("ifpb.gpes.domain.LambdaWithArguments", "m4[]", "java.util.Set<java.lang.Integer>", "ifpb.gpes.domain.LambdaWithArguments", "m1[]", "add[java.lang.Integer]")
        )
        );

    }

    @Test
    public void listPredicate() {
        List<Predicate<Call>> predicates = Arrays
                .asList(new MethodPredicate("accept"), new TypePredicate("java.util.List"));
        Predicate<Call> compositePredicate
                = predicates.stream().reduce(w -> true, Predicate::and);

        List<Call> collect = calls().stream().filter(compositePredicate).collect(Collectors.toList());

    }

    @Test//(expected = VerificationException.class)
    public void andPredicate() {
        Predicate<Call> and = new DecoratorPredicate()
                .and(new TypePredicate("java.util.List"))
                .or(new TypePredicate("java.util.Collection"))
                .and(new MethodPredicate("add"));

//        
        List<Call> collect = calls().stream().filter(and).collect(Collectors.toList());

    }

    @Test
    public void decoratorPredicate() {
        Predicate<Call> compositePredicate = DecoratorPredicate
                .and(new MethodPredicate("accept"),
                        new TypePredicate("java.util.List")
                );
        List<Call> calls = calls();
        System.out.println(calls);
        List<Call> collect = calls.stream().filter(compositePredicate).collect(Collectors.toList());

    }

    private static List<Call> calls() {
//        return Arrays.asList(
//                Call.of("ifpb.gpes.domain.HasJCFObject", "getElements[]", "java.util.List<ifpb.gpes.domain.HasJCFObject>", "ifpb.gpes.domain.LambdaWithArguments", "m1[]", "forEach[java.util.function.Consumer<? super ifpb.gpes.domain.HasJCFObject>]"),
//                Call.of("java.util.function.Predicate", "negate[]", "java.util.function.Predicate<ifpb.gpes.domain.HasJCFObject>", "java.lang.Object", "accept[ifpb.gpes.domain.HasJCFObject]", null));

        Project project = Project
                .root("")
                .path(sources + "ifpb/gpes/domain/LambdaWithArguments.java") // root
                .sources(sources) // root - não obrigatorio
                .filter(".java");

        return Parse.with(ParseStrategies.JDT).from(project);
    }

}

class MethodPredicate implements Predicate<Call> {

    private final String method;

    public MethodPredicate(String add) {
        this.method = add;
    }

    @Override
    public boolean test(Call t) {
        String retorno = t.getCallMethod();
        return retorno == null ? false : t.getCallMethod().contains(method);
    }
}

class TypePredicate implements Predicate<Call> {

    private String value;

    public TypePredicate(String value) {
        this.value = value;
    }

    @Override
    public boolean test(Call t) {
        return t.isFrom(value);
    }

}

class DecoratorPredicate<T> {

    public static <T> Predicate<T> and(Predicate<T>... predicates) {
        Predicate<T> compositePredicate
                = Arrays.asList(predicates).stream().reduce(c -> true, Predicate::and);
//        Arrays.asList(predicates).stream().reduce(Predicate::and).orElse(x->true);
//        Arrays.asList(predicates).stream().reduce(Predicate::or).orElse(x->false);
        return compositePredicate;
    }

}
