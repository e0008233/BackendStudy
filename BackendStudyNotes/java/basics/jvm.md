JVM overview:  
https://zhuanlan.zhihu.com/p/214027455
![Alt text](./../images/jvm.png?raw=true)

Heap:  
https://zhuanlan.zhihu.com/p/267090711
- JVM在进行GC时，并不是对年轻代、老年代统一回收；大部分时候，回收都是在年轻代
- GC分为两种：  
-- 轻GC（清理年轻代）  
-- 重GC（清理年轻代+老年代） 

内存（时间复杂度）效率：复制算法 > 标记清除算法 > 标记压缩算法  
内存整齐度：复制算法 = 标记压缩法 > 标记清除法  
内存利用率：标记压缩法 = 标记清除法 > 复制算法

没有最优的算法，只有最合适的算法GC 也称为 分代收集算法

- 对于年轻代： 对象存活率低，用复制算法
- 对于老年代：区域大，对象存活率高，用标记清除+标记压缩混合实现
  
![Alt text](./../images/person_class.jpg?raw=true)
![Alt text](./../images/person_memory.jpg?raw=true)




