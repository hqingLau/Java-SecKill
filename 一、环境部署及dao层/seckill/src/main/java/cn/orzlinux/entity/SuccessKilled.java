/**
 * mysql> show columns from success_killed;
 * +-------------+------------+------+-----+-------------------+-------+
 * | Field       | Type       | Null | Key | Default           | Extra |
 * +-------------+------------+------+-----+-------------------+-------+
 * | seckill_id  | bigint(20) | NO   | PRI | NULL              |       |
 * | user_phone  | bigint(20) | NO   | PRI | NULL              |       |
 * | state       | tinyint(4) | NO   |     | -1                |       |
 * | create_time | timestamp  | NO   | MUL | CURRENT_TIMESTAMP |       |
 * +-------------+------------+------+-----+-------------------+-------+
 */
package cn.orzlinux.entity;

import java.util.Date;

public class SuccessKilled {
    private long seckillId;
    private long userPhone;
    private short state;
    private Date createTime;

    // 多对一
    private SecKill secKill;

    public SecKill getSecKill() {
        return secKill;
    }

    public void setSecKill(SecKill secKill) {
        this.secKill = secKill;
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public long getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(long userPhone) {
        this.userPhone = userPhone;
    }

    public short getState() {
        return state;
    }

    public void setState(short state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "SuccessKilled{" + "seckillId=" + seckillId +
                ", userPhone=" + userPhone + ", state=" + state +
                ", createTime=" + createTime + '}';
    }


}
