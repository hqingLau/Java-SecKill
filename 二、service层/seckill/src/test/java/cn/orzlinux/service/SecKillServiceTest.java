package cn.orzlinux.service;

import cn.orzlinux.dto.Exposer;
import cn.orzlinux.dto.SeckillExecution;
import cn.orzlinux.entity.SecKill;
import cn.orzlinux.exception.RepeatKillException;
import cn.orzlinux.exception.SeckillCloseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring/spring-dao.xml",
        "classpath:/spring/spring-service.xml"
})
public class SecKillServiceTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SecKillService secKillService;
    @Test
    public void getSecKillList() {
        List<SecKill> list = secKillService.getSecKillList();;
        logger.info("list={}",list);
        // 输出信息： [main] [89] [DEBUG] JDBC Connection [com.mchange.v2.c3p0.impl.NewProxyConnection@5443d039] will not be managed by Spring
        //[DEBUG] ==>  Preparing: select seckill_id,name,number,start_time,end_time,create_time from seckill order by create_time DESC limit 0,?;
        //[DEBUG] ==> Parameters: 4(Integer)
        //[DEBUG] <==      Total: 4
        //[DEBUG]
        // ------------------Closing non transactional SqlSession ---------------------
        // [org.apache.ibatis.session.defaults.DefaultSqlSession@66c61024]
        //[INFO ] list=[SecKill{seckillId=1000, name='1000秒杀iphone13', number=99, startTime=Tue Oct 05 02:00:00 CST 2021, endTime=Wed Oct 06 02:00:00 CST 2021, createTime=Tue Oct 05 10:01:47 CST 2021}, SecKill{seckillId=1001, name='500秒杀iphone12', number=200, startTime=Tue Oct 05 02:00:00 CST 2021, endTime=Wed Oct 06 02:00:00 CST 2021, createTime=Tue Oct 05 10:01:47 CST 2021}, SecKill{seckillId=1002, name='300秒杀iphone11', number=300, startTime=Tue Oct 05 02:00:00 CST 2021, endTime=Wed Oct 06 02:00:00 CST 2021, createTime=Tue Oct 05 10:01:47 CST 2021}, SecKill{seckillId=1003, name='100秒杀iphone6', number=400, startTime=Tue Oct 05 02:00:00 CST 2021, endTime=Wed Oct 06 02:00:00 CST 2021, createTime=Tue Oct 05 10:01:47 CST 2021}]
    }

    @Test
    public void getById() {
        long id = 1000;
        SecKill secKill = secKillService.getById(id);
        logger.info("seckill={}",secKill);
        // seckill=SecKill{seckillId=1000, name='1000秒杀iphone13', n...}
    }

    @Test
    public void exportSecKillUrl() {
        long id = 1000;
        Exposer exposer = secKillService.exportSecKillUrl(id);
        logger.info("exposer={}",exposer);
        //exposer=Exposer{exposed=true, md5='c78a6784f8e8012796c934dbb3f76c03',
        //          seckillId=1000, now=0, start=0, end=0}
        // 表示在秒杀时间范围内
    }

    @Test
    public void executeSeckill() {
        long id = 1000;
        long phone = 10134256781L;
        String md5 = "c78a6784f8e8012796c934dbb3f76c03";

        // 重复测试会抛出异常，junit会认为测试失败，要把异常捕获一下更好看
        try {
            SeckillExecution seckillExecution = secKillService.executeSeckill(id,phone,md5);
            logger.info("result: {}",seckillExecution);
        } catch (RepeatKillException | SeckillCloseException e) {
            logger.error(e.getMessage());
            // 再运行一次： [ERROR] seckill repeated
        }


        // 有事务记录
        // Committing JDBC transaction on Connection [com.mchange.v2.c3p0.impl.NewProxyConnection@4b40f651]
        //[2021-10-05 16:45:00.000] [org.springframework.jdbc.datasource.DataSourceTransactionManager] [main] [384] [DEBUG] Releasing JDBC Connection [com.mchange.v2.c3p0.impl.NewProxyConnection@4b40f651] after transaction
        //result: SeckillExecution{seckillId=1000, state=1, stateInfo='秒杀成功', successKilled=SuccessKilled{seckillId=1000, userPhone=10134256781, state=0, createTime=Wed Oct 06 00:45:00 CST 2021}}
    }

    // 集成测试完整逻辑实现
    @Test
    public void testSeckillLogic() {
        long id = 1001;
        Exposer exposer = secKillService.exportSecKillUrl(id);
        if(exposer.isExposed()) {
            logger.info("exposer={}",exposer);

            long phone = 10134256781L;
            String md5 = exposer.getMd5();

            // 重复测试会抛出异常，junit会认为测试失败，要把异常捕获一下更好看
            try {
                SeckillExecution seckillExecution = secKillService.executeSeckill(id,phone,md5);
                logger.info("result: {}",seckillExecution);
            } catch (RepeatKillException | SeckillCloseException e) {
                logger.error(e.getMessage());
                // 再运行一次： [ERROR] seckill repeated
            }

        } else {
            // 秒杀未开启
            logger.warn("exposer={}",exposer);
        }
    }
}