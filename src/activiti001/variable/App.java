package activiti001.variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

public class App {

	ProcessEngine pe = ProcessEngines.getDefaultProcessEngine();
	
	/**
	 * 设置流程变量1：在启动流程实例时设置
	 */
	@Test
	public void test1() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("k1", "v1");
		map.put("k2", 200);
		ProcessInstance pi = pe.getRuntimeService().startProcessInstanceByKey("leaveProcess", map);
		System.out.println(pi.getId());
		
	}
	
	/**
	 * 设置流程变量2：办理任务时设置遍历
	 */
	@Test
	public void test2() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("k3", "v3");
		map.put("k4", 400f);
		pe.getTaskService().complete("506", map);
	}
	
	/**
	 * 设置流程变量3：使用RuntimeService的方法设置
	 */
	@Test
	public void test3() {
		pe.getRuntimeService().setVariable("501", "qjly", "请假理由：离婚");
	}
	
	/**
	 * 设置流程变量4：使用TaskService的方法设置
	 */
	@Test
	public void test4() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("k5", "v3");
		map.put("k6", 400f);
		pe.getTaskService().setVariables("606", map);
	}
	
	/**
	 * 获取流程变量1：使用runtimeService
	 */
	@Test
	public void test5() {
		//Object v = pe.getRuntimeService().getVariable("501", "qjly");
		//Map<String, Object> variables = pe.getRuntimeService().getVariables("501");
		List<String> list = new ArrayList<String>();
		list.add("k1");
		list.add("k2");
		Map<String, Object> variables = pe.getRuntimeService().getVariables("501", list);
		for(Object o : variables.values()) {
			System.out.println(o);
		}
	}
	
	/**
	 * 获取流程变量2：使用taskService
	 */
	@Test
	public void test6() {
		Object variable = pe.getTaskService().getVariable("606", "k1");
		System.out.println(variable);
	}
}
