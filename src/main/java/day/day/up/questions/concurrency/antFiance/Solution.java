package day.day.up.questions.concurrency.antFiance;

import lombok.SneakyThrows;

public class Solution{
    private volatile boolean isOneTurn;
    Object lock;
    public Solution(){
        isOneTurn = true;
        lock = new Object();
    }

    public void test(){
//        MyThread1 thread1 = new MyThread1("Thread1");
        Thread t1 = new Thread(new MyThread1("Thread1"));

//        MyThread2 thread2 = new MyThread2("Thread2");
        Thread t2 = new Thread(new MyThread2("Thread2"));

        t1.start();
        t2.start();
    }

    class MyThread1 implements Runnable {
        public String name;

        public MyThread1(String name) {
            this.name = name;
        }
        @SneakyThrows
        @Override
        public void run() {
            for (int i = 1; i <= 100; i++) {
                synchronized (lock) {
                    if (!isOneTurn){
                        lock.wait();
                    }

//                    Thread.sleep(10000);
                    System.out.println(name + "-" + i);
                    isOneTurn = false;
                    lock.notifyAll();
                }
            }
        }
    }


    class MyThread2 implements Runnable {
        public String name;

        public MyThread2(String name) {
            this.name = name;
        }
        @SneakyThrows
        @Override
        public void run() {
            for (int i = 1; i <= 100; i++) {
                synchronized (lock) {
                    if (isOneTurn){
                        lock.wait();
                    }


                    System.out.println(name + "-" + i);
                    isOneTurn = true;
                    lock.notifyAll();

                }
            }
        }
    }
}