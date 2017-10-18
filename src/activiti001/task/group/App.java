package activiti001.task.group;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;

public class App {
	
	ProcessEngine pe = ProcessEngines.getDefaultProcessEngine();


	/**
	 * 查询公共任务列表
	 * 
	 */
	@Test
	public void test1() {
		TaskQuery query = pe.getTaskService().createTaskQuery();
		
		//根据候选人过滤
		query.taskCandidateUser("小李");
		List<Task> list = query.list();
		for (Task task : list) {
			System.out.println(task.getName());
			System.out.println(task.getId());
			
			//拾取公共任务，即将公共任务变成个人任务
			//pe.getTaskService().claim(task.getId(), "小王");
			pe.getTaskService().claim(task.getId(), "小李");
		}
	}
	
	/**
	 * 退回任务
	 */
	@Test
	public void test2() {
		pe.getTaskService().setAssignee("1802", null);
	}

}
