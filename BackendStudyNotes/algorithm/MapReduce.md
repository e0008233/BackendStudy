MapReduce is used widely in system design to process large amounts of data. As its name suggests, a MapReduce program requires you to write a Map step and a Reduce step. The rest is handled by the system.
1. The system splits up the data across different machines.  
2. Each machine starts running the user-provided Map program.  
3. The Map program takes some data and emits a <key J value> pair.  
4. The system-provided Shuffle process reorganizes the data so that all <key, value> pairs associated with a given key go to the same machine, to be processed by Reduce.
5. The user-provided Reduce program takes a key and a set of associated values and "reduces" them in some way, emitting a new key and value. The results of this might be fed back into the Reduce program for more reducing.


The typical example of using MapReduce-basically the "Hello World" of MapReduce-is counting the frequency of words within a set of documents.

```
void map (String name, String document): 
    for each word w in document:
        emit(w, 1)
        
void reduce(String word, Iterator partialCounts): 
    int sum =e
    for each count in partialCounts:
        sum += count 
    emit(word, sum)
```

void map (String name, String document): for each word w in document:
emit(w, 1)
void reduce(String word, Iterator partialCounts): int sum =e
for each count in partialCounts:
sum += count emit(word, sum)

![Alt text](images/map_reduce_1.png?raw=true)  

