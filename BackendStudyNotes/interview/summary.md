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
    

* 消息队列 https://juejin.cn/post/6844904000840531981
    * 使用场景：异步、削峰、解耦。
    * 缺点  
      1. 系统可用性降低:你想啊，本来其他系统只要运行好好的，那你的系统就是正常的。现在你非要加个消息队列进去，那消息队列挂了，你的系统不是呵呵了。因此，系统可用性降低
      2. 系统复杂性增加:要多考虑很多方面的问题，比如一致性问题、如何保证消息不被重复消费，如何保证保证消息可靠传输。因此，需要考虑的东西更多，系统复杂性增大。
    * Kafka (百万级别吞吐量，少许丢失)，Rocket MQ（十万级别吞吐量，零丢失），Rabbit MQ (万级别吞吐量，社区文档优势)
    * 如何保证消息队列是高可用的  
      * 普通集群模式：这个方案主要的目的是提高吞吐量的，就是说让集群中的多个节点来服务某个queue的读写操作。
        普通集群模式，意思就是在多台机器上启动多个 RabbitMQ 实例，每个机器启动一个。你创建的 queue，只会放在一个 RabbitMQ 实例上，但是每个实例都同步 queue 的元数据（元数据可以认为是 queue 的一些配置信息，通过元数据，可以找到 queue 所在实例）。消费的时候，实际上如果连接到了另外一个实例，那么那个实例会从 queue 所在实例上拉取数据过来。
        这种方式是个普通集群。因为消费者每次随机连接一个实例然后拉取数据，或者固定连接那个 queue 所在实例消费数据，前者有数据拉取的开销，后者导致单实例性能瓶颈。
        而且如果放 queue 的实例宕机了，会导致接下来其他实例就无法从那个实例拉取，如果开启了消息持久化，让 RabbitMQ 落地存储消息的话，消息不一定会丢，得等这个实例恢复了，然后才可以继续从这个 queue 拉取数据。
        这方案主要是提高吞吐量的，就是说让集群中多个节点来服务某个 queue 的读写操作。
      * 镜像集群模式
        RabbitMQ 的高可用模式。在镜像集群模式下，创建的 queue，无论元数据还是 queue 里的消息都会存在于多个实例上，就是说，每个 RabbitMQ 节点都有这个 queue 的一个完整镜像，包含 queue 的全部数据的意思。然后每次你写消息到 queue 的时候，都会自动把消息同步到多个实例的 queue 上。
        RabbitMQ 有很好的管理控制台，就是在后台新增一个策略，这个策略是镜像集群模式的策略，指定的时候是可以要求数据同步到所有节点的，也可以要求同步到指定数量的节点，再次创建 queue 的时候，应用这个策略，就会自动将数据同步到其他的节点上去了。
        任何一个机器宕机了，其它机器（节点）还包含了这个 queue 的完整数据，别的 consumer 都可以到其它节点上去消费数据。
        第一，这个性能开销大了，消息需要同步到所有机器上，导致网络带宽压力和消耗  
        第二，不是分布式的，没有扩展性可言，如果某个 queue 负载很重，加机器，新增的机器也包含了这个 queue 的所有数据，并没有办法线性扩展queue。
    * 如何保证消息不被重复消费
      * 原因：正常情况下，消费者在消费消息时候，消费完毕后，会发送一个确认信息给消息队列，消息队列就知道该消息被消费了，就会将该消息从消息队列中删除。只是不同的消息队列发送的确认信息形式不同,例如RabbitMQ是发送一个ACK确认消息，RocketMQ是返回一个CONSUME_SUCCESS成功标志，kafka实际上有个offset的概念，就是因为网络传输等等故障，确认信息没有传送到消息队列，导致消息队列不知道自己已经消费过该消息了，再次将该消息分发给其他的消费者。
      * 如何解决?这个问题针对业务场景来答分以下几点
        (1)比如，你拿到这个消息做数据库的insert操作。那就容易了，给这个消息做一个唯一主键，那么就算出现重复消费的情况，就会导致主键冲突，避免数据库出现脏数据。
        (2)再比如，你拿到这个消息做redis的set的操作，那就容易了，不用解决，因为你无论set几次结果都是一样的，set操作本来就算幂等操作。
        (3)如果上面两种情况还不行，上大招。准备一个第三方介质,来做消费记录。以redis为例，给消息分配一个全局id，只要消费过该消息，将<id,message>以K-V形式写入redis。那消费者开始消费前，先去redis中查询有没消费记录即可。  
    * 如何保证消费的可靠性传输? 生产者弄丢数据、消息队列弄丢数据、消费者弄丢数据
      1. 生产者弄丢数据：RabbitMQ提供transaction和confirm模式来确保生产者不丢消息。
        transaction机制就是说，发送消息前，开启事物(channel.txSelect())，然后发送消息，如果发送过程中出现什么异常，事物就会回滚(channel.txRollback())，如果发送成功则提交事物(channel.txCommit())。
        然而缺点就是吞吐量下降了。因此，按照博主的经验，生产上用confirm模式的居多。一旦channel进入confirm模式，所有在该信道上面发布的消息都将会被指派一个唯一的ID(从1开始)，一旦消息被投递到所有匹配的队列之后，rabbitMQ就会发送一个Ack给生产者(包含消息的唯一ID)，这就使得生产者知道消息已经正确到达目的队列了.如果rabiitMQ没能处理该消息，则会发送一个Nack消息给你，你可以进行重试操作。
      2. 消息队列丢数据: 处理消息队列丢数据的情况，一般是开启持久化磁盘的配置
         这个持久化配置可以和confirm机制配合使用，你可以在消息持久化磁盘后，再给生产者发送一个Ack信号。这样，如果消息持久化磁盘之前，rabbitMQ阵亡了，那么生产者收不到Ack信号，生产者会自动重发。
         那么如何持久化呢，这里顺便说一下吧，其实也很容易，就下面两步
         1、将queue的持久化标识durable设置为true,则代表是一个持久的队列
         2、发送消息的时候将deliveryMode=2
         这样设置以后，rabbitMQ就算挂了，重启后也能恢复数据
      3. 消费者丢数据 
         消费者丢数据一般是因为采用了自动确认消息模式。这种模式下，消费者会自动确认收到信息。这时rahbitMQ会立即将消息删除，这种情况下如果消费者出现异常而没能处理该消息，就会丢失该消息。
         至于解决方案，采用手动确认消息即可。
    * 如何保证消息的顺序性？   
      针对这个问题，通过某种算法，将需要保持先后顺序的消息放到同一个消息队列中(kafka中就是partition,rabbitMq中就是queue)。然后只用一个消费者去消费该队列。
      有的人会问:那如果为了吞吐量，有多个消费者去消费怎么办？
      这个问题，没有固定回答的套路。比如我们有一个微博的操作，发微博、写评论、删除微博，这三个异步操作。如果是这样一个业务场景，那只要重试就行。比如你一个消费者先执行了写评论的操作，但是这时候，微博都还没发，写评论一定是失败的，等一段时间。等另一个消费者，先执行写评论的操作后，再执行，就可以成功。
      总之，针对这个问题，我的观点是保证入队有序就行，出队以后的顺序交给消费者自己去保证，没有固定套路。


* Redis: C语言开发的一个开源的（遵从BSD协议）高性能键值对（key-value）的内存数据库，可以用作数据库、缓存、消息中间件等。
    * 使用场景：防止把数据库打崩，快速相应
    * 性能高效原因
      * 完全基于内存，查找和操作的时间复杂度都是 O(1)。
      * IO 多路复用，非阻塞IO (SELECT:遍历查询，有数量限制/POLL：遍历查询，无数量限制/EPOLL：读写就绪时查询关键点)
      * 采用单线程，避免了不必要的上下文切换和竞争条件，也不存在多进程或者多线程导致的切换而消耗 CPU
      * 数据结构简单，对数据操作也简单，如哈希表、跳表都有很高的性能。
    * Redis和Memcached的区别  
      1、存储方式上：memcache会把数据全部存在内存之中，断电后会挂掉，数据不能超过内存大小。redis有部分数据存在硬盘上，这样能保证数据的持久性。  
      2、数据支持类型上：memcache对数据类型的支持简单，只支持简单的key-value，，而redis支持五种数据类型。  
      3、使用底层模型不同：它们之间底层实现方式以及与客户端之间通信的应用协议不一样。redis直接自己构建了VM机制，因为一般的系统调用系统函数的话，会浪费一定的时间去移动和请求。  
      4、value的大小：redis可以达到1GB，而memcache只有1MB。
    * 数据结构：String，Hash，Set，Sorted Set（跳跃表 skipList），List
      (一)String
      这个其实没啥好说的，最常规的set/get操作，value可以是String也可以是数字。一般做一些复杂的计数功能的缓存。
      (二)hash
      这里value存放的是结构化的对象，比较方便的就是操作其中的某个字段。博主在做单点登录的时候，就是用这种数据结构存储用户信息，以cookieId作为key，设置30分钟为缓存过期时间，能很好的模拟出类似session的效果。
      (三)list
      使用List的数据结构，可以做简单的消息队列的功能。另外还有一个就是，可以利用lrange命令，做基于redis的分页功能，性能极佳，用户体验好。
      (四)set
      因为set堆放的是一堆不重复值的集合。所以可以做全局去重的功能。为什么不用JVM自带的Set进行去重？因为我们的系统一般都是集群部署，使用JVM自带的Set，比较麻烦，难道为了一个做一个全局去重，再起一个公共服务，太麻烦了。
      另外，就是利用交集、并集、差集等操作，可以计算共同喜好，全部的喜好，自己独有的喜好等功能。
      (五)sorted set
      sorted set多了一个权重参数score,集合中的元素能够按score进行排列。可以做排行榜应用，取TOP N操作。另外，参照另一篇《分布式之延时任务方案解析》，该文指出了sorted set可以用来做延时任务。最后一个应用就是可以做范围查找。
    * Redis持久化: RDB (Redis Database Backup) & AOF (Append Only File) https://mp.weixin.qq.com/s/O_qDco6-Dasu3RomWIK_Ig
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
         - 一旦主节点宕机，从节点晋升为主节点，同时需要修改应用方的主节点地址，还需要命令所有从节点去复制新的主节点，整个过程需要人工干预。
         - 主节点的写能力受到单机的限制。
         - 主节点的存储能力受到单机的限制。
         - 原生复制的弊端在早期的版本中也会比较突出，比如：redis复制中断后，从节点会发起psync。此时如果同步不成功，则会进行全量同步，主库执行全量备份的同时，可能会造成毫秒或秒级的卡顿。
      2. 哨兵sentinel：监控，自动转移，哨兵发现主服务器挂了后，就会从slave中重新选举一个主服务器。哨兵必须用三个实例去保证自己的健壮性。如果只有2个，当其中一个哨兵和redis 所在的机器下线，剩下的一个哨兵没法进行故障转移
         哨兵就是一个进程。
         - 集群监控：负责监控 Redis master 和 slave 进程是否正常工作。
         - 消息通知：如果某个 Redis 实例有故障，那么哨兵负责发送消息作为报警通知给管理员。
         - 故障转移：如果 master node 挂掉了，会自动转移到 slave node 上。
         - 配置中心：如果故障转移发生了，通知 client 客户端新的 master 地址。
      3. 集群：为了解决单机Redis容量有限的问题，将数据按一定的规则分配到多台机器，内存/QPS不受限于单机，可受益于分布式集群高扩展性。
    * Redis Key的过期策略
        1. 惰性删除：当读/写一个已经过期的key时，会触发惰性删除策略，直接删除掉这个过期key，很明显，这是被动的。
        2. 定期删除：由于惰性删除策略无法保证冷数据被及时删掉，所以 Redis 会定期主动淘汰一批已过期的key。默认100ms就随机抽一些设置了过期时间的key，去检查是否过期，过期了就删了
        3. 主动删除：当前已用内存超过maxMemory限定时，触发主动清理策略。主动设置的前提是设置了maxMemory的值。
    * 内存淘汰机制
        1. noeviction：当内存不足以容纳新写入数据时，新写入操作会报错。应该没人用吧。
        2. allkeys-lru：当内存不足以容纳新写入数据时，在键空间中，移除最近最少使用的key。推荐使用，目前项目在用这种。
        3. allkeys-random：当内存不足以容纳新写入数据时，在键空间中，随机移除某个key。应该也没人用吧，你不删最少使用Key,去随机删。
        4. volatile-lru：当内存不足以容纳新写入数据时，在设置了过期时间的键空间中，移除最近最少使用的key。这种情况一般是把redis既当缓存，又做持久化存储的时候才用。不推荐
        5. volatile-random：当内存不足以容纳新写入数据时，在设置了过期时间的键空间中，随机移除某个key。依然不推荐
        6. volatile-ttl：当内存不足以容纳新写入数据时，在设置了过期时间的键空间中，有更早过期时间的key优先移除。不推荐
        ps：如果没有设置 expire 的key, 不满足先决条件(prerequisites); 那么 volatile-lru, volatile-random 和 volatile-ttl 策略的行为, 和 noeviction(不删除) 基本上一致。
    * Pipeline: 可以将多次IO往返的时间缩减为一次，前提是pipeline执行的指令之间没有因果相关性。
    * https://zhuanlan.zhihu.com/p/118561398
    * https://www.cnblogs.com/rjzheng/p/9096228.html
    
* java
    * 大Class类：这个类描述的是所有的类的公共特性（类名:com.test,0或者多个方法、修饰符、字段、静态方法）
    * Field类：字段的公共特性
    * 反射机制：在运行状态中，对于任意一个类，都能够知道这个类的所有属性和方法；对于任意一个对象，都能够调用它的任意一个方法；这种动态获取信息以及动态调用对象方法的功能称为JAVA语言的反射机制。   
      在IDE中一按点 编译工具就会自动的把该对象能够使用的所有的方法和属性全部都列出来，这就是利用了Java反射的原理。
        * 获取反射中的Class对象
            1. 第一种，使用 Class.forName 静态方法。当你知道该类的全路径名时，你可以使用该方法获取 Class 类对象。  
                Class clz = Class.forName("java.lang.String");  
            2. 第二种，使用 .class 方法。  
                Class clz = String.class;
            3. 第三种，使用类对象的 getClass() 方法。
                String str = new String("Hello");  
                Class clz = str.getClass();  


* Spring
    * Spring 为我们做了哪些事情？
      * 通过代码解耦，提高代码灵活性（依赖注入 DI）
        - 灵活：依提供的面向接口的编程方法，为类和类之间建立更灵活的关系
        - 解耦：移除了直接在程序中 new 实例的高耦合做法
        - 方便：要替换类的实现，只需要在 Spring 容器中更换声明，无需改动代码
      * 简化开发，用最少的代码，做最多的事情（AOP，Template）
        - 消除重复的模板代码（性能监控，异常封装，参数跟踪等……）
        - 更代码结构更加整洁，通用的逻辑基本都可以用 AOP 来处理
        - Spring 的声明式事务就是 Spring AOP 的优秀实现案例
      * 提供公共抽象，屏蔽底层，开箱即用，替换方便（Spring Cache，框架，中间件集成）
      * MVC模型的快速实现（视图解析，数据转换）  
    * Spring八大模块
      * Spring Core： 基础,可以说 Spring 其他所有的功能都需要依赖于该类库。主要提供 IoC 依赖注入功能。
      * Spring Aspects ：该模块为与AspectJ的集成提供支持。
      * Spring AOP ：提供了面向切面的编程实现。 
      * Spring JDBC : Java数据库连接。
      * Spring JMS ：Java消息服务。
      * Spring ORM : 用于支持Hibernate等ORM工具。
      * Spring Web : 为创建Web应用程序提供支持。
      * Spring Test : 提供了对 JUnit 和 TestNG 测试的支持。
    * Spring 框架中都用到了哪些设计模式？ 
      1. 工厂模式：BeanFactory就是简单工厂模式的体现，用来创建对象的实例；
      2. 单例模式：Bean默认为单例模式。
      3. 代理模式：Spring的AOP功能用到了JDK的动态代理和CGLIB字节码生成技术；
      4. 模板方法：用来解决代码重复的问题。比如. RestTemplate, JmsTemplate, JpaTemplate。
      5. 观察者模式：定义对象键一种一对多的依赖关系，当一个对象的状态发生改变时，所有依赖于它的对象都会得到通知被制动更新，如Spring中listener的实现–ApplicationListener。
    * Spring 容器启动流程（初始化与刷新）
      容器初始化：
        1. 基于java-config 技术分析的入口AnnotationConfigApplicationContext，或者
        2. 如果我想生成 bean 对象，那么就需要一个 beanFactory 工厂（DefaultListableBeanFactory）；
        3. 如果我想对加了特定注解（如 @Service、@Repository）的类进行读取转化成 BeanDefinition 对象（BeanDefinition 是 Spring 中极其重要的一个概念，它存储了 bean 对象的所有特征信息，如是否单例，是否懒加载，factoryBeanName 等），那么就需要一个注解配置读取器（AnnotatedBeanDefinitionReader）；
        4. 如果我想对用户指定的包目录进行扫描查找 bean 对象，那么还需要一个路径扫描器（ClassPathBeanDefinitionScanner）
      刷新：
        1. 刷新前的预处理  prepareRefresh();
        2. 获取 beanFactory，即前面创建的【DefaultListableBeanFactory】 ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();
        3. 预处理 beanFactory，向容器中添加一些组件 prepareBeanFactory(beanFactory);
        4. 子类通过重写这个方法可以在 BeanFactory 创建并与准备完成以后做进一步的设置 postProcessBeanFactory(beanFactory);
        5. 执行 BeanFactoryPostProcessor 方法，beanFactory 后置处理器 invokeBeanFactoryPostProcessors(beanFactory);
        6. 注册 BeanPostProcessors，bean 后置处理器 registerBeanPostProcessors(beanFactory);
        7. 初始化 MessageSource 组件（做国际化功能；消息绑定，消息解析） initMessageSource();
        8. 初始化事件派发器，在注册监听器时会用到 initApplicationEventMulticaster();
        9. 留给子容器（子类），子类重写这个方法，在容器刷新的时候可以自定义逻辑，web 场景下会使用 onRefresh();
        10. 注册监听器，派发之前步骤产生的一些事件（可能没有）  registerListeners();
        11. 实例化 bean finishBeanFactoryInitialization(beanFactory);
            - spring bean 容器的生命周期流程如下：
            - Spring 容器根据配置中的 bean 定义中实例化 bean。
            - Spring 使用依赖注入填充所有属性，如 bean 中所定义的配置。
            - 如果 bean 实现 BeanNameAware 接口，则工厂通过传递 bean 的 ID 来调用 setBeanName()。 
            - 如果 bean 实现 BeanFactoryAware 接口，工厂通过传递自身的实例来调用 setBeanFactory()。
            - 如果存在与 bean 关联的任何 BeanPostProcessors，则调用 preProcessBeforeInitialization() 方法。
            - 如果为 bean 指定了 init 方法（<bean> 的 init-method 属性），那么将调用它。
            - 最后，如果存在与 bean 关联的任何 BeanPostProcessors，则将调用 postProcessAfterInitialization() 方法。
            - 如果 bean 实现 DisposableBean 接口，当 spring 容器关闭时，会调用 destory()。
            - 如果为 bean 指定了 destroy 方法（<bean> 的 destroy-method 属性），那么将调用它。
        12. 发布容器刷新完成事件  finishRefresh();
   * Spring 循环依赖的解决方法
     * spring单例对象的初始化大略分为三步：
       1. createBeanInstance：实例化，其实也就是调用对象的构造方法实例化对象
       2. populateBean：填充属性，这一步主要是多bean的依赖属性进行填充
       3. initializeBean：调用spring xml中的init 方法。
     * 循环依赖的三种情况及处理
       1. 构造器的循环依赖：这种依赖spring是处理不了的，直 接抛出BeanCurrentlylnCreationException异常。 
       2. 单例模式下的setter循环依赖：通过“三级缓存”处理循环依赖。 
       3. 非单例循环依赖：无法处理。
     * setter循环依赖如何用三级缓存解决循环依赖  
       * 三级缓存的作用 
         1. singletonFactories ： 进入实例化阶段的单例对象工厂的cache （三级缓存）
         2. earlySingletonObjects ：完成实例化但是尚未初始化的，提前暴光的单例对象的Cache （二级缓存）
         3. singletonObjects：完成初始化的单例对象的cache（一级缓存）
      * 如何解决循环依赖性：A的某个field或者setter依赖了B的实例对象，同时B的某个field或者setter依赖了A的实例对象”这种循环依赖的情况。
         1. A首先完成了初始化的第一步，并且将自己提前曝光到singletonFactories中，此时进行初始化的第二步，发现自己依赖对象B，此时就尝试去get(B)，发现B还没有被create，
         2. 所以走create流程，B在初始化第一步的时候发现自己依赖了对象A，于是尝试get(A)，
         3. 尝试一级缓存singletonObjects(肯定没有，因为A还没初始化完全)，尝试二级缓存earlySingletonObjects（也没有），尝试三级缓存singletonFactories，
         4. 由于A通过ObjectFactory将自己提前曝光了，所以B能够通过ObjectFactory.getObject拿到A对象(虽然A还没有初始化完全，但是总比没有好呀)，
         5. B拿到A对象后顺利完成了初始化阶段1、2、3，完全初始化之后将自己放入到一级缓存singletonObjects中。
         6. 此时返回A中，A此时能拿到B的对象顺利完成自己的初始化阶段2、3，最终A也完成了初始化，进去了一级缓存singletonObjects中，而且更加幸运的是，由于B拿到了A的对象引用，所以B现在hold住的A对象完成了初始化
   * Spring IOC: 控制反转就是把创建和管理(生命周期) bean 的过程转移给了第三方,一般有BeanFactory和ApplicationContext
      * BeanFactory：简单粗暴，可以理解为 HashMap。但它一般只有 get, put 两个功能，所以称之为“低级容器”。Key - bean name， Value - bean object
      * ApplicationContext 它是 BeanFactory 的子类多了很多功能，因为它继承了多个接口，可称之为“高级容器”
      * 初始过程
        XML/Annotation - 读取 -> Resource - 解析 -> beanDefinition - 注册 -> beanFactory
    * AOP （https://mp.weixin.qq.com/s/NXZp8a3n-ssnC6Y1Hy9lzw）
      * 作用：在不修改源代码的前提下，为系统中不同的业务组件添加某些通用功能，安全，事务，缓存，性能等业务无关的相同行为
      * 相关定义
        - JoinPoint: 程序在执行流程中经过的一个个时间点，这个时间点可以是方法调用时，或者是执行方法中异常抛出时，也可以是属性被修改时等时机，在这些时间点上你的切面代码是可以（注意是可以但未必）被注入的
        - Pointcut: JoinPoints 只是切面代码可以被织入的地方，但我并不想对所有的 JoinPoint 进行织入，这就需要某些条件来筛选出那些需要被织入的 JoinPoint，Pointcut 就是通过一组规则(使用 AspectJ pointcut expression language 来描述) 来定位到匹配的 joinpoint
        - 代码织入（也叫增强），Pointcut 通过其规则指定了哪些 joinpoint 可以被织入，而 Advice 则指定了这些 joinpoint 被织入（或者增强）的具体时机与逻辑，是切面代码真正被执行的地方，主要有五个织入时机  
          1. Before Advice: 在 JoinPoints 执行前织入
          2. After Advice: 在 JoinPoints 执行后织入（不管是否抛出异常都会织入）
          3. After returning advice: 在 JoinPoints 执行正常退出后织入（抛出异常则不会被织入）
          4. After throwing advice: 方法执行过程中抛出异常后织入
          4. Around Advice: 这是所有 Advice 中最强大的，它在 JoinPoints 前后都可织入切面代码，也可以选择是否执行原有正常的逻辑，如果不执行原有流程，它甚至可以用自己的返回值代替原有的返回值，甚至抛出异常。
      * AOP的使用
        1. 定义注解
            * @Retention作用是定义被它所注解的注解保留多久
                - RetentionPolicy.SOURCE：Discard during the compile. These annotations don't make any sense after the compile has completed, so they aren't written to the bytecode.
                Example: @Override, @SuppressWarnings
                - RetentionPolicy.CLASS: Discard during class load. Useful when doing bytecode-level post-processing. Somewhat surprisingly, this is the default.
                - RetentionPolicy.RUNTIME: Do not discard. The annotation should be available for reflection at runtime. Example: @Deprecated
            * @Target 说明了Annotation所修饰的对象范围：Annotation可被用于 packages、types（类、接口、枚举、Annotation类型）、类型成员（方法、构造方法、成员变量、枚举值）
        2. 在方法签名上加上上述我们定义好的注解 
        3. 然后再指定注解形式的 pointcuts 及 around advice
      * Spring为什么使用代理，
        - 代理模式是一种结构性设计模式。为对象提供一个替身，以控制对这个对象的访问。即通过代理对象访问目标对象，并允许在将请求提交给对象前后进行一些处理。（https://zhuanlan.zhihu.com/p/187381270）
          1. 静态代理：由程序员创建代理类或特定工具自动生成源代码再对其编译。在程序运行前代理类的 .class 文件就已经存在了。每个对象都要有一个单独的代理类 去实现借口
             缺点：
             - 代理类和委托类实现了相同的接口，代理类通过委托类实现了相同的方法。这样就出现了大量的代码重复。如果接口增加一个方法，除了所有实现类需要实现这个方法外，所有代理类也需要实现此方法。增加了代码维护的复杂度。
             - 代理对象只服务于一种类型的对象，如果要服务多类型的对象。势必要为每一种对象都进行代理，静态代理在程序规模稍大时就无法胜任了。
          2. 动态代理:在程序运行时运用反射机制动态创建而成，动态就是在程序运行时生成的，而不是编译时。静态代理会产生很多静态类，所以我们要想办法可以通过一个代理类完成全部的代理功能，这就引出了动态代理。
             - JDK 代理、接口代理。
             - cglib 代理（可以在内存动态的创建对象，而不是实现接口，属于动态代理的范畴）
        - 可以简化暴露的借口，从而更容易被调用程序使用，通过动态代理，可以对目标类加入通知或者拦截器，从而可以提供切面功能，或者提供灵活的可配置的参数，参考spring的声明式事物管理部分。
      * Spring 的 AOP 有哪几种创建代理的方式
        - Spring 中的 AOP 目前支持 JDK 动态代理和 Cglib 代理。
        - 通常来说：如果被代理对象实现了接口，则使用 JDK 动态代理，否则使用 Cglib 代理。另外，也可以通过指定 proxyTargetClass=true 来实现强制走 Cglib 代理。
      * JDK 动态代理和 Cglib 代理的区别
        1、JDK 动态代理本质上是实现了被代理对象的接口，而 Cglib 本质上是继承了被代理对象，覆盖其中的方法。
        2、JDK 动态代理只能对实现了接口的类生成代理，Cglib 则没有这个限制。但是 Cglib 因为使用继承实现，所以 Cglib 无法代理被 final 修饰的方法或类。
        3、在调用代理方法上，JDK 是通过反射机制调用，Cglib是通过FastClass 机制直接调用。FastClass 简单的理解，就是使用 index 作为入参，可以直接定位到要调用的方法直接进行调用。
        4、在性能上，JDK1.7 之前，由于使用了 FastClass 机制，Cglib 在执行效率上比 JDK 快，但是随着 JDK 动态代理的不断优化，从 JDK 1.7 开始，JDK 动态代理已经明显比 Cglib 更快了。

RetentionPolicy.CLASS: Discard during class load. Useful when doing bytecode-level post-processing. Somewhat surprisingly, this is the default.

RetentionPolicy.RUNTIME: Do not discard. The annotation should be available for reflection at runtime. Example: @Deprecated

* Distributed system  
    * 缓存更新策略   
      * 先更新数据库，再更新缓存；大多数场景不符合  
        - 缺点  
          1. 资源浪费: 在一些大型的信息网站中（博客、贴吧），我们引入缓存主要是对热数据（请求频繁的）进行缓存，而这时候，如果很多用户对于冷数据（长时间没人访问，或者访问量很少）进行更新，然后再去更新缓存，这就造成了缓存资源的大量浪费（因为访问量少，导致这些缓存命中低，浪费缓存资源）。  
          2. 脏数据: 这是由于出现了并发操作的原因导致的，如：同时有两个请求A和B对数据进行了更新操作，由于网络原因，可能存在以下情况： 
            请求A更新了数据库；  
            请求B更新了数据库；  
            请求B更新了缓存；  
            请求A更新了缓存。  
          3. 请求时间： 如果缓存不是一种简单的数据缓存，而是需要经过较为复杂的运算，才能得出缓存值，这时候，请求将会在计算缓存值上，耗费一部分时间，而这就导致了请求的响应时间变长，增加系统的负担，降低了系统的处理能力。
        - 适合场景:个人博客,手册网站（w3cschool、菜鸟教程等）
          1. 读请求占据网站的总流量的99%；
          2. 网站数据量不大（几十万的文章数据）；
          3. 很少会去更新数据（一般文章写好后，不会去更新）。
      * 先更新数据库，再删除缓存；
        - 缺点  
          1. 脏数据：：数据发生了变更，先删除了缓存，然后要去修改数据库，此时还没修改。一个请求过来，去读缓存，发现缓存空了，去查询数据库，查到了修改前的旧数据，放到了缓存中。随后数据变更的程序完成了数据库的修改。数据库和缓存中的数据不一样了  
          2. 缓存删除失败   
        - 解决方案：
          1. 设置缓存的有效时间(大量失效导致缓存雪崩)  
          2. 消息队列：更新数据库； 删除缓存失败； 将需要删除的Key发送到消息队列； 隔断时间从消息队列中拉取要删除的key； 继续删除，直至成功为止。 
      * 先删除缓存，再更新数据库；  
        - 缺点  
          1. 脏数据  
        - 解决方案：  
          1. 设置缓存的有效时间(大量失效导致缓存雪崩)  
          2. 消息队列  




            