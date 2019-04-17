package ifpb.gpes;

import java.util.HashMap;

/**
 *
 * @author juan
 */
public class AssignVerifier {

    private final HashMap primitiveMap;
    private final String baseClass;

    public AssignVerifier(String baseClass) {
        this.baseClass = baseClass;
        //
        primitiveMap = new HashMap<String, Object>();
        primitiveMap.put("int", int.class);
        primitiveMap.put("long", long.class);
        primitiveMap.put("double", double.class);
        primitiveMap.put("char", char.class);
        primitiveMap.put("short", short.class);
        primitiveMap.put("byte", byte.class);
        primitiveMap.put("boolean", boolean.class);
        primitiveMap.put("void", void.class);
    }

    public boolean isAssignable(String nomeDaClasse) {
        if (nomeDaClasse.contains("[]")){
            String replacedClass = nomeDaClasse.replaceAll("\\[]", "");
            return isAssignable(replacedClass);
        }if (primitiveMap.containsKey(nomeDaClasse)) {
            return false;
        }
        try {
            Class baseClass = Class.forName(this.baseClass);
            Class classToCompare = Class.forName(nomeDaClasse);
            return baseClass.isAssignableFrom(classToCompare);
        } catch (Exception ex) {
            System.out.println("An error occurred trying get a class from a string name, returning false.");
            return false;
        }
    }
}
