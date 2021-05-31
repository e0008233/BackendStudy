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
    * 粒度分类
        * 单机限流： 以上四种算法都是单机限流，例如Guava中开源出来一个令牌桶算法的工具类RateLimiter
        * 分布式限：
          1. 分布式限流最简单的实现就是利用中心化存储，即将单机限流存储在本地的数据存储到同一个存储空间中，如常见的Redis等。
          2. 当然也可以从上层流量入口进行限流，Nginx代理服务就提供了限流模块，同样能够实现高性能，精准的限流，其底层是漏桶算法。
          
* Distributed System
    * Kafka (百万级别吞吐量，少许丢失)，Rocket MQ（十万级别吞吐量，零丢失），Rabbit MQ (万级别吞吐量，社区文档优势)

* 消息队列 https://juejin.cn/post/6844904000840531981
    * 使用场景：异步、削峰、解耦
    
* Redis
    * 使用场景：防止把数据库打崩，
    * 数据结构：String，Hash，Set，Sorted Set，List
    * Redis持久化: RDB (Redis Database Backup) & AOF (Append Only File)
      * RDB：RDB 持久化机制，是对 Redis 中的数据执行周期性的持久化。
        - RDB的原理：fork和cow。fork是指redis通过创建子进程来进行RDB操作，cow指的是copy on write，子进程创建后，父子进程共享数据段，父进程继续提供读写服务，写脏的页面数据会逐渐和子进程分离开来。
        - 优点： 他会生成多个数据文件，每个数据文件分别都代表了某一时刻Redis里面的数据，这种方式，有没有觉得很适合做冷备，完整的数据运维设置定时任务，定时同步到远端的服务器，比如阿里的云服务，这样一旦线上挂了，你想恢复多少分钟之前的数据，就去远端拷贝一份之前的数据就好了。RDB对Redis的性能影响非常小，是因为在同步数据的时候他只是fork了一个子进程去做持久化的，而且他在数据恢复的时候速度比AOF来的快。
        - 缺点： RDB都是快照文件，都是默认五分钟甚至更久的时间才会生成一次，这意味着你这次同步到下次同步这中间五分钟的数据都很可能全部丢失掉。AOF则最多丢一秒的数据，数据完整性上高下立判。
          还有就是RDB在生成数据快照的时候，如果文件很大，客户端可能会暂停几毫秒甚至几秒，你公司在做秒杀的时候他刚好在这个时候fork了一个子进程去生成一个大快照，哦豁，出大问题。
      * AOF：AOF 机制对每条写入命令作为日志，以 append-only 的模式写入一个日志文件中，因为这个模式是只追加的方式，所以没有任何磁盘寻址的开销，所以很快，有点像Mysql中的binlog。
        - 优点：上面提到了，RDB五分钟一次生成快照，但是AOF是一秒一次去通过一个后台的线程fsync操作，那最多丢这一秒的数据。
          AOF在对日志文件进行操作的时候是以append-only的方式去写的，他只是追加的方式写数据，自然就少了很多磁盘寻址的开销了，写入性能惊人，文件也不容易破损。
          AOF的日志是通过一个叫非常可读的方式记录的，这样的特性就适合做灾难性数据误删除的紧急恢复了，比如公司的实习生通过flushall清空了所有的数据，只要这个时候后台重写还没发生，你马上拷贝一份AOF日志文件，把最后一条flushall命令删了就完事了。
        - 缺点： AOF开启后，Redis支持写的QPS会比RDB支持写的要低，他不是每秒都要去异步刷新一次日志嘛fsync，当然即使这样性能还是很高，我记得ElasticSearch也是这样的，异步刷新缓存区的数据去持久化，为啥这么做呢，不直接来一条怼一条呢，那我会告诉你这样性能可能低到没办法用的，大家可以思考下为啥哟。
      * 出现问题时候，用RDB恢复，然后AOF做数据补全
    * Redis模式：主从模式 VS 哨兵sentinel模式 VS Redis cluster模式。主从复制是为了数据备份，哨兵是为了高可用，Redis主服务器挂了哨兵可以切换，集群则是因为单实例能力有限，搞多个分散压力。sentinel着眼于高可用，Cluster提高并发量。
      1. 主从模式：读写分离，备份数据、负载均衡，一个Master可以有多个Slaves。
      2. 哨兵sentinel：监控，自动转移，哨兵发现主服务器挂了后，就会从slave中重新选举一个主服务器。哨兵必须用三个实例去保证自己的健壮性。如果只有2个，当其中一个哨兵和redis 所在的机器下线，剩下的一个哨兵没法进行故障转移
         哨兵就是一个进程。
         - 集群监控：负责监控 Redis master 和 slave 进程是否正常工作。
         - 消息通知：如果某个 Redis 实例有故障，那么哨兵负责发送消息作为报警通知给管理员。
         - 故障转移：如果 master node 挂掉了，会自动转移到 slave node 上。
         - 配置中心：如果故障转移发生了，通知 client 客户端新的 master 地址。
      3. 集群：为了解决单机Redis容量有限的问题，将数据按一定的规则分配到多台机器，内存/QPS不受限于单机，可受益于分布式集群高扩展性。
    * Redis Key的过期策略
        1. 惰性删除：当读/写一个已经过期的key时，会触发惰性删除策略，直接删除掉这个过期key，很明显，这是被动的。
        2. 定期删除：由于惰性删除策略无法保证冷数据被及时删掉，所以 Redis 会定期主动淘汰一批已过期的key。
        3. 主动删除：当前已用内存超过maxMemory限定时，触发主动清理策略。主动设置的前提是设置了maxMemory的值。
    * Pipeline: 可以将多次IO往返的时间缩减为一次，前提是pipeline执行的指令之间没有因果相关性。

            