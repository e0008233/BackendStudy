Set  
Set:注重独一无二的性质,该体系集合可以知道某物是否已近存在于集合中,不会存储重复的元素  

用于存储无序(存入和取出的顺序不一定相同)元素，值不能重复。

对象的相等性

引用到堆上同一个对象的两个引用是相等的。如果对两个引用调用hashCode方法，会得到相同的结果，如果对象所属的类没有覆盖Object的hashCode方法的话，hashCode会返回每个对象特有的序号（java是依据对象的内存地址计算出的此序号），所以两个不同的对象的hashCode值是不可能相等的。

如果想要让两个不同的Person对象视为相等的，就必须覆盖Object继下来的hashCode方法和equals方法，因为Object  hashCode方法返回的是该对象的内存地址，所以必须重写hashCode方法，才能保证两个不同的对象具有相同的hashCode，同时也需要两个不同对象比较equals方法会返回true

HashSet到底是如何判断两个元素重复。

通过hashCode方法和equals方法来保证元素的唯一性，add()返回的是boolean类型

判断两个元素是否相同，先要判断元素的hashCode值是否一致，只有在该值一致的情况下，才会判断equals方法，如果存储在HashSet中的两个对象hashCode方法的值相同equals方法返回的结果是true，那么HashSet认为这两个元素是相同元素，只存储一个（重复元素无法存入）。

注意：HashSet集合在判断元素是否相同先判断hashCode方法，如果相同才会判断equals。如果不相同，是不会调用equals方法的。

 

HashSet 和ArrayList集合都有判断元素是否相同的方法，

boolean contains(Object o)

HashSet使用hashCode和equals方法，ArrayList使用了equals方法

***
![Alt text](images/Set_Hashtable.png?raw=true "Hashtable")  
图1：hashCode值不相同的情况

图2：hashCode值相同，但equals不相同的情况。

HashSet：通过hashCode值来确定元素在内存中的位置。一个hashCode位置上可以存放多个元素。

当hashcode() 值相同equals() 返回为true 时,hashset 集合认为这两个元素是相同的元素.只存储一个（重复元素无法放入）。调用原理:先判断hashcode 方法的值,如果相同才会去判断equals 如果不相同,是不会调用equals方法的。

***
Methods used for comparison for default java object (not overridden)
- equals(object obj): two objects are equal if and only if they are store in the same memory address
- hashcode(): returns an unique integer representation of the memory address

***
String: equals() & == , equals() method is overridden in String
- Main difference between .equals() method and == operator is that one is method and other is operator.
- We can use == operators for reference comparison (address comparison) and .equals() method for content comparison. In simple words, == checks if both objects point to the same memory location whereas .equals() evaluates to the comparison of values in the objects.

*** 
TreeSet in Java

TreeSet is one of the most important implementations of the SortedSet interface in Java that uses a Tree for storage. The ordering of the elements is maintained by a set using their natural ordering whether or not an explicit comparator is provided.  
TreeSet<String> ts1 = new TreeSet<String>(); 

*** 

源码中的hashCode其实就是JVM启动的一个参数，每一个分支对应一个生成策略。通过-XX:hashCode，可以任意切换hashCode的生成策略。首先解释一下入参oop obj就是对象的逻辑地址。所以与地址相关的生成策略有两条，在hashCode等于1或4的时候。
* hashCode==1：这种方式具有幂等的性质，在STW（stop-the-world）操作中，这种策略通常用于同步方案中。利用对象地址计算，使用不经常更新的随机数参与运算。
* hashCode==4：与创建对象的内存位置有关，原样输出。
* 其他情况：hashCode==0：简单地返回随机数，与对象的内存地址没有联系。然而根据随机数生成并全局地读写在多处理器下并不占优势。
* hashCode==2：始终返回完全相同的标识，即hashCode=1。这可用于测试依赖对象标识的代码。
* hashcode==3：从零开始计算哈希代码值。它看起来不是线程安全的，因此多个线程可以生成具有相同哈希代码的对象。
* hashCode>=5（默认）：在jdk1.8中，这是默认的hashCode生成算法，支持多线程生成。使用了Marsaglia的xor-shift算法产生伪随机数。

作者：噜噜呀
链接：https://zhuanlan.zhihu.com/p/121555725
来源：知乎
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。