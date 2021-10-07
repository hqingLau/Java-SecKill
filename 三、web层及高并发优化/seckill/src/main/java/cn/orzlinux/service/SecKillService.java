package cn.orzlinux.service;

import cn.orzlinux.dto.Exposer;
import cn.orzlinux.dto.SeckillExecution;
import cn.orzlinux.entity.SecKill;
import cn.orzlinux.exception.RepeatKillException;
import cn.orzlinux.exception.SeckillCloseException;
import cn.orzlinux.exception.SeckillException;

import java.util.List;

/**
 * 业务接口：站在使用者角度设计接口
 * 三个方面：
 * 方法定义粒度 - 方便调用
 * 参数 - 简练直接
 * 返回类型 - return（类型、异常）
 */
public interface SecKillService {
    /**
     * 查询所有秒杀记录
     * @return
     */
    List<SecKill> getSecKillList();

    SecKill getById(long seckillId);

    /**
     * 秒杀开启时，输出秒杀接口地址
     * 否则输出系统时间和秒杀时间
     * @param seckillId
     */
    Exposer exportSecKillUrl(long seckillId);

    // 执行秒杀操作验证MD5秒杀地址，抛三个异常（有继承关系）
    // 是为了更精确的抛出异常
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
        throws SeckillException, RepeatKillException, SeckillCloseException;

    /**
     * 存储过程执行秒杀
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     * @throws SeckillException
     * @throws RepeatKillException
     * @throws SeckillCloseException
     */
    SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5);

}
