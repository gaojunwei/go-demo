package com.test.service.java;

import com.alibaba.fastjson2.JSON;
import liquibase.pro.packaged.S;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.*;
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
    RepositoryService repositoryService = null;

    @BeforeEach
    public void test() {
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
        repositoryService = processEngine.getRepositoryService();
    }

    @Test
    @DisplayName("部署流程")
    public void testDeploy() throws IOException {
        // 部署流程 获取RepositoryService对象
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
    public void test001() {
        // 此删除不会清除历史记录，只是将流程结束，并设置删除原因,然后调用historyService的删除即可
        runtimeService.deleteProcessInstance("45001", "终止");
        //historyService.deleteHistoricProcessInstance("2501");
    }

    @Test
    @DisplayName("查询进行中的流程实例")
    public void test002() {
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().list();
        System.out.println("查询进行中的流程实例");
        for (ProcessInstance processInstance : list){
            System.out.println("getId = "+ processInstance.getId());
            System.out.println("getProcessInstanceId = "+ processInstance.getProcessInstanceId());
            System.out.println("getName = "+ processInstance.getName());
            System.out.println("getStartUserId = "+ processInstance.getStartUserId());

            System.out.println("**********************");
        }


    }

    @Test
    @DisplayName("查询历史流程实例")
    public void test004() {
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery()
                .includeProcessVariables().list();
        System.out.println("查询历史流程实例");
        System.out.println(JSON.toJSONString(list));
    }


    @Test
    @DisplayName("删除历史任务")
    public void deleteHistoryTask() {
        //未完成的任务 不能进行删除
        String taskId = "57504";
        historyService.deleteHistoricTaskInstance(taskId);
    }

    @Test
    @DisplayName("查询流程实例历史审核任务")
    public void test00421() {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId("50001")
                .orderByHistoricTaskInstanceStartTime().desc()
                //.includeProcessVariables()
                //.finished()
                .list();
        System.out.println("查询流程实例历史审核任务 任务数:="+list.size());
        list.forEach(item->{
            System.out.println("********************");
            System.out.println("getId = "+item.getId());
            System.out.println("getName = "+item.getName());
            System.out.println("getTaskDefinitionKey = "+item.getTaskDefinitionKey());
            System.out.println("getAssignee = "+item.getAssignee());
            //System.out.println("getIdentityLinks = "+JSON.toJSONString(item.getIdentityLinks()));
            System.out.println("getFormKey = "+item.getFormKey());
            //System.out.println("getProcessVariables = "+JSON.toJSONString(item.getProcessVariables()));
            System.out.println("getCreateTime = "+item.getCreateTime());
            System.out.println("getEndTime = "+item.getEndTime());
            System.out.println("getTime = "+item.getTime());
            System.out.println("getExecutionId = "+item.getExecutionId());
            System.out.println("getDeleteReason = "+item.getDeleteReason());
        });
    }

    @Test
    @DisplayName("删除已完成的任务记录")
    public void test00422() {
        historyService.createHistoricTaskInstanceQuery().taskId("27506").finished().delete();
        System.out.println("删除已完成的任务记录");
    }

    /**
     * 参考网址：https://blog.csdn.net/qh870754310/article/details/99692923
     */
    @Test
    @DisplayName("节点回退")
    public void test012() {
        runtimeService.createChangeActivityStateBuilder()
                .processInstanceId("25001")
                .moveActivityIdTo("sid-CC04EA90-362D-431B-ACF0-556B2E85254E","sid-5AE4B702-3503-46AE-B81F-5D96F00EDD7A")
                //.moveActivityIdsToSingleActivityId(Arrays.asList("sid-602CE508-641C-43C7-8001-065A32DA56A5"), "sid-C9093C97-1EBC-4CA0-B6E1-791230846996")
                .changeState();
    }


    @Test
    @DisplayName("查询待处理任务信息")
    public void test008() {
        List<Task> list = taskService.createTaskQuery()
                .includeProcessVariables()
                .includeIdentityLinks().list();
        System.out.println("查询待处理任务信息****");
        list.forEach(task -> {
            System.out.println("*****************");
            System.out.println("getId =" + task.getId());
            System.out.println("getName =" + task.getName());
            System.out.println("getProcessInstanceId =" + task.getProcessInstanceId());
            System.out.println("getProcessDefinitionId =" + task.getProcessDefinitionId());
            System.out.println("getTaskDefinitionId =" + task.getTaskDefinitionId());
            System.out.println("getTaskDefinitionKey =" + task.getTaskDefinitionKey());
            System.out.println("getAssignee =" + task.getAssignee());
            System.out.println("getProcessVariables =" + JSON.toJSONString(task.getProcessVariables()));
            System.out.println("getIdentityLinks =" + JSON.toJSONString(task.getIdentityLinks()));
        });
    }

    @Test
    @DisplayName("添加任务候选人")
    public void test009() {
        taskService.addCandidateUser("2511", "组长1");
        taskService.addCandidateUser("2511", "组长2");
        taskService.addCandidateUser("2511", "组长3");
    }

    @Test
    @DisplayName("流程删除")
    public void testDeployDelete() {
        // 删除流程，指定流程ID,如果部署的流程启动了就不允许删除了
        // repositoryService.deleteDeployment("2501");
        // 第二个参数是级联删除，如果流程启动了 相关的任务一并被删除
        repositoryService.deleteDeployment("22501", true);
    }

    @Test
    @DisplayName("查询任务候选人信息")
    public void test010() {
        List<IdentityLink> list = taskService.getIdentityLinksForTask("2511");
        list.forEach(item -> {
            System.out.println("***候选人信息");
            System.out.println("getUserId = " + item.getUserId());
        });
    }

    @Test
    @DisplayName("设置受让人")
    public void test011() {
        // taskService.setAssignee("25019", "组长2");
        queryTask("组长2");
        queryTask("组长3");
    }

    @Test
    @DisplayName("新增候选人")
    public void test0111() {
        // taskService.deleteCandidateUser("25019", "组长4");
        // taskService.setAssignee("2519", null);
        queryTask("组长2");
        queryTask("车间主任1");
    }

    @Test
    @DisplayName("完成任务")
    public void testCompleteTask011() {
        //String userId = "组长1";
        String userId = "车间主任1";
        //String userId = "财务1";
        //String userId = "总经理1";
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery()
                .taskCandidateOrAssigned(userId)
                .list();
        System.out.println("待处理任务 *** 待处理人=" + userId+ " 待处理任务数="+tasks.size());
        // 添加流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put("result_"+userId, userId + "-通过"); // 拒绝请假
        // 完成任务
        for (Task task : tasks) {
            System.out.println();
            System.out.println("********* 任务ID = " + task.getId() + " " + task.getName());
            taskService.setAssignee(task.getId(),userId);
            taskService.complete(task.getId(), variables);
            System.out.println("通过 " + task.getId() + " " + task.getName() + " 流程实例ID：" + task.getProcessInstanceId());
        }
    }

    @Test
    @DisplayName("发起流程")
    public void testStartProcess() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("zuzhang", Arrays.asList("组长1", "组长2", "组长3"));
        variables.put("chejian", Arrays.asList("车间主任1", "车间主任2", "车间主任3"));
        variables.put("caiwu", Arrays.asList("财务1", "财务2", "财务3"));
        variables.put("zongjingli", Arrays.asList("总经理1", "总经理2", "总经理3"));
        variables.put("parentProcessId", "A0001");
        variables.put("num", 1000);
        ProcessInstance holidayRequest = runtimeService.startProcessInstanceByKey("a2023chuchai", "dagongzai", variables);
        System.out.println("流程定义的ID：" + holidayRequest.getProcessDefinitionId());
        System.out.println("流程实例的ID：" + holidayRequest.getId());
        System.out.println("当前活动的ID：" + holidayRequest.getActivityId());
    }

    @Test
    @DisplayName("根据变量查询流程")
    public void testStartProcess1() {
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery()
                .variableValueEquals("parentProcessId","A0001")
                .list();
        printHistoricProcessInstance(list ,null);
    }

    @Test
    @DisplayName("查询代办任务")
    public void testQueryTask() {
        queryTask("组长1");
        queryTask("车间主任1");
        queryTask("总经理1");
        queryTask("财务1");
    }

    @Test
    @DisplayName("组长-完成任务")
    public void testCompleteTask() {
        String userId = "总经理1";
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery()
                .taskCandidateOrAssigned(userId)
                .list();
        System.out.println("待处理任务 *** 待处理人=" + userId+ " 待处理任务数="+tasks.size());
        // 添加流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put("result_"+userId, userId + "-通过"); // 拒绝请假
        // 完成任务
        for (Task task : tasks) {
            System.out.println();
            System.out.println("********* 任务ID = " + task.getId() + " " + task.getName());
            taskService.complete(task.getId(), variables);
            System.out.println("通过 " + task.getId() + " " + task.getName() + " 流程实例ID：" + task.getProcessInstanceId());
        }
    }

    @Test
    @DisplayName("流程发起人查询历史流程")
    public void fdfsdf() {
        processHiByProcessInstanceBusinessKey("dagongzai");
    }

    @Test
    @DisplayName("查询指定流程实例下审批明细")
    public void testQueryHide() {
        processHiDe("25001");
    }

    /**
     * 查询指定用户的代办任务
     */
    private void queryTask(String taskCandidateOrAssigned) {
        List<Task> list = taskService.createTaskQuery()
                .taskCandidateOrAssigned(taskCandidateOrAssigned)
                .includeCaseVariables()
                .includeProcessVariables()
                .includeTaskLocalVariables()
                .includeIdentityLinks()
                .list();
        System.out.println("代办任务:" + taskCandidateOrAssigned);
        for (Task task : list) {
            System.out.println("task.getProcessDefinitionId() = " + task.getProcessDefinitionId());
            System.out.println("task.getProcessInstanceId() = " + task.getProcessInstanceId());
            System.out.println("task.getId() = " + task.getId());
            System.out.println("task.getAssignee() = " + task.getAssignee());
            System.out.println("task.getName() = " + task.getName());
            System.out.println("task.getCaseVariables()= " + task.getCaseVariables());
            System.out.println("task.getTaskLocalVariables()= " + task.getTaskLocalVariables());
            System.out.println("task.getProcessVariables()= " + task.getProcessVariables());
            System.out.println("task.getProcessVariables()= " + task.getDueDate());
            System.out.println("task.getExecutionId()= " + task.getExecutionId());
            System.out.println("task.getTaskDefinitionKey()= " + task.getTaskDefinitionKey());
            System.out.println("task.getTaskDefinitionId()= " + task.getTaskDefinitionId());
            System.out.println("task.getIdentityLinks()= " + JSON.toJSONString(task.getIdentityLinks()));
            System.out.println("task.getFormKey()= " + task.getFormKey());

            System.out.println("***候选人列表");
            task.getIdentityLinks().forEach(item -> {
                System.out.println("候选人ID = " + item.getUserId());
            });

            System.out.println();
        }
    }

    private void printHistoricProcessInstance(List<HistoricProcessInstance> list, String involvedUser){
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
            System.out.println("getStartUserId = " + historicProcessInstance.getStartUserId());
            System.out.println("getDeleteReason = " + historicProcessInstance.getDeleteReason());
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
            System.out.println("getTaskDefinitionKey = " + historicTaskInstance.getTaskDefinitionKey());
        }
    }

    @Test
    public void test00001() {
        // historyService.createHistoricTaskInstanceQuery().processDefinitionKey()

        HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId("2501").includeProcessVariables().singleResult();

        System.out.println("->>>>" + JSON.toJSONString(processInstance));
    }


    @Test
    public void update000001() {
        String processDefinitionKey = "a2023chuchai";

        // 获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();

        // 根据流程定义键获取流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processDefinitionKey)
                .singleResult();

        // 获取BPMN模型
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());

        // 获取所有流程元素
        List<FlowElement> flowElements = (List<FlowElement>) bpmnModel.getMainProcess().getFlowElements();

        // 获取TaskService
        TaskService taskService = processEngine.getTaskService();

        // 遍历每个流程元素，查找未产生任务的用户任务节点
        for (FlowElement flowElement : flowElements) {
            if (flowElement instanceof UserTask) {
                UserTask userTask = (UserTask) flowElement;

                System.out.println(userTask.getId());
                System.out.println(taskService.createTaskQuery().processDefinitionId(processDefinition.getId())
                        .taskDefinitionKey(userTask.getId()).count());
                // 判断节点是否已产生任务
                if (taskService.createTaskQuery().processDefinitionId(processDefinition.getId())
                        .taskDefinitionKey(userTask.getId()).count() == 0) {

                    // 获取当前节点的候选人列表
                    List<String> candidateUsers = userTask.getCandidateUsers();
                    System.out.println("节点 = " + userTask.getId());
                    System.out.println("candidateUsers = " + JSON.toJSONString(candidateUsers));
                    System.out.println("getAssignee = " + userTask.getAssignee());
                    System.out.println("getName = " + userTask.getName());

                    /*// 添加新的候选人
                    candidateUsers.add("newCandidateUser1");
                    candidateUsers.add("newCandidateUser2");

                    // 删除旧的候选人
                    candidateUsers.remove("oldCandidateUser1");
                    candidateUsers.remove("oldCandidateUser2");*/

                    // 更新节点的候选人列表
                    userTask.setCandidateUsers(candidateUsers);
                }
            }
        }

        // 保存对流程定义的更改
        // repositoryService.saveProcessDefinition(processDefinition);
    }
}