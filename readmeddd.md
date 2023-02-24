

#### 整个DDD包下面的东西是DDD的一套规范,工程结构可以如下5层结构
> 更加详细的说明请参考 com.yilun.gl.dof.excute.framework.ddd 包里面内容，有详细的类说明
##### business-xxx-xxx-application
###### 【说明】重点做跨域的编排工作，无业务逻辑
##### business-xxx-xxx-client
###### 【说明】对外提供服务 如http接口，rpc的提供者
##### business-xxx-xxx-domain
###### 【说明】域服务,聚合根，值对象，领域参数，仓库定义
##### business-xxx-xxx-infrastructure
###### 【说明】所有技术代码在这一层。mybatis，redis，mq，job，opensearch代码都在这里实现，domain通过依赖倒置不依赖这些技术代码和JAR。
##### business-xxx-xxx-model
###### 【说明】要用的共享对象



