-- 数据库初始化脚本

-- 创建数据库
CREATE DATABASE seckill;
-- 使用数据库
use seckill;
-- 创建秒杀库存表
CREATE TABLE seckill(
    `seckill_id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
    `name` varchar(120) NOT NULL COMMENT '商品名称',
    `number` int NOT NULL COMMENT '库存数量',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `start_time` timestamp NOT NULL COMMENT '秒杀开启时间',
    `end_time` timestamp NOT NULL COMMENT '秒杀结束时间',
    PRIMARY KEY (seckill_id),
    KEY idx_start_time(start_time),
    KEY idx_end_time(end_time),
    KEY idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT="秒杀库存表";

-- 初始化数据
insert into seckill(name,number,start_time,end_time)
values
    ('1000秒杀iphone13',999999,'2021-10-04 18:00:00','2021-10-05 18:00:00'),
    ('500秒杀iphone12',999999,'2021-10-04 18:00:00','2031-10-05 18:00:00'),
    ('300秒杀iphone11',99999,'2031-10-04 18:00:00','2032-10-05 18:00:00'),
    ('100秒杀iphone6',0,'2021-10-04 18:00:00','2021-10-05 18:00:00');

-- 秒杀成功明细表
-- 用户登录认证相关的信息
-- 需要明确用户身份、这里简化为一个字段
create table success_killed(
    `seckill_id` bigint NOT NULL COMMENT '秒杀商品id',
    `user_phone` bigint NOT NULL COMMENT '用户手机号',
    `state` tinyint NOT NULL DEFAULT -1 COMMENT '状态标识：-1无效，0成功，1已付款，2已发货',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY(seckill_id,user_phone),/*联合主键*/
    KEY idx_create_time(create_time)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT="秒杀成功明细表";

-- 连接数据库控制台
-- mysql -uroot -p