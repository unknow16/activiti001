package activiti001.task.receive;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

public class App {
	static ProcessEngine pe = null;
	static {
		pe = ProcessEngines.getDefaultProcessEngine();
	}

	/**
	 * 部署
	 */
	@Test
	public void test1() {
		DeploymentBuilder builder = pe.getRepositoryService().createDeployment();

		builder.addClasspathResource("activiti001/task/receive/receiveTask.bpmn");
		builder.addClasspathResource("activiti001/task/receive/receiveTask.png");

		Deployment deploy = builder.deploy();
		System.out.println(deploy.getId());
	}

	/**
	 * 启动流程实例
	 */
	@Test
	public void test2() {
		ProcessInstance pi = pe.getRuntimeService().startProcessInstanceByKey("receiveTask");
		System.out.println(pi.getId());
	}

	/**
	 * 执行任务
	 */
	@Test
	public void test3() {
		pe.getTaskService().complete("302");
	}
	
	/**
	 * 让接受类型任务流转到下一个节点
	 */
	@Test
	public void test4() {
		pe.getRuntimeService().signal("302");
	}
}
