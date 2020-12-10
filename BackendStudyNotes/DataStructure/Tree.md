二叉树: 每个节点最多有两个叶子节点.

完全二叉树: 叶节点只能出现在最下层和次下层，并且最下面一层的结点都集中在该层最左边的若干位置的二叉树.

平衡二叉树: l左右两个子树的高度差的绝对值不超过1，并且左右两个子树都是一棵平衡二叉树。

红黑树： 是一个平衡的二叉树，但不是一个完美的平衡二叉树。虽然我们希望一个所有查找都能在~lgN次比较内结束，但是这样在动态插入中保持树的完美平衡代价太高，所以，我们稍微放松逛一下限制，希望找到一个能在对数时间内完成查找的数据结构。这个时候，红黑树站了出来。
阅读以下需要了解普通二叉树的插入以及删除操作。
红黑树是在普通二叉树上，对没个节点添加一个颜色属性形成的，同时整个红黑二叉树需要同时满足一下五条性质
红黑树需要满足的五条性质：  

性质一：节点是红色或者是黑色；在树里面的节点不是红色的就是黑色的。 
性质二：根节点是黑色；  
根节点总是黑色的。它不能为红。  
性质三：每个叶节点（NIL或空节点）是黑色；   
性质四：每个红色节点的两个子节点都是黑色的（也就是说不存在两个连续的红色节点）；
就是连续的两个节点不能是连续的红色，连续的两个节点的意思就是父节点与子节点不能是连续的红色。  
性质五：从任一节点到其没个叶节点的所有路径都包含相同数目的黑色节点  
![Alt text](images/Tree_blackRedTree.png?raw=true "methods")  

Left rotation:   
![Alt text](images/Tree_leftRotation.gif?raw=true "methods")  

Right rotation:   
![Alt text](images/Tree_rightRotation.gif?raw=true "methods") 


Red–black trees offer worst-case guarantees for insertion time, deletion time, and search time.
![Alt text](images/Tree_blackRedTree_efficiency.png?raw=true "methods") 

Link: https://blog.csdn.net/sun_tttt/article/details/65445754
