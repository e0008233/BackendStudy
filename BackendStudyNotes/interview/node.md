https://www.zhihu.com/question/60949531/answer/1810687397

* 数据库调优 database tuning  
    * Sql performance tuning
        * add and keep necessary indexes(composite index includes left most single index) and avoid index suppression ( not erqual on indexes, like statement like %123 ), no operation on the index, no tye conversion on the index
        * only querying necessary data(limit, avoid *)
        * run the complicated query in off-peak hours (through scheduler)
        * covering index (index includes queried columns,like composite index containing many columns)
        * link: https://juejin.cn/post/6844904098999828488
    * Command query responsibility segregation (CQRS,读写分离)
        * It is useful when having a lot of read command, a large amount of concurrent requests and less strict requirement on data consistency
        * Implementation: critical requests are handled by master, less critical requests are using master-slave replication (achieved by binlog)
        
* 限流线路
    * 算法分类
        * 固定创口计数器: 维护一个固定单位时间内的计数器，如果检测到单位时间已经过去就重置计数器为零。
          1. 时间线划分为多个独立且固定大小窗口；
          2. 落在每一个时间窗口内的请求就将计数器加1；
          3. 如果计数器超过了限流阈值，则后续落在该窗口的请求都会被拒绝。但时间达到下一个时间窗口时，计数器会被重置为0。
          同时也存在两个比较严重缺陷。试想一下，固定时间窗口1s限流阈值为100，但是前100ms，已经请求来了99个，那么后续的900ms只能通过一个了，就是一个缺陷，基本上没有应对突发流量的能力。第二个缺陷，在00:00:00这个时间窗口的后500ms，请求通过了100个，在00:00:01这个时间窗口的前500ms还有100个请求通过，对于服务来说相当于1秒内请求量达到了限流阈值的2倍。
        * 滑动窗口计数器：滑动时间窗口算法是对固定时间窗口算法的一种改进，固定窗口计数器可以说是滑动窗口计数器的一种特例。
          1. 将单位时间划分为多个区间，一般都是均分为多个小的时间段；
          2. 每一个区间内都有一个计数器，有一个请求落在该区间内，则该区间内的计数器就会加一；
          3. 每过一个时间段，时间窗口就会往右滑动一格，抛弃最老的一个区间，并纳入新的一个区间；
          4. 计算整个时间窗口内的请求总数时会累加所有的时间片段内的计数器，计数总和超过了限制数量，则本窗口内所有的请求都被丢弃。
          常见的实现方式主要有基于redis zset的方式和循环队列实现。基于redis zset可将Key为限流标识ID，Value保持唯一，可以用UUID生成，Score 也记为同一时间戳，最好是纳秒级的。使用redis提供的 ZADD、EXPIRE、ZCOUNT 和 zremrangebyscore 来实现，并同时注意开启 Pipeline 来尽可能提升性能。实现很简单，但是缺点就是zset的数据结构会越来越大。
        * 漏桶算法：漏桶算法是水先进入到漏桶里，漏桶再以一定的速率出水，当流入水的数量大于流出水时，多余的水直接溢出。把水换成请求来看，漏桶相当于服务器队列，但请求量大于限流阈值时，多出来的请求就会被拒绝服务。漏桶算法使用队列实现，可以以固定的速率控制流量的访问速度，可以做到流量的“平整化”处理。
          1. 将每个请求放入固定大小的队列进行存储；
          2. 队列以固定速率向外流出请求，如果队列为空则停止流出；
          3. 如队列满了则多余的请求会被直接拒绝· 
        * 令牌桶算法：
          1. 令牌以固定速率生成并放入到令牌桶中；
          2. 如果令牌桶满了则多余的令牌会直接丢弃，当请求到达时，会尝试从令牌桶中取令牌，取到了令牌的请求可以执行；
          3. 如果桶空了，则拒绝该请求。
        * 四种策略该如何选择？
          1. 固定窗口：实现简单，但是过于粗暴，除非情况紧急，为了能快速止损眼前的问题可以作为临时应急的方案。
          2. 滑动窗口：限流算法简单易实现，可以应对有少量突增流量场景。
          3. 漏桶：对于流量绝对均匀有很强的要求，资源的利用率上不是极致，但其宽进严出模式，保护系统的同时还留有部分余量，是一个通用性方案。
          4. 令牌桶：系统经常有突增流量，并尽可能的压榨服务的性能。
* Distributed System
    * Kafka (百万级别吞吐量，少许丢失)，Rocket MQ（十万级别吞吐量，零丢失），Rabbit MQ (万级别吞吐量，社区文档优势)

* 
            