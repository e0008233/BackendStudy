Redis: 
- Use redis for in-memory storage
- Adding cache for frequently used methods to reduce redis hit
- Combining multiple redis write methods into one, such as message count for hosts (im)
- Pipeline for multiple redis operation

Mysql:
- Indexing
- De-normalization: adding redundant information into a database to speed up the reads. Save time from joining 2 tables
- Or no-sql

Database partitioning (sharding)

Asynchronous processing
- Slow operation should ideally be done asynchronously. Otherwise, a user night get stuck waiting for results.  
