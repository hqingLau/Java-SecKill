package cn.orzlinux.dao.cache;

import cn.orzlinux.entity.SecKill;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.nio.charset.StandardCharsets;

public class RedisDao {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private JedisPool jedisPool;
    public RedisDao(String ip,int port) {
        jedisPool = new JedisPool(ip,port);
    }

    private RuntimeSchema<SecKill> schema = RuntimeSchema.createFrom(SecKill.class);

    public SecKill getSeckill(long seckillId) {
        // redis操作逻辑
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:"+seckillId;
                // redis并没有实现内部序列化操作
                // get得到的是一个二进制数组byte[],通过反序列化-> Object(SecKill)
                // 采用自定义序列化 protostuff
                // protostuff:pojo 有get set这些方法
                byte[] bytes = jedis.get(key.getBytes(StandardCharsets.UTF_8));
                // 获取到了，需要protostuff转化
                // 需要字节数组和schema
                if (bytes != null) {
                    // 创建一个空对象来放反序列化生成的对象
                    SecKill secKill = schema.newMessage();
                    ProtostuffIOUtil.mergeFrom(bytes,secKill,schema);
                    // seckill被反序列化
                    return secKill;
                }
            } finally {
                jedis.close();
            }

        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    public String putSeckill(SecKill secKill) {
        // set: objest(SecKill) -> bytes[] 序列化操作
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:"+secKill.getSeckillId();
                byte[] bytes = ProtostuffIOUtil.toByteArray(secKill,schema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                // 超时缓存
                int timeout = 60*60; // 1小时
                String result = jedis.setex(key.getBytes(StandardCharsets.UTF_8),timeout,bytes);
                return result; //加入缓存信息，成功还是失败
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }
}
