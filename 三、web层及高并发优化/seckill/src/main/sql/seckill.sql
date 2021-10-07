-- 秒杀执行存储过程
DELIMITER $$ -- console ;转化为\$\$ 表示sql可以执行操作了
-- 定义存储过程
-- 参数：in 输入参数; out 输出参数
-- row_count(): 返回上一条修改类型sql的影响行数
-- row_count: 0未修改数据，>0 修改的行数，<0 sql错误或未执行

# SUCCESS(1,"秒杀成功"),
# END(0,"秒杀结束"),
# REPEAT_KILL(-1,"重复秒杀"),
# INNER_ERROR(-2,"系统异常"),
# DATA_REWRITE(-3,"数据篡改")
CREATE PROCEDURE `seckill`.`execute_seckill`
    (in v_seckill_id bigint, in v_phone bigint,
        in v_kill_time timestamp,out r_result int)
    BEGIN
        DECLARE insert_count int DEFAULT 0;
        START TRANSACTION;
        insert ignore into success_killed
            (seckill_id, user_phone,create_time)
            values (v_seckill_id,v_phone,v_kill_time);
        select row_count() into insert_count;
        IF (insert_count=0) THEN
            ROLLBACK;
            set r_result = -1;
        ELSEIF (insert_count<0) THEN
            ROLLBACK;
            set r_result = -2;
        ELSE
            update seckill
                set number = number-1
                where seckill_id = v_seckill_id
                    and end_time > v_kill_time
                    and start_time < v_kill_time
                    and number>0;
            select row_count() into insert_count;
            IF (insert_count = 0) THEN
                ROLLBACK;
                set r_result = 0;
            ELSEIF(insert_count<0) then
                ROLLBACK;
                set r_result = -2;
            ELSE
                COMMIT;
                set r_result = 1;
            end if;
        end if;
    END;
$$ -- 存储过程定义结束
delimiter ;

-- console定义变量
set @r_result=-3;
-- 执行存储过程
call execute_seckill(1001,19385937587,now(),@r_result);
-- 获取结果
select @r_result;

