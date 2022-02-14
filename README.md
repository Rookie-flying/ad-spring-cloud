# ad-spring-cloud
 advertising systems
# 广告系统概览

![image-20211017105014994](https://gitee.com//Rookie-flying/tuchuang/raw/master/img/image-20211017105014994.png)

1. 广告主的广告投放，即有投放广告意愿的公司或个人，例如：宝马

2. 媒体方的广告曝光，即展示广告创意的媒介，最简单的认为就是广告位，例如：户外大屏幕，网页边栏、门户网站等等；

广告主预先在广告系统中投放广告（所以，广告主自然就是甲方，付费的一方），广告系统把广告数据曝光到媒体方（具体谁来付费给媒体，这个可以是广告系统来购买流量费用，也可以是媒体主动加入广告系统，参与广告曝光的分成）。CPM:Cost Per Mile；CPT:Cost Per Time; CPC:Cost Per Click

![image-20211017105649180](https://gitee.com//Rookie-flying/tuchuang/raw/master/img/image-20211017105649180.png)

![image-20211017105710222](https://gitee.com//Rookie-flying/tuchuang/raw/master/img/image-20211017105710222.png)

1. SpringCloud: 微服务框架，自动配置和服务发现等功能
2. MySQL: 广告数据的存储，检索系统监听MySQL，做增量索引
3. Kafka: 消息系统，各个微服务/进程之间的通信
