package activiti001.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class MyExecutionListener2 implements ExecutionListener {

	private static final long serialVersionUID = -974507521598407936L;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		System.out.println("execution listener....end");
	}

	
}
