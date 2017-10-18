package activiti001.listener;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.junit.Test;

public class App {

	ProcessEngine pe = ProcessEngines.getDefaultProcessEngine();
	
	@Test
	public void test1() {
		
		DeploymentBuilder builder = pe.getRepositoryService().createDeployment();
		builder.addClasspathResource("activiti001/listener/listenerTest.bpmn");
		builder.addClasspathResource("activiti001/listener/listenerTest.png");
		
		Deployment deploy = builder.deploy();
		System.out.println(deploy.getId());
	}
	
	@Test
	public void test2() {
		pe.getRuntimeService().startProcessInstanceByKey("listenerTest");
	}
	
	@Test
	public void test3() {
		pe.getTaskService().complete("1004");
	}
}
