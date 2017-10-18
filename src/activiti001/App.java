package activiti001;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
	
	ProcessEngine processEngine = null;

	/**
	 * 使用框架提供的自动建表（不提供配置文件）
	 */
	@Test
	public void testAutoCreateTable() {
		//创建一个流程引擎配置对象
		ProcessEngineConfiguration conf = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
		
		//设置数据源信息
		conf.setJdbcDriver("com.mysql.jdbc.Driver");
		conf.setJdbcUrl("jdbc:mysql://localhost:3306/activiti");
		conf.setJdbcUsername("root");
		conf.setJdbcPassword("123456");
		
		//设置自动建表
		conf.setDatabaseSchemaUpdate("true");
		
		//创建一个流程引擎对象，在创建流程引擎对象过程中会自动建表
		ProcessEngine processEngine = conf.buildProcessEngine();
	}
	
	/**
	 * 使用框架提供的自动建表（提供配置文件）--可从框架提供的例子中获取
	 * 默认配置文件名称， activiti-context.xml或者activiti.cfg.xml,
	 */
	@Test
	public void testAutoCreateTableWithXML() {
		String resource = "activiti-context.xml"; //默认配置文件名称
		String beanName = "processEngineConfiguration"; //配置id值
		ProcessEngineConfiguration conf = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource(resource, beanName);
		ProcessEngine processEngine = conf.buildProcessEngine();
		
	}
	
	/**
	 * 使用默认配置文件和id
	 */
	@Before
	public void testUseDefaultConf() {
		processEngine = ProcessEngines.getDefaultProcessEngine();
	}
	
	/**
	 * 部署流程定义（操作数据表：act_re_deployment, act_re_prodef,act_ge_bytearry）
	 */
	@Test
	public void testDeploy() {
		//获得一个部署构建对象，用于加载流程定义文件，完成流程定义文件的部署
		DeploymentBuilder builder = processEngine.getRepositoryService().createDeployment();
		
		//加载流程定义文件
		builder.addClasspathResource("test1.bpmn");
		builder.addClasspathResource("test1.png");
		
		//部署流程定义
		Deployment deploy = builder.deploy();
		System.out.println(deploy.getId());
	}
	
	/**
	 * 查询流程定义列表（act_re_prodef >> id）
	 */
	@Test
	public void testQueryProcess() {
		
		//processEngine.getRuntimeService().createProcessInstanceQuery().list();
		//processEngine.getTaskService().createTaskQuery().list();
		
		ProcessDefinitionQuery query = processEngine.getRepositoryService().createProcessDefinitionQuery();
		//添加过滤条件
		query.processDefinitionKey("leaveProcess");
		//添加排序条件
		query.orderByProcessDefinitionVersion().desc();
		//分页查询
		List<ProcessDefinition> list = query.listPage(0, 10);
		//查询全部
		//List<ProcessDefinition> list = query.list();
		for (ProcessDefinition processDefinition : list) {
			System.out.println(processDefinition.getId() + processDefinition.getDeploymentId());
		}
	}
	
	/**
	 * 根据流程定义id启动一个流程实例（一次请假流程）
	 * 每启动一个流程实例，就会在 （act_ru_execution） 中创建一条记录
	 * 其中外键proc_def_id,关联是从哪张表启动的
	 * act_id 保存流程到哪一步，
	 * 
	 * (act__ru_task) 当前的任务表
	 */
	@Test
	public void testStartProcessInstance() {
		String id = "leaveProcess:1:4";
		ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceById(id);
		String id2 = processInstance.getId();
		System.out.println(id2);
	}
	
	/**
	 * 查询任务列表
	 */
	@Test
	public void testQueryTaskList() {
		TaskQuery query = processEngine.getTaskService().createTaskQuery();
		query.taskAssignee("王五");
		List<Task> list = query.list();
		for (Task task : list) {
			System.out.println(task.getId());
		}
	}
	
	/**
	 * 办理任务
	 */
	@Test
	public void testExecTask() {
		processEngine.getTaskService().complete("602");//任务id
	}
	
	
	@Test
	public void testSpring() {
		ApplicationContext ac = new ClassPathXmlApplicationContext("activiti-context-spring.xml");
		ProcessEngine pe = (ProcessEngine) ac.getBean("processEngine");
		DeploymentBuilder builder = pe.getRepositoryService().createDeployment();
		builder.addClasspathResource("test1.bpmn");
		builder.addClasspathResource("test1.png");
		
		builder.deploy();
	}

}
