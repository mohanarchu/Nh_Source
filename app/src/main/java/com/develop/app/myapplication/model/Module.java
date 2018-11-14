
package com.develop.app.myapplication.model;

import java.util.List;

public class Module {

    private String moduleName;
    private String modulelistService;
    private String modulecountService;
    private List<SubModule> subModules = null;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModulelistService() {
        return modulelistService;
    }

    public void setModulelistService(String modulelistService) {
        this.modulelistService = modulelistService;
    }

    public String getModulecountService() {
        return modulecountService;
    }

    public void setModulecountService(String modulecountService) {
        this.modulecountService = modulecountService;
    }

    public List<SubModule> getSubModules() {
        return subModules;
    }

    public void setSubModules(List<SubModule> subModules) {
        this.subModules = subModules;
    }

}
