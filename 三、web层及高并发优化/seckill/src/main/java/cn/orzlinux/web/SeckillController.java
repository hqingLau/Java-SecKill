package cn.orzlinux.web;

import cn.orzlinux.dto.Exposer;
import cn.orzlinux.dto.SeckillExecution;
import cn.orzlinux.dto.SeckillResult;
import cn.orzlinux.entity.SecKill;
import cn.orzlinux.enums.SecKillStatEnum;
import cn.orzlinux.exception.RepeatKillException;
import cn.orzlinux.exception.SeckillCloseException;
import cn.orzlinux.service.SecKillService;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.rmi.server.ServerCloneException;
import java.util.Date;
import java.util.List;

@Controller // @Service @Component放入spring容器
@RequestMapping("/seckill") // url:模块/资源/{id}/细分
public class SeckillController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SecKillService secKillService;
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public String list(Model model) {
        // list.jsp + model = modelandview
        List<SecKill> list = secKillService.getSecKillList();
        model.addAttribute("list",list);
        return "list";
    }

    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
        if (seckillId == null) {
            // 0. 不存在就重定向到list
            // 1. 重定向访问服务器两次
            // 2. 重定向可以重定义到任意资源路径。
            // 3. 重定向会产生一个新的request，不能共享request域信息与请求参数
            return "redrict:/seckill/list";

        }
        SecKill secKill = secKillService.getById(seckillId);
        if (secKill == null) {
            // 0. 为了展示效果用forward
            // 1. 转发只访问服务器一次。
            // 2. 转发只能转发到自己的web应用内
            // 3. 转发相当于服务器跳转，相当于方法调用，在执行当前文件的过程中转向执行目标文件，
            //      两个文件(当前文件和目标文件)属于同一次请求，前后页 共用一个request，可以通
            //      过此来传递一些数据或者session信息
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill",secKill);
        return "detail";
    }

    // ajax json
    @RequestMapping(value = "/{seckillId}/exposer",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF8"})
    @ResponseBody
    public SeckillResult<Exposer> exposer(Long seckillId) {
        SeckillResult<Exposer> result;
        try {
            Exposer exposer = secKillService.exportSecKillUrl(seckillId);
            result = new SeckillResult<Exposer>(true,exposer);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            result = new SeckillResult<>(false,e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/{seckillId}/{md5}/execution",
        method = RequestMethod.POST,
        produces = {"application/json;charset=UTF8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(
            @PathVariable("seckillId") Long seckillId,
            // required = false表示cookie逻辑由我们程序处理，springmvc不要报错
            @CookieValue(value = "killPhone",required = false) Long userPhone,
            @PathVariable("md5") String md5) {
        if (userPhone == null) {
            return new SeckillResult<SeckillExecution>(false, "未注册");
        }
        SeckillResult<SeckillExecution> result;
        try {
            // 通过存储过程调用
            SeckillExecution execution = secKillService.executeSeckillProcedure(seckillId, userPhone, md5);
            if(execution.getState()==1) result = new SeckillResult<SeckillExecution>(true, execution);
            else result = new SeckillResult<>(false,execution);
            return result;
        } catch (SeckillCloseException e) { // 秒杀关闭
            SeckillExecution execution = new SeckillExecution(seckillId, SecKillStatEnum.END);
            return new SeckillResult<SeckillExecution>(false,execution);
        } catch (RepeatKillException e) { // 重复秒杀
            SeckillExecution execution = new SeckillExecution(seckillId, SecKillStatEnum.REPEAT_KILL);
            return new SeckillResult<SeckillExecution>(false,execution);
        } catch (Exception e) {
            // 不是重复秒杀或秒杀结束，就返回内部错误
            logger.error(e.getMessage(), e);
            SeckillExecution execution = new SeckillExecution(seckillId, SecKillStatEnum.INNER_ERROR);
            return new SeckillResult<SeckillExecution>(false,execution);
        }

    }

    @RequestMapping(value = "/time/now",method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time() {
        Date now = new Date();
        return new SeckillResult<Long>(true,now.getTime());
    }
}
