Depth First Search (DFS):  
- Using stack  
- Adding the node, its one child, its one child's child, pop it out if the last one in stack has no more children
- https://www.youtube.com/watch?v=iaBEKo5sM7w


![Alt text](images/tree12.gif?raw=true "complexity")  
(a) Inorder (Left, Root, Right) : 4 2 5 1 3
(b) Preorder (Root, Left, Right) : 1 2 4 5 3
(c) Postorder (Left, Right, Root) : 4 5 2 3 1


Breadth First Search (BFS):  
- Using Queue 
- Enqueue the node, and all its children, dequeue the first one. Enqueue all the children of the current first one, dequeue the first one again...
- https://www.youtube.com/watch?v=QRq6p9s8NVg  
