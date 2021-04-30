https://www.zhihu.com/question/60949531/answer/1810687397

* 数据库调优 database tuning  
    * sql performance tuning
        * add and keep necessary indexes(composite index includes left most single index) and avoid index suppression ( not erqual on indexes, like statement like %123 ), no operation on the index, no tye conversion on the index
        * only querying necessary data(limit, avoid *)
        * run the complicated query in off-peak hours (through scheduler)
        * covering index (index includes queried columns,like composite index containing many columns)
        * link: https://juejin.cn/post/6844904098999828488
            