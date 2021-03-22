Redis: 
- Use redis for in-memory storage
- Adding cache for frequently used methods to reduce redis hit
- Combining multiple redis write methods into one, such as message count for hosts (im)
- Pipeline for multiple redis operation

Mysql:
- Indexing
- De-normalization: adding redundant information into a database to speed up the reads. Save time from joining 2 tables
- Or no-sql
- Mysql master slave  
  https://segmentfault.com/a/1190000008942618  
- mysql read write splitting 
-- 在开发工作中，有时候会遇见某个sql 语句需要锁表，导致暂时不能使用读的服务，这样就会影响现有业务，使用主从复制，让主库负责写，从库负责读，这样，即使主库出现了锁表的情景，通过读从库也可以保证业务的正常运作。   
-- 读写分离就是在主服务器上修改，数据会同步到从服务器，从服务器只能提供读取数据，不能写入，实现备份的同时也实现了数据库性能的优化，以及提升了服务器安全  
-- https://cloud.tencent.com/developer/article/1521538?from=information.detail.%E8%AF%BB%E5%86%99%E5%88%86%E7%A6%BB%E7%9A%84%E5%A5%BD%E5%A4%84
   
Asynchronous processing
- Slow operation should ideally be done asynchronously. Otherwise, a user night get stuck waiting for results. 

Database partitioning (sharding) (???)

https://www.cnblogs.com/qcloud1001/archive/2019/02/20/10405281.html


MapReduce to handle a large amount of data 



Coding review issues
GZ Server Huanghua
1. 缺少异常日志，所有异常要增加日志，以便快速检查问题         improved done
2. 增加防刷功能（每个用户3秒内仅能访问一次）                        improved done
   SG Server  ZhangHao
1. 时区与数据部门保持一致，目前是utc， 数据部门是utc +3     improved done
2. 在2 个接口的首行增加以个唯一表示的日志，用于做用户访问量统计.    Improved done
3. remainingUsd<=req.getPrice() 改为 < 否则，用户账号有100usd 无法购买100 usd 的coins.   Improved done
4. 重写增加金币的方法（避免快速访问时出现多加金币的问题） improved done
5. 判断用户已经兑换的coins 记录，要从readonly 库改用write 库（避免用户快速访问时由于读写数据同步延时查询到未扣减记录，导致可重复扣减） improved done
6. 对加经验点数的接口增加方法同步synchronized 保证单节点同步 , 然后业务实现逻辑，运用全局锁key 采用 userid 保证全局同步（避免并发导致重复增加coins) improved done

insertSelective throw exception
都 OK 了
