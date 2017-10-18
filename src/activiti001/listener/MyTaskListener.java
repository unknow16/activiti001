package activiti001.listener;

import java.util.Set;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class MyTaskListener implements TaskListener {

	private static final long serialVersionUID = -3534067884047561689L;

	@Override
	public void notify(DelegateTask delegateTask) {
		System.out.println("任务监听器。。。。。。");
		
		String assignee = delegateTask.getAssignee();
		String eventName = delegateTask.getEventName();
		String name = delegateTask.getName();
		delegateTask.getProcessInstanceId();
		Set<String> variableNames = delegateTask.getVariableNames();
		for (String string : variableNames) {
			Object val = delegateTask.getVariable(string);
			System.out.println(string + " == " + val);
		}
		
		System.out.println("一个任务" + name + "被创建了，由" + assignee + "负责办理");
	}

}
