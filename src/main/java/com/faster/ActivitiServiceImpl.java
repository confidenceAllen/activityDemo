package com.faster;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;

public class ActivitiServiceImpl implements ActivitiService{

	
	ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();  
	@Override
	public Deployment createDeploymentProcessDefinition(String name) {
		//与流程定义和部署对象相关的Service  
        RepositoryService repositoryService=processEngine.getRepositoryService();  
          
        DeploymentBuilder deploymentBuilder=repositoryService.createDeployment();//创建一个部署对象  
        deploymentBuilder.name(name);//添加部署的名称  
        deploymentBuilder.addClasspathResource("diagrams/MyProcess.bpmn");//从classpath的资源加载，一次只能加载一个文件  
        deploymentBuilder.addClasspathResource("diagrams/MyProcess.png");//从classpath的资源加载，一次只能加载一个文件  
          
        Deployment deployment=deploymentBuilder.deploy();//完成部署  
          
        //打印我们的流程信息  
        System.out.println("流程Id:"+deployment.getId());  
        System.out.println("流程Name:"+deployment.getName());
		return deployment;  
		
	}

	@Override
	public void queryDeployment() {
		 // 部署查询对象，查询表act_re_deployment  
        DeploymentQuery query = processEngine.getRepositoryService()  
                .createDeploymentQuery();  
        List<Deployment> list = query.list(); 
        if("".equals(list)||list.size()==0){
        	System.out.println("没有部署列表");
        }else{
            for (Deployment deployment : list) {  
                System.out.println("正在进行的流程Id:"+deployment.getId());  
                System.out.println("正在进行的流程Name:"+deployment.getName()); 
            }  
        }

		
	}

	@Override
	public void queryProcessDefinition() {
	  // 流程定义查询对象，查询表act_re_procdef  
        ProcessDefinitionQuery query = processEngine.getRepositoryService()  
                .createProcessDefinitionQuery();  
        List<ProcessDefinition> list = query.list(); 
        if("".equals(list)||list.size()==0){
        	System.out.println("没有流程定义列表");
        	}else{
	        for (ProcessDefinition pd : list) {  
	            System.out.println("*** 流程定义ID:" + pd.getId() +"*** 流程定义name:"+pd.getName() + 
	            		"*** 所属的部署ID:" +pd.getDeploymentId());  
        }  
        }
		
	}

	@Override
	public void deleteDeployment(String deploymentId) {
		  processEngine.getRepositoryService().deleteDeployment(deploymentId,//级联删除,通过删除部署信息达到删除流程定义的目的,删除流程定义使用相同方法
	                true);  
		System.out.println("删除的部署ID是："+deploymentId);
	}

	@Override
	public void startProcessInstance(String processDefinitionKey) {
		 ProcessInstance pi = processEngine.getRuntimeService()// 于正在执行的流程实例和执行对象相关的Service  
	                .startProcessInstanceByKey(processDefinitionKey);// 使用流程定义的key启动流程实例，key对应hellworld.bpmn文件中id的属性值，使用key值启动，默认是按照最新版本的流程定义启动  
	        System.out.println("流程实例ID:" + pi.getId());
	        System.out.println("流程定义ID:" + pi.getProcessDefinitionId()); 
	}

	@Override
	public void processInstanceQuery(String processDefinitionKey) {
		 //流程实例查询对象，查询act_ru_execution表  
        ProcessInstanceQuery query = processEngine.getRuntimeService().createProcessInstanceQuery();  
        query.processDefinitionKey(processDefinitionKey);  
        query.orderByProcessInstanceId().desc();  
        query.listPage(0, 10);//分页  
        List<ProcessInstance> list = query.list();  
        for (ProcessInstance pi : list) {  
            System.out.println("正执行的事例ID："+pi.getId() + "*** 所属的流程：" + pi.getProcessDefinitionId() +"*** 开始的ActivityId:"+pi.getActivityId());  
        }  
		
	}

	@Override
	public void deleteProcessInstance(String processInstanceId,String reason) {
		processEngine.getRuntimeService().deleteProcessInstance(processInstanceId , reason); 
		System.out.println("结束的流程ID是："+processInstanceId);
		System.out.println("结束的原因是："+reason);
	}
	
	@Override
	public void queryTask() {
		
		List<Task> tasks =  processEngine.getTaskService().createTaskQuery().list();
    	for (Task task:tasks) {
    		System.out.println("执行ID:"+task.getId());
    		
    	}
		
	}

	@Override
	public void findTaskByAssignee(String assignee) {
		//获取事务Service  
        TaskService taskService=processEngine.getTaskService();  
        List<Task> taskList=taskService.createTaskQuery()//创建任务查询对象  
                   .taskAssignee(assignee)//指定个人任务查询，指定办理人  
                   .list();//获取该办理人下的事务列表  
          
        if(taskList!=null&&taskList.size()>0){  
            for(Task task:taskList){  
                System.out.println("任务ID："+task.getId());  
                System.out.println("任务名称："+task.getName());  
                System.out.println("任务的创建时间："+task.getCreateTime());  
                System.out.println("任务办理人："+task.getAssignee());  
                System.out.println("流程实例ID："+task.getProcessInstanceId());  
                System.out.println("执行对象ID："+task.getExecutionId());  
                System.out.println("流程定义ID："+task.getProcessDefinitionId());  
                System.out.println("#############################################");  
            }  
        }  
		
	}

	@Override
	public void completeTaskByTaskId(String taskId) {
        processEngine.getTaskService()//与正在执行的认为管理相关的Service  
                .complete(taskId); 
        System.out.println("完成任务:任务ID:"+taskId);  
		
	}

	@Override
	public void queryHistoricActivityInstance() {
		   HistoricActivityInstanceQuery query = processEngine.getHistoryService()  
	                .createHistoricActivityInstanceQuery();  
	        // 按照流程实例排序  
	        query.orderByProcessInstanceId().desc();  
	        query.orderByHistoricActivityInstanceEndTime().asc();  
	        List<HistoricActivityInstance> list = query.list();  
	        for (HistoricActivityInstance hi : list) {  
	            System.out.println("ActivityId："+hi.getActivityId()+"*** Assignee:"+hi.getAssignee() + "*** ActivityName" + hi.getActivityName()  
	                    + "*** ActivityType：" + hi.getActivityType()+"*** ExecutionId:"+hi.getExecutionId()+"*** StartTime"+hi.getStartTime()
	                    + "*** EndTime"+hi.getEndTime());  
	        }  
		
	}

	@Override
	public void historicTaskInstanceQuery() {
		 HistoricTaskInstanceQuery query = processEngine.getHistoryService()  
	                .createHistoricTaskInstanceQuery();  
	        query.orderByProcessInstanceId().asc();  //根据ProcessInstanceId升序
	        query.orderByHistoricTaskInstanceEndTime().desc();  //结束时间降序
	        List<HistoricTaskInstance> list = query.list();  
	        for (HistoricTaskInstance hi : list) {  
	            System.out.println("执行ID：" + hi.getId()+"*** 关键人KEY："+hi.getAssignee() + "*** 执行事件:" + hi.getName() + "*** 开始时间："  
	                    + hi.getStartTime()+"*** 结束时间："+ hi.getEndTime() + "*** ExecutionId:"+hi.getExecutionId() 
	                    + "*** 结束原因："+hi.getDeleteReason()+"*** 所属实例："+hi.getProcessInstanceId());  
	        }  
		
	}



}
