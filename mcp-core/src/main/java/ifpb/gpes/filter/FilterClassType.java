package ifpb.gpes.filter;

import ifpb.gpes.Call;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 *
 * @author juan
 */
public class FilterClassType implements Predicate<Call> {

    private Class type;
    private List<Class> types;

    //estrategia para receber varios tipos de classes possiveis dentro do JCF    
    public FilterClassType(Class... types) {
        this.types = Arrays.asList(types);
    }

    public FilterClassType(Class type) {
        this.type = type;
    }

    @Override
    public boolean test(Call t) {
        //recupera a segunda parte da string do objeto type
        //defini a seguda parte pois todo tostring de Class segue um padrão 
        //"identificador nomeQualificado"
        String value = type.toString().split(" ")[1];
        return t.getReturnType().contains(value);
    }

//    @Override
//    public boolean test(Call t) {
//        return types.stream().anyMatch((oneType) -> {
//            String value = oneType.toString().split(" ")[1];
//            return t.getReturnType().contains(value);
//        });
//    }
}
