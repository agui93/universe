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







