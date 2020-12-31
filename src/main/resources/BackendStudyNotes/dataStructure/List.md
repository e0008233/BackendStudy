List主要分为3类，ArrayList， LinkedList和Vector
![Alt text](images/List_overview.png?raw=true "Overview")  

- ArrayList是一个数组实现的列表，由于数据是存入数组中的，所以它的特点也和数组一样，查询很快，但是中间部分的插入和删除很慢
- Vector就是ArrayList的线程安全版，它的方法前都加了synchronized锁，其他实现逻辑都相同。
  如果对线程安全要求不高的话，可以选择ArrayListp，毕竟synchronized也很耗性能
- LinkedList 故名思意就是链表，和我们大学在数据结构里学的链表是一会事，LinkedList还是一个双向链表。
  
    LinkedList<String> cars = new LinkedList<String>();  
    ArrayList<String> cars = new ArrayList<String>();  
    Vector<String> cars = new Vector<String>();

List的3个特性  
1.是按顺序查找  
2.允许存储项为空  
3.允许多个存储项的值相等  

然后对比LinkedList和ArrayList的实现方式不同，可以在不同的场景下使用不同的List
ArrayList是由数组实现的，方便查找，返回数组下标对应的值即可，适用于多查找的场景
LinkedList由链表实现，插入和删除方便，适用于多次数据替换的场景

arraylist-vs-linkedlist-complexity

![Alt text](images/List_efficiency.png?raw=true "complexity")  

*** 
Deadlock describes a situation where two or more threads are blocked forever, waiting for each other. Deadlock occurs when multiple threads need the same locks but obtain them in different order. A Java multithreaded program may suffer from the deadlock condition because the synchronized keyword causes the executing thread to block while waiting for the lock, or monitor, associated with the specified object. 
- Thread A holding resource A needs resource B
- Thread B holding resource B needs resource A
- Two threads are waiting for each other

How to avoid dead locks?
- Avoid Nested Locks – You must avoid giving locks to multiple threads, this is the main reason for a deadlock condition. It normally happens when you give locks to multiple threads.
- Avoid Unnecessary Locks – The locks should be given to the important threads. Giving locks to the unnecessary threads that cause the deadlock condition.
- Using Thread Join – A deadlock usually happens when one thread is waiting for the other to finish. In this case, we can use Thread.join with a maximum time that a thread will take.


