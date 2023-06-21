package com.test.service.java;

import liquibase.pro.packaged.S;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;

/**
 * 并行网关及流程关联form测试
 */
@Slf4j
public class FlowableTest04 {

    ProcessEngine processEngine = null;

    @BeforeEach
    public void test() {
        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:mysql://127.0.0.1:3306/flowable-learn4?serverTimezone=UTC&nullCatalogMeansCurrent=true")
                .setJdbcUsername("root")
                .setJdbcPassword("tiger")
                .setJdbcDriver("com.mysql.cj.jdbc.Driver")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        processEngine = cfg.buildProcessEngine();
    }

    @Test
    @DisplayName("部署流程")
    public void testDeploy() throws IOException {
        // 部署流程 获取RepositoryService对象
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()// 创建Deployment对象
                .addClasspathResource("2023公司请假申请流程-包容网关.bpmn20.xml") // 添加流程部署文件
                .name("2023公司请假申请流程-包容网关") // 设置部署流程的名称
                .deploy(); // 执行部署操作

        System.out.println("deployment.getId() = " + deployment.getId());
        System.out.println("deployment.getName() = " + deployment.getName());
        System.out.println("deployment.getKey() = " + deployment.getKey());
    }

    @Test
    @DisplayName("查看流程定义")
    public void testDeployQuery() {
        // 部署流程 获取RepositoryService对象
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 获取流程定义对象
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().list();
        /**
         * Preparing: SELECT RES.* from ACT_RE_PROCDEF RES WHERE RES.NAME_ = ? and RES.RESOURCE_NAME_ like ? and RES.DEPLOYMENT_ID_ = ? order by RES.ID_ asc
         * Parameters: Holiday Request(String), holiday-request.bpmn20.xml(String), 1(String)
         */
        for (ProcessDefinition processDefinition : processDefinitions) {
            System.out.println("processDefinition.getId() = " + processDefinition.getId());
            System.out.println("processDefinition.getName() = " + processDefinition.getName());
            System.out.println("processDefinition.getKey() = " + processDefinition.getKey());
            System.out.println("processDefinition.getDeploymentId() = " + processDefinition.getDeploymentId());
            System.out.println("processDefinition.getDescription() = " + processDefinition.getDescription());
            System.out.println("processDefinition.getResourceName() = " + processDefinition.getResourceName());
            System.out.println("processDefinition.isSuspended() = " + processDefinition.isSuspended());
            System.out.println();
        }
    }

    @Test
    @DisplayName("流程定义删除")
    public void testDeployDelete() {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //删除流程，指定流程ID,如果部署的流程启动了就不允许删除了
        //repositoryService.deleteDeployment("2501");
        //第二个参数是级联删除，如果流程启动了 相关的任务一并被删除
        repositoryService.deleteDeployment("40001", true);
    }

    @Test
    @DisplayName("挂起/激活流程")
    public void test05() {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId("a2023chuchai:1:20004")
                .singleResult();
        // 获取流程定义的状态
        boolean suspended = processDefinition.isSuspended();
        System.out.println("suspended = " + suspended);
        if (suspended) {
            // 表示被挂起
            repositoryService.activateProcessDefinitionById(processDefinition.getId(), true, null);
            System.out.println("激活流程定义");
        } else {
            // 表示激活状态
            repositoryService.suspendProcessDefinitionById(processDefinition.getId(), true, null);
            System.out.println("挂起流程");
        }
    }


    @Test
    @DisplayName("流程实例删除")
    public void testDeployDeletedd() {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.deleteProcessInstance("5001", "废除");
    }

    @Test
    @DisplayName("发起流程")
    public void testStartProcess() {
        //通过RuntimeService来启动流程实例
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //构建流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put("dagongzai", "打工仔");
        variables.put("jingli", "经理");
        variables.put("shangji", "上级");
        variables.put("caiwu", "财务");
        variables.put("renshi", "人事");
        variables.put("num", 2);
        ProcessInstance holidayRequest = runtimeService.startProcessInstanceByKey("a2023_chuchai_key", "dagongzai", variables);
        System.out.println("流程定义的ID：" + holidayRequest.getProcessDefinitionId());
        System.out.println("流程实例的ID：" + holidayRequest.getId());
        System.out.println("当前活动的ID：" + holidayRequest.getActivityId());
    }

    @Test
    @DisplayName("打工仔-完成审批")
    public void testCompleteTask() {
        doCheck("打工仔");
    }

    @Test
    @DisplayName("项目经理-查询代办任务")
    public void testQueryTask12() {
        queryTask("打工仔");
        queryTask("经理");
        queryTask("上级");
        queryTask("财务");
        queryTask("人事");
    }

    @Test
    @DisplayName("经理-完成审批")
    public void testCompleteTaskee() {
        doCheck("经理");
    }

    @Test
    @DisplayName("上级-完成审批")
    public void testCompleteTaskesse() {
        doCheck("上级");
    }

    @Test
    @DisplayName("财务-完成审批")
    public void testCompleteTaskeeee() {
        doCheck("财务");
    }


    @Test
    @DisplayName("人事-完成审批")
    public void testCompleteTaskesee() {
        doCheck("人事");
    }


    @Test
    @DisplayName("流程发起人查询历史流程")
    public void fdfsdf() {
        processHiByProcessInstanceBusinessKey("dagongzai");
    }

    @Test
    @DisplayName("组长-查询指定历史流程")
    public void testQueryHistory2() {
        processHi("打工仔");
        System.out.println("********************");
        processHi("经理");
        System.out.println("********************");
        processHi("上级");
        System.out.println("********************");
        processHi("财务");
        System.out.println("********************");
        processHi("人事");
        System.out.println("********************");
    }

    @Test
    @DisplayName("查询指定流程实例下审批明细")
    public void testQueryHide() {
        processHiDe("2501");
    }

    /**
     * 查询指定用户的代办任务
     */
    private void queryTask(String taskCandidateOrAssigned) {
        TaskService taskService = processEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery()
                .taskCandidateOrAssigned(taskCandidateOrAssigned)
                .includeCaseVariables()
                .includeProcessVariables()
                .includeTaskLocalVariables()
                .list();
        System.out.println(taskCandidateOrAssigned + " 代办任务列表 ：");
        for (Task task : list) {
            System.out.println("代办任务:");
            System.out.println("task.getProcessDefinitionId() = " + task.getProcessDefinitionId());
            System.out.println("task.getId() = " + task.getId());
            System.out.println("task.getAssignee() = " + task.getAssignee());
            System.out.println("task.getName() = " + task.getName());
            System.out.println("task.getFormKey() = " + task.getFormKey());//流程关联的form表单标识
            System.out.println("task.getCaseVariables()= " + task.getCaseVariables());
            System.out.println("task.getTaskLocalVariables()= " + task.getTaskLocalVariables());
            System.out.println("task.getProcessVariables()= " + task.getProcessVariables());
            System.out.println("task.getProcessVariables()= " + task.getDueDate());
            System.out.println("task.getExecutionId()= " + task.getExecutionId());
            System.out.println();
        }
    }

    /**
     * 查询审批历史流程
     */
    private void processHi(String involvedUser) {
        HistoryService historyService = processEngine.getHistoryService();
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery()
                .finished()//已结束的流程
                .involvedUser(involvedUser)
                .includeProcessVariables()
                .orderByProcessInstanceStartTime().desc().list();
        for (HistoricProcessInstance historicProcessInstance : list) {
            System.out.println("第历史审批*****流程   " + involvedUser);
            System.out.println("getId = " + historicProcessInstance.getId());
            System.out.println("getName = " + historicProcessInstance.getName());
            System.out.println("getStartTime = " + historicProcessInstance.getStartTime());
            System.out.println("getEndTime = " + historicProcessInstance.getEndTime());
            System.out.println("getDurationInMillis = " + historicProcessInstance.getDurationInMillis());
            System.out.println("getDeploymentId = " + historicProcessInstance.getDeploymentId());
            System.out.println("getDurationInMillis = " + historicProcessInstance.getProcessDefinitionId());
            System.out.println("getProcessDefinitionKey = " + historicProcessInstance.getProcessDefinitionKey());
            System.out.println("getProcessDefinitionName = " + historicProcessInstance.getProcessDefinitionName());
            System.out.println("getProcessVariables = " + historicProcessInstance.getProcessVariables());
            System.out.println("getBusinessStatus = " + historicProcessInstance.getBusinessStatus());
            System.out.println("getBusinessKey = " + historicProcessInstance.getBusinessKey());
        }
    }

    /**
     * 查询审批历史流程
     */
    private void processHiByProcessInstanceBusinessKey(String processInstanceBusinessKey) {
        HistoryService historyService = processEngine.getHistoryService();
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery()
                .processInstanceBusinessKey(processInstanceBusinessKey)
                .includeProcessVariables()
                .orderByProcessInstanceStartTime().desc().list();
        for (HistoricProcessInstance historicProcessInstance : list) {
            System.out.println("发起人历史*****流程   " + processInstanceBusinessKey);
            System.out.println("getId = " + historicProcessInstance.getId());
            System.out.println("getName = " + historicProcessInstance.getName());
            System.out.println("getStartTime = " + historicProcessInstance.getStartTime());
            System.out.println("getEndTime = " + historicProcessInstance.getEndTime());
            System.out.println("getDurationInMillis = " + historicProcessInstance.getDurationInMillis());
            System.out.println("getDeploymentId = " + historicProcessInstance.getDeploymentId());
            System.out.println("getDurationInMillis = " + historicProcessInstance.getProcessDefinitionId());
            System.out.println("getProcessDefinitionKey = " + historicProcessInstance.getProcessDefinitionKey());
            System.out.println("getProcessDefinitionName = " + historicProcessInstance.getProcessDefinitionName());
            System.out.println("getProcessVariables = " + historicProcessInstance.getProcessVariables());
            System.out.println("getBusinessStatus = " + historicProcessInstance.getBusinessStatus());
            System.out.println("getBusinessKey = " + historicProcessInstance.getBusinessKey());
        }
    }

    /**
     * 查询流程审批明细
     */
    private void processHiDe(String processInstanceId) {
        HistoryService historyService = processEngine.getHistoryService();
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricTaskInstanceStartTime().asc()
                .list();

        for (HistoricTaskInstance historicTaskInstance : list) {
            System.out.println("流程实例ID 审批明细   " + processInstanceId);
            System.out.println("getId = " + historicTaskInstance.getId());
            System.out.println("getName = " + historicTaskInstance.getName());
            System.out.println("getCreateTime = " + historicTaskInstance.getCreateTime());
            System.out.println("getEndTime = " + historicTaskInstance.getEndTime());
            System.out.println("getDurationInMillis = " + historicTaskInstance.getDurationInMillis());
            System.out.println("getWorkTimeInMillis = " + historicTaskInstance.getWorkTimeInMillis());
            System.out.println("getProcessDefinitionId = " + historicTaskInstance.getProcessDefinitionId());
            System.out.println("getProcessInstanceId = " + historicTaskInstance.getProcessInstanceId());
        }
    }

    /**
     * 完成审批
     */
    private void doCheck(String taskAssignee) {
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery()
                .taskAssignee(taskAssignee)
                .list();
        // 添加流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(taskAssignee + "result", taskAssignee + "-通过"); // 拒绝请假
        // 完成任务
        for (Task task : tasks) {
            if(Objects.equals("人事",taskAssignee)){
                //修改流程实例的业务状态
                updateBusinessStatus(task.getProcessInstanceId(),BusinessStatusEnums.pass);
            }
            taskService.complete(task.getId(), variables);
            System.out.println(taskAssignee + " 通过 " + task.getId() + " " + task.getName() + " 流程实例ID：" + task.getProcessInstanceId());
        }
    }

    /**
     * 设置业务状态
     */
    private void updateBusinessStatus(String processInstanceId,BusinessStatusEnums statusEnums) {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.updateBusinessStatus(processInstanceId, statusEnums.value);
        System.out.println("设置流程实例业务状态 processInstanceId="+processInstanceId+" statusEnums="+statusEnums.getValue()+"："+statusEnums.getDesc());
    }

    /**
     * 业务状态枚举
     */
    @Getter
    enum BusinessStatusEnums {
        pass("pass", "通过"),
        unPass("unPass", "未通过");

        private String value;
        private String desc;

        BusinessStatusEnums(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }
    }
}