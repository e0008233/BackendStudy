Queue： 基本上，一个队列就是一个先入先出（FIFO）的数据结构
Queue接口与List, Set同一级别，都是继承了Collection接口。LinkedList实现了Deque接口.

Queue的实现


1、没有实现的阻塞接口的LinkedList： 

   实现了java.util.Queue接口和java.util.AbstractQueue接口
　　内置的不阻塞队列： PriorityQueue 和 ConcurrentLinkedQueue
　　PriorityQueue 和 ConcurrentLinkedQueue 类在 Collection Framework 中加入两个具体集合实现。 
　　PriorityQueue 类实质上维护了一个有序列表。加入到 Queue 中的元素根据它们的天然排序（通过其 java.util.Comparable 实现）或者根据传递给构造函数的 java.util.Comparator 实现来定位。
　　ConcurrentLinkedQueue 是基于链接节点的、线程安全的队列。并发访问不需要同步。因为它在队列的尾部添加元素并从头部删除它们，所以只要不需要知道队列的大 小，　　　　    　　ConcurrentLinkedQueue 对公共集合的共享访问就可以工作得很好。收集关于队列大小的信息会很慢，需要遍历队列。


2)实现阻塞接口的：

　　java.util.concurrent 中加入了 BlockingQueue 接口和五个阻塞队列类。它实质上就是一种带有一点扭曲的 FIFO 数据结构。不是立即从队列中添加或者删除元素，线程执行操作阻塞，直到有空间或者元素可用。
五个队列所提供的各有不同：
　　* ArrayBlockingQueue ：一个由数组支持的有界队列。
　　* LinkedBlockingQueue ：一个由链接节点支持的可选有界队列。
　　* PriorityBlockingQueue ：一个由优先级堆支持的无界优先级队列。
　　* DelayQueue ：一个由优先级堆支持的、基于时间的调度队列。
　　* SynchronousQueue ：一个利用 BlockingQueue 接口的简单聚集（rendezvous）机制。

LinkedBlockingQueue vs ConcurrentLinkedQueue

| Tables        |LinkedBlockingQueue| ConcurrentLinkedQueue        |
| ------------- |-------------| --------------------|
| Blocking Nature      | It is a blocking queue and implements the BlockingQueue interface | 	It is a non-blocking queue and does not implement the BlockingQueue interface |
| Queue Size    | 	It is an optionally bounded queue, which means there are provisions to define the queue size during creation      |   It is an unbounded queue, and there is no provision to specify the queue size during creation |
| Blocking Behavior | It is a blocking queue. So, it blocks the accessing threads when the queue is empty      |    It does not block the accessing thread when the queue is empty and returns null |

***
Queue<Integer> q = new LinkedList<>(); 
PriorityQueue<String> queue = new PriorityQueue<String>(); 
BlockingQueue<Integer> boundedQueue = new LinkedBlockingQueue<>(100);
ConcurrentLinkedQueue queue = new ConcurrentLinkedQueue();

***

同步、异步 (synchronous vs asynchronous) : 同步与异步描述的是被调用者的. Server side

如A调用B：

如果是同步，B在接到A的调用后，会立即执行要做的事。A的本次调用可以得到结果。

如果是异步，B在接到A的调用后，不保证会立即执行要做的事，但是保证会去做，B在做好了之后会通知A。A的本次调用得不到结果，但是B执行完之后会通知A。

阻塞、非阻塞 (Blocking vs non-blocking): 阻塞与非阻塞描述的是调用者的. Client side

如A调用B：

如果是阻塞，A在发出调用后，要一直等待，等着B返回结果。

如果是非阻塞，A在发出调用后，不需要等待，可以去做自己的事情。



同步不一定阻塞，异步也不一定非阻塞。没有必然关系。

举个简单的例子，老张烧水。 1 老张把水壶放到火上，一直在水壶旁等着水开。（同步阻塞） 2 老张把水壶放到火上，去客厅看电视，时不时去厨房看看水开没有。（同步非阻塞） 3 老张把响水壶放到火上，一直在水壶旁等着水开。（异步阻塞） 4 老张把响水壶放到火上，去客厅看电视，水壶响之前不再去看它了，响了再去拿壶。（异步非阻塞）

1和2的区别是，调用方在得到返回之前所做的事情不一行。 1和3的区别是，被调用方对于烧水的处理不一样。

***
Final methods cannot be overridden.
Final class cannot be subclassed. 

***
Static methods and variables can be accessed without objects/instances
Only inner class can be static

***
Abstract class vs Interface
Abstraction: Hiding the internal implementation of the feature and only showing the functionality to the users. i.e. what it works (showing), how it works (hiding). Both abstract class and interface are used for abstraction.

- Type of methods: Interface can have only abstract methods. Abstract class can have abstract and non-abstract methods. From Java 8, it can have default and static methods also.
- Final Variables: Variables declared in a Java interface are by default final. An abstract class may contain non-final variables.
- Type of variables: Abstract class can have final, non-final, static and non-static variables. Interface has only static and final variables.
- Implementation: Abstract class can provide the implementation of interface. Interface can’t provide the implementation of abstract class.
- Inheritance vs Abstraction: A Java interface can be implemented using keyword “implements” and abstract class can be extended using keyword “extends”.
- Multiple implementation: An interface can extend another Java interface only, an abstract class can extend another Java class and implement multiple Java interfaces.
- Accessibility of Data Members: Members of a Java interface are public by default. A Java abstract class can have class members like private, protected, etc.