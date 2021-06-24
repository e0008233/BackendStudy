* 数据库调优 database tuning  （mutability, transaction, indexes, access control)
    * Sql performance tuning
        * 排查缓存干扰 （SELECT SQL_NO_CACHE * from...)
        * 从slow_log排查慢查询  
        * 用explain查看sql：https://zhuanlan.zhihu.com/p/51771446
          * id:表示查询中执行SELECT子句或操作表的顺序
          * type：关联类型或者访问类型，它指明了MySQL决定如何查找表中符合条件的行
            - ALL: 全表扫描，这个类型是性能最差的查询之一。通常来说，我们的查询不应该出现 ALL 类型
            - index：全索引扫描，和 ALL 类型类似，只不过 ALL 类型是全表扫描，而 index 类型是扫描全部的索引，主要优点是避免了排序，但是开销仍然非常大。如果在 Extra 列看到 Using index，说明正在使用覆盖索引，只扫描索引的数据，它比按索引次序全表扫描的开销要少很多。
            - range：范围扫描，就是一个有限制的索引扫描，它开始于索引里的某一点，返回匹配这个值域的行。这个类型通常出现在 =、<>、>、>=、<、<=、IS NULL、<=>、BETWEEN、IN() 的操作中，key 列显示使用了哪个索引，当 type 为该值时，则输出的 ref 列为 NULL，并且 key_len 列是此次查询中使用到的索引最长的那个。
            - key：这一列显示MySQL实际决定使用的索引。如果没有选择索引，键是NULL。
            - rows：这一列显示了估计要找到所需的行而要读取的行数，这个值是个估计值，原则上值越小越好。
            - extra：
                * Using index：使用覆盖索引，表示查询索引就可查到所需数据，不用扫描表数据文件，往往说明性能不错。
                * Using Where：在存储引擎检索行后再进行过滤，使用了where从句来限制哪些行将与下一张表匹配或者是返回给用户。
                * Using temporary：在查询结果排序时会使用一个临时表，一般出现于排序、分组和多表 join 的情况，查询效率不高，建议优化。
                * Using filesort：对结果使用一个外部索引排序，而不是按索引次序从表里读取行，一般有出现该值，都建议优化去掉，因为这样的查询 CPU 资源消耗大。
        * add and keep necessary indexes(composite index includes left most single index) 
        * avoid index suppression (在索引上执行函数会导致索引失效，not erqual on indexes, like statement like %123 ), no operation on the index, no tye conversion on the index
        * only querying necessary data(limit, avoid *)
        * run the complicated query in off-peak hours (through scheduler)
        * covering index (index includes queried columns,like composite index containing many columns)
          - 当sql语句的所求查询字段（select列）和查询条件字段（where子句）全都包含在一个索引中（联合索引），可以直接使用索引查询而不需要回表。这就是覆盖索引。
        * 前缀索引: 使用前缀索引，定义好长度，就可以做到既节省空间，又不用额外增加太多的查询成本。 
          - 因为存在一个磁盘占用的问题，索引选取的越长，占用的磁盘空间就越大，相同的数据页能放下的索引值就越少，搜索的效率也就会越低。
          - 采用倒序，hash或者删减字符串操作去区分
        * 普通索引与唯一索引的选择问题
          * change buffer：当需要更新一个数据页时，如果数据页在内存中就直接更新，而如果这个数据页还没有在内存中的话，在不影响数据一致性的前提下，InooDB 会将这些更新操作缓存在 change buffer 中，这样就不需要从磁盘中读入这个数据页了。在下次查询需要访问这个数据页的时候，将数据页读入内存，然后执行 change buffer 中与这个页有关的操作。通过这种方式就能保证这个数据逻辑的正确性。
          * 将 change buffer 中的操作应用到原数据页，得到最新结果的过程称为 merge。除了访问这个数据页会触发 merge 外，系统有后台线程会定期 merge。在数据库正常关闭（shutdown）的过程中，也会执行 merge 操作。
          * 显然，如果能够将更新操作先记录在 change buffer，减少读磁盘，语句的执行速度会得到明显的提升。而且，数据读入内存是需要占用 buffer pool 的，所以这种方式还能够避免占用内存，提高内存利用率。
          * 普通索引和唯一索引应该怎么选择。其实，这两类索引在查询能力上是没差别的，主要考虑的是对更新性能的影响。所以，我建议你尽量选择普通索引。
        * link: https://juejin.cn/post/6844904098999828488
    * Command query responsibility segregation (CQRS,读写分离)
        * It is useful when having a lot of read command, a large amount of concurrent requests and less strict requirement on data consistency
        * Implementation: critical requests are handled by master, less critical requests are using master-slave replication (achieved by binlog)
    * 分库分表（sharding）水平分表方法
      * Range 可以指定一个数据（id）范围来进行分表
        * RANGE方法优点： 扩容简单，提前建好库、表就好
        * RANGE方法缺点： 大部分读和写都访会问新的数据（热点数据过于集中），有IO瓶颈，这样子造成新库压力过大，不建议采用。
      * HASH取模：咱们可以采用根据用户ID HASG取模的方式进行分库分表。
        * HASH取模方法优点： 能保证数据较均匀的分散落在不同的库、表中，减轻了数据库压力
        * HASH取模方法缺点： 扩容麻烦、迁移数据时每次都需要重新计算hash值分配到不同的库和表
      * 一致性HASH算法：https://zhuanlan.zhihu.com/p/78285304
        * 按照常用的hash算法来将对应的key哈希到一个具有2^32次方个节点的空间中，即0~ (2^32)-1的数字空间中。现在我们可以将这些数字头尾相连，想象成一个闭合的环形
        * 一致性HASH方法优点： 通过虚拟节点方式能保证数据较均匀的分散落在不同的库、表中，并且新增、删除节点不影响其他节点的数据，高可用、容灾性强。
      * 那分表后的ID怎么保证唯一性：
        * 设定步长，比如1-1024张表我们设定1024的基础步长，这样主键落到不同的表就不会冲突了。
        * 分布式ID，自己实现一套分布式ID生成算法或者使用开源的比如雪花算法这种
        * 分表后不使用主键作为查询依据，而是每张表单独新增一个字段作为唯一主键使用，比如订单表订单号是唯一的，不管最终落在哪张表都基于订单号作为查询依据，更新也一样。
      * 分表后非sharding_key的查询怎么处理呢？
        * 可以做一个mapping表，比如这时候商家要查询订单列表怎么办呢？不带user_id查询的话你总不能扫全表吧？所以我们可以做一个映射关系表，保存商家和用户的关系，查询的时候先通过商家查询到用户列表，再通过user_id去查询。
        * 打宽表，一般而言，商户端对数据实时性要求并不是很高，比如查询订单列表，可以把订单表同步到离线（实时）数仓，再基于数仓去做成一张宽表，再基于其他如es提供查询服务。
        * 数据量不是很大的话，比如后台的一些查询之类的，也可以通过多线程扫表，然后再聚合结果的方式来做。或者异步的形式也是可以的。
    * 数据库设计三范式
        1. 第一范式1NF：确保每个字段保持原子性，不可分割
        2. 第二范式2NF：确保字段完全依赖于主键。也就是说在一个数据库表中，一个表中只能保存一种数据，不可以把多种数据保存在同一张数据库表中。
        3. 第三范式3NF：必须满足2NF，实体中每个属性与主键直接相关而不能间接相关。
    * mysql主从同步  
      1. master提交完事务后，写入binlog
      2. slave连接到master，获取binlog
      3. master创建dump线程，推送binglog到slave
      4. slave启动一个IO线程读取同步过来的master的binlog，记录到relay log中继日志中
      5. slave再开启一个sql线程读取relay log事件并在slave执行，完成同步
      6. slave记录自己的binglog
      * 由于mysql默认的复制方式是异步的，主库把日志发送给从库后不关心从库是否已经处理，这样会产生一个问题就是假设主库挂了，从库处理失败了，这时候从库升为主库后，日志就丢失了。由此产生两个概念。
        * 全同步复制: 主库写入binlog后强制同步日志到从库，所有的从库都执行完成后才返回给客户端，但是很显然这个方式的话性能会受到严重影响。
        * 半同步复制: 和全同步不同的是，半同步复制的逻辑是这样，从库写入日志成功后返回ACK确认给主库，主库收到至少一个从库的确认就认为写操作完成。
    * 数据库锁
        * 按锁的粒度分
          1. 表锁:开销小，加锁快；不会出现死锁；锁定力度大，发生锁冲突概率高： 表读锁(Table Read Lock)&& 表写锁(Table Write Lock)
             - 在表读锁和表写锁的环境下：读读不阻塞，读写阻塞，写写阻塞！ 读读不阻塞：当前用户在读数据，其他的用户也在读数据，不会加锁 读写阻塞：当前用户在读数据，其他的用户不能修改当前用户读的数据，会加锁！ 写写阻塞：当前用户在修改数据，其他的用户不能修改当前用户正在修改的数据，会加锁！
          2. 行锁:开销大，加锁慢；会出现死锁；锁定粒度小，发生锁冲突的概率低，并发度高：共享锁和排他锁
             - 共享锁(S锁、读锁)：允许一个事务去读一行，阻止其他事务获得相同数据集的排他锁。即多个客户可以同时读取同一个资源，但不允许其他客户修改。
             - 排他锁(X锁、写锁)：允许获得排他锁的事务更新数据，阻止其他事务取得相同数据集的读锁和写锁。写锁是排他的，写锁会阻塞其他的写锁和读锁 
        * 按锁的使用方式分
          1. 悲观锁 我们使用悲观锁的话其实很简单(手动加行锁就行了)
             - select * from xxxx for update
             - 在select 语句后边加了for update相当于加了排它锁(写锁)，加了写锁以后，其他事务就不能对它修改了 
          2. 乐观锁：乐观锁不是数据库层面上的锁，需要用户手动去加的锁。一般我们在数据库表中添加一个版本字段version来实现，例如操作1和操作2在更新User表的时，执行语句如下：
             - update A set Name=lisi,version=version+1 where ID=#{id} and version=#{version}， 
             - 此时即可避免更新丢失。
        * 间隙锁GAP 当我们用范围条件检索数据而不是相等条件检索数据，并请求共享或排他锁时，InnoDB会给符合范围条件的已有数据记录的索引项加锁
          - 对于键值在条件范围内但并不存在 的记录，叫做“间隙(GAP)”。InnoDB也会对这个“间隙”加锁，这种锁机制就是所谓的间隙锁。例子
          - 假如emp表中只有101条记录，其empid的值分别是1,2,...,100,101
          - Select * from emp where empid > 100 for update;
          - 上面是一个范围查询，InnoDB不仅会对符合条件的empid值为101的记录加锁，也会对empid大于101(这些记录并不存在)的“间隙”加锁
          * InnoDB使用间隙锁的目的有2个：
              1. 为了防止幻读(上面也说了，Repeatable read隔离级别下再通过GAP锁即可避免了幻读)
              2. 满足恢复和复制的需要：MySQL的恢复机制要求在一个事务未提交前，其他并发事务不能插入满足其锁定条件的任何记录，也就是不允许出现幻读
          * 唯一索引是不会有间隙索引的。
    * 事物个隔离级别：事务的隔离级别就是通过锁的机制来实现，锁的应用最终导致不同事务的隔离级别 只不过隐藏了加锁细节，事务的隔离级别有4种
        - Read uncommitted：会出现脏读，不可重复读，幻读
        - Read committed：会出现不可重复读，幻读
        - Repeatable read：会出现幻读(Mysql默认的隔离级别，但是Repeatable read配合gap锁不会出现幻读！)
        - Serializable：串行，避免以上的情况
    * MyISAM和InnoDB区别：
        * InnoDB 支持事务，MyISAM 不支持事务。
          * 事务具有ACID（atomicity,consistency,isolation,durability ）四个特性。也即：
            * 原子性：一个事务中的操作要么全部成功，要么全部失败
            * 一致性：数据库总是从一个一致性的状态转换到另外一个一致性的状态。比如A转账给B100块钱，假设中间sql执行过程中系统崩溃A也不会损失100块，因为事务没有提交，修改也就不会保存到数据库。
            * 隔离性：一个事务的修改在最终提交前，对其他事务是不可见的。
            * 持久性：一旦事务提交，所做的修改就会永久保存到数据库中。
          * ACD三个特性是通过Redo log（重做日志）和Undo log 实现的。 而隔离性是通过锁来实现的
            * Redo log 内存中的重做日志缓冲 重做日志文件 
            * Undo log 实现事务回滚和MVCC（Multi versioning concurrency control）
            * A原子性由undo log日志保证，它记录了需要回滚的日志信息，事务回滚时撤销已经执行成功的sql
            * C一致性一般由代码层面来保证
            * I隔离性由MVCC和锁来保证
            * D持久性由内存+redo log来保证，mysql修改数据同时在内存和redo log记录这次操作，事务提交的时候通过redo log刷盘，宕机的时候可以从redo log恢复
        * InnoDB 支持外键，而 MyISAM 不支持。对一个包含外键的 InnoDB 表转为 MYISAM 会失败
        * InnoDB 是聚集索引，MyISAM 是非聚集索引。聚簇索引的文件存放在主键索引的叶子节点上，因此 InnoDB 必须要有主键，通过主键索引效率很高。但是辅助索引需要两次查询，先查询到主键，然后再通过主键查询到数据。而 MyISAM 是非聚集索引，数据文件是分离的，索引保存的是数据文件的指针。主键索引和辅助索引是独立的。
        * InnoDB 不保存表的具体行数，执行 select count(*) from table 时需要全表扫描。而MyISAM 用一个变量保存了整个表的行数，执行上述语句时只需要读出该变量即可，速度很快；
        * InnoDB 最小的锁粒度是行锁，MyISAM 最小的锁粒度是表锁。一个更新语句会锁住整张表，导致其他查询和更新都会被阻塞，因此并发访问受限。这也是 MySQL 将默认存储引擎从 MyISAM 变成 InnoDB 的重要原因之一；
        * 如何选择：
          1. 是否要支持事务，如果要请选择 InnoDB，如果不需要可以考虑 MyISAM；
          2. 如果表中绝大多数都只是读查询，可以考虑 MyISAM，如果既有读写也挺频繁，请使用InnoDB。
          3. 系统奔溃后，MyISAM恢复起来更困难，能否接受，不能接受就选 InnoDB；
    * 幻读：mvcc和间隙锁解决：https://zhuanlan.zhihu.com/p/368774742
       * 间隙锁是可重复读级别下才会有的锁，结合MVCC和间隙锁可以解决幻读的问题 
    * Statement和PreparedStatement的区别
       1. prepareStatement可以替换变量,在SQL语句中可以包含"?" 可以用set方法替换
       2. PreparedStatement有预编译的过程，已经绑定sql，之后无论执行多少遍，都不会再去进行编译，而 statement 不同，如果执行多变，则相应的就要编译多少遍sql，所以从这点看，preStatement 的效率会比 Statement要高一些.
       3. preStatement是预编译的，所以可以有效的防止 SQL注入等问题

* 限流限速
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
    * Redis 全局锁遇到的错误和解决方法：
      * 锁未被释放： 添加在finally block里，设置过期时间
      * B的锁被A给释放了：一般我们在每个线程加锁时要带上自己独有的value值来标识，只释放指定value的key，否则就会出现释放锁混乱的场景。
      * 锁过期了，业务还没执行完： redisson在加锁成功后，会注册一个定时任务监听这个锁，每隔10秒就去查看这个锁，如果还持有锁，就对过期时间进行续期。默认过期时间30秒。这个机制也被叫做：“看门狗”
      * redis主从复制的坑（给锁的master宕机）：只能尽量保证机器的稳定性，减少发生此事件的概率。
    * https://zhuanlan.zhihu.com/p/118561398
    * https://www.cnblogs.com/rjzheng/p/9096228.html
    
* java
    * 4 principles for oop
      1. Abstraction
      2. Polymorphism (overriding and overloading) 
      3. Inheritance (key word: extends)  
      4. Encapsulation  
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
                Class clz = str.getClass()
    * 两种数据类型，基本类型和引用类型。 
      1. 基本类型包括: byte、short、int、long、float、double、char、boolean
      2. 引用类型包括三种，类类型，数组类型，和接口类型。   
    * java 1.8更新
      * 使用元空间替换永久代：元空间并不在虚拟机中，而是使用本地内存。并且可以动态扩容，避免OOM异常。合并HotSpot和JRockit的代码，JRockit从来没有所谓的永久代，也不需要开发运维人员设置永久代的大小，但是运行良好
    * Java对象保存在内存中时，由以下三部分组成：
      1，对象头 (Mark Word：记录了对象和锁有关的信息, 指向类的指针, 数组长度（只有数组对象才有）)
      2，实例数据：对象的实例数据就是在java代码中能看到的属性和他们的值。
      3，对齐填充字节：因为JVM要求java的对象占的内存大小应该是8bit的倍数，所以后面有几个字节用于把对象的大小补齐至8bit的倍数，没有特别的功能。
    
* JVM 体系结构: https://zhuanlan.zhihu.com/p/267090711
    * Java虚拟机主要分为五大模块：https://www.zhihu.com/search?type=content&q=jvm%20%E9%9D%A2%E8%AF%95
      1. 类装载器子系统：加载类文件到内存。Class loader只管加载，只要符合文件结构就加载，至于能否运行，它不负责，那是有Exectution Engine 负责的。
         * 双亲委派机制 工作流程
           1. 如果一个类加载器收到了类加载请求，它并不会自己先加载，而是把这个请求委托给父类的加载器去执行 
           2. 如果父类加载器还存在其父类加载器，则进一步向上委托，依次递归，请求最终将到达顶层的引导类加载器；
           3. 如果父类加载器可以完成类加载任务，就成功返回，倘若父类加载器无法完成加载任务，子加载器才会尝试自己去加载，这就是双亲委派机制
           4. 父类加载器一层一层往下分配任务，如果子类加载器能加载，则加载此类，如果将加载任务分配至系统类加载器也无法加载此类，则抛出异常
         * 为什么要设计这种机制
           * 如果有人想替换系统级别的类：String.java，篡改它的实现，在这种机制下这些系统的类已经被Bootstrap classLoader加载过了 所以其他类加载器并没有机会再去加载，从一定程度上防止了危险代码的植入。
           * 避免类的重复加载
           * 保护程序安全，防止核心API被随意篡改
      2. 运行时数据区
         - 线程私有的（程序计数器 虚拟机栈 本地方法栈）
           * 程序计数器：可以看作是当前线程所执行的字节码的行号指示器，它的生命周期随着线程的创建而创建，随着线程的结束而死亡。
           * Java 虚拟机栈：它的生命周期和线程相同，描述的是 Java 方法执行的内存模型。  
             Java 虚拟机栈会出现两种异常：StackOverFlowError 和 OutOfMemoryError。
             - StackOverFlowError： 若Java虚拟机栈的内存大小不允许动态扩展，那么当线程请求栈的深度超过当前Java虚拟机栈的最大深度的时候，就抛出StackOverFlowError异常。
             - OutOfMemoryError： 若 Java 虚拟机栈的内存大小允许动态扩展，且当线程请求栈时内存用完了，无法再动态扩展了，此时抛出OutOfMemoryError异常。
           * 本地方法栈(for native methods):用于存放该本地方法的局部变量表、操作数栈、动态链接、出口信息。
         - 线程共有的（堆 方法区 直接内存） 
          * 堆：Java 虚拟机所管理的内存中最大的一块，Java 堆是所有线程共享的一块内存区域，在虚拟机启动时创建。此内存区域的唯一目的就是存放对象实例，几乎所有的对象实例以及数组都在这里分配内存。
            - 年轻代：用于存放新产生的对象。年轻代中包含两个区：Eden 和survivor，并且用于存储新产生的对象，其中有两个survivor区
            - 老年代：用于存放被长期引用的对象。年轻代在垃圾回收多次都没有被GC回收的时候就会被放到老年代，以及一些大的对象（比如缓存，这里的缓存是弱引用），这些大对象可以不进入年轻代就直接进入老年代
            - 持久带：用于存放Class，method元信息（1.8之后改为元空间）
            * 为什么分代？
              - 因为不同对象的生命周期是不一样的。80%-98%的对象都是“朝生夕死”，生命周期很短，大部分新对象都在年轻代，可以很高效地进行回收，不用遍历所有对象。而老年代对象生命周期一般很长，每次可能只回收一小部分内存，回收效率很低。
              - 年轻代和老年代的内存回收算法完全不同，因为年轻代存活的对象很少，标记清楚再压缩的效率很低，所以采用复制算法将存活对象移到survivor区，更高效。而老年代则相反，存活对象的变动很少，所以采用标记清楚压缩算法更合适。
          * 方法区：方法区与 Java 堆一样，是各个线程共享的内存区域，它用于存储已被虚拟机加载的类信息、常量、静态变量、即时编译器编译后的代码等数据
          * 运行时常量池：运行时常量池是方法区的一部分。JDK1.7及之后版本的 JVM 已经将运行时常量池从方法区中移了出来，在 Java 堆（Heap）中开辟了一块区域存放运行时常量池。
            - 字面量：final常量，文本字符串，基本数据类型的值
            - 符号引用：方法名和描述符，字段名和描述符，类和结构的完全限定名
      3. 执行引擎：执行引擎也叫解释器，负责解释命令，交由操作系统执行。
      4. 本地方法接口：本地接口的作用是融合不同的语言为java所用。
      5. 垃圾收集模块
    * 对象的创建：类加载检查->分配内存->初始化零值->设置对象头->执行init方法
      1. 类加载检查： 虚拟机遇到一条 new 指令时，首先将去检查这个指令的参数是否能在常量池中定位到这个类的符号引用，并且检查这个符号引用代表的类是否已被加载过、解析和初始化过。如果没有，那必须先执行相应的类加载过程。
      2. 分配内存：在类加载检查通过后，接下来虚拟机将为新生对象分配内存。对象所需的内存大小在类加载完成后便可确定，为对象分配空间的任务等同于把一块确定大小的内存从 Java 堆中划分出来。由垃圾收集器是否带有压缩整理功能决定
         * 指针碰撞：用过的内存全部放到一边，没用的内存放在另一边，中间有分界值指针，分配地址时只需要将指针向没用的内存移动对象内存大小位置即可(GC收集器：ParNew,Serial)
         * 空闲列表：虚拟机维护一个列表，该列表记录那些内存块是可用的，在分配内存时找一块足够大的内存分配给对象实例，然后更新列表记录；（GC收集器：cms）
         * 内存分配并发问题（补充内容，需要掌握）在创建对象的时候有一个很重要的问题，就是线程安全，因为在实际开发过程中，创建对象是很频繁的事情，作为虚拟机来说，必须要保证线程是安全的，通常来讲，虚拟机采用两种方式来保证线程安全：
           - CAS+失败重试： CAS 是乐观锁的一种实现方式。所谓乐观锁就是，每次不加锁而是假设没有冲突而去完成某项操作，如果因为冲突失败就重试，直到成功为止。虚拟机采用 CAS 配上失败重试的方式保证更新操作的原子性。 
           - TLAB： 为每一个线程预先在 Eden 区分配一块内存。JVM 在给线程中的对象分配内存时，首先在 TLAB 分配，当对象大于TLAB 中的剩余内存或 TLAB 的内存已用尽时，再采用上述的 CAS 进行内存分配。
      3. 初始化零值： 内存分配完成后，虚拟机需要将分配到的内存空间都初始化为零值（不包括对象头），这一步操作保证了对象的实例字段在 Java 代码中可以不赋初始值就直接使用，程序能访问到这些字段的数据类型所对应的零值
      4. 设置对象头： 初始化零值完成之后，虚拟机要对对象进行必要的设置，例如这个对象是那个类的实例、如何才能找到类的元数据信息、对象的哈希吗、对象的 GC 分代年龄等信息。 这些信息存放在对象头中。 另外，根据虚拟机当前运行状态的不同，如是否启用偏向锁等，对象头会有不同的设置方式
      5. 执行 init 方法： 在上面工作都完成之后，从虚拟机的视角来看，一个新的对象已经产生了，但从 Java 程序的视角来看，对象创建才刚开始， <init> 方法还没有执行，所有的字段都还为零。所以一般来说，执行 new 指令之后会接着执行 <init> 方法，把对象按照程序员的意愿进行初始化，这样一个真正可用的对象才算完全产生出来。
    * 内存分配策略
      1. 优先在Eden区分配  
         * 在大多数情况下, 对象在新生代Eden区中分配, 当Eden区没有足够空间分配时, VM发起一次Minor GC, 将Eden区和其中一块Survivor区内尚存活的对象放入另一块Survivor区域, 
         * 如果在Minor GC期间发现新生代存活对象无法放入空闲的Survivor区, 则会通过空间分配担保机制使对象提前进入老年代(空间分配担保见下).
      2. 大对象直接进入老年代  
         * Serial和ParNew两款收集器提供了-XX:PretenureSizeThreshold的参数, 令大于该值的大对象直接在老年代分配, 
      3. 长期存活对象进入老年区
         * 如果对象在Eden出生并经过第一次Minor GC后仍然存活，并且能被Survivor容纳的话，将被移动到Survivor空间中，并将对象年龄设为1，
         * 对象在Survivor区中每熬过一次 Minor GC，年龄就增加1，当它的年龄增加到一定程度(默认为15)_时，就会被晋升到老年代中。
      4. 对象年龄动态判定
         * 如果在 Survivor空间中相同年龄所有对象大小的综合大于Survivor空间的一半，年龄大于或等于该年龄的对象就可以直接进入老年代
      5. 空间分配担保
         * 在发生Minor GC之前，虚拟机会先检查老年代最大可用的连续空间是否大于新生代所有对象总空间，如果这个条件成立，那么Minor GC可以确保是安全的
         * 如果不成立，则虚拟机会查看HandlePromotionFailure设置值是否允许担保失败。
         * 如果允许，那么会继续检查老年代最大可用的连续空间是否大于历次晋升到老年代对象的平均大小，如果大于，将尝试着进行一次Minor GC，尽管这次Minor GC是有风险的，如果担保失败则会进行一次Full GC；
           如果小于，或者HandlePromotionFailure设置不允许冒险，那这时也要改为进行一次Full GC。
    * GC执行的机制
      1. Minor GC(young GC)
         * 一般情况下，当新对象生成，并且在Eden申请空间失败时，就会触发Minor GC，对Eden区域进行GC，清除非存活对象，并且把尚且存活的对象移动到Survivor区。
         * 然后整理Survivor的两个区。这种方式的GC是对年轻代的Eden区进行，不会影响到年老代。因为大部分对象都是从Eden区开始的，同时Eden区不会分配的很大，所以Eden区的GC会频繁进行。因而，一般在这里需要使用速度快、效率高的算法，使Eden去能尽快空闲出来。
      2. Full GC
         * 对整个堆进行整理，包括Young、Tenured和Perm。Full GC因为需要对整个堆进行回收，所以比Minor GC要慢，因此应该尽可能减少Full GC的次数。在对JVM调优的过程中，很大一部分工作就是对于FullGC的调节。有如下原因可能导致Full GC：
            - 年老代（Tenured）被写满 
            - 持久代（Perm）被写满 
            - System.gc()被显示调用
            - 上一次GC之后Heap的各域分配策略动态变化
    * 对象生死判定方法
      1. 引用计数：每个对象有一个引用计数属性，新增一个引用时计数加1，引用释放时计数减1，计数为0时可以回收。此方法简单，无法解决对象相互循环引用的问题。(A引用，B引用，互相等待释放)
      2. 可达性分析算法: 通过一系列的称为 GC Roots 的对象作为起点, 然后向下搜索; 搜索所走过的路径称为引用链/Reference Chain, 当一个对象到 GC Roots 没有任何引用链相连时, 即该对象不可达, 也就说明此对象是不可用的
        * 在Java, 可作为GC Roots的对象包括:
            1. 方法区: 类静态属性引用的对象;
            2. 方法区: 常量引用的对象;
            3. 虚拟机栈(本地变量表)中引用的对象.
            4. 本地方法栈JNI(Native方法)中引用的对象。
        * 即使在可达性分析算法中不可达的对象, VM也并不是马上对其回收, 因为要真正宣告一个对象死亡, 至少要经历两次标记过程: 第一次是在可达性分析后发现没有与GC Roots相连接的引用链, 第二次是GC对在F-Queue执行队列中的对象进行的小规模标记(对象需要覆盖finalize()方法且没被调用过).
            * @Override protected void finalize() throws Throwable{}
    * GC原理-垃圾回收算法
      1. 标记-清除算法:这种垃圾回收一次回收分为两个阶段：标记、清除。首先标记所有需要回收的对象，在标记完成后回收所有被标记的对象。这种回收算法会产生大量不连续的内存碎片，当要频繁分配一个大对象时，jvm在新生代中找不到足够大的连续的内存块，会导致jvm频繁进行内存回收(目前有机制，对大对象，直接分配到老年代中)
         * 优点: 利用率百分之百
         * 缺点: 标记和清除的效率都不高（比对复制算法）,会产生大量的不连续的内存碎片
      2. 复制算法:这种算法会将内存划分为两个相等的块，每次只使用其中一块。当这块内存不够使用时，就将还存活的对象复制到另一块内存中，然后把这块内存一次清理掉。这样做的效率比较高，也避免了内存碎片。      
         * 优点：简单高效，不会出现内存碎片问题
         * 缺点：内存利用率低，只有一半，存活对象较多时效率明显会降低
      3. 标记-整理算法:这是标记-清除算法的升级版。在完成标记阶段后，不是直接对可回收对象进行清理，而是让存活对象向着一端移动，然后清理掉边界以外的内存
         * MARK - traverse live object graph to mark reachable objects
         * SWEEP - scans memory to find unmarked memory
         * COMPACT - relocating marked objects to defragment free memory
         * When relocating objects in the heap, the JVM should correct all references to this object. During the relocation process the object graph is inconsistent, that is why STW pause is required.
         * 优点：利用率百分之百，没有内存碎片
         * 缺点：标记和清除的效率都不高，效率相对标记-清除要低
      4. CMS(Concurrent Mark Sweep):基于"标记-清除"算法实现的
         1) 初始标记 初始标记仅仅只是标记一下GC Roots能直接关联到的对象，速度很快，需要Stop The World(暂停所有的用户线程)
         2) 并发标记 并发标记阶段就是进行GC Roots Tracing的过程 (用户不暂停)—用户不暂停就还可能产生一些对象与GC Roots不可达
         3) 重新标记重新标记阶段是为了修正 并发标记期间 因用户程序继续运作而导致标记产生变动 的那一部分对象的标记记录，这个阶段的停顿时间会比初始阶段稍长一些,但是远比并发标记的时间短，仍然需要"Stop The World"
         4) 并发清除并发清除阶段会清除对象**（用户不暂停）**
         * 整个过程中耗时最长的并发表及和并发清除过程收集线程可以与用户线程一起工作，所以整体上来说，CMS收集器的内存回收过程与用户线程一起并发执行       
         * 优点：CMS是一款优秀的收集器，主要优点:并发、低停顿
         * 缺点：由于CMS采用"标记-清理"算法实现，所以当垃圾收集后，会产生大量的内存碎片。CMS收集器对CPU非常敏感，虽然不会导致用户线程停顿，但是会因为占用了一部分线程(或者说CPU资源)而导致应用程序变慢，总吞吐量降低
      5. 分代收集算法:当前商业虚拟机都采用这种算法. 首先根据对象存活周期的不同将内存分为几块即新生代、老年代，然后根据不同年代的特点，采用不同的收集算法
         * 新生代: 每次垃圾收集都能发现大批对象已死, 只有少量存活. 因此选用复制算法, 只需要付出少量存活对象的复制成本就可以完成 (Mark and Copy)
         * 老年代: 因为对象存活率高、没有额外空间对它进行分配担保, 就必须采用“标记—清理”或“标记—整理”算法来进行回收, 不必进行内存复制, 且直接腾出空闲内存. (mark-sweep & mark-sweep-compact)
      * 内存（时间复杂度）效率：复制算法 > 标记清除算法 > 标记压缩算法
      * 内存整齐度：复制算法 = 标记压缩法 > 标记清除法  
      * 内存利用率：标记压缩法 = 标记清除法 > 复制算法
    *  Java内存实战
      1. sudo jps -ml  // 查询所有运行的java进程
      2. sudo jmap -heap [端口号]  | head -n20 // 可以得知新生代的分区大小
      3. sudo jmap -histo 7276 | head -n20  // 通过jmap命令查看堆内存中的对象
      4. 观察老年代（heapOldGen）的使用情况，排查是否有内存泄漏 
      5. sudo jmap -dump:format=b,file=heap 7276 // 因此我们先从服务管理平台摘掉了此节点，然后通过以下命令dump堆内存，寻找可疑的object
      6. 从程序中解决，或者将将CMS收集器的分代年龄调到15（最大15）
       


* Rest api
    * Client - server: client and server should be separated and developed without dependency
    * Stateless: meaning that calls can be made independently of one another, and each call contains all of the data necessary to complete itself successfully.
    * Cache: Because a stateless API can increase request overhead by handling large loads of incoming and outbound calls, a REST API should be designed to encourage the storage of cacheable data.
    * Uniform Interface: This interface should provide an unchanging, standardized means of communicating between the client and the server, such as using HTTP with URI resources, CRUD (Create, Read, Update, Delete), and JSON.
    * Layered System: each layer having a specific functionality and responsibility
    

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
    * Spring 中的 bean 的作用域有哪些
      1. singleton : 唯一 bean 实例，Spring 中的 bean 默认都是单例的。
      2. prototype : 每次请求都会创建一个新的 bean 实例。
      3. request : 每一次HTTP请求都会产生一个新的bean，该bean仅在当前HTTP request内有效。
      4. session : 每一次HTTP请求都会产生一个新的 bean，该bean仅在当前 HTTP session 内有效。
      5. global-session： 全局session作用域，仅仅在基于portlet的web应用中才有意义，Spring5已经没有了。Portlet是能够生成语义代码(例如：HTML)片段的小型Java Web插件。它们基于portlet容器，可以像servlet一样处理HTTP请求。但是，与 servlet 不同，每个 portlet 都有不同的会话
    * Spring 容器启动流程（初始化与刷新）
      容器初始化：
        1. 基于java-config 技术分析的入口AnnotationConfigApplicationContext，或者基于xml分析 入口即为 ClassPathXmlApplicationContext，它们俩的共同特征便是都继承了 AbstractApplicationContext 类
        2. 如果我想生成 bean 对象，那么就需要一个 beanFactory 工厂（DefaultListableBeanFactory）；
        3. 如果我想对加了特定注解（如 @Service、@Repository）的类进行读取转化成 BeanDefinition 对象（BeanDefinition 是 Spring 中极其重要的一个概念，它存储了 bean 对象的所有特征信息，如是否单例，是否懒加载，factoryBeanName 等），那么就需要一个注解配置读取器（AnnotatedBeanDefinitionReader）；
        4. 如果我想对用户指定的包目录进行扫描查找 bean 对象，那么还需要一个路径扫描器（ClassPathBeanDefinitionScanner）
        5. 将配置类的BeanDefinition注册到容器中
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
   * Spring 事务
     * 方式: 编程式事务，在代码中硬编码。(不推荐使用)  声明式事务，在配置文件中配置（推荐使用）
     * Spring 事务中的隔离级别有哪几种?  事务的隔离级别就是通过锁的机制来实现，锁的应用最终导致不同事务的隔离级别
       1. TransactionDefinition.ISOLATION_DEFAULT:  使用后端数据库默认的隔离级别，Mysql 默认采用的 REPEATABLE_READ隔离级别 Oracle 默认采用的 READ_COMMITTED隔离级别.
       2. TransactionDefinition.ISOLATION_READ_UNCOMMITTED: 最低的隔离级别，允许读取尚未提交的数据变更，可能会导致脏读、幻读或不可重复读
       3. TransactionDefinition.ISOLATION_READ_COMMITTED:   允许读取并发事务已经提交的数据，可以阻止脏读，但是幻读或不可重复读仍有可能发生
       4. TransactionDefinition.ISOLATION_REPEATABLE_READ:  对同一字段的多次读取结果都是一致的，除非数据是被本身事务自己所修改，可以阻止脏读和不可重复读，但幻读仍有可能发生。
       5. TransactionDefinition.ISOLATION_SERIALIZABLE:   最高的隔离级别，完全服从ACID的隔离级别。所有的事务依次逐个执行，这样事务之间就完全不可能产生干扰，也就是说，该级别可以防止脏读、不可重复读以及幻读。但是这将严重影响程序的性能。通常情况下也不会用到该级别。
     * Spring 事务中哪几种事务传播行为 (https://mp.weixin.qq.com/s/IglQITCkmx7Lpz60QOW7HA)
          - 支持当前事务的情况：
       1. TransactionDefinition.PROPAGATION_REQUIRED： 如果当前存在事务，则加入该事务；如果当前没有事务，则创建一个新的事务。
       2. TransactionDefinition.PROPAGATION_SUPPORTS： 如果当前存在事务，则加入该事务；如果当前没有事务，则以非事务的方式继续运行。
       3. TransactionDefinition.PROPAGATION_MANDATORY： 如果当前存在事务，则加入该事务；如果当前没有事务，则抛出异常。（mandatory：强制性） 
          - 不支持当前事务的情况：
       4. TransactionDefinition.PROPAGATION_REQUIRES_NEW： 创建一个新的事务，如果当前存在事务，则把当前事务挂起。
       5. TransactionDefinition.PROPAGATION_NOT_SUPPORTED： 以非事务方式运行，如果当前存在事务，则把当前事务挂起。
       6. TransactionDefinition.PROPAGATION_NEVER： 以非事务方式运行，如果当前存在事务，则抛出异常。
          - 其他情况：
       7. TransactionDefinition.PROPAGATION_NESTED： 如果当前存在事务，则创建一个事务作为当前事务的嵌套事务来运行；如果当前没有事务，则该取值等价于TransactionDefinition.PROPAGATION_REQUIRED。
     * 只要是以代理方式实现的声明式事务，无论是JDK动态代理，还是CGLIB直接写字节码生成代理，都只有public方法上的事务注解才起作用。而且必须在代理类外部调用才行，如果直接在目标类里面调用，事务照样不起作用。
       private方法不能被继承，final方法不能被重写，static方法和继承不相干,Spring选择让protected方法和package方法不支持事务
   * Spring boot: SpringBoot基本上是 Spring框架的扩展，它消除了设置 Spring应用程序所需的 XML配置，为更快，更高效的开发生态系统铺平了道路。
     * SpringBoot中的一些特征：
        1. 创建独立的 Spring应用。
        2. 嵌入式 Tomcat、 Jetty、 Undertow容器（无需部署war文件）。
        3. 提供的 starters 简化构建配置
        4. 尽可能自动配置 spring应用。
        5. 提供生产指标,例如指标、健壮检查和外部化配置
        6. 完全没有代码生成和 XML配置要求         

* Mybatis
  * MyBatis 是一款优秀的持久层框架，一个半 ORM（对象关系映射）框架，它支持定制化 SQL、存储过程以及高级映射。MyBatis 可以使用简单的 XML 或注解来配置和映射原生类型、接口和 Java 的 POJO（Plain Old Java Objects，普通老式 Java 对象）为数据库中的记录。
  * ORM（Object Relational Mapping），对象关系映射，是一种为了解决关系型数据库数据与简单Java对象（POJO）的映射关系的技术。简单的说，ORM是通过使用描述对象和数据库之间映射的元数据，将程序中的对象自动持久化到关系型数据库中。
  * 为什么说Mybatis是半自动ORM映射工具？它与全自动的区别在哪里？
    * Hibernate属于全自动ORM映射工具，使用Hibernate查询关联对象或者关联集合对象时，可以根据对象关系模型直接获取，所以它是全自动的。
    * JPA (Java Persistent API) just is Interface. Hibernate is the implementation of JPA.
    * 而Mybatis在查询关联对象或关联集合对象时，需要手动编写sql来完成，所以，称之为半自动ORM映射工具。
  * Comparison between MyBatis and JPA/Hibernate
    * Mybatis: simpler and has a smaller package size
    * Hibernate: generates SQL automatically. No sql needed
    * Mybatis: It uses sql which could be database dependent
    * Hibernate: It uses HQL (Hibernate Query Language), which relatively independent of db, e.g. change from mysql to mongoDB
    * Mybatis: It maps ResultSet from jdbc api to your POJO objects.
    * Hibernate: maps your Java POJO Object to database tables.
  * you can use Spring Data JPA for writing (INSERT, UPDATE, DELETE query). It’s a good choice when we want to write something in the database. You will create less code that means decrease bugs. It will make your code more readable. 
  * In case, we need to join many tables(even though 3–4 tables) for report features. If you use Spring Data JPA we will make complex code with mapping the result. But with MyBatis we can do it easily by mapping mechanism. Of course, we have to add more code in this case. However, we will gain good reading performance.
  * 面试题:#{}和${}的区别是什么？
    1. #{}是预编译处理，${}是字符串替换。
    2. MyBatis在处理#{}时，会将SQL中的#{}替换为?号，使用PreparedStatement的set方法来赋值；MyBatis在处理 $ { } 时，就是把 ${ } 替换成变量的值。 
    3. 使用 #{} 可以有效的防止SQL注入，提高系统安全性。
       * String sql = "select * from tb_name where name = '" + varname + "' and passwd = '" + varpasswd + "' ";
       * 如果我们把['or'1'='1]作为varpasswd传入进来.用户名随意,看看会成为什么? 
       * select * from tb_name = 随意' and passwd = ' ' or '1'='1'; 因为'1'='1'肯定成立,所以可以任何通过验证


* RPC（Remote Procedure Call）—远程过程调用，它是一种通过网络从远程计算机程序上请求服务，而不需要了解底层网络技术的协议。比如两个不同的服务 A、B 部署在两台不同的机器上，那么服务 A 如果想要调用服务 B 中的某个方法该怎么办呢？使用 HTTP请求 当然可以，但是可能会比较慢而且一些优化做的并不好。 RPC 的出现就是为了解决这个问题。
  * 调用过程：  
    1. 服务消费方（client）调用以本地调用方式调用服务；
    2. client stub接收到调用后负责将方法、参数等组装成能够进行网络传输的消息体； 
    3. client stub找到服务地址，并将消息发送到服务端；
    4. server stub收到消息后进行解码；server stub根据解码结果调用本地的服务；
    5. 本地服务执行并将结果返回给server stub；server stub将返回结果打包成消息并发送至消费方；
    6. client stub接收到消息，并进行解码；服务消费方得到最终结果。
  *. 让分布式或者微服务系统中不同服务之间的调用像本地调用一样简单。
  * 既有 HTTP ,为啥用 RPC 进行服务调用?
    * RPC 只是一种设计而已RPC 只是一种概念、一种设计，就是为了解决 不同服务之间的调用问题, 它一般会包含有 传输协议 和 序列化协议 这两个。
    * 但是，HTTP 是一种协议，RPC框架可以使用 HTTP协议作为传输协议或者直接使用TCP作为传输协议，使用不同的协议一般也是为了适应不同的场景。

* 计算机网络 https://www.youtube.com/watch?v=yCaplCr3L94
  * TCP的三次握手过程？
    1. 第一次握手：建立连接时，客户端发送syn包（syn=x）到服务器，并进入SYN_SENT状态，等待服务器确认；SYN：同步序列编号（Synchronize Sequence Numbers）；
    2. 第二次握手：服务器收到syn包，必须确认客户的SYN（ack=x+1），同时自己也发送一个SYN包（syn=y），即SYN+ACK包，此时服务器进入SYN_RECV状态；
    3. 第三次握手：客户端收到服务器的SYN+ACK包，向服务器发送确认包ACK(ack=y+1），此包发送完毕，客户端和服务器进入ESTABLISHED（TCP连接成功）状态，完成三次握手。
  * TCP的四次挥手？
    1. 客户端进程发出连接释放报文，并且停止发送数据。释放数据报文首部，FIN=1，其序列号为seq=u（等于前面已经传送过来的数据的最后一个字节的序号加1），此时，客户端进入FIN-WAIT-1（终止等待1）状态。 TCP规定，FIN报文段即使不携带数据，也要消耗一个序号。
    2. - 服务器收到连接释放报文，发出确认报文，ACK=1，ack=u+1，并且带上自己的序列号seq=v，此时，服务端就进入了CLOSE-WAIT（关闭等待）状态。TCP服务器通知高层的应用进程，客户端向服务器的方向就释放了，这时候处于半关闭状态，即客户端已经没有数据要发送了，但是服务器若发送数据，客户端依然要接受。这个状态还要持续一段时间，也就是整个CLOSE-WAIT状态持续的时间。 
       - 客户端收到服务器的确认请求后，此时，客户端就进入FIN-WAIT-2（终止等待2）状态，等待服务器发送连接释放报文（在这之前还需要接受服务器发送的最后的数据）。
    3. 服务器将最后的数据发送完毕后，就向客户端发送连接释放报文，FIN=1，ack=u+1，由于在半关闭状态，服务器很可能又发送了一些数据，假定此时的序列号为seq=w，此时，服务器就进入了LAST-ACK（最后确认）状态，等待客户端的确认。
    4. - 客户端收到服务器的连接释放报文后，必须发出确认，ACK=1，ack=w+1，而自己的序列号是seq=u+1，此时，客户端就进入了TIME-WAIT（时间等待）状态。注意此时TCP连接还没有释放，必须经过2MSL（最长报文段寿命）的时间后，当客户端撤销相应的TCB后，才进入CLOSED状态。
       - 服务器只要收到了客户端发出的确认，立即进入CLOSED状态。同样，撤销TCB后，就结束了这次的TCP连接。可以看到，服务器结束TCP连接的时间要比客户端早一些。 
  * 为什么不能用两次握手进行连接？  
    * 3次握手完成两个重要的功能，既要双方做好发送数据的准备工作(双方都知道彼此已准备好)，也要允许双方就初始序列号进行协商，这个序列号在握手过程中被发送和确认。
    * 现在把三次握手改成仅需要两次握手，死锁是可能发生的。
    * 作为例子，考虑计算机S和C之间的通信，假定C给S发送一个连接请求分组，S收到了这个分组，并发送了确认应答分组。按照两次握手的协定，S认为连接已经成功地建立了，可以开始发送数据分组。可是，C在S的应答分组在传输中被丢失的情况下，将不知道S 是否已准备好，不知道S建立什么样的序列号，C甚至怀疑S是否收到自己的连接请求分组。在这种情况下，C认为连接还未建立成功，将忽略S发来的任何数据分 组，只等待连接确认应答分组。而S在发出的分组超时后，重复发送同样的分组。这样就形成了死锁。
  * 4、为什么四次挥手的TIME_WAIT状态需要经过2MSL(最大报文段生存时间)才能返回到CLOSE状态？
    * 虽然按道理，四个报文都发送完毕，我们可以直接进入CLOSE状态了，但是我们必须假象网络是不可靠的，
    * 有可以最后一个ACK丢失。所以TIME_WAIT状态就是用来重发可能丢失的ACK报文。在Client发送出最后的ACK回复，但该ACK可能丢失。Server如果没有收到ACK，将不断重复发送FIN片段。
    * 所以Client不能立即关闭，它必须确认Server接收到了该ACK。Client会在发送出ACK之后进入到TIME_WAIT状态。Client会设置一个计时器，等待2MSL的时间。如果在该时间内再次收到FIN，那么Client会重发ACK并再次等待2MSL。
    * 所谓的2MSL是两倍的MSL(Maximum Segment Lifetime)。MSL指一个片段在网络中最大的存活时间，2MSL就是一个发送和一个回复所需的最大时间。如果直到2MSL，Client都没有再次收到FIN，那么Client推断ACK已经被成功接收，则结束TCP连接。
  * TCP协议如何来保证传输的可靠性？
    * 数据包校验：目的是检测数据在传输过程中的任何变化，若校验出包有错，则丢弃报文段并且不给出响应，这时TCP发送数据端超时后会重发数据；
    * 对失序数据包重排序：既然TCP报文段作为IP数据报来传输，而IP数据报的到达可能会失序，因此TCP报文段的到达也可能会失序。TCP将对失序数据进行重新排序，然后才交给应用层；
    * 丢弃重复数据：对于重复数据，能够丢弃重复数据；
    * 应答机制：当TCP收到发自TCP连接另一端的数据，它将发送一个确认。这个确认不是立即发送，通常将推迟几分之一秒；
    * 超时重发：当TCP发出一个段后，它启动一个定时器，等待目的端确认收到这个报文段。如果不能及时收到一个确认，将重发这个报文段；
    * 流量控制：TCP连接的每一方都有固定大小的缓冲空间。TCP的接收端只允许另一端发送接收端缓冲区所能接纳的数据，这可以防止较快主机致使较慢主机的缓冲区溢出，这就是流量控制。TCP使用的流量控制协议是可变大小的滑动窗口协议。
  * TCP的流量控制和拥塞控制
    * 流量控制：让发送方发慢点，杨让接受不了方来得及接受，TCP利用滑动窗口机制来实现流量控制
    * 接受窗口：接收方分局接受缓存设置的值，并告诉发送方，反映接收方容量
    * 拥塞窗口：发送方根据自己估算的网络拥塞程度而设置的窗口值，反应网络当前容量
    * 发送窗口：min（接受窗口rwnd，拥塞窗口cwnd）
    * 慢开始和拥塞避免的算法
      * 慢开始：当发送方每收到一个ACK，拥塞窗口cwnd的大小就会翻倍，就是2的n次幂递增。可以看到指数级增长。一般遵循以下判定：
        - cwnd < ssthresh，继续使用慢开始算法；
        - cwnd = ssthresh，即可使用慢开始算法，也可以使用拥塞避免算法。
        - cwnd > ssthresh，停止使用慢开始算法，改用拥塞避免算法；
      * 拥塞避免：当拥塞窗口cwnd超过慢启动门限ssthresh就会进入拥塞避免算法。
        - 加法增大：每经过一个往返时间RTT（Round-TripTime），拥塞窗口+1，让拥塞窗口缓慢增大，按照线性规律增长。
        - 乘法减小：当出现网络拥塞，比如丢包时，将慢开始门限（ssthresh）设为原先的一半，然后将cwnd设为1，执行慢开始算法（较低的起点，指数级增长）。
    * 快重传和快恢复：为了减少因为拥塞导致的数据包丢失带来的重传时间，快重传的机制是：
      - 接收方如果一个包丢失，则对后续的包继续发送针对该包的重传请求；
      - 一旦发送方接收到三个一样的确认，就知道该包之后出现了错误，立刻重传该包；
      - 此时发送方开始执行“快恢复”算法；
      - 慢开始门限减半；
      - cwnd设为慢开始门限减半后的数量；
      - 执行拥塞避免算法（高起点，线性增长
  * UDP：qq好友之间的消息是通过UDP传输的，在应用层控制数据丢失问题
    1. UDP 是无链接的，减少开销和发送数据之前的延时
    2. UDP无拥塞控制，适合实时应用 视频
    3. UDP首部开销小，8 Byte 而TCP有20 Byte
    3. UDP使用最大努力交付，但不保证可靠交付
    4. UDP是面向报文的，适合一次性传输少量数据的网络应用
  * TCP
    1. TCP是面向连接的传输层协议，需要先建立连接再断开连接
    2. TCP是提供可靠交付的服务，无差错，不丢失，不重复，按序到达。可靠有序，不丢不重
    3. 每一条Tcp连接只能有2个端点，点对点的连接
    4. TCP提供全双工通信。双方都可接受不了和发送
       * 发送缓存：准备发送的数据&已发送但尚未收到确认的数据
       * 接受缓存：按序到达但尚未接受应用程序读取的数据*不按序到达的数据
    5. TCP是面向字节流 （流：流入到进程或出来的字节序列）

    





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
      * 先更新数据库，再删除缓存；Cache-Aside Pattern
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
      * Read-Through/Write through
        * Read-Through实际只是在Cache-Aside之上进行了一层封装，它会让程序代码变得更简洁，同时也减少数据源上的负载。
        * Write-Through模式下，当发生写请求时，也是由缓存抽象层完成数据源和缓存数据的更新, 
      * Write behind
        * Write behind跟Read-Through/Write-Through有相似的地方，都是由Cache Provider来负责缓存和数据库的读写。
        * 它两又有个很大的不同：Read/Write Through是同步更新缓存和数据的，Write Behind则是只更新缓存，不直接更新数据库，通过批量异步的方式来更新数据库


    

* Multi-threading
  * Synchronized： https://github.com/e0008233/BackendStudy/blob/master/BackendStudyNotes/java/concurrency/04.%E5%BD%BB%E5%BA%95%E7%90%86%E8%A7%A3synchronized/java%E5%85%B3%E9%94%AE%E5%AD%97---synchronized.md
    * 作用在方法上锁住的是类对象，相当于synchronized(Demo.class) 如果锁的是类对象的话，尽管new多个实例对象，但他们仍然是属于同一个类依然会被锁住，即线程之间保证同步关系。
    * 流程
      * 执行同步代码块后首先要先执行monitorenter指令，退出的时候monitorexit指令。通过分析之后可以看出，使用Synchronized进行同步，其关键就是必须要对对象的监视器monitor进行获取，当线程获取monitor后才能继续往下执行，否则就只能等待。而这个获取的过程是互斥的，即同一时刻只有一个线程能够获取到monitor
      * 每个对象拥有一个计数器，当线程获取该对象锁后，计数器就会加一，释放锁后就会将计数器减一    
      * 任意一个对象都拥有自己的监视器，当这个对象由同步块或者这个对象的同步方法调用时，执行方法的线程必须先获取该对象的监视器才能进入同步块和同步方法，如果没有获取到监视器的线程将会被阻塞在同步块和同步方法的入口处，进入到BLOCKED状态
    * 锁的升级：无锁 -> 偏向锁 -> 轻量级锁 -> 重量级锁
      * 偏向锁：
        * 当一个线程访问同步块并获取锁时，会在对象头和栈帧中的锁记录里存储锁偏向的线程ID，以后该线程在进入和退出同步块时不需要进行CAS操作来加锁和解锁，只需简单地测试一下对象头的Mark Word里是否存储着指向当前线程的偏向锁。
        * 如果测试成功，表示线程已经获得了锁。如果测试失败，则需要再测试一下Mark Word中偏向锁的标识是否设置成1（表示当前是偏向锁）：如果没有设置，则使用CAS竞争锁；如果设置了，则尝试使用CAS将对象头的偏向锁指向当前线程
        * 偏向锁使用了一种等到竞争出现才释放锁的机制，所以当其他线程尝试竞争偏向锁时，持有偏向锁的线程才会释放锁。
        * 偏向锁的撤销，需要等待全局安全点（在这个时间点上没有正在执行的字节码）。它会首先暂停拥有偏向锁的线程，然后检查持有偏向锁的线程是否活着，如果线程不处于活动状态，则将对象头设置成无锁状态；如果线程仍然活着，拥有偏向锁的栈会被执行，遍历偏向对象的锁记录，栈中的锁记录和对象头的Mark Word要么重新偏向于其他线程，要么恢复到无锁或者标记对象不适合作为偏向锁，最后唤醒暂停的线程。  
      * 轻量级锁
        * 线程在执行同步块之前，JVM会先在当前线程的栈桢中创建用于存储锁记录的空间，并将对象头中的Mark Word复制到锁记录中，官方称为Displaced Mark Word。然后线程尝试使用CAS将对象头中的Mark Word替换为指向锁记录的指针。如果成功，当前线程获得锁，如果失败，表示其他线程竞争锁，当前线程便尝试使用自旋来获取锁。
        * 轻量级解锁时，会使用原子的CAS操作将Displaced Mark Word替换回到对象头，如果成功，则表示没有竞争发生。如果失败，表示当前锁存在竞争，锁就会膨胀成重量级锁。下图是两个线程同时争夺锁，导致锁膨胀的流程图。  
      * 重量级锁
        * 重量级锁
          * 重量级锁显然，此忙等是有限度的（有个计数器记录自旋次数，默认允许循环10次，可以通过虚拟机参数更改）。如果锁竞争情况严重，某个达到最大自旋次数的线程，会将轻量级锁升级为重量级锁（依然是CAS修改锁标志位，但不修改持有锁的线程ID）。当后续线程尝试获取锁时，发现被占用的锁是重量级锁，则直接将自己挂起（而不是忙等），等待将来被唤醒。
          * 重量级锁是指当有一个线程获取锁之后，其余所有等待获取该锁的线程都会处于阻塞状态。
  * ReentrantLock: 同一个线程对于已经获得到的锁，可以多次继续申请到该锁的使用权
    * AbstractQueuedSynchronizer（AQS）：FairSync & NonFairSync
      1. 公平锁每次获取到锁为同步队列中的第一个节点，保证请求资源时间上的绝对顺序，而非公平锁有可能刚释放锁的线程下次继续获取该锁，则有可能导致其他线程永远无法获取到锁，造成“饥饿”现象。
      2. 公平锁为了保证时间上的绝对顺序，需要频繁的上下文切换，而非公平锁会降低一定的上下文切换，降低性能开销。因此，ReentrantLock默认选择的是非公平锁，则是为了减少一部分上下文切换，保证了系统更大的吞吐量。
    * 锁的流程：
      * 独占锁的获取（acquire方法）：调用lock()方法是获取独占式锁，获取失败就将当前线程加入同步队列，成功则线程执行
      * 线程获取锁失败，线程被封装成Node进行入队操作，核心方法在于addWaiter()和enq()，同时enq()完成对同步队列的头结点初始化工作以及CAS操作失败的重试;
      * 线程获取锁是一个自旋的过程，当且仅当 当前节点的前驱节点是头结点并且成功获得同步状态时，节点出队即该节点引用的线程获得锁，否则，当不满足条件时就会调用LookSupport.park()方法使得线程阻塞；
      * 释放锁的时候会唤醒后继节点；    
    * 可中断式获取锁（acquireInterruptibly方法）parkAndCheckInterrupt返回true时即线程阻塞时该线程被中断，代码抛出被中断异常。
    * 超时等待式获取锁  
      1. 在超时时间内，当前线程成功获取了锁；
      2. 当前线程在超时时间内被中断；
      3. 超时时间结束，仍未获得锁返回false。
  * Thread Pool：线程池通过复用线程，避免线程频繁创建和销毁。
    * 使用场景
      * 第1种是：固定大小线程池，特点是线程数固定，使用无界队列，适用于任务数量不均匀的场景、对内存压力不敏感，但系统负载比较敏感的场景；
      * 第2种是：Cached线程池，特点是不限制线程数，适用于要求低延迟的短期任务场景；
      * 第3种是：单线程线程池，也就是一个线程的固定线程池，适用于需要异步执行但需要保证任务顺序的场景；
      * 第4种是：Scheduled线程池，适用于定期执行任务场景，支持按固定频率定期执行和按固定延时定期执行两种方式；
      * 第5种是：工作窃取线程池，使用的ForkJoinPool，是固定并行度的多任务队列，适合任务执行时长不均匀的场景。
    * 线程池参数介绍
      * 第1个参数：设置核心线程数。默认情况下核心线程会一直存活。
      * 第2个参数：设置最大线程数。决定线程池最多可以创建的多少线程。
      * 第3个参数和第4个参数：用来设置线程空闲时间，和空闲时间的单位，当线程闲置超过空闲时间就会被销毁。可以通过AllowCoreThreadTimeOut方法来允许核心线程被回收。
      * 第5个参数：设置缓冲队列，图中左下方的三个队列是设置线程池时常使用的缓冲队列。其中Array Blocking Queue是一个有界队列，就是指队列有最大容量限制。Linked Blocking Queue是无界队列，就是队列不限制容量。最后一个是Synchronous Queue，是一个同步队列，内部没有缓冲区。
      * 第6个参数：设置线程池工厂方法，线程工厂用来创建新线程，可以用来对线程的一些属性进行定制，例如线程的Group、线程名、优先级等。一般使用默认工厂类即可。
      * 第7个参数：设置线程池满时的拒绝策略。如右下角所示有四种策略，abort策略在线程池满后，提交新任务时会抛出Rejected Execution Exception，这个也是默认的拒绝策略。
    * 线程池执行流程：我们向线程提交任务时可以使用Execute和Submit，区别就是Submit可以返回一个Future对象，通过Future对象可以了解任务执行情况，可以取消任务的执行，还可获取执行结果或执行异常。Submit最终也是通过Execute执行的。
      * 向线程池提交任务时，会首先判断线程池中的线程数是否大于设置的核心线程数，如果不大于，就创建一个核心线程来执行任务。
      * 如果大于核心线程数，就会判断缓冲队列是否满了，如果没有满，则放入队列，等待线程空闲时执行任务。
      * 如果队列已经满了，则判断是否达到了线程池设置的最大线程数，如果没有达到，就创建新线程来执行任务。
      * 如果已经达到了最大线程数，则执行指定的拒绝策略。这里需要注意队列的判断与最大线程数判断的顺序，不要搞反。
  * The TheadLocal construct allows us to store data that will be accessible only by a specific thread.
    * 使用场景：user context，session
    * we should be extra careful when we're using ThreadLocals and thread pools together as different request may use the same thread
       * 强引用。直接new
       * 软引用。通过SoftReference创建，在内存空间不足的时候直接销毁，即它可能最后的销毁地点是在老年区
       * 弱引用。通过WeakReference创建，在GC的时候直接销毁。即其销毁地点必定为伊甸区
       * 虚引用。通过PhantomReference创建，它和不存也一样，「非常虚，只能通过引用队列在进行一些操作，主要用于堆外内存回收」
    * ThreadLocal为啥使用弱饮用
       * thread运行结束后回到线程池，ThreadLocal一直被线程对象引用，所以在线程销毁之前都是可达的，都无法GC
       * 而我们将ThreadLocal对象的引用作为弱引用，那么就很好的解决了这个问题
       * Value不为弱饮用 是因为不清楚这个Value除了map的引用还是否还存在其他引用，如果不存在其他引用，当GC的时候就会直接将这个Value干掉了，而此时我们的ThreadLocal还处于使用期间，就会造成Value为null的错误，所以将其设置为强引用
    * 内存泄露：当 threadlocal 使用完后，将栈中的 threadlocal 变量置为 null，threadlocal 对象下一次 GC 会被回收，那么 Entry 中的与之关联的弱引用 key 就会变成 null，如果此时当前线程还在运行，那么 Entry 中的 key 为 null 的 Value 对象并不会被回收（存在强引用），这就发生了内存泄漏，当然这种内存泄漏分情况，如果当前线程执行完毕会被回收，那么 Value 自然也会被回收，但是如果使用的是线程池呢，线程跑完任务以后放回线程池（线程没有销毁，不会被回收），Value 会一直存在，这就发生了内存泄漏。
  * 并发编程中有3大重要特性
    * 原子性：一个操作或者多个操作，要么全部执行成功，要么全部执行失败。满足原子性的操作，中途不可被中断
    * 可见性：多个线程共同访问共享变量时，某个线程修改了此变量，其他线程能立即看到修改后的值
    * 有序性：程序执行的顺序按照代码的先后顺序执行。（由于JMM模型中允许编译器和处理器为了效率，进行指令重排序的优化。指令重排序在单线程内表现为串行语义，在多线程中会表现为无序。那么多线程并发编程中，就要考虑如何在多线程环境下可以允许部分指令重排，又要保证有序性）
  * synchronized关键字同时保证上述三种特性
    * synchronized是同步锁，同步块内的代码相当于同一时刻单线程执行，故不存在原子性和指令重排序的问题
    * synchronized关键字的语义JMM有两个规定，保证其实现内存可见性：
      - 线程解锁前，必须把共享变量的最新值刷新到主内存中；
      - 线程加锁前，将清空工作内存中共享变量的值，从主内存中冲洗取值。
  * volatile关键字作用的是保证可见性和有序性，并不保证原子性。  
    * 当对volatile变量执行写操作后，JMM会把工作内存中的最新变量值强制刷新到主内存，写操作会导致其他线程中的缓存无效
    * volatile变量的禁止指令重排序，在指令序列中添加“内存屏障”来禁止指令重排序的。
      * LoadLoad屏障： 对于这样的语句Load1; LoadLoad; Load2，在Load2及后续读取操作要读取的数据被访问前，保证Load1要读取的数据被读取完毕。
      * StoreStore屏障：对于这样的语句Store1; StoreStore; Store2，在Store2及后续写入操作执行前，保证Store1的写入操作对其它处理器可见。
      * LoadStore屏障：对于这样的语句Load1; LoadStore; Store2，在Store2及后续写入操作被刷出前，保证Load1要读取的数据被读取完毕。
      * StoreLoad屏障： 对于这样的语句Store1; StoreLoad; Load2，在Load2及后续所有读取操作执行前，保证Store1的写入对所有处理器可见。
      * JVM的实现会在volatile读写前后均加上内存屏障，在一定程度上保证有序性。如下所示：
        * LoadLoadBarrier -> volatile 读操作 -> LoadStoreBarrier
        * StoreStoreBarrier -> volatile 写操作 -> StoreLoadBarrier
  * SYNCHRONIZED,REENTRANTLOCK的区别  
    * 锁的实现：Synchronized是依赖于JVM实现的，而ReenTrantLock是JDK实现的，有什么区别，说白了就类似于操作系统来控制实现和用户自己敲代码实现的区别。前者的实现是比较难见到的，后者有直接的源码可供阅读。
    * 便利性：很明显Synchronized的使用比较方便简洁，并且由编译器去保证锁的加锁和释放，而ReenTrantLock需要手工声明来加锁和释放锁，为了避免忘记手工释放锁造成死锁，所以最好在finally中声明释放锁。
    * 锁的细粒度和灵活度：很明显ReenTrantLock优于Synchronized
    * ReenTrantLock独有的能力：
      1.ReenTrantLock可以指定是公平锁还是非公平锁。而synchronized只能是非公平锁。所谓的公平锁就是先等待的线程先获得锁。
      2.ReenTrantLock提供了一个Condition（条件）类，用来实现分组唤醒需要唤醒的线程们，而不是像synchronized要么随机唤醒一个线程要么唤醒全部线程。
      3.ReenTrantLock提供了一种能够中断等待锁的线程的机制，通过lock.lockInterruptibly()来实现这个机制。使用 synchronized 时，等待的线程会一直等待下去，不能够响应中断。

* Java深浅拷贝：https://zhuanlan.zhihu.com/p/338820391
  * 引用拷贝：引用拷贝会生成一个新的对象引用地址，但是两个最终指向依然是同一个对象
  * 浅拷贝：浅拷贝会创建一个新对象，新对象和原对象本身没有任何关系，新对象和原对象不等，但是新对象的属性和老对象相同  
    * 实现方法：需要拷贝的类上实现Cloneable接口并重写其clone()方法。
      * 如果属性是基本类型(int,double,long,boolean等)，拷贝的就是基本类型的值；
      * 如果属性是引用类型，拷贝的就是内存地址（即复制引用但不复制引用的对象） ，因此如果其中一个对象改变了这个地址，就会影响到另一个对象。
        ````
           @Override
           protected Object clone() throws CloneNotSupportedException {
               return super.clone();
           }
        ````
    * 深拷贝:在对引用数据类型进行拷贝的时候，创建了一个新的对象，并且复制其内的成员变量。
      * 重写clone()方法:要将类中所有自定义引用变量的类也去实现Cloneable接口实现clone()方法。对于字符类可以创建一个新的字符串实现拷贝。
      * 序列法：自定义的类需要实现Serializable接口。在需要深拷贝的类(Son)中定义一个函数返回该类对象
        ````
          protected Son deepClone() throws IOException, ClassNotFoundException {
            Son son=null;
            //在内存中创建一个字节数组缓冲区，所有发送到输出流的数据保存在该字节数组中
            //默认创建一个大小为32的缓冲区
            ByteArrayOutputStream byOut=new ByteArrayOutputStream();
            //对象的序列化输出
            ObjectOutputStream outputStream=new ObjectOutputStream(byOut);//通过字节数组的方式进行传输
            outputStream.writeObject(this);  //将当前student对象写入字节数组中

            //在内存中创建一个字节数组缓冲区，从输入流读取的数据保存在该字节数组缓冲区
            ByteArrayInputStream byIn=new ByteArrayInputStream(byOut.toByteArray()); //接收字节数组作为参数进行创建
            ObjectInputStream inputStream=new ObjectInputStream(byIn);
            son=(Son) inputStream.readObject(); //从字节数组中读取
            return  son;
          }
        ````
        
* jave IO：https://zhuanlan.zhihu.com/p/292151192
  * 主要分类
    * 按数据流的方向：输入流、输出流
    * 按处理数据单位：字节流、字符流，
      1. 字节流一般用来处理图像、视频、音频、PPT、Word等类型的文件。字符流一般用于处理纯文本类型的文件。
      2. 字节流本身没有缓冲区，缓冲字节流相对于字节流，效率提升非常高。而字符流本身就带有缓冲区。
    * 按功能：节点流、处理流
      1. 节点流：直接操作数据读写的流类，比如FileInputStream
      2. 处理流：对一个已存在的流的链接和封装，通过对数据进行处理为程序提供功能强大、灵活的读写功能，例如BufferedInputStream（缓冲字节流）
         ![Alt text](./images/inputStream.png?raw=true)
         * 在诸多处理流中，有一个非常重要，那就是缓冲流.我们知道，程序与磁盘的交互相对于内存运算是很慢的，容易成为程序的性能瓶颈。减少程序与磁盘的交互，是提升程序效率一种有效手段。缓冲流，就应用这种思路：普通流每次读写一个字节，而缓冲流在内存中设置一个缓存区，缓冲区先存储足够的待操作数据后，再与内存或磁盘进行交互。这样，在总数据量不变的情况下，通过提高每次交互的数据量，减少了交互次数。
         
* Java NIO：https://zhuanlan.zhihu.com/p/369062109
  * Java NIO（New IO/Non-blocking IO）是从 Java 1.4 版本开始引入的一个新的 IO API，可以替代标准的 Java IO API。NIO 与原来的 IO 有同样的作用和目的，但是使用方式完全不同，NIO 支持面向缓冲区的、基于通道的 IO 操作。NIO 将以更加高效的方式进行文件的读写操作。
  * NIO 与 BIO 的区别
    * IO：阻塞IO，面向流
      ![Alt text](./images/io.png?raw=true)
      1. 我们需要把磁盘文件或者网络文件中的数据读取到程序中来，我们需要建立一个用于传输数据的管道，原来我们传输数据面对的直接就是管道里面一个个字节数据的流动（我们弄了一个 byte 数组，来回进行数据传递），所以说原来的 IO 它面对的就是管道里面的一个数据流动，所以我们说原来的 IO 是面向流的
      2. 我们说传统的 IO 还有一个特点就是，它是单向的。解释一下就是：如果说我们想把目标地点的数据读取到程序中来，我们需要建立一个管道，这个管道我们称为输入流。相应的，如果如果我们程序中有数据想要写到目标地点去，我们也得再建立一个管道，这个管道我们称为输出流。所以我们说传统的 IO 流是单向的
      3. 每次有一个客户端连接进来的时候，都会有一个新的线程去处理，缺点显而易见，如果连接比较多的时候，我们就要建立大量的线程去一一处理。  
    * NIO: 非阻塞I，面向缓冲区，选择器   
      ![Alt text](./images/nio.png?raw=true)
      1. 即便你用 NIO ，它也是为了数据传输，所以你要想完成数据传输，你也得建立一个用于传输数据的通道，这个通道你不能把它理解为之前的水流了，但是你可以把它理解为铁路，铁路本身是不能完成运输的，铁路要想完成运输它必须依赖火车，说白了这个通道就是为了连接目标地点和源地点。
      2. 所以注意通道本身不能传输数据，要想传输数据必须要有缓冲区，这个缓冲区你就可以完全把它理解为火车，比如说你现在想把程序中的数据写到文件中，那么你就可以把数据都写到缓冲区，然后缓冲区通过通道进行传输，最后再把数据从缓冲区拿出来写到文件中，你想把文件中的数据传数到程序中，也是一个道理，把数据写到缓冲区，缓冲区通过通道进行传输，到程序中把数据拿出来。所以我们说原来的 IO 单向的现在的缓冲区是双向的，这种传输数据的方式也叫面向缓冲区。
      3. 总结一下，就是通道只负责连接，缓冲区才负责存储数据。
      4. 提供了很多异步的IO操作方法  
    * 阻塞与非阻塞的区别：
      - 同步阻塞io：client在调用read（）方法时，stream里没有数据可读，线程停止向下执行，直至stream有数据。
      - 同步非阻塞io：调用read方法后，如果stream没有数据，方法就返回，然后这个线程就就干别的去了。
    * 异步与同步的区别
      - 服务端调用read()方法，若stream中无数据则返回，程序继续向下执行。当stream中有数据时，操作系统会负责把数据拷贝到用户空间，然后通知这个线程，这里的消息通知机制就是异步
      - NIO那样，自己起一个线程去监控stream里面有没有数据
    * NIO的缓冲区：在 Java NIO 中负责数据的存取。缓冲区就是数组。用于存储不同类型的数据。根据数据类型的不同（boolean 除外），提供了相应类型的缓冲区（ByteBuffer,CharBuffer,IntBuffer...）
      * 上述缓冲区管理方式几乎一致，都是通过 allocate() 来获取缓冲区 
      * 缓冲区存取数据的两个核心方法 put():存入数据到缓冲区中 get():获取缓冲区中的数据
      * 缓冲区中的四个核心属性
        * capacity: 容量，表示缓冲区中最大存储数据的容量。一旦声明不能更改。
        * limit: 界限，表示缓冲区中可以操作数据的大小。（limit 后的数据不能进行读写）
        * position: 位置，表示缓冲区中正在操作数据的位置。
        * mark: 标记，表示记录当前 position 的位置。可以通过 reset() 恢复到 mark 的位置。
    * NIO的通道：Channel 表示 IO 源与目标打开的连接。Channel 类似于传统的流，只不过 Channel 本身不能直接访问数据，Channel 只能与 Buffer 进行交互。
      * 主要实现类：FileChannel, SocketChannel, ServerSocketChannel,DatagramChannel
    * NIO的Selector：Selector运行单线程处理多个Channel，如果你的应用打开了多个通道，但每个连接的流量都很低，使用Selector就会很方便。例如在一个聊天服务器中。要使用Selector, 得向Selector注册Channel，然后调用它的select()方法。这个方法会一直阻塞到某个注册的通道有事件就绪。一旦这个方法返回，线程就可以处理这些事件，事件的例子有如新的连接进来、数据接收等。
      * 用于检查一个或多个NIO Channel（通道）的状态是否处于可读、可写。如此可以实现单线程管理多个channels,也就是可以管理多个网络链接
      * 可以监听四种不同类型的事件：
        1. Connect: 比如某个Channel成功连接到另一个服务器称为"连接就绪"
        2. Accept: 一个Server Socket Channel准备好接收新进入的连接称为"接收就绪"
        3. Read: 一个有数据可读的通道可以说是"读就绪
        4. Write: 等待写数据的通道可以说是"写就绪"
  * Java AIO: 与NIO不同，当进行读写操作时，只须直接调用API的read或write方法即可。
    * 这两种方法均为异步的，对于读操作而言，当有流可读取时，操作系统会将可读的流传入read方法的缓冲区，并通知应用程序；
    * 对于写操作而言，当操作系统将write方法传递的流写入完毕时，操作系统主动通知应用程序。  即可以理解为，read/write方法都是异步的，完成后会主动调用回调函数
  * Reactor: https://www.zhihu.com/search?q=reactor%20proactor&utm_content=search_history&type=content
    * 对 I/O 多路复用作了一层封装，让使用者不用考虑底层网络 API 的细节，只需要关注应用代码的编写。
    * Reactor 模式也叫 Dispatcher 模式，我觉得这个名字更贴合该模式的含义，即 I/O 多路复用监听事件，收到事件后，根据事件类型分配（Dispatch）给某个进程 / 线程。
    * Reactor 模式主要由 Reactor 和处理资源池这两个核心部分组成，它俩负责的事情如下：
      1. Reactor 负责监听和分发事件，事件类型包含连接事件、读写事件； 
      2. 处理资源池负责处理事件，如 read -> 业务逻辑 -> send；
    * Reactor与Proactor区别：
      * Reactor 是非阻塞同步网络模式，感知的是就绪可读写事件。在每次感知到有事件发生（比如可读就绪事件）后，就需要应用进程主动调用 read 方法来完成数据的读取，也就是要应用进程主动将 socket 接收缓存中的数据读到应用进程内存中，这个过程是同步的，读取完数据后应用进程才能处理数据。
      * Proactor 是异步网络模式，感知的是已完成的读写事件。在发起异步读写请求时，需要传入数据缓冲区的地址（用来存放结果数据）等信息，这样系统内核才可以自动帮我们把数据的读写工作完成，这里的读写工作全程由操作系统来做，并不需要像 Reactor 那样还需要应用进程主动发起 read/write 来读写数据，操作系统完成读写工作后，就会通知应用进程直接处理数据。
      * 在 Linux 下的异步 I/O 是不完善的， aio 系列函数是由 POSIX 定义的异步操作接口，不是真正的操作系统级别支持的，而是在用户空间模拟出来的异步，并且仅仅支持基于本地文件的 aio 异步操作，网络编程中的 socket 是不支持的，这也使得基于 Linux 的高性能网络程序都是使用 Reactor 方案。
    





* 项目经历
  * 提高并发量，从支持数千人在线到支持上万人
    * 数据的预处理
    * 多级缓存
    * 限流限速
  * 缓存不一致问题:https://zhuanlan.zhihu.com/p/374014490
    * 如果缓存更新频繁：设置较短的缓存过期时间
    * 如果高并发：延时双删,防止更新过程中其他请求读取旧值写到redis中  
    * 如果缓存删除失败：删除缓存重试机制 ：不管是延时双删还是Cache-Aside的先操作数据库再删除缓存，如果第二步的删除缓存失败呢，删除失败会导致脏数据哦
      * 缓存因为某些原因，删除失败 -> 把删除失败的key放到消息队列 -> 消费消息队列的消息，获取要删除的key -> 重试删除缓存操作
    * 如果需要强一致性：Read-Through/Write-Through（读写穿透:服务端把缓存作为主要数据存储。应用程序跟数据库缓存交互，都是通过抽象缓存层完成的。
    
    
    
    

      








            