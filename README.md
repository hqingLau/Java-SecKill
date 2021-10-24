[![Awesome](https://awesome.re/badge.svg)](https://awesome.re)
[![github](https://img.shields.io/badge/作者-hqinglau-blue.svg)](https://orzlinux.cn)
[![github](https://img.shields.io/badge/博客-orzlinux.cn-brightgreen.svg)](https://orzlinux.cn)
[![github](https://img.shields.io/badge/csdn-@hqinglau-orange.svg)](https://blog.csdn.net/qq_36704378?spm=1010.2135.3001.5343&type=blog)



# Java秒杀系统

本文是[Java高并发秒杀API](https://www.imooc.com/u/2145618/courses?sort=publish)视频课程的项目，源项目地址[链接](https://github.com/liyifeng1994/seckill)。

## 项目过程中的笔记

一、[秒杀系统环境搭建与DAO层设计](https://orzlinux.cn/blog/javaseckill1-20211004.html)

二、 [秒杀系统Service层](https://orzlinux.cn/blog/javaseckill2-20211005.html)

三、[秒杀系统web层](https://orzlinux.cn/blog/javaseckill3-20211005.html)

四、[秒杀系统高并发优化](https://orzlinux.cn/blog/javaseckill4-20211006.html)

## 具体介绍

![image-20211006182314439](https://gitee.com/hqinglau/img/raw/master/img/20211006182314.png)



跳转到详情页，cookie中没有手机号要弹窗，手机号不正确（11位数字）要提示错误：

![image-20211006191323525](https://gitee.com/hqinglau/img/raw/master/img/20211006191323.png)

输入手机号后存入cookie，进入秒杀详情页：

<img src="https://gitee.com/hqinglau/img/raw/master/img/20211006201149.png" alt="image-20211006201149488" style="zoom:80%;" />

没有开始秒杀展示倒计时：

<img src="https://gitee.com/hqinglau/img/raw/master/img/20211006194407.png" alt="image-20211006194407145" style="zoom:67%;" />

最后使用了redis和mysql存储过程来优化系统设计。
