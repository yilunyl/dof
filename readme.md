
## 框架说明
### 一 背景 
#### 概述 
    在单体/微服务应用下，随着业务的复杂度不断提高，业务功能不断迭代，在保证业务快速发展的前提下，如何写出更可靠、复用的代码成为了难题，为了尝试解决该问题，衍生出了一套自己的方法.
    主要想解决以下业务中碰到的一些问题
    1、业务核心流程不清晰
    2、业务核心功能不清晰，导致开发时到处写if
    3、边界混乱,业务和业务、业务和技术相互依赖
    5、缺乏系统性的规范，如异常、code等
    6、开发不规范
> 本方案主要是使用单体应用内，尝试解决的是单体内问题。
#### 目标
    业务：领域编排 可插拔(易拓展) 稳定 易用 复用
    技术：ddd spring netty pool thread engine zk redis lock tree dag  
### 二 DDD的基本结构
 DDD的整体架构如下

<img alt="img.png" height="300" src="img.png" width="400"/>

 具体编程规范可以参考 com.yilun.gl.dof.excute.framework.ddd的分包
### 三 编排核心结构
画了个示意图 实在没想好怎么画合适

<img alt="img_2.png" height="300" src="img_2.png" width="400"/>

### 四 使用说明
#### 一、引入相关依赖
    <groupId>org.apache.dof</groupId>
    <artifactId>biz-dof</artifactId>
    <version>最新版本</version>
#### 二、初始化application(流程和参数初始化)
> 继承 com.gl.dof.core.excute.framework.entry.AbstractApplication，并实现如下方法
##### 1、#initDoSvrGroup方法：核心编排方法
```
	@Override
	public LogicExecutor initDoSvrGroup() {
		log.warn("SomeThing业务createLogicExecutor");
		return new TreeApplicationExecutor(new BasicApplication() {
			@Override
			protected void init(TreeWrapper treeWrapper) {
			    //操作a b c
				treeWrapper.parallelAdd(grayDoSvr, timeDoSvr, channelDoSvr);
				//操作d e
				treeWrapper.parallelAdd(persionSelectDoSvr, predioctDesDoSvr);
				//操作f g h 
				treeWrapper.parallelAdd(tripDoSvr, featureDoSvr, carorderDoSvr);
				//操作i
				treeWrapper.add(libraDoSvr);
				//操作j
				treeWrapper.add(strategyResponseDataDoSvr);
			}
		});
	}

```
##### 2、#initContext方法：初始化全局上下文
```
	@Override
	protected void initContext(HandleContext ctx, TestRequest testRequest, Object... others) {
		ctx.attr(TestRequest.class).set(testRequest);
		log.info("SomeThingApplication success");
	}
```
##### 3、#buildResponse方法：构建最终返回值
```
	@Override
	protected TestResponse buildResponse(LogicResult logicResult, HandleContext ctx) {
		boolean hasAttr = ctx.hasAttr(TestRequest.class);
		if(!hasAttr){
			return null;
		}
		TestRequest testRequest2 = ctx.attr(TestRequest.class).get();
		TestResponse testResponse = new TestResponse();
		testResponse.setFinalStringName(testRequest2.getName());
		return testResponse;
	}
```
>    详细参考参考 com.yilun.gl.dof.excute.framework.application.SomeThingApplication
#### 三、使用application
    自动注入@bean#SomeThingApplication
    然后使用 someThingApplication.doLogicSchedule(testRequest)
```java
public class EntryTest extends BizDofApplicationTests {
	@Resource
	private SomeThingApplication someThingApplication;
	@Test
	public void entryTest(){
		TestRequest testRequest = new TestRequest();
		TestResponse response = someThingApplication.doLogicSchedule(testRequest);
		Assert.assertNotNull(response);
	}
}
```
#### 四、自定义业务逻辑的处理
    实现父接口 com.gl.dof.core.excute.framework.logic.DomainService 其核心接口如下
```angular2html
    /**
    * 执行操作，判断当前领域服务是否应该执行
    */
    boolean  isMatch(HandleContext context);
    /**
    * 业务逻辑实现处，实际写业务逻辑的地方，建议是对聚合根进行编排
    */
    LogicResult doLogic(HandleContext context);
    /**
    * 回滚操作，当doLogic执行失败，或者主链路失败时的回滚操作
    */
    void reverse(HandleContext context, LogicResult logicResult);
```

#### 五、补充-HandleContext
    继承自 AttributeMap，有如下关键方法
```
	public <T> Attribute<T> attr(Class<T> c);
	public <T> Attribute<T> attr(Class<T> c, String alias);
	public <T> boolean hasAttr(Class<T> c);
	public <T> boolean hasAttr(Class<T> c, String alias);
```
    用来获取HandleContext上下中的一些对象，线程安全
#### 五、补充-TreeApplicationExecutor
    编排执行器，是父接口 LogicExecutor 的树结构的一种实现，有如下关键方法
```
    LogicResult doLogicSchedule(HandleContext ctx);
```
     
### 五 使用示例
#### 单侧启动 
    由于使用了jdk17 单侧启动需要增加参数--add-opens java.base/java.lang=ALL-UNNAMED 
#### 启动类 
    com.yilun.gl.dof.excute.framework.usage.client.EntryTest.entryTest
#### 构建流程类
    com.yilun.gl.dof.excute.framework.application.SomeThingApplication
### 六 其他
#### 1、如何实现DagApplicationExecutor，将编排框架升级为有向无换图结构，可以更加的灵活的编排领域服务？
#### 2、如何更加优雅的使用大的context，全局使用一个大的上下文数据结构不是很优雅？？
###### dof-0.0.1:采用全局context 需要使用者自己继承context，添加自己需要属性 
>该方式经过实际验证,详细使用实例在tag[dof-0.0.1]上面
###### 最新master:采用AttributeMap的形式去构建，参考netty
>目前还在开发中
#### 3、目前框架实现的编排领域服务，是否有必要对聚合根进行编排？？
### 七 版本说明
    dof-0.0.1 
