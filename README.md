### 什么是流程实例？
* 根据一个流程定义具体的一次执行过程(一次请假过程)就是一个流程实例,一个流程定义对应多个流程实例(一对多关系)

###	Activiti框架提供的Service对象
* RepositoryService----操作静态的资源（流程定义，bpmn、png）
* RuntimeService-----操作流程实例（启动流程实例、查询流程实例、结束流程实例）
* TaskService-----操作任务（查询任务、办理任务）
* HistoryService----操作历史数据

###	Activiti框架提供的对象(和表有对应关系)
* Deployment-----act_re_deployment
* ProcessDefinition----act_re_procdef
* ProcessInstance-----act_ru_execution
* Task-----act_ru_task
