package com.yilun.gl.dof.excute.framework.ddd;

/**
 *每一层都定义了相应的接口主要目的是规范代码：
 * ●    application:CRQS模式，ApplicationCmdService是command，ApplicationQueryService是query
 * ●    service：是领域服务规范，其中定义了DomainService,应用系统需要继承它。
 * ●    model:是聚合根，实体，值对象的规范。
 *          ○    Aggregate和BaseAggregate：聚合根定义
 *          ○    Entity和BaseEntity：实体定义
 *          ○   Value和BaseValue：值对象定义
 *          ○   Param和BaseParam：领域层参数定义，用作域服务，聚合根和实体的方法参数
 *          ○   Lazy：描述聚合根属性是延迟加载属性，类似与hibernate。
 *          ○   Field：实体属性，用来实现update-tracing
 * ●    repository
 *          ○    Repository：仓库定义
 *          ○   AggregateRepository：聚合根仓库,定义聚合根常用的存储和查询方法
 * ●    event：事件处理
 * ●    exception：定义了不同层用的异常
 *          ○    AggregateException：聚合根里面抛的异常
 *          ○    RepositoryException：基础层抛的异常
 *          ○    EventProcessException：事件处理抛的
 */

/**
 *  整个DDD包下面的东西是DDD的一套规范,工程结构可以如下
 * business-xxx-xxx-application
 *【说明】重点做跨域的编排工作，无业务逻辑
 * business-xxx-xxx-client
 *【说明】对外提供服务 如http接口，rpc的提供者
 * business-xxx-xxx-domain
 *【说明】域服务,聚合根，值对象，领域参数，仓库定义
 * business-xxx-xxx-infrastructure
 *【说明】所有技术代码在这一层。mybatis，redis，mq，job，opensearch代码都在这里实现，domain通过依赖倒置不依赖这些技术代码和JAR。
 * business-xxx-xxx-model
 *【说明】要用的共享对象
 * 参考文档 https://blog.csdn.net/AlibabaTech1024/article/details/125674376
 */
