package com.rty.concurrent.quick.combat;

import java.util.Random;

public class MyTask implements ITaskProcesser<Integer, Integer> {
    @Override
    public TaskResult<Integer> taskExecute(Integer data) {
        Random r = new Random();
        int flag = r.nextInt(500);
        try {
            Thread.sleep(flag);
            if(flag<=300){//正常处理的情况
                Integer returnValue=data.intValue()+flag;
                return  new TaskResult<Integer>(TaskResultType.Success,returnValue);
            }else if(flag>301&& flag<=400){//处理失败的情况
                return  new TaskResult<Integer>(TaskResultType.Failure,-1,"");
            }else{
                try{
                    throw  new RuntimeException("异常发生了");
                }catch (Exception e){
                    return  new TaskResult<Integer>(TaskResultType.Exception,-1,e.getMessage());
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
