package com.faster;

import org.activiti.engine.repository.Deployment;

public interface ActivitiService {
	
	 /**部署流程定义*/  
	public Deployment createDeploymentProcessDefinition(String name);
    /** 
     * 查询部署列表 
     */ 
	public void queryDeployment();
    /** 
     * 查询流程定义列表 
     */ 
	public void queryProcessDefinition();
    /** 
     * 删除部署信息 
     */  
	public void deleteDeployment(String deploymentId);
    /** 
     * 启动流程实例 
     */  
	public void startProcessInstance(String processDefinitionKey);
    /**
     * 查询进行流程实例列表
     * */
	public void processInstanceQuery(String processDefinitionKey);
	
	 /**
     * 结束流程实例
     * */
	public void deleteProcessInstance(String processInstanceId,String reason);
	
	/**查询进行的任务*/
	public void queryTask();
	
	/**查询当前的个人任务(实际就是查询act_ru_task表)*/  
	public void findTaskByAssignee(String assignee);
	
	 /** 
     * 完成我的任务 
     */  
	public void completeTaskByTaskId(String taskId);
	
	/** 
     * 查询历史活动数据列表 
     */  
	public void queryHistoricActivityInstance();
	
	  /** 
     * 查询历史任务数据列表 
     */ 
	 public void historicTaskInstanceQuery();

}
