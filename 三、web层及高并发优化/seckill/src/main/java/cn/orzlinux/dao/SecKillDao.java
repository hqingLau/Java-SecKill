package cn.orzlinux.dao;

import cn.orzlinux.entity.SecKill;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SecKillDao {
    /***
     * 减库存
     * @param seckillId
     * @param killTime
     * @return 如果影响行数>1，表示更新的记录行数
     */
    int reduceNumber(@Param("seckillId") long seckillId,@Param("killTime") Date killTime);

    /**
     * 根据Id查秒杀对象
     * @param seckillId
     * @return
     */
    SecKill queryById(long seckillId);

    List<SecKill> queryAll(@Param("offset") int offset, @Param("limit") int limit);

    // 使用存储过程执行秒杀
    void killByProcedure(Map<String,Object> paramMap);
}
