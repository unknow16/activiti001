package activiti001.history;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.junit.Test;

public class App1 {

	ProcessEngine pe = ProcessEngines.getDefaultProcessEngine();
	
	/**
	 * 查询流程实例历史(act_hi_procinst)
	 * 历史任务（act_hi_taskinst） pe.getHistoryService().createHistoricTaskInstanceQuery()
	 */
	@Test
	public void testHistory() {
		//pe.getHistoryService().createHistoricTaskInstanceQuery()
		HistoricProcessInstanceQuery query = pe.getHistoryService().createHistoricProcessInstanceQuery();
		List<HistoricProcessInstance> list = query.list();
		for (HistoricProcessInstance hpi : list) {
			System.out.println(hpi.getDurationInMillis());
		}
	}
	
	/**
	 * 查看活动实例历史（act_hi_actinst）
	 */
	@Test
	public void test1() {
		HistoricActivityInstanceQuery query = pe.getHistoryService().createHistoricActivityInstanceQuery();
		query.orderByProcessInstanceId().desc();
		List<HistoricActivityInstance> list = query.list();
		for (HistoricActivityInstance historicActivityInstance : list) {
			String id = historicActivityInstance.getProcessDefinitionId();
			System.out.println(id);
		}
	}
}
