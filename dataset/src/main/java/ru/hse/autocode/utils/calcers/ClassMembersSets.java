package ru.hse.autocode.utils.calcers;

import java.util.HashSet;
import java.util.Set;


/**
 * Instance members info for class
 */
public class ClassMembersSets {
    private Set<String> methods = new HashSet<>();
    private Set<String> fields = new HashSet<>();
    private final Set<String> total = new HashSet<>();

    public Set<String> getMethods() {
        return methods;
    }

    public void setMethods(Set<String> methods) {
        this.methods = methods;
    }

    public Set<String> getFields() {
        return fields;
    }

    public void setFields(Set<String> fields) {
        this.fields = fields;
    }

    public Set<String> getTotal() {
        return total;
    }
}
