package cn.orzlinux.dao;

import cn.orzlinux.entity.SuccessKilled;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {
    // 注入DAO实现类依赖,resource注解去spring容器找其实现类
    @Resource
    private SuccessKilledDao successKilledDao;
    @Test
    public void insertSuccessKilled() {
        long id = 1000L;
        long phone = 12345678901L;
        int insertCount = successKilledDao.insertSuccessKilled(id,phone);
        System.out.println(insertCount);
        // 输出1
        // 再运行一次输出 0，联合主键的效果
    }

    @Test
    public void queryByIdWithSeckill() {
        long id = 1000L;
        long phone = 12345678901L;
        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(id,phone);
        System.out.println(successKilled);
        System.out.println(successKilled.getSecKill());
        // SuccessKilled{seckillId=1000, userPhone=12345678901, state=-1,
        //              createTime=Tue Oct 05 10:33:43 CST 2021}
        // SecKill{seckillId=1000, name='1000秒杀iphone13', number=99,
        //          startTime=Tue Oct 05 02:00:00 CST 2021, endTime=Wed Oct 06 02:00:00 CST 2021, createTime=Tue Oct 05 10:01:47 CST 2021}
    }
}