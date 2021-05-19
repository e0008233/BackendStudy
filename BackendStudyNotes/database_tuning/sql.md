用explain 和 SQL_NO_CACH去查查询sql  
https://zhuanlan.zhihu.com/p/51771446

sql intro
https://juejin.cn/post/6844904079118827527
连接器-查询缓存 - 分析器（sql语法）- 优化器（索引，合表）- 执行器 - 引擎

database tuning
https://juejin.cn/post/6844903919970156552
index suppression & performance degradation (索引被抑制)

日常工作中你是怎么优化SQL的？
加索引
避免返回不必要的数据
适当分批量进行
优化sql结构
分库分表
读写分离

索引介绍
1. https://juejin.cn/post/6844904083397017614
2. https://juejin.cn/post/6844904175441018887
3. https://juejin.cn/post/6844904194055340040

sql注意事项
https://juejin.cn/post/6844904098999828488

索引总结
1，最左前缀匹配原则。这是非常重要、非常重要、非常重要（重要的事情说三遍）的原则，MySQL会一直向右匹配直到遇到范围查询（>,<,BETWEEN,LIKE）就停止匹配。  
3，尽量选择区分度高的列作为索引，区分度的公式是 COUNT(DISTINCT col) / COUNT(*)。表示字段不重复的比率，比率越大我们扫描的记录数就越少。  
4，索引列不能参与计算，尽量保持列“干净”。比如，FROM_UNIXTIME(create_time) = '2016-06-06' 就不能使用索引，原因很简单，B+树中存储的都是数据表中的字段值，但是进行检索时，需要把所有元素都应用函数才能比较，显然这样的代价太大。所以语句要写成 ： create_time = UNIX_TIMESTAMP('2016-06-06')。  
5，尽可能的扩展索引，不要新建立索引。比如表中已经有了a的索引，现在要加（a,b）的索引，那么只需要修改原来的索引即可。  
6，单个多列组合索引和多个单列索引的检索查询效果不同，因为在执行SQL时，MySQL只能使用一个索引，会从多个单列索引中选择一个限制最为严格的索引。  

作者：Java3y
链接：https://juejin.cn/post/6844903645125820424
来源：掘金
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。

sql总结
https://juejin.cn/post/6883270227078070286

sql 索引和锁
https://juejin.cn/post/6844903645125820424

如何设计索引
https://zhuanlan.zhihu.com/p/343312997

