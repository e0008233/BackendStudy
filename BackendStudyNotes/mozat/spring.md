https://zhuanlan.zhihu.com/p/349386138  
我们到底为什么要用 IoC 和 AOP  
作为一名 Java 开发，对 Spring 框架是再熟悉不过的了。Spring 支持的控制反转（Inversion of Control，缩写为IoC）和面向切面编程（Aspect-oriented programming，缩写为AOP）早已成为我们的开发习惯，仿佛 Java 开发天生就该如此。人总是会忽略习以为常的事物，所有人都熟练使用 IoC 和 AOP，却鲜有人说得清楚到底为什么要用 IoC 和 AOP。

传统mvc遇到的问题：    private UserService userService = new UserServiceImpl();
- 创建了许多重复对象，造成大量资源浪费；
- 更换实现类需要改动多个地方；
- 创建和配置组件工作繁杂，给组件调用方带来极大不便。

透过现象看本质，这些问题的出现都是同一个原因：组件的调用方参与了组件的创建和配置工作。

1. 控制反转和依赖注入

控制反转，是指对象的创建和配置的控制权从调用方转移给容器。好比在家自己做菜，菜的味道全部由自己控制；去餐馆吃饭，菜的味道则是交由餐馆控制。IoC 容器就担任了餐馆的角色。

有了 IoC 容器，我们可以将对象交由容器管理，交由容器管理后的对象称之为 Bean。调用方不再负责组件的创建，要使用组件时直接获取 Bean 即可：

```
@Component
public class UserServiceImpl implements UserService{
    @Autowired // 获取 Bean
    private UserDao userDao;
}
```

调用方只需按照约定声明依赖项，所需要的 Bean 就自动配置完毕了，就好像在调用方外部注入了一个依赖项给其使用，所以这种方式称之为 依赖注入（Dependency Injection，缩写为 DI）。控制反转和依赖注入是一体两面，都是同一种开发模式的表现形式。  

2. 控制反转和依赖注入
   
面向对象编程（Object-oriented programming，缩写：OOP）的三大特性：封装、继承、多态，我们早已用得炉火纯青。OOP 的好处已无需赘言，相信大家都有体会。这里咱们来看一下 OOP 的局限性。  

当有重复代码出现时，可以就将其封装出来然后复用。我们通过分层、分包、分类来规划不同的逻辑和职责，就像之前讲解的三层架构。但这里的复用的都是核心业务逻辑，并不能复用一些辅助逻辑，比如：日志记录、性能统计、安全校验、事务管理，等等。这些边缘逻辑往往贯穿你整个核心业务，传统 OOP 很难将其封装：  

```
public class UserServiceImpl implements UserService {
    @Override
    public void doService() {
        System.out.println("---安全校验---");
        System.out.println("---性能统计 Start---");
        System.out.println("---日志打印 Start---");
        System.out.println("---事务管理 Start---");

        System.out.println("业务逻辑");

        System.out.println("---事务管理 End---");
        System.out.println("---日志打印 End---");
        System.out.println("---性能统计 End---");
    }
}
```
为了方便演示，这里只用了打印语句，就算如此这代码看着也很难受，而且这些逻辑是所有业务方法都要加上，想想都恐怖。

OOP 是至上而下的编程方式，犹如一个树状图，A调用B、B调用C，或者A继承B、B继承C。这种方式对于业务逻辑来说是合适的，通过调用或继承以复用。而辅助逻辑就像一把闸刀横向贯穿所有方法，如图2-4所示：

![Alt text](images/aop.jpg?raw=true)

这一条条横线仿佛切开了 OOP 的树状结构，犹如一个大蛋糕被切开多层，每一层都会执行相同的辅助逻辑，所以大家将这些辅助逻辑称为层面或者切面。  

代理模式用来增加或增强原有功能再适合不过了，但切面逻辑的难点不是不修改原有业务，而是对所有业务生效。对一个业务类增强就得新建一个代理类，对所有业务增强，每个类都要新建代理类，这无疑是一场灾难。而且这里只是演示了一个日志打印的切面逻辑，如果我再加一个性能统计切面，就得新建一个切面代理类来代理日志打印的代理类，一旦切面多起来这个代理类嵌套就会非常深。  

面向切面编程（Aspect-oriented programming，缩写为 AOP）正是为了解决这一问题而诞生的技术。  

AOP 不是 OOP 的对立面，它是对 OOP 的一种补充。OOP 是纵向的，AOP 是横向的，两者相结合方能构建出良好的程序结构。AOP 技术，让我们能够不修改原有代码，便能让切面逻辑在所有业务逻辑中生效。

我们只需声明一个切面，写上切面逻辑： 

```
@Aspect // 声明一个切面
@Component
public class MyAspect {
    // 原业务方法执行前
    @Before("execution(public void com.rudecrab.test.service.*.doService())")
    public void methodBefore() {
        System.out.println("===AspectJ 方法执行前===");
    }

    // 原业务方法执行后
    @AfterReturning("execution(* com.rudecrab.test.service..doService(..))")
    public void methodAddAfterReturning() {
        System.out.println("===AspectJ 方法执行后===");
    }
}
```
无论你有一个业务方法，还是一万个业务方法，对我们开发者来说只需编写一次切面逻辑，就能让所有业务方法生效，极大提高了我们的开发效率。

