package day.day.up.questions.algorithms.data_structure.number146;

import java.util.LinkedHashMap;
import java.util.Map;


public class LRUCache {

    private MyLinkedHashMap<Integer,Integer> myCaChe;

    public LRUCache(int capacity) {

        myCaChe = new MyLinkedHashMap<>(capacity, .75f, true);
    }

    public int get(int key) {
        if (myCaChe.containsKey(key)) return myCaChe.get(key);
        else return -1;

    }

    public void put(int key, int value) {
        myCaChe.put(key,value);
    }


    class MyLinkedHashMap<K, V> extends LinkedHashMap<K, V> {

        private int MAX_ENTRIES ;

        public MyLinkedHashMap(
                int initialCapacity, float loadFactor, boolean accessOrder) {
            super(initialCapacity, loadFactor, accessOrder);
            this.MAX_ENTRIES = initialCapacity;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return size() > MAX_ENTRIES;
        }

    }
}
