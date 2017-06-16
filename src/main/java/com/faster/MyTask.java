package com.faster;

import org.activiti.engine.repository.Deployment;

public class MyTask {
	
	

	public static void main(String[] args){
		String processInstanceQuery = "Test";
		ActivitiService activitiService = new ActivitiServiceImpl(); //创建service
		Deployment deployment = activitiService.createDeploymentProcessDefinition("审批流程"); //部署流程
		activitiService.queryDeployment(); //查询部署列表
		activitiService.queryProcessDefinition();		//查询流程定义列表
		activitiService.startProcessInstance(processInstanceQuery);  //启动流程实例
		activitiService.queryTask();  //查询正在进行的任务
		activitiService.completeTaskByTaskId("1102"); //完成流程
		activitiService.processInstanceQuery(processInstanceQuery);
	}
}
