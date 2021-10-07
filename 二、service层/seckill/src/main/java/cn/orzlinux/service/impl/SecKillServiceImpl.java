package cn.orzlinux.service.impl;

import cn.orzlinux.dao.SecKillDao;
import cn.orzlinux.dao.SuccessKilledDao;
import cn.orzlinux.dto.Exposer;
import cn.orzlinux.dto.SeckillExecution;
import cn.orzlinux.entity.SecKill;
import cn.orzlinux.entity.SuccessKilled;
import cn.orzlinux.enums.SecKillStatEnum;
import cn.orzlinux.exception.RepeatKillException;
import cn.orzlinux.exception.SeckillCloseException;
import cn.orzlinux.exception.SeckillException;
import cn.orzlinux.service.SecKillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Service
public class SecKillServiceImpl implements SecKillService {
    // 使用指定类初始化日志对象，在日志输出的时候，可以打印出日志信息所在类
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // 需要两个 dao的配合
    // 注入service依赖
    @Resource
    private SecKillDao secKillDao;
    @Resource
    private SuccessKilledDao successKilledDao;

    // 加入盐、混淆效果，如瞎打一下:
    private final String slat="lf,ad.ga.dfgm;adrktpqerml[fasedfa]";

    @Override
    public List<SecKill> getSecKillList() {
        return secKillDao.queryAll(0,4);
    }

    @Override
    public SecKill getById(long seckillId) {
        return secKillDao.queryById(seckillId);
    }

    /**
     * 秒杀开启时，输出秒杀接口地址
     * 否则输出系统时间和秒杀时间
     *
     * @param seckillId
     */
    @Override
    public Exposer exportSecKillUrl(long seckillId) {
        SecKill secKill = secKillDao.queryById(seckillId);
        if(secKill == null) {
            // 查不到id，false
            return new Exposer(false,seckillId);
        }
        Date startTime = secKill.getStartTime();
        Date endTime = secKill.getEndTime();
        Date nowTime = new Date();
        if(nowTime.getTime()<startTime.getTime()
                || nowTime.getTime()>endTime.getTime()) {
            return new Exposer(false,seckillId, nowTime.getTime(),
                    startTime.getTime(),endTime.getTime());
        }
        // 不可逆
        String md5 = getMD5(seckillId);
        return new Exposer(true,md5,seckillId);
    }

    private String getMD5(long seckillId) {
        String base = seckillId+"/orzlinux.cn/"+slat;
        // spring已有实现
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes(StandardCharsets.UTF_8));
        return md5;
    }

    @Override
    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws
            SeckillException, RepeatKillException, SeckillException {
        if(md5==null || !md5.equals(getMD5(seckillId))) {
            // 秒杀的数据被重写修改了
            throw new SeckillException("seckill data rewrite");
        }
        // 执行秒杀逻辑：减库存、加记录购买行为
        Date nowTime = new Date();
        // 减库存
        int updateCount = secKillDao.reduceNumber(seckillId,nowTime);
        try {
            if(updateCount<=0) {
                // 没有更新记录,秒杀结束
                throw new SeckillCloseException("seckill is closed");
            } else {
                // 记录购买行为
                int insertCount = successKilledDao.insertSuccessKilled(seckillId,userPhone);
                if(insertCount<=0) {
                    // 重复秒杀
                    throw new RepeatKillException("seckill repeated");
                } else {
                    //秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
                    //return new SeckillExecution(seckillId,1,"秒杀成功",successKilled);
                    return new SeckillExecution(seckillId, SecKillStatEnum.SUCCESS,successKilled);
                }
            }
        } catch (SeckillCloseException | RepeatKillException e1){
            throw e1;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            // 所有编译期异常转化为运行期异常，这样spring才能回滚
            throw new SeckillException("seckill inner error"+e.getMessage());
        }
    }

}
