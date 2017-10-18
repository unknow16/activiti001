package activiti001;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class ApiTest {
	
	ProcessEngine pe = ProcessEngines.getDefaultProcessEngine();

	/**
	 * 部署流程定义
	 * 1.读取单个的流程文件
	 * 2.读取zip压缩文件
	 */
	@Test
	public void test1() {
		DeploymentBuilder builder = pe.getRepositoryService().createDeployment();
		/*
		 *  1.读取单个的流程文件
		 * //加载流程定义文件
		 * */
		builder.addClasspathResource("activiti001/task/group/groupTask.bpmn");
		builder.addClasspathResource("activiti001/task/group/groupTask.png");
		builder.name("公共任务测试");
		//部署流程定义
		Deployment deploy = builder.deploy();
		System.out.println(deploy.getId());
		
		//2.读取zip压缩文件
		/*ZipInputStream zipInputStream = new ZipInputStream(
				this.getClass().getClassLoader().getResourceAsStream("process.zip"));
		builder.addZipInputStream(zipInputStream);
		builder.name("请假流程部署");
		Deployment deploy = builder.deploy();
		System.out.println(deploy.getId());*/
	}
	
	
	/**
	 * 查询部署列表   act_re_deployment
	 */
	@Test
	public void test2() {
		//部署查询对象，act_re_deployment
		DeploymentQuery query = pe.getRepositoryService().createDeploymentQuery();
		List<Deployment> list = query.list();
		for (Deployment deployment : list) {
			System.out.println(deployment.getId() + "==" + deployment.getDeploymentTime());
		}
	}
	
	/**
	 * 查询流程定义列表  act_re_prodef 《《---每次部署都会产生，一一对应
	 * 场景：没有走完的流程按原来的流程定义走，新开始的流程实例默认按最新部署的流程定义走
	 */
	@Test
	public void test3() {
		ProcessDefinitionQuery query = pe.getRepositoryService().createProcessDefinitionQuery();
		List<ProcessDefinition> list = query.list();
		for (ProcessDefinition p : list) {
			System.out.println(p.getDeploymentId());
		}
	}
	
	/**
	 * 删除部署信息
	 * 删除流程定义(通过删除部署信息级联删除流程定义)
	 */
	@Test
	public void test4() {
		//删除 act_re_deployment  + act_re_prodef
		//pe.getRepositoryService().deleteDeployment("101");
		
		//级联删除相关联的表（act_ru_execution）
		pe.getRepositoryService().deleteDeployment("101", true);
	}
	
	/**
	 * 查询每次部署对应的文件名和输入流（bpmn,png）
	 * @throws Exception 
	 */
	@Test
	public void test5() throws Exception{
		//InputStream png = pe.getRepositoryService().getProcessDiagram("");//processDefinitionId 
		
		String deploymentId = "1001";
		List<String> names = pe.getRepositoryService().getDeploymentResourceNames(deploymentId);
		for (String name : names) {
			System.out.println(name);
			InputStream in = pe.getRepositoryService().getResourceAsStream(deploymentId, name);
			FileOutputStream out = new FileOutputStream("d:\\" + name);
			
/*			byte[] buf = new byte[1024];
			int len = -1;
			while((len = in.read(buf)) != -1) {
				out.write(buf, 0, len);
			}
			out.flush();*/
			FileUtils.copyInputStreamToFile(in, new File("d:\\" + name));
			
			out.close();
			in.close();
		}
		
	}
	
	/**
	 * 启动流程实例
	 * 1.根据流程定义id启动
	 * 2.根据流程定义key启动（自动选择最新版本的流程定义文件启动流程实例）
	 */
	@Test
	public void test8() {
		//ProcessInstance processInstance = pe.getRuntimeService().startProcessInstanceById("leaveProcess:2:104");
		ProcessInstance processInstance = pe.getRuntimeService().startProcessInstanceByKey("myProcess");
		String id = processInstance.getId();
		System.out.println(id);
	}
	
	/**
	 * 查询流程实例列表（act_ru_execution）
	 */
	@Test
	public void test9() {
		ProcessInstanceQuery query = pe.getRuntimeService().createProcessInstanceQuery();
		query.processDefinitionKey("leaveProcess");
		query.orderByProcessInstanceId().desc();
		List<ProcessInstance> list = query.listPage(0, 2);
		//List<ProcessInstance> list = query.list();
		for (ProcessInstance processInstance : list) {
			System.out.println(processInstance.getId() + "==" + processInstance.getActivityId());
		}
	}
	
	/**
	 * 结束流程实例, act_ru_execution, act_ru_task
	 */
	@Test
	public void test10() {
		pe.getRuntimeService().deleteProcessInstance("1201", "delete reason");
	}
	
	/**
	 * 查询任务列表
	 */
	@Test
	public void test11() {
		TaskQuery query = pe.getTaskService().createTaskQuery();
		query.taskAssignee("张三");
		query.orderByTaskCreateTime().desc();
		List<Task> list = query.list();
		for (Task task : list) {
			System.out.println(task.getId() + " == " + task.getName());
		}
	}
	/**
	 * 办理任务
	 */
	@Test
	public void test12() {
		pe.getTaskService().complete("1704");
	}
	
	/**
	 * 直接将流程向下一步流程
	 */
	@Test
	public void test13() {
		pe.getRuntimeService().signal("1301"); //executionId流程实例id
	}
	
	/**
	 * 查询最新版本的所有流程定义
	 */
	@Test
	public void test14() {
		ProcessDefinitionQuery query = pe.getRepositoryService().createProcessDefinitionQuery();
		query.orderByProcessDefinitionVersion().asc();
		List<ProcessDefinition> list = query.list();
		
		Map<String, ProcessDefinition> map = new HashMap<String,ProcessDefinition>();
		for (ProcessDefinition pd : list) {
			map.put(pd.getKey(), pd);
		}
		
		List<ProcessDefinition> list1 = new ArrayList<ProcessDefinition>(map.values());
		System.out.println(list1);
		for (ProcessDefinition pd : list1) {
			System.out.println(pd.getKey() + "==" + pd.getVersion());
		}
	}
}
