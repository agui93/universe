# kafka

https://kafka.apache.org/0102/javadoc/index.html

raw编程 <br> 
语言开发框架(不同的语言版本以及不同的方案对比) <br>
源码搭建 <br>  
原理设计和实现 <br>  
性能测试(指标 测试方案 测验报告) <br>
监控指标和监控工具 <br>
集群的部署和日常维护(另外:容器编排的方式维护) <br> 
经典的业务场景和问题 <br>
主要的配置属性 <br>
不同版本的kafka特性 <br>
其他类型的消息中间件的对比 <br>
参考 <br>


## 基本特点

异步 解耦 数据持久化 局部顺序保证 扩展和容灾 缓冲和消峰


## Core Concept
Topic Partition Log
Broker
Leader Follower Replica  In-Sync-Replica 
HW(HighWatermark) LEO(Log End Offset)
Cluster＆Controller
Producer Consumer Consumer-Group


## 源码

KafkaProducer的使用和相关配置，剖析核心代码及原理<br>
API样例;<br>
分析(宏观流程 部分关键的调用流程);<br>
关键功能:拦截消息、序列化消息、路由消息功能;RecordAccumulator的结构和实现;NetworkClient;新Kafka集群元数据;Sender线程 <br>

KafkaConsumer
API样例;<br>
消息的传递保证;  Consumer Group ReBalance;



## 主要的配置属性



## raw code

## 源码分析思路
梳理:基于主干实现的基本样例,源码分析环境构建

梳理:时序图 类图 

梳理:实例和类直接引用关系 

梳理:核心类、类的核心字段和方法

梳理:使用的模式

梳理:对象间协作 线程间的协作  -> 引用关系是分析的重要线索之一

尝试:根据分析结果的反向验证

对比提高:针对实现场景的抽象、针对实现思路的抽象 、对比类似的场景的其他实现(例如线程工具和效率保障、数据结构的设计、算法的抉择、网络交互、内存重用、回调设计、整体结构的设计等等)


## 源码分析-生产者

网络交互(网络负载的优化技巧、网络交互的统一抽象)

内存回收

线程协作

回调

开发人员使用的api、控制生产者的配置、生产者状况的指标判断/分析/调优思路








