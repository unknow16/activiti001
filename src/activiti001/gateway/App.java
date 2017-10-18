package activiti001.gateway;

import java.util.HashMap;
import java.util.Map;

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
		builder.addClasspathResource("activiti001/gateway/gateway1.bpmn");
		builder.addClasspathResource("activiti001/gateway/gateway1.png");
		
		Deployment deploy = builder.deploy();
		System.out.println(deploy.getId());
	}
	
	@Test
	public void test2() {
		pe.getRuntimeService().startProcessInstanceByKey("myProcess");
	}
	
	@Test
	public void test3() {
		//Map<String, Object> map = new HashMap<>();
		//map.put("bxje", 800);
		pe.getTaskService().complete("2102");
	}
}
