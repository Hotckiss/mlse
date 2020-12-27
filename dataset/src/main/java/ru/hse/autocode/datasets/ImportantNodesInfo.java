package ru.hse.autocode.datasets;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that stores important information about class
 */
public class ImportantNodesInfo {
    private List<String> imports = new ArrayList<>();
    private List<String> varsNames = new ArrayList<>();
    private List<String> varsTypes = new ArrayList<>();
    private String packageString = "";

    public ImportantNodesInfo() {
    }

    public List<String> getVarsNames() {
        return varsNames;
    }

    public void setVarsNames(List<String> varsNames) {
        this.varsNames = varsNames;
    }

    public List<String> getVarsTypes() {
        return varsTypes;
    }

    public void setVarsTypes(List<String> varsTypes) {
        this.varsTypes = varsTypes;
    }

    public List<String> getImports() {
        return imports;
    }

    public void setImports(List<String> imports) {
        this.imports = imports;
    }

    public String getPackageString() {
        return packageString;
    }

    public void setPackageString(String packageString) {
        this.packageString = packageString;
    }
}
