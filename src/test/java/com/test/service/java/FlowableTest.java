package com.test.service.java;

import com.alibaba.fastjson2.JSON;
import liquibase.pro.packaged.S;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

@Slf4j
public class FlowableTest {

    ProcessEngine processEngine = null;

    @BeforeEach
    public void test(){
        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:mysql://127.0.0.1:3306/flowable-learn?serverTimezone=UTC&nullCatalogMeansCurrent=true")
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
                .addClasspathResource("2023-请假流程.bpmn20.xml") // 添加流程部署文件
                .name("2023请假流程") // 设置部署流程的名称
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
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId("2501")
                .processDefinitionResourceNameLike("holiday-request.bpmn20.xml")
                .processDefinitionName("请假流程")
                .singleResult();
        /**
         * Preparing: SELECT RES.* from ACT_RE_PROCDEF RES WHERE RES.NAME_ = ? and RES.RESOURCE_NAME_ like ? and RES.DEPLOYMENT_ID_ = ? order by RES.ID_ asc
         * Parameters: Holiday Request(String), holiday-request.bpmn20.xml(String), 1(String)
         */
        System.out.println("processDefinition.getId() = " + processDefinition.getId());
        System.out.println("processDefinition.getName() = " + processDefinition.getName());
        System.out.println("processDefinition.getDeploymentId() = " + processDefinition.getDeploymentId());
        System.out.println("processDefinition.getDescription() = " + processDefinition.getDescription());
        System.out.println("processDefinition.getResourceName() = " + processDefinition.getResourceName());
    }

    @Test
    @DisplayName("流程删除")
    public void testDeployDelete(){
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //删除流程，指定流程ID,如果部署的流程启动了就不允许删除了
        //repositoryService.deleteDeployment("2501");
        //第二个参数是级联删除，如果流程启动了 相关的任务一并被删除
        repositoryService.deleteDeployment("140001",true);
    }

    @Test
    @DisplayName("启动流程实例")
    public void testStartProcess(){
        //通过RuntimeService来启动流程实例
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //构建流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put("employee","寒冬");
        variables.put("nrOfHolidays",3);
        variables.put("description","工作累了，出去玩玩");
        variables.put("jiaoshi","lao6");

        ProcessInstance holidayRequest = runtimeService.startProcessInstanceByKey("holidayFlowable", variables);
        System.out.println("流程定义的ID：" + holidayRequest.getProcessDefinitionId());
        System.out.println("流程实例的ID：" + holidayRequest.getId());
        System.out.println("当前活动的ID：" + holidayRequest.getActivityId());
    }

    @Test
    @DisplayName("查看代办任务")
    public void testQueryTask(){
        TaskService taskService = processEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery()
                //.processDefinitionKey("holidayRequest")//指定查询的流程编码
                .taskCandidateOrAssigned("xiangcunlaoshi")
                .includeCaseVariables()
                .taskDueAfter(new Date())
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
            System.out.println();
        }
    }

    @Test
    @DisplayName("完成任务")
    public void testCompleteTask(){
        TaskService taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery()
                .processDefinitionKey("holidayFlowable")
                .taskAssignee("xiangcunlaoshi")
                .taskId("150010")
                .list();
        // 添加流程变量
        Map<String,Object> variables = new HashMap<>();
        variables.put("approved",false); // 拒绝请假
        // 完成任务
        for (Task task:tasks){
            taskService.complete(task.getId(),variables);
            System.out.println("拒绝 "+task.getId()+" "+task.getName());
        }
    }

    /**
     * 查看历史
     */
    @Test
    public void testQueryHistory(){
        HistoryService historyService = processEngine.getHistoryService();
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                //.processDefinitionId("holidayRequestNew:1:10003")
                .orderByHistoricActivityInstanceEndTime().asc()
                .list();
        for (HistoricActivityInstance historicActivityInstance : list) {
            System.out.println(historicActivityInstance.getActivityId() + " took "
                    + historicActivityInstance.getDurationInMillis() + " milliseconds");
        }
    }

    @Test
    @DisplayName("查询指定历史流程")
    public void testQueryHistory2(){
        HistoryService historyService = processEngine.getHistoryService();
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().involvedUser("zhangshan").includeProcessVariables()
                .orderByProcessInstanceStartTime().desc().list();
        for (HistoricProcessInstance historicProcessInstance : list) {
            System.out.println("第*****流程");
            System.out.println("getProcessDefinitionName = "+historicProcessInstance.getProcessDefinitionName());
            System.out.println("getProcessDefinitionId = "+historicProcessInstance.getProcessDefinitionId());
            System.out.println("getProcessDefinitionKey = "+historicProcessInstance.getProcessDefinitionKey());
            System.out.println("getStartTime = "+historicProcessInstance.getStartTime());
            System.out.println("getEndTime = "+historicProcessInstance.getEndTime());
            System.out.println("getDurationInMillis = "+historicProcessInstance.getDurationInMillis());
            System.out.println("getProcessVariables = "+historicProcessInstance.getProcessVariables());
        }
    }

    @Test
    @DisplayName("查询指定历史流程")
    public void testQueryService(){
        RepositoryService repositoryService = processEngine.getRepositoryService();//Flowable的资源管理类
        HistoryService historyService = processEngine.getHistoryService();
        TaskService taskService = processEngine.getTaskService();
        RuntimeService runtimeService = processEngine.getRuntimeService();//Flowable的流程运行管理类
        FormService formService = processEngine.getFormService();
        IdentityService identityService = processEngine.getIdentityService();
        ManagementService managementService = processEngine.getManagementService();
        DynamicBpmnService dynamicBpmnService = processEngine.getDynamicBpmnService();
        ProcessMigrationService processMigrationService = processEngine.getProcessMigrationService();




        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().involvedUser("zhangshan").includeProcessVariables()
                .orderByProcessInstanceStartTime().desc().list();
        for (HistoricProcessInstance historicProcessInstance : list) {
            System.out.println("第*****流程");
            System.out.println("getProcessDefinitionName = "+historicProcessInstance.getProcessDefinitionName());
            System.out.println("getProcessDefinitionId = "+historicProcessInstance.getProcessDefinitionId());
            System.out.println("getProcessDefinitionKey = "+historicProcessInstance.getProcessDefinitionKey());
            System.out.println("getStartTime = "+historicProcessInstance.getStartTime());
            System.out.println("getEndTime = "+historicProcessInstance.getEndTime());
            System.out.println("getDurationInMillis = "+historicProcessInstance.getDurationInMillis());
            System.out.println("getProcessVariables = "+historicProcessInstance.getProcessVariables());
        }
    }
}
















































