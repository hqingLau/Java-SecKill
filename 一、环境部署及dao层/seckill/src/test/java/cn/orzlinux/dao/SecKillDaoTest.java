package cn.orzlinux.dao;

import cn.orzlinux.entity.SecKill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * 配置spring和junit整合，junit启动时加载spring ioc容器
 * spring-test,junit
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SecKillDaoTest {
    // 注入DAO实现类依赖,resource注解去spring容器找其实现类
    @Resource
    private SecKillDao secKillDao;

    @Test
    public void reduceNumberTest() {
        Date killTime = new Date();
        int updateCount = secKillDao.reduceNumber(1000L,killTime);
        System.out.println(updateCount);
        // 1:表示更改了一条记录
    }

    @Test
    public void queryByIdTest() {
        long id =1000;
        SecKill secKill = secKillDao.queryById(id);
        System.out.println(secKill.getName());
        System.out.println(secKill);
        // output：1000秒杀iphone13
        //SecKill{seckillId=1000, name='1000秒杀iphone13',
        // number=100, startTime=Tue Oct 05 02:00:00 CST 2021,
        // endTime=Wed Oct 06 02:00:00 CST 2021,
        // createTime=Tue Oct 05 10:01:47 CST 2021}
    }

    @Test
    public void queryAllTest() {
        List<SecKill> secKillList = secKillDao.queryAll(0,100);
        for(SecKill secKill:secKillList) {
            System.out.println(secKill);
        }
    }
}