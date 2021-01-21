AOP(Aspect Oriented Programming) is a programming paradigm that aims to increase modularity by allowing the separation of cross-cutting concerns. It does so by adding additional behavior to existing code without modification of the code itself.

简单写个例子让你更容易理解，比如用户登录鉴权，我们希望调用接口时用户必须已经登录过，所以调用接口必须要检查请求中token是否合法，保证用户已登录。

而这样就必须在每个方法中都做登录检查操作，会产生很多重复性功能代码

![Alt text](../images/aop1.png?raw=true "complexity") 

而我们用AOP以后，将检查操作拦截在所有接口前边，token检查功能抽离出来统一处理。

![Alt text](../images/aop2.png?raw=true "complexity")

![Alt text](../images/aop3.png?raw=true "complexity")

AOP实质上就是把共同的功能向上抽象。还能实现比如：日志统计、异常的处理、事务管理等，基于此思路可以做很多的功能。