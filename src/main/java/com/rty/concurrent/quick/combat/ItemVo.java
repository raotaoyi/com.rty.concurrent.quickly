package com.rty.concurrent.quick.combat;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class ItemVo<T> implements Delayed {
    //到期时间，但传入的数值代表过期的时长，传入单位毫秒
    private long activeTime;
    //业务数据，泛型
    private T data;
    //传入过期的时长，单位秒，内部转换
    public ItemVo(long expirationTime,T data){
        this.activeTime=expirationTime*1000+System.currentTimeMillis();
        this.data=data;
    }

    public long getActiveTime(){
        return activeTime;
    }

    public T getData(){
        return data;
    }

    /*这个方法返回到激活日期的剩余时间，时间单位有单位参数指定的*/
    @Override
    public long getDelay(TimeUnit unit) {
        long d=unit.convert(this.activeTime-System.currentTimeMillis(),unit);
        return d;
    }

    @Override
    public int compareTo(Delayed o) {
        long d=getDelay(TimeUnit.MICROSECONDS)-o.getDelay(TimeUnit.MICROSECONDS);
        if(d==0){
            return 0;
        }else{
            if(d<0)
                return -1;
            else
                return 1;
        }
    }
}
