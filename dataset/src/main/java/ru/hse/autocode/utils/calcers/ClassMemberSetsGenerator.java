package ru.hse.autocode.utils.calcers;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.MethodCallExpr;

import java.util.ArrayList;
import java.util.List;

/**
 * Class members info generator
 */
public class ClassMemberSetsGenerator {
    /**
     * Collects all instance methods and fields names in sets
     * @param cur current AST node
     * @return class instance members info
     */
    public ClassMembersSets instanceMembers(Node cur) {
        ClassMembersSets result = new ClassMembersSets();

        if (cur instanceof ClassOrInterfaceDeclaration) {
            ClassOrInterfaceDeclaration decl = (ClassOrInterfaceDeclaration)cur;
            result.getTotal().add(decl.getName());
        }

        if (cur instanceof MethodDeclaration) {
            MethodDeclaration methodDeclaration = (MethodDeclaration)cur;
            result.getMethods().add(methodDeclaration.getName());
            result.getTotal().add(methodDeclaration.getName());
            return result;
        }

        if (cur instanceof FieldDeclaration) {
            FieldDeclaration decl = (FieldDeclaration)cur;
            List<VariableDeclarator> vars = decl.getVariables();
            if (vars != null) {
                for (VariableDeclarator vd :vars) {
                    if (vd.getId() != null) {
                        result.getTotal().add(vd.getId().getName());
                        result.getFields().add(vd.getId().getName());
                    }

                }
            }

            return result;
        }

        for (Node n: cur.getChildrenNodes()) {
            ClassMembersSets subres = instanceMembers(n);
            result.getTotal().addAll(subres.getTotal());
            result.getFields().addAll(subres.getFields());
            result.getMethods().addAll(subres.getMethods());
        }

        return result;
    }

    /**
     * Calculate method calls between members method
     * @param cur current AST node
     * @return method calls list
     */
    public List<String> methodsCalls(Node cur) {
        List<String> calls = new ArrayList<>();

        if (cur instanceof MethodCallExpr) {
            MethodCallExpr callExpr = (MethodCallExpr)cur;
            calls.add(callExpr.getName());
        }

        for (Node n: cur.getChildrenNodes()) {
            List<String> subres = methodsCalls(n);
            calls.addAll(subres);
        }

        return calls;
    }

    /**
     * Calculate method connectivity between members method
     * @param root root AST node
     * @return method calls list
     */
    public double calculateMethodsConnectivity(CompilationUnit root) {
        ClassMembersSets members = instanceMembers(root);

        return members.getMethods().isEmpty()
                ?
                0
                :
                (double)methodsCalls(root)
                        .stream()
                        .filter(s -> members.
                                getMethods()
                                .contains(s))
                        .count() / members
                        .getMethods()
                        .size();
    }
}
