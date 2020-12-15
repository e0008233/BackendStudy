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


2-3 tree:  

Youtube link: https://www.youtube.com/c/DavidTaylorSJSU/videos  

a 2–3 tree is a tree data structure, where every node with children (internal node) has either two children (2-node) and one data element or three children (3-nodes) and two data elements. A 2-3 tree is a B-tree of order 3. Nodes on the outside of the tree (leaf nodes) have no children and one or two data elements.  

We say that an internal node is a 2-node if it has one data element and two children.  
![Alt text](images/2node.png?raw=true "methods")  
 
We say that an internal node is a 3-node if it has two data elements and three children.   
![Alt text](images/3node.png?raw=true "methods")  

We say that T is a 2–3 tree if and only if one of the following statements hold:[5]

- T is empty. In other words, T does not have any nodes.
- T is a 2-node with data element a. If T has left child p and right child q, then  
  - p and q are non-empty 2–3 trees of the same height;
  - a is greater than each element in p; and  
  - a is less than or equal to each data element in q.  
- T is a 3-node with data elements a and b, where a < b. If T has left child p, middle child q, and right child r, then  
  - p, q, and r are non-empty 2–3 trees of equal height;  
  - a is greater than each data element in p and less than or equal to each data element in q; and  
  - b is greater than each data element in q and less than or equal to each data element in r.  

2-3 tree keeps a fairly simple balancing mechanism  
O(lg n) performance

***
- Degree represents the lower bound on the number of children a node in the B Tree can have (except for the root). i.e the minimum number of children possible. 
- Whereas the Order represents the upper bound on the number of children. ie. the maximum number possible.

***
Disks are slow, with two performance measures:
- Latency (seek time): how long to get your first bit of data
- Throughput (transfer rate): how quick once started

Link: https://blog.csdn.net/sun_tttt/article/details/65445754

***
B-Tree:  
B-Tree is known as a self-balancing tree as its nodes are sorted in the inorder traversal. In B-tree, a node can have more than two children. B-tree has a height of logM N (Where ‘M’ is the order of tree and N is the number of nodes). And the height is adjusted automatically at each update. In the B-tree data is sorted in a specific order, with the lowest value on the left and the highest value on the right. To insert the data or key in B-tree is more complicated than a binary tree.
There are some conditions that must be hold by the B-Tree:  
- All the leaf nodes of the B-tree must be at the same level.
- Above the leaf nodes of the B-tree, there should be no empty sub-trees.
- B- tree’s height should lie as low as possible.  
![Alt text](images/b_tree.png?raw=true "methods")  

B+ Tree:  
B+ tree eliminates the drawback B-tree used for indexing by <b>storing data pointers only at the leaf nodes of the tree</b>. Thus, the structure of leaf nodes of a B+ tree is quite different from the structure of internal nodes of the B tree. It may be noted here that, since data pointers are present only at the leaf nodes, the leaf nodes must necessarily store all the key values along with their corresponding data pointers to the disk file block, in order to access them. Moreover, the leaf nodes are linked to providing ordered access to the records. The leaf nodes, therefore form the first level of the index, with the internal nodes forming the other levels of a multilevel index. Some of the key values of the leaf nodes also appear in the internal nodes, to simply act as a medium to control the searching of a record.
![Alt text](images/bplus.png?raw=true "methods") 

***
LSM 树:  
LSM（Log-Structured Merge-Trees）和 B+ 树相比，是牺牲了部分读的性能来换取写的性能(通过批量写入)，实现读写之间的平衡。 Hbase、LevelDB、Tair（Long DB）、nessDB 采用 LSM 树的结构。LSM可以快速建立索引
- B+ 树读性能好，但由于需要有序结构，当key比较分散时，磁盘寻道频繁，造成写性能较差。
- LSM 是将一个大树拆分成N棵小树，先写到内存（无寻道问题，性能高），在内存中构建一颗有序小树（有序树），随着小树越来越大，内存的小树会flush到磁盘上。当读时，由于不知道数据在哪棵小树上，因此必须遍历（二分查找）所有的小树，但在每颗小树内部数据是有序的。