***
![Alt text](system_design.png?raw=true "Hashtable")  
***
- CDN (Content delivery networks): like cache, for faster static content fetching
- Memory cache for popular product
- Load balancer and multiple web servers
- 读写分离，写：异步，读：多台机器 (读多写少的优化)
- MapReducer (Hadoop, Hive)