https://zhuanlan.zhihu.com/p/64147696
https://link.zhihu.com/?target=http%3A//mp.weixin.qq.com/s%3F__biz%3DMzIwMTY0NDU3Nw%3D%3D%26mid%3D504458938%26idx%3D1%26sn%3D37f6f80073a4a50838e8697e5dcf7275%26chksm%3D0d0f01f43a7888e2ae2f67c884664482c8fb5fc930cfa188e39346eda7c51a983bb7f4deb1f6%23rd

<h3>1. JDK 和 JRE 有什么区别？</h3>
- JDK：Java Development Kit 的简称，java 开发工具包，提供了 java 的开发环境和运行环境。
- JRE：Java Runtime Environment 的简称，java 运行环境，为 java 的运行提供了所需环境  

---
<h3>2. == 和 equals 的区别是什么？</h3>
> == 解读   
> 对于基本类型和引用类型 == 的作用效果是不同的，如下所示： 
> - 基本类型：比较的是值是否相同； 
> - 引用类型：比较的是引用是否相同；
>
> 代码示例：

```java
public class demo {
    void test() {
        String x = "string";
        String y = "string";
        String z = new String("string");
        System.out.println("22");
        System.out.println(x == y);// true
        System.out.println(x == z); // false
        System.out.println(x.equals(y)); // true
        System.out.println(x.equals(z)); // true
    }
}
```

代码解读：因为 x 和 y 指向的是同一个引用，所以 == 也是 true，而 new String()方法则重写开辟了内存空间，所以 == 结果为 false，而 equals 比较的一直是值，所以结果都为 true。

> equals 解读  
> equals 本质上就是 ==，只不过 String 和 Integer 等重写了 equals 方法，把它变成了值比较。看下面的代码就明白了。   
> 总结 ：== 对于基本类型来说是值比较，对于引用类型来说是比较的是引用；而 equals 默认情况下是引用比较，只是很多类重新了 equals 方法，比如 String、Integer 等把它变成了值比较，所以一般情况下 equals 比较的是值是否相等。

<h3>3. 两个对象的 hashCode()相同，则 equals()也一定为 true，对吗？</h3>
不对，两个对象的 hashCode()相同，equals()不一定 true。例如HashSet先不叫hashCode()，再比较equals

<h3>4. final 在 java 中有什么作用？</h3>
- final 修饰的类叫最终类，该类不能被继承。
- final 修饰的方法不能被重写。
- final 修饰的变量叫常量，常量必须初始化，初始化之后值就不能被修改。

<h3>5. java 中操作字符串都有哪些类？它们之间有什么区别？</h3>
操作字符串的类有：String、StringBuffer、StringBuilder。  

String 和 StringBuffer、StringBuilder 的区别在于 String 声明的是不可变的对象，每次操作都会生成新的 String 对象，然后将指针指向新的 String 对象，而 StringBuffer、StringBuilder 可以在原有对象的基础上进行操作，所以在经常改变字符串内容的情况下最好不要使用 String。   



StringBuffer 和 StringBuilder 最大的区别在于，StringBuffer 是线程安全的，而 StringBuilder 是非线程安全的，但 StringBuilder 的性能却高于 StringBuffer，所以在单线程环境下推荐使用 StringBuilder，多线程环境下推荐使用 StringBuffer。
<h3></h3>
<h3></h3>
<h3></h3>
<h3></h3>
<h3></h3>
<h3></h3>
<h3></h3>
<h3></h3>




