package cn.orzlinux.dao.cache;

import cn.orzlinux.dao.SecKillDao;
import cn.orzlinux.dto.SeckillResult;
import cn.orzlinux.entity.SecKill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDaoTest {
    private long id = 1001;
    @Autowired
    private RedisDao redisDao;

    @Autowired
    private SecKillDao secKillDao;

    @Test
    public void testSeckill() {
        // get and put
        // 从缓存中拿
        SecKill secKill = redisDao.getSeckill(id);
        if(secKill==null) {
            secKill = secKillDao.queryById(id);
            if(secKill != null) {
                String result = redisDao.putSeckill(secKill);
                System.out.println(result);
                secKill = redisDao.getSeckill(id);
                System.out.println(secKill);
            }
        }
    }
}