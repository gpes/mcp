package ifpb.gpes.jdt;

import ifpb.gpes.Call;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import org.eclipse.jdt.core.dom.*;

//"Nem todos aqueles vagueiam estao perdidos" - Gandalf
public class DefaultVisitor extends ASTVisitor {

    private List<Call> calls;

    private MethodDeclaration currentMethodDeclaration;
    private Expression currentExpression;
    //TODO: criar uma classe para fazer substituir
    private final Stack<MethodDeclaration> stackMethodDeclaration = new Stack<>();

    public DefaultVisitor() {
        this(new ArrayList<>());
    }

    public DefaultVisitor(List<Call> elements) {
        this.calls = elements;
    }

    @Override
    public boolean visit(MethodDeclaration md) {
        this.currentMethodDeclaration = md;
        this.currentExpression = null;
        return super.visit(md);
    }

    @Override
    public boolean visit(LambdaExpression node) {
        this.currentExpression = node;
        return super.visit(node);
    }

    @Override
    public void endVisit(LambdaExpression node) {
        this.currentExpression = null;
        super.endVisit(node);
    }

    @Override
    public boolean visit(ExpressionMethodReference node) {
        Expression ex = node.getExpression();
        Expression castedEx = (Expression) node;
        String methodName = fillMethodName(
                node.getName().getFullyQualifiedName(),
                node.resolveTypeBinding().getTypeArguments()
        );
        IMethodBinding imb = castedEx.resolveTypeBinding().getFunctionalInterfaceMethod();
        String calledInMethod = fillMethodName(imb.getName(), imb.getParameterTypes());

        Call no = new Call();

        no.setClassType(ex.resolveTypeBinding().getQualifiedName());
        no.setMethodName(methodName);
        no.setReturnType(node.getName().resolveTypeBinding().toString());
        no.setCalledInClass(castedEx.resolveTypeBinding().getQualifiedName());
        no.setCalledInMethod(calledInMethod);
        no.setCalledInMethodReturnType(imb.getReturnType().getQualifiedName());
        no.setCallMethod(null);

        no.setInvokedBy(ex.toString());
        calls.add(no);

        return super.visit(node);
    }

    @Override
    public void endVisit(ExpressionMethodReference node) {
        super.endVisit(node);
    }

    @Override
    public boolean visit(AnonymousClassDeclaration node) {
        this.stackMethodDeclaration.push(currentMethodDeclaration);
        return super.visit(node);
    }

    @Override
    public void endVisit(AnonymousClassDeclaration node) {
        this.currentMethodDeclaration = stackMethodDeclaration.pop();
        super.endVisit(node);
    }

    @Override
    public boolean visit(MethodInvocation mi) {
        Call no = new Call();
        String classType = "not found";
        String returnType = "not found";
        IMethodBinding imb = mi.resolveMethodBinding();
        ITypeBinding[] bindings = {};
        if (imb != null) {
            bindings = imb.getParameterTypes();
            classType = imb.getDeclaringClass().getBinaryName();
            returnType = imb.getReturnType().getQualifiedName();
        }
        
        no.setClassType(classType);
        no.setReturnType(returnType);

        String methodName = fillMethodName(mi.getName().toString(), bindings);
        no.setMethodName(methodName);

        ITypeBinding callClass = inferCalledInClass(currentMethodDeclaration, currentMethodDeclaration.resolveBinding());

        String calledInClass;

        if (callClass != null) {
            calledInClass = callClassToString(callClass);
        } else {
            TypeDeclaration parent = (TypeDeclaration) currentMethodDeclaration.getParent();
            calledInClass = ((CompilationUnit) parent.getParent()).getPackage().getName() + "." + parent.getName().getFullyQualifiedName();
        }

        no.setCalledInClass(calledInClass);

        bindings = inferMethodDeclarationParamenters(currentMethodDeclaration, currentMethodDeclaration.resolveBinding());

        String calledInMethod = fillMethodName(currentMethodDeclaration.getName().getIdentifier(), bindings);
        no.setCalledInMethod(calledInMethod);

        if (currentMethodDeclaration.resolveBinding() != null) {
            no.setCalledInMethodReturnType(currentMethodDeclaration.resolveBinding().getReturnType().getQualifiedName());
        } else {
            Type returnType2 = currentMethodDeclaration.getReturnType2();
            no.setCalledInMethodReturnType(retrieveCalledInMethodReturnTypeValue(returnType2, currentMethodDeclaration));
        }

        if (currentExpression != null) { //lambda
            no.setCalledInClass(currentExpression
                    .resolveTypeBinding()
                    .getQualifiedName());
            ITypeBinding[] bindingsLambda = currentExpression.resolveTypeBinding().
                    getTypeArguments();
            String calledInMethodLambda = fillMethodName(currentExpression
                    .resolveTypeBinding()
                    .getFunctionalInterfaceMethod()
                    .getName(), bindingsLambda);
            no.setCalledInMethod(calledInMethodLambda);
            String calledInMethodLambdaReturnType = currentExpression
                    .resolveTypeBinding()
                    .getFunctionalInterfaceMethod()
                    .getReturnType().getQualifiedName();
            no.setCalledInMethodReturnType(calledInMethodLambdaReturnType);
        }

        int count = calls.size();

        //TODO: podemos usar a ideia do Stack para analisar?
        no.setInvokedBy(updateInv(mi));

        String methodInvocation = getMethodInvocation(count, mi.getName().toString());

        no.setCallMethod(methodInvocation);
        calls.add(no);
        return super.visit(mi);
    }

    @Override
    public void endVisit(MethodDeclaration node) {
        this.currentMethodDeclaration = node;
        this.currentExpression = null;
        super.visit(node);
    }

    public String callClassToString(ITypeBinding callClass) {
        if (callClass.isAnonymous()) {
            return callClass.getSuperclass().getQualifiedName();
        }
        return callClass.getQualifiedName();
    }

    private String updateInv(MethodInvocation mi) {
        Expression inv = mi.getExpression();
        if (inv == null) {
            return "this";
        }
        return inv.toString();
    }

    private String getMethodInvocation(int count, String methodName) {
        if (count < 1) {
            return null;
        }

        if (!calls.get(count - 1).getInvokedBy().contains(methodName)) {
            return null;
        }

        return calls.get(count - 1).getMethodName();
    }

    private String fillMethodName(String methodName, ITypeBinding[] bindings) {
        String prefix = methodName + "[";
        String sufix = "]";
        return Arrays.stream(bindings)
                .map(ITypeBinding::getQualifiedName)
                .collect(Collectors.joining(", ", prefix, sufix));
    }

    private ITypeBinding inferCalledInClass(MethodDeclaration md, IMethodBinding binding) {
        return binding != null ? binding.getDeclaringClass() : ((TypeDeclaration) md.getParent()).resolveBinding();
    }

    private ITypeBinding[] inferMethodDeclarationParamenters(MethodDeclaration md, IMethodBinding binding) {
        if (binding != null) {
            return md.resolveBinding().getParameterTypes();
        }
        List<ITypeBinding> types = (List<ITypeBinding>) md.parameters().stream().map(p -> ((SingleVariableDeclaration) p).resolveBinding().getType()).collect(Collectors.toList());
        return types.toArray(new ITypeBinding[types.size()]);
    }

    // since an unresolved binding can return an ArrayType, it hasn't a Name to get, we need to get the final string here.
    private String retrieveCalledInMethodReturnTypeValue(Type type, MethodDeclaration md) {
        if (!md.isConstructor()) {
            if (type.resolveBinding() == null) {
                if (type instanceof SimpleType sp) {
                    return sp.getName().getFullyQualifiedName();
                } else {
                    return type.toString();
                }
            } else {
                return type.resolveBinding().getQualifiedName();
            }
        } else {
            return PrimitiveType.VOID.toString();
        }
    }

    public List<Call> methodsCall() {
        return Collections.unmodifiableList(calls);
    }

    public List<Call> methodsCallFilter() {
        return Collections
                .unmodifiableList(calls.stream()
                        .filter(t -> t.getCallMethod() != null || "null".equalsIgnoreCase(t.getCallMethod()))
                        .collect(Collectors.toList()));
    }
}
