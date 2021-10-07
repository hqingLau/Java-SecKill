package cn.orzlinux.dao;

import cn.orzlinux.entity.SuccessKilled;
import org.apache.ibatis.annotations.Param;

public interface SuccessKilledDao {
    // 插入购买明细，可过滤重复
    // 通过联合唯一主键
    // 返回插入的行数
    int insertSuccessKilled(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);

    // 根据商品id和用户查询秒杀成功对象实体（带秒杀商品实体）
    // 原视频应该有误，这里仅凭一个商品Id得不到唯一的秒杀成功对象
    SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);
}
