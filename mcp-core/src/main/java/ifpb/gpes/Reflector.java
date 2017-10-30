package ifpb.gpes;

import java.util.HashMap;

/**
 *
 * @author juan
 */
public class Reflector {

    private static final HashMap primitiveMap = new HashMap<String, Object>();

    static {
        primitiveMap.put("int", int.class);
        primitiveMap.put("long", long.class);
        primitiveMap.put("double", double.class);
        primitiveMap.put("char", char.class);
        primitiveMap.put("short", short.class);
        primitiveMap.put("byte", byte.class);
        primitiveMap.put("boolean", boolean.class);
        primitiveMap.put("void", void.class);
    }

    public static boolean isAssignableFrom(String classe, String nomeDaClasse) {
        if (nomeDaClasse.contains("[]")){
            String replacedClass = nomeDaClasse.replaceAll("\\[]", "");
            return isAssignableFrom(classe, replacedClass);
        }if (primitiveMap.containsKey(nomeDaClasse)) {
            return false;
        }
        try {
            Class baseClass = Class.forName(classe);
            Class classToCompare = Class.forName(nomeDaClasse);
            return baseClass.isAssignableFrom(classToCompare);
        } catch (Exception ex) {
            throw new VerificationException((ex));
        }
    }
}
