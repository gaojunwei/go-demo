package com.test.service.java;

import liquibase.pro.packaged.S;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.*;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void testDeploy(){
        // 部署流程 获取RepositoryService对象
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()// 创建Deployment对象
                .addClasspathResource("holiday-request.bpmn20.xml") // 添加流程部署文件
                .name("请求流程") // 设置部署流程的名称
                .deploy(); // 执行部署操作
        System.out.println("deployment.getId() = " + deployment.getId());
        System.out.println("deployment.getName() = " + deployment.getName());
    }

    @Test
    @DisplayName("查看流程定义")
    public void testDeployQuery(){
        // 部署流程 获取RepositoryService对象
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 获取流程定义对象
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId("1")
                .processDefinitionResourceNameLike("holiday-request.bpmn20.xml")
                .processDefinitionName("Holiday Request")
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
        repositoryService.deleteDeployment("2501",true);
    }

    @Test
    @DisplayName("启动流程实例")
    public void testStartProcess(){
        //通过RuntimeService来启动流程实例
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //构建流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put("employee","张三");
        variables.put("nrOfHolidays",3);
        variables.put("description","工作累了，出去玩玩");

        ProcessInstance holidayRequest = runtimeService.startProcessInstanceByKey("holidayRequest", variables);
        System.out.println("流程定义的ID：" + holidayRequest.getProcessDefinitionId());
        System.out.println("流程实例的ID：" + holidayRequest.getId());
        System.out.println("当前活动的ID：" + holidayRequest.getActivityId());
    }

    @Test
    @DisplayName("查看任务")
    public void testQueryTask(){
        TaskService taskService = processEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey("holidayRequest")//指定查询的流程编码
                //.taskAssignee("zhangshan")
                .list();
        for (Task task : list) {
            System.out.println("task.getProcessDefinitionId() = " + task.getProcessDefinitionId());
            System.out.println("task.getId() = " + task.getId());
            System.out.println("task.getAssignee() = " + task.getAssignee());
            System.out.println("task.getName() = " + task.getName());
        }
    }
}
















































