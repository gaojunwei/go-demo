package com.go.groovy.groovy.service.enums;

public enum GroovyScriptEnum {
    SPRING_METHOD_1("groovy_script", "dev_group", "doWork", "groovy调用bean实例的无参方法"),
    SPRING_METHOD_2("groovy_script", "dev_group", "doWork2", "groovy调用bean实例的有参方法");

    private String dataId;
    private String groupId;
    private String methodName;
    private String desc;

    GroovyScriptEnum(String dataId, String groupId, String methodName, String desc) {
        this.dataId = dataId;
        this.groupId = groupId;
        this.methodName = methodName;
        this.desc = desc;
    }

    public String getKey() {
        return this.dataId.concat("|").concat(this.groupId);
    }

    public String getDataId() {
        return dataId;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getDesc() {
        return desc;
    }
}
