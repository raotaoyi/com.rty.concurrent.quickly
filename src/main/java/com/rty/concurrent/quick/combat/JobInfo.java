package com.rty.concurrent.quick.combat;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 类说明:提交给框架执行的工作实体类
 * 工作：表示本批次需要处理的同性质任务(Task)的一个集合
 *
 * @author rty
 * @since 2020-11-07
 */
public class JobInfo<R> {
    private final String jobName;/*工作名，用以区分框架中惟一的工作*/
    private final int jobLength;/*工作任务的列表*/
    private final ITaskProcesser<?, ?> taskProcesser;/*处理工作中任务的处理器*/
    private AtomicInteger successCount;/*任务的成功次数*/
    private AtomicInteger taskProcessCount;/*工作中任务目前已经处理的次数*/
    private LinkedBlockingDeque<TaskResult<R>> taskDetailQueue;/*处理每个任务处理的结果,供查询用*/
    private final long expireTime;/*保留的工作的結果信息供查询的时长*/
    private static CheckJobProcesser checkJob=CheckJobProcesser.getInstance();


    public JobInfo(String jobName, int jobLength, ITaskProcesser<?, ?> taskProcesser, long expireTime) {
        this.jobName = jobName;
        this.jobLength = jobLength;
        this.taskProcesser = taskProcesser;
        this.successCount = new AtomicInteger(0);
        this.taskProcessCount = new AtomicInteger(0);
        this.taskDetailQueue = new LinkedBlockingDeque<TaskResult<R>>(jobLength);
        this.expireTime = expireTime;
    }

    public int getSuccessCount() {
        return successCount.get();
    }

    public int getTaskProcessCount() {
        return taskProcessCount.get();
    }

    /*提供工作中失敗的次数*/
    public int getFailCount() {
        return taskProcessCount.get() - successCount.get();
    }

    public ITaskProcesser<?, ?> getTaskProcesser() {
        return taskProcesser;
    }

    public int getJobLength() {
        return jobLength;
    }

    /*提供了工作的整体的信息进度*/
    public String getTotalProcess() {
        return "success [" + successCount.get() + "]/current["
                + taskProcessCount.get() + "] Total[" + jobLength + "]";
    }

    /*提供工作中每个任务的处理结果*/
    public List<TaskResult<R>> getTaskDetail() {
        List<TaskResult<R>> taskResultList = new LinkedList<>();
        TaskResult<R> taskResult;
        while ((taskResult = taskDetailQueue.pollFirst()) != null) {
            taskResultList.add(taskResult);
        }
        return taskResultList;
    }

    /*每个任务处理完成后，记录任务的处理结果，因为从业务的应用角度来说，
    对查询任务进度数据的一致性要求不高
    我们保证最终一致性即可，无需对整个方法加锁*/
    public void addTaskResult(TaskResult<R> taskResult){
        if(TaskResultType.Success.equals(taskResult.getResultType())){
            successCount.incrementAndGet();
        }
        taskProcessCount.incrementAndGet();
        taskDetailQueue.addLast(taskResult);
        if(taskProcessCount.get()==jobLength){
            checkJob.putJob(jobName,expireTime);
        }
    }
}
