package ifpb.gpes;

import java.util.Objects;

/**
 *
 * @author Juan
 */
public class Call {

    private String classType;
    private String methodName;
    private String returnType;
    private String calledInClass;
    private String calledInMethod;
    private String callMethod;
    private String invokedBy;

    public Call() {
    }

    public Call(String classType, String methodName, String returnType, String calledInClass,
            String calledInMethod, String callMethod) {
        this.classType = classType;
        this.methodName = methodName;
        this.returnType = returnType;
        this.calledInClass = calledInClass;
        this.calledInMethod = calledInMethod;
        this.callMethod = callMethod;
    }

    public static Call of(String classType, String methodName, String returnType, String calledInClass,
            String calledInMethod, String callMethod) {
        return new Call(classType, methodName, returnType, calledInClass, calledInMethod, callMethod);
    }

    //TODO: refatorar
    public static Call of(String line) {
        //java.util.List, add[ifpb.gpes.domain.HasJCFObject], boolean, ifpb.gpes.domain.SampleObject, m2[], null
        String[] fields = line.split(",");
        String campo = fields[5].trim();
        return new Call(fields[0].trim(), fields[1].trim(), fields[2].trim(), fields[3].trim(), fields[4].trim(), campo.equals("null") ? null : campo);
    }

    public boolean isFrom(String classe) throws VerificationException {
        try {
            System.out.println("\t"+returnType.split("<")[0]);
            Class classType = Class.forName(returnType.split("<")[0]);
            return Class.forName(classe).isAssignableFrom(classType);
        } catch (Exception ex) {
            throw new VerificationException(ex);
        }
    }

    public String getInvokedBy() {
        return invokedBy;
    }

    public void setInvokedBy(String invokedBy) {
        this.invokedBy = invokedBy;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getCalledInClass() {
        return calledInClass;
    }

    public void setCalledInClass(String calledInClass) {
        this.calledInClass = calledInClass;
    }

    public String getCalledInMethod() {
        return calledInMethod;
    }

    public void setCalledInMethod(String calledInMethod) {
        this.calledInMethod = calledInMethod;
    }

    public String getCallMethod() {
        return callMethod;
    }

    public void setCallMethod(String callMethod) {
        this.callMethod = callMethod;
    }

    @Override
    public String toString() {
        return "No{" + "classType=" + classType + ", methodName=" + methodName + ", returnType=" + returnType + ", calledInClass=" + calledInClass + ", calledInMethod=" + calledInMethod + ", callMethod=" + callMethod + ", invokedBy=" + invokedBy + '}';
    }

    public String callGraph() {
        return "<" + classType + ", " + methodName + ", " + returnType + ", " + calledInClass + ", " + calledInMethod + ", " + callMethod + ">";
    }

    public String noOf() {
        return "No.of(\"" + classType + "\", \"" + methodName + "\",\"" + returnType + "\",\"" + calledInClass + "\",\"" + calledInMethod + "\",\"" + callMethod + "\"),";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.classType);
        hash = 89 * hash + Objects.hashCode(this.methodName);
        hash = 89 * hash + Objects.hashCode(this.returnType);
        hash = 89 * hash + Objects.hashCode(this.calledInClass);
        hash = 89 * hash + Objects.hashCode(this.calledInMethod);
        hash = 89 * hash + Objects.hashCode(this.callMethod);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Call other = (Call) obj;
        if (!Objects.equals(this.classType, other.classType)) {
            return false;
        }
        if (!Objects.equals(this.methodName, other.methodName)) {
            return false;
        }
        if (!Objects.equals(this.returnType, other.returnType)) {
            return false;
        }
        if (!Objects.equals(this.calledInClass, other.calledInClass)) {
            return false;
        }
        if (!Objects.equals(this.calledInMethod, other.calledInMethod)) {
            return false;
        }
        if (!Objects.equals(this.callMethod, other.callMethod)) {
            return false;
        }
        return true;
    }

}
