nginx

分布式系统的CAP理论
https://zhuanlan.zhihu.com/p/33999708

分布式锁
1、在分布式系统环境下，一个方法在同一时间只能被一个机器的一个线程执行；
2、高可用、高性能的获取锁与释放锁；
3、具备可重入特性；
4、具备锁失效机制，防止死锁；
5、具备非阻塞锁特性，即没有获取到锁将直接返回获取锁失败。

分布式锁的三种实现方式：
1、基于数据库实现分布式锁；
2、基于缓存（Redis）实现分布式锁；
3、基于Zookeeper实现分布式；
