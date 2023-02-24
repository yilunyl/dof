
## 框架说明
### 一 背景 
#### 概述 
    在单体应用下，随着业务的复杂度提高，结合DDD设计思想，基于四层模型结构，充分提高业务代码的复用性，在此基础上衍生出了能对领域服务进行灵活编排，快速适应业务的诉求。
#### 目标
    业务：领域编排 可插拔(易拓展) 稳定 易用 复用
    技术：ddd spring netty pool thread engine zk redis lock tree dag  
### 二 DDD的基本结构
DDD的整体架构如下
    ![img.png](img.png)
具体编程规范可以参考 com.yilun.gl.dof.excute.framework.ddd的分包
### 三 编排核心结构
    >画了个示意图 实在没想好怎么画合适！！！
![img_2.png](img_2.png)
### 四 使用说明
#### 引入相关依赖
    <groupId>org.apache.dof</groupId>
    <artifactId>biz-dof</artifactId>
    <version>最新版本</version>
#### 初始化application(流程和参数初始化)
    继承 com.yilun.gl.dof.excute.framework.core.entry.AbstractApplication，并实现如下方法
        #initDoSvrGroup方法：核心编排方法
        #initContext方法：初始化全局上下文
        #buildResponse方法：构建最终返回值
    详细参考参考 com.yilun.gl.dof.excute.framework.usage.application.SomeThingApplication
#### 使用application
    自动注入@bean#SomeThingApplication
    然后使用 someThingApplication.doLogicSchedule(testRequest)
### 五 使用示例
#### 单侧启动 
    由于使用了jdk17 单侧启动需要增加参数--add-opens java.base/java.lang=ALL-UNNAMED 
#### 启动类 
    com.yilun.gl.dof.excute.framework.usage.client.EntryTest.entryTest
#### 构建流程类
    com.yilun.gl.dof.excute.framework.usage.application.SomeThingApplication
### 六 其他
#### 1、如何实现DagApplicationExecutor，将编排框架升级为有向无换图结构，可以更加的灵活的编排领域服务？
#### 2、如何更加优雅的使用大的context，全局使用一个大的上下文数据结构不是很优雅？？
###### dof-0.0.1:采用全局context 需要使用者自己继承context，添加自己需要属性 
>该方式经过实际验证,详细使用实例在tag[dof-0.0.1]上面
###### 最新master:采用AttributeMap的形式去构建，参考netty
>目前还在开发中
#### 3、目前框架实现的编排领域服务，是否有必要对聚合根进行编排？？
