package cn.orzlinux.dto;

import cn.orzlinux.entity.SuccessKilled;
import cn.orzlinux.enums.SecKillStatEnum;

/**
 * 封装秒杀执行后结果
 */
public class SeckillExecution {
    private long seckillId;
    // 秒杀结果状态
    private int state;

    // 状态信息
    private String stateInfo;

    // 秒杀成功对象
    private SuccessKilled successKilled;

    // 秒杀成功，返回所有信息
    public SeckillExecution(long seckillId, SecKillStatEnum statEnum,SuccessKilled successKilled) {
        this.seckillId = seckillId;
        this.state = statEnum.getState();
        this.stateInfo = statEnum.getStateInfo();
        this.successKilled = successKilled;
    }
    // 秒杀失败，successKilled就不返回了
    public SeckillExecution(long seckillId, SecKillStatEnum statEnum) {
        this.seckillId = seckillId;
        this.state = statEnum.getState();
        this.stateInfo = statEnum.getStateInfo();
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public SuccessKilled getSuccessKilled() {
        return successKilled;
    }

    public void setSuccessKilled(SuccessKilled successKilled) {
        this.successKilled = successKilled;
    }

    @Override
    public String toString() {
        return "SeckillExecution{" + "seckillId=" + seckillId + ", state=" + state + ", stateInfo='" + stateInfo + '\'' + ", successKilled=" + successKilled + '}';
    }
}
