// 存放主要交互逻辑js
// javascript 模块化
var seckill={
    // 封装秒杀相关ajax的URL
    URL:{
        now: function () {
            return '/seckill/time/now';
        },
        exposer: function(seckillId) {
            return '/seckill/'+seckillId+'/exposer';
        },

        execution: function (seckillId,md5) {
            return '/seckill/'+seckillId+'/'+md5+'/execution';
        }
    },
    // 验证手机号
    validatePhone: function (phone) {
        if(phone && phone.length==11 && !isNaN(phone)) {
            return true;
        } else {
            return false;
        }
    },

    // id和显示计时的那个模块
    handleSeckill: function (seckillId,node) {
        // 处理秒杀逻辑
        // 在计时的地方显示一个秒杀按钮
        node.hide()
            .html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
        // 获取秒杀地址
        $.post(seckill.URL.exposer(),{seckillId},function (result) {
            if(result && result['success']) {
                var exposer = result['data'];
                if(exposer['exposed']) {
                    // 如果开启了秒杀
                    // 获取秒杀地址
                    var md5 = exposer['md5'];
                    var killUrl = seckill.URL.execution(seckillId,md5);
                    console.log("killurl: "+killUrl);
                    // click永远绑定，one只绑定一次
                    $('#killBtn').one('click',function () {
                        // 执行秒杀请求操作
                        // 先禁用按钮
                        $(this).addClass('disabled');
                        // 发送秒杀请求
                        $.post(killUrl,{},function (result) {
                            if(result) {

                                var killResult = result['data'];
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                // 显示秒杀结果
                                if(result['success']) {
                                    node.html('<span class="label label-success">'+stateInfo+'</span>');
                                } else {
                                    node.html('<span class="label label-danger">'+stateInfo+'</span>');
                                }


                            }
                            console.log(result);
                        })
                    });
                    node.show();
                } else {
                    // 未开始秒杀，这里是因为本机显示时间和服务器时间不一致
                    // 可能浏览器认为开始了，服务器其实还没开始
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];
                    // 重新进入倒计时逻辑
                    seckill.countdown(seckillId,now,start,end);
                }
            } else {
                console.log('result='+result);
            }
        })
    },

    countdown: function (seckillId,nowTime,startTime,endTime) {
        var seckillBox = $('#seckillBox');
        if(nowTime>endTime) {
            seckillBox.html('秒杀结束！');
        } else  if(nowTime<startTime) {
            // 秒杀未开始，计时
            var killTime = new Date(startTime + 1000);
            seckillBox.countdown(killTime,function (event) {
                // 控制时间格式
                var format = event.strftime('秒杀开始倒计时：%D天 %H时 %M分 %S秒');
                seckillBox.html(format);
                // 时间完成后回调事件
            }).on('finish.countdown', function () {
                // 获取秒杀地址，控制显示逻辑，执行秒杀
                seckill.handleSeckill(seckillId,seckillBox) ;
            })
        } else {
            // 秒杀开始
            seckill.handleSeckill(seckillId,seckillBox) ;
        }
    },

    // 详情页秒杀逻辑
    detail: {
        // 详情页初始化
        init: function (params) {
            // 手机验证和登录，计时交互
            // 规划交互流程
            // 在cookie中查找手机号
            var killPhone = $.cookie('killPhone');
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];
            // 验证手机号
            if(!seckill.validatePhone(killPhone)) {
                // 绑定手机号，获取弹窗输入手机号的div id
                var killPhoneModal = $('#killPhoneModal');
                killPhoneModal.modal({
                    show: true, //显示弹出层
                    backdrop: 'static',//禁止位置关闭
                    keyboard: false, //关闭键盘事件
                });
                $('#killPhoneBtn').click(function () {
                    var inputPhone = $('#killphoneKey').val();
                    // 输入格式什么的ok了就刷新页面
                    if(seckill.validatePhone(inputPhone)) {
                        // 将电话写入cookie
                        $.cookie('killPhone',inputPhone,{expires:7,path:'/seckill'});
                        window.location.reload();
                    } else {
                        // 更好的方式是把字符串写入字典再用
                        $('#killphoneMessage').hide().html('<label class="label label-danger">请输入11位手机号</label>').show(500);
                    }
                });
            }
            // 已经登录
            // 计时交互
            $.get(seckill.URL.now(),{},function (result) {
                if(result && result['success']) {
                    var nowTime = result['data'];
                    // 写到函数里处理
                    seckill.countdown(seckillId,nowTime,startTime,endTime);
                } else {
                    console.log('result: '+result);
                }
            });
        }
    }
}