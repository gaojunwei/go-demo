package com.test.service.java;

import com.alibaba.fastjson2.JSON;
import liquibase.pro.packaged.S;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务标识及业务状态动态维护测试
 */
@Slf4j
public class FlowableTest02 {

    ProcessEngine processEngine = null;
    RuntimeService runtimeService = null;
    TaskService taskService = null;
    HistoryService historyService = null;

    @BeforeEach
    public void test(){
        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:mysql://127.0.0.1:3306/flowable-learn2?serverTimezone=UTC&nullCatalogMeansCurrent=true")
                .setJdbcUsername("root")
                .setJdbcPassword("tiger")
                .setJdbcDriver("com.mysql.cj.jdbc.Driver")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        processEngine = cfg.buildProcessEngine();
        runtimeService = processEngine.getRuntimeService();
        taskService = processEngine.getTaskService();
        historyService = processEngine.getHistoryService();
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
    @DisplayName("流程实例删除")
    public void test001(){
        runtimeService.deleteProcessInstance("5001","废除");
    }

    @Test
    @DisplayName("查询进行中的流程实例")
    public void test002(){
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().list();
        System.out.println("查询进行中的流程实例");
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    @DisplayName("查询历史流程实例")
    public void test004(){
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery()
                .includeProcessVariables().list();
        System.out.println("查询历史流程实例");
        System.out.println(JSON.toJSONString(list));
    }

    @Test
    @DisplayName("查询流程已完成任务列表")
    public void test005(){
        String processInstanceId = "2501";
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .taskWithoutDeleteReason()
                .orderByHistoricTaskInstanceStartTime().desc()
                .finished().list();

        System.out.println("查询流程任务列表");
        list.forEach(item->{
            System.out.println("*******************"+item.getProcessInstanceId());
            System.out.println("getId ="+item.getId());
            System.out.println("getProcessInstanceId ="+item.getProcessInstanceId());
            System.out.println("getName ="+item.getName());
            System.out.println("getTaskLocalVariables ="+JSON.toJSONString(item.getTaskLocalVariables()));
            System.out.println("getAssignee ="+item.getAssignee());
            System.out.println("getExecutionId ="+item.getExecutionId());
            System.out.println("getTaskDefinitionKey ="+item.getTaskDefinitionKey());
            System.out.println("getDeleteReason ="+item.getDeleteReason());
        });
    }

    /**
     * 已审批完成的任务无法修改处理人
     */
    @Test
    @DisplayName("已完成任务受让人修改")
    public void test006(){
        taskService.setAssignee("17504", "车间主任审批2");
    }

    @Test
    @DisplayName("删除流程实例")
    public void test007(){
        //historyService.deleteHistoricProcessInstance("27501");
        //次删除不会清除历史记录，只是将流程结束，并设置删除原因,然后调用historyService的删除即可
        //runtimeService.deleteProcessInstance("15001","终止");
        //historyService.deleteHistoricProcessInstance("15001");
    }

    @Test
    @DisplayName("查询任务信息")
    public void test008(){
        TaskQuery query = taskService.createTaskQuery();
        query.processInstanceId("2501");
        List<Task> tasks = query.list();
        printTask(tasks);
    }

    @Test
    @DisplayName("删除流程实例")
    public void test009(){
        taskService.addCandidateUser("2511","组长1");
        taskService.addCandidateUser("2511","组长2");
        taskService.addCandidateUser("2511","组长3");
    }

    @Test
    @DisplayName("查询任务候选人信息")
    public void test010(){
        List<IdentityLink> list = taskService.getIdentityLinksForTask("2511");
        list.forEach(item->{
            System.out.println("***候选人信息");
            System.out.println("getUserId = "+item.getUserId());
        });
    }

    @Test
    @DisplayName("设置受让人")
    public void test011(){
        taskService.setAssignee("12503","组长");
    }

    @Test
    @DisplayName("节点回退")
    public void test012(){
        runtimeService.createChangeActivityStateBuilder()
                .processInstanceId("2501")
                .moveActivityIdsToSingleActivityId(Arrays.asList("sid-602CE508-641C-43C7-8001-065A32DA56A5"), "sid-C9093C97-1EBC-4CA0-B6E1-791230846996")
                .changeState();
    }

    private void printTask(List<Task> list){
        list.forEach(task -> {
            System.out.println("*****************");
            System.out.println("getId ="+task.getId());
            System.out.println("getName ="+task.getName());
            System.out.println("getExecutionId ="+task.getExecutionId());
            System.out.println("getScopeId ="+task.getScopeId());
            System.out.println("getProcessDefinitionId ="+task.getProcessDefinitionId());
            System.out.println("getTaskDefinitionId ="+task.getTaskDefinitionId());
            System.out.println("getScopeDefinitionId ="+task.getScopeDefinitionId());
            System.out.println("getAssignee ="+task.getAssignee());
            System.out.println("getProcessVariables ="+JSON.toJSONString(task.getProcessVariables()));
        });
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
    @DisplayName("查询代办任务")
    public void testQueryTask(){
        queryTask("组长");
        queryTask("车间主任");
        queryTask("总经理");
        queryTask("财务");
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
    @DisplayName("车间主任-修改报销金额")
    public void update001(){
        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.setVariable("12501","num",900);
        System.out.println("车间主任-修改报销金额");
        queryTask("车间主任");
    }

    @Test
    @DisplayName("车间主任-完成任务")
    public void testCompleteTask1(){
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery()
                .taskCandidateOrAssigned("车间主任")
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
            System.out.println("代办任务:"+taskCandidateOrAssigned);
            System.out.println("task.getProcessDefinitionId() = " + task.getProcessDefinitionId());
            System.out.println("task.getId() = " + task.getId());
            System.out.println("task.getAssignee() = " + task.getAssignee());
            System.out.println("task.getName() = " + task.getName());
            System.out.println("task.getCaseVariables()= "+task.getCaseVariables());
            System.out.println("task.getTaskLocalVariables()= "+task.getTaskLocalVariables());
            System.out.println("task.getProcessVariables()= "+task.getProcessVariables());
            System.out.println("task.getProcessVariables()= "+task.getDueDate());
            System.out.println("task.getExecutionId()= "+task.getExecutionId());
            System.out.println("task.getTaskDefinitionKey()= "+task.getTaskDefinitionKey());
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
















































