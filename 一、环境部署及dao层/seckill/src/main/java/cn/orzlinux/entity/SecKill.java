/**
 * 对应数据库中的字段
 * mysql> select * from seckill;
 * +------------+------------------+--------+---------------------+---------------------+---------------------+
 * | seckill_id | name             | number | create_time         | start_time          | end_time            |
 * +------------+------------------+--------+---------------------+---------------------+---------------------+
 * |       1000 | 1000秒杀iphone13 |    100 | 2021-10-04 19:22:42 | 2021-10-04 18:00:00 | 2021-10-05 18:00:00 |
 * |       1001 | 500秒杀iphone12  |    200 | 2021-10-04 19:22:42 | 2021-10-04 18:00:00 | 2021-10-05 18:00:00 |
 * |       1002 | 300秒杀iphone11  |    300 | 2021-10-04 19:22:42 | 2021-10-04 18:00:00 | 2021-10-05 18:00:00 |
 * |       1003 | 100秒杀iphone6   |    400 | 2021-10-04 19:22:42 | 2021-10-04 18:00:00 | 2021-10-05 18:00:00 |
 * +------------+------------------+--------+---------------------+---------------------+---------------------+
 */
package cn.orzlinux.entity;

import java.util.Date;

public class SecKill {
    private long seckillId;
    private String name;
    private int number;
    private Date startTime;
    private Date endTime;
    private Date createTime;

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }



    public void setNumber(int number) {
        this.number = number;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "SecKill{" + "seckillId=" + seckillId + ", name='" + name + '\'' + ", number=" + number + ", startTime=" + startTime + ", endTime=" + endTime + ", createTime=" + createTime + '}';
    }
}
