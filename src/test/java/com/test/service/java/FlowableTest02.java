package com.test.service.java;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务标识及业务状态动态维护测试
 */
@Slf4j
public class FlowableTest02 {

    ProcessEngine processEngine = null;

    @BeforeEach
    public void test(){
        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:mysql://127.0.0.1:3306/flowable-learn2?serverTimezone=UTC&nullCatalogMeansCurrent=true")
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
                .addClasspathResource("2023年出差流程.bpmn20.xml") // 添加流程部署文件
                .name("2023年出差流程") // 设置部署流程的名称
                .deploy(); // 执行部署操作

        System.out.println("deployment.getId() = " + deployment.getId());
        System.out.println("deployment.getName() = " + deployment.getName());
        System.out.println("deployment.getKey() = " + deployment.getKey());
    }

    @Test
    @DisplayName("查看流程定义")
    public void testDeployQuery(){
        // 部署流程 获取RepositoryService对象
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 获取流程定义对象
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().list();
        /**
         * Preparing: SELECT RES.* from ACT_RE_PROCDEF RES WHERE RES.NAME_ = ? and RES.RESOURCE_NAME_ like ? and RES.DEPLOYMENT_ID_ = ? order by RES.ID_ asc
         * Parameters: Holiday Request(String), holiday-request.bpmn20.xml(String), 1(String)
         */
        for (ProcessDefinition processDefinition : processDefinitions){
            System.out.println("processDefinition.getId() = " + processDefinition.getId());
            System.out.println("processDefinition.getName() = " + processDefinition.getName());
            System.out.println("processDefinition.getDeploymentId() = " + processDefinition.getDeploymentId());
            System.out.println("processDefinition.getDescription() = " + processDefinition.getDescription());
            System.out.println("processDefinition.getResourceName() = " + processDefinition.getResourceName());
            System.out.println("processDefinition.isSuspended() = " + processDefinition.isSuspended());
            System.out.println();
        }
    }

    @Test
    @DisplayName("流程定义删除")
    public void testDeployDelete(){
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //删除流程，指定流程ID,如果部署的流程启动了就不允许删除了
        //repositoryService.deleteDeployment("2501");
        //第二个参数是级联删除，如果流程启动了 相关的任务一并被删除
        repositoryService.deleteDeployment("2501",true);
    }

    @Test
    @DisplayName("挂起/激活流程")
    public void test05(){
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId("a2023chuchai:1:20004")
                .singleResult();
        // 获取流程定义的状态
        boolean suspended = processDefinition.isSuspended();
        System.out.println("suspended = " + suspended);
        if(suspended){
            // 表示被挂起
            repositoryService.activateProcessDefinitionById(processDefinition.getId(),true,null);
            System.out.println("激活流程定义");
        }else{
            // 表示激活状态
            repositoryService.suspendProcessDefinitionById(processDefinition.getId(),true,null);
            System.out.println("挂起流程");
        }
    }


    @Test
    @DisplayName("流程实例删除")
    public void testDeployDeletedd(){
        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.deleteProcessInstance("5001","废除");
    }

    @Test
    @DisplayName("发起流程")
    public void testStartProcess(){
        //通过RuntimeService来启动流程实例
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //构建流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put("zuzhang","组长");
        variables.put("chejian","车间主任");
        variables.put("caiwu","财务");
        variables.put("zongjingli","总经理");
        variables.put("num",1000);
        ProcessInstance holidayRequest = runtimeService.startProcessInstanceByKey("a2023chuchai","dagongzai", variables);
        System.out.println("流程定义的ID：" + holidayRequest.getProcessDefinitionId());
        System.out.println("流程实例的ID：" + holidayRequest.getId());
        System.out.println("当前活动的ID：" + holidayRequest.getActivityId());
    }

    @Test
    @DisplayName("组长查询代办任务")
    public void testQueryTask(){
        queryTask("组长");
    }

    @Test
    @DisplayName("组长-完成任务")
    public void testCompleteTask(){
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery()
                .taskAssignee("组长")
                .list();
        // 添加流程变量
        Map<String,Object> variables = new HashMap<>();
        variables.put("result","组长-通过"); // 拒绝请假
        // 完成任务
        for (Task task:tasks){
            taskService.complete(task.getId(),variables);
            System.out.println("通过 "+task.getId()+" "+task.getName()+" 流程实例ID："+task.getProcessInstanceId());
        }
    }

    @Test
    @DisplayName("车间主任查询代办任务")
    public void testQueryTask1(){
        queryTask("车间主任");
    }

    @Test
    @DisplayName("车间主任-修改报销金额")
    public void update001(){
        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.setVariable("22507","num",900);
        System.out.println("车间主任-修改报销金额");
        queryTask("车间主任");
    }

    @Test
    @DisplayName("车间主任-完成任务")
    public void testCompleteTask1(){
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery()
                .taskAssignee("车间主任")
                .list();
        // 添加流程变量
        Map<String,Object> variables = new HashMap<>();
        variables.put("result","车间主任-通过"); // 拒绝请假
        // 完成任务
        for (Task task:tasks){
            taskService.complete(task.getId(),variables);
            System.out.println("通过 "+task.getId()+" "+task.getName()+" 流程实例ID："+task.getProcessInstanceId());
        }
    }

    @Test
    @DisplayName("总经理-查询代办任务")
    public void testQueryTask12(){
        queryTask("总经理");
    }

    @Test
    @DisplayName("财务-查询代办任务")
    public void testQueryTask1222(){
        queryTask("财务");
    }
    @Test
    @DisplayName("总经理-完成任务")
    public void testCompleteTask1w(){
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery()
                .taskAssignee("总经理")
                .list();
        // 添加流程变量
        Map<String,Object> variables = new HashMap<>();
        variables.put("result","总经理-通过");
        variables.put("remark","财务管好账目啊");
        // 完成任务
        for (Task task:tasks){
            taskService.complete(task.getId(),variables);
            System.out.println("通过 "+task.getId()+" "+task.getName()+" 流程实例ID："+task.getProcessInstanceId());
        }
    }

    @Test
    @DisplayName("财务-设置审批业务状态")
    public void update002(){
        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.setVariable("22507","businessStatus","pass");
        System.out.println("车间主任-设置审批业务状态");
        queryTask("车间主任");
    }

    @Test
    @DisplayName("财务-完成任务")
    public void testCompleteTask1erw(){
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery()
                .taskAssignee("财务")
                .list();
        // 添加流程变量
        Map<String,Object> variables = new HashMap<>();
        variables.put("result","财务-通过");
        variables.put("caiwu-remark","好的,经理");
        // 完成任务
        for (Task task:tasks){
            taskService.complete(task.getId(),variables);
            System.out.println("通过 "+task.getId()+" "+task.getName()+" 流程实例ID："+task.getProcessInstanceId());
        }
    }

    @Test
    @DisplayName("流程发起人查询历史流程")
    public void fdfsdf(){
        processHiByProcessInstanceBusinessKey("dagongzai");
    }

    @Test
    @DisplayName("组长-查询指定历史流程")
    public void testQueryHistory2(){
        processHi("组长");
        System.out.println("********************");
        processHi("车间主任");
        System.out.println("********************");
        processHi("总经理");
        System.out.println("********************");
        processHi("财务");
        System.out.println("********************");
    }

    @Test
    @DisplayName("查询指定流程实例下审批明细")
    public void testQueryHide(){
        processHiDe("2501");
    }

    /**
     * 查询指定用户的代办任务
     */
    private void queryTask(String taskCandidateOrAssigned){
        TaskService taskService = processEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery()
                .taskCandidateOrAssigned(taskCandidateOrAssigned)
                .includeCaseVariables()
                .includeProcessVariables()
                .includeTaskLocalVariables()
                .list();
        for (Task task : list) {
            System.out.println("代办任务:");
            System.out.println("task.getProcessDefinitionId() = " + task.getProcessDefinitionId());
            System.out.println("task.getId() = " + task.getId());
            System.out.println("task.getAssignee() = " + task.getAssignee());
            System.out.println("task.getName() = " + task.getName());
            System.out.println("task.getCaseVariables()= "+task.getCaseVariables());
            System.out.println("task.getTaskLocalVariables()= "+task.getTaskLocalVariables());
            System.out.println("task.getProcessVariables()= "+task.getProcessVariables());
            System.out.println("task.getProcessVariables()= "+task.getDueDate());
            System.out.println("task.getExecutionId()= "+task.getExecutionId());
            System.out.println();
        }
    }

    /**
     * 查询审批历史流程
     */
    private void processHi(String involvedUser){
        HistoryService historyService = processEngine.getHistoryService();
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery()
                .involvedUser(involvedUser)
                .includeProcessVariables()
                .orderByProcessInstanceStartTime().desc().list();
        for (HistoricProcessInstance historicProcessInstance : list) {
            System.out.println("第历史审批*****流程   "+involvedUser);
            System.out.println("getId = "+historicProcessInstance.getId());
            System.out.println("getName = "+historicProcessInstance.getName());
            System.out.println("getStartTime = "+historicProcessInstance.getStartTime());
            System.out.println("getEndTime = "+historicProcessInstance.getEndTime());
            System.out.println("getDurationInMillis = "+historicProcessInstance.getDurationInMillis());
            System.out.println("getDeploymentId = "+historicProcessInstance.getDeploymentId());
            System.out.println("getDurationInMillis = "+historicProcessInstance.getProcessDefinitionId());
            System.out.println("getProcessDefinitionKey = "+historicProcessInstance.getProcessDefinitionKey());
            System.out.println("getProcessDefinitionName = "+historicProcessInstance.getProcessDefinitionName());
            System.out.println("getProcessVariables = "+historicProcessInstance.getProcessVariables());
            System.out.println("getBusinessStatus = "+historicProcessInstance.getBusinessStatus());
            System.out.println("getBusinessKey = "+historicProcessInstance.getBusinessKey());
        }
    }

    /**
     * 查询审批历史流程
     */
    private void processHiByProcessInstanceBusinessKey(String processInstanceBusinessKey){
        HistoryService historyService = processEngine.getHistoryService();
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery()
                .processInstanceBusinessKey(processInstanceBusinessKey)
                .includeProcessVariables()
                .orderByProcessInstanceStartTime().desc().list();
        for (HistoricProcessInstance historicProcessInstance : list) {
            System.out.println("发起人历史*****流程   "+processInstanceBusinessKey);
            System.out.println("getId = "+historicProcessInstance.getId());
            System.out.println("getName = "+historicProcessInstance.getName());
            System.out.println("getStartTime = "+historicProcessInstance.getStartTime());
            System.out.println("getEndTime = "+historicProcessInstance.getEndTime());
            System.out.println("getDurationInMillis = "+historicProcessInstance.getDurationInMillis());
            System.out.println("getDeploymentId = "+historicProcessInstance.getDeploymentId());
            System.out.println("getDurationInMillis = "+historicProcessInstance.getProcessDefinitionId());
            System.out.println("getProcessDefinitionKey = "+historicProcessInstance.getProcessDefinitionKey());
            System.out.println("getProcessDefinitionName = "+historicProcessInstance.getProcessDefinitionName());
            System.out.println("getProcessVariables = "+historicProcessInstance.getProcessVariables());
            System.out.println("getBusinessStatus = "+historicProcessInstance.getBusinessStatus());
            System.out.println("getBusinessKey = "+historicProcessInstance.getBusinessKey());
        }
    }

    /**
     * 查询流程审批明细
     */
    private void processHiDe(String processInstanceId){
        HistoryService historyService = processEngine.getHistoryService();
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricTaskInstanceStartTime().asc()
                .list();

        for (HistoricTaskInstance historicTaskInstance : list) {
            System.out.println("流程实例ID 审批明细   "+processInstanceId);
            System.out.println("getId = "+historicTaskInstance.getId());
            System.out.println("getName = "+historicTaskInstance.getName());
            System.out.println("getCreateTime = "+historicTaskInstance.getCreateTime());
            System.out.println("getEndTime = "+historicTaskInstance.getEndTime());
            System.out.println("getDurationInMillis = "+historicTaskInstance.getDurationInMillis());
            System.out.println("getWorkTimeInMillis = "+historicTaskInstance.getWorkTimeInMillis());
            System.out.println("getProcessDefinitionId = "+historicTaskInstance.getProcessDefinitionId());
            System.out.println("getProcessInstanceId = "+historicTaskInstance.getProcessInstanceId());
        }
    }
}
















































