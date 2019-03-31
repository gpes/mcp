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

    public FilterClassType(String type) {
        try {
            this.type = Class.forName(type);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean test(Call t) {
        try {
//        recupera a segunda parte da string do objeto type
//        defini a seguda parte pois todo tostring de Class segue um padrão
//        "identificador nomeQualificado"
//        String value = type.toString().split(" ")[1];
            if (t.getClassType() == null) {
                System.out.println(t);
                return false;
            }
            return t.getClassType().contains("java.util.")
                && type.isAssignableFrom(
                    Class.forName(
                        t.getClassType().split("<")[0]
                    )
                );
        } catch (ClassNotFoundException ex) {
            System.out.println("não foi possivel encontrar a classe: " + t.toString());
        }
        return false;
    }

//    @Override
//    public boolean test(Call t) {
//        return types.stream().anyMatch((oneType) -> {
//            String value = oneType.toString().split(" ")[1];
//            return t.getReturnType().contains(value);
//        });
//    }
}
