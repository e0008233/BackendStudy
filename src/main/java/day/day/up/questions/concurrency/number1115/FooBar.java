package day.day.up.questions.concurrency.number1115;

import java.util.concurrent.CountDownLatch;

class FooBar {
        private int n;
        private volatile boolean flag;
        public FooBar(int n) {
                this.n = n;
                flag = true;
        }

        public void foo(Runnable printFoo) throws InterruptedException {

                for (int i = 0; i < n; i++) {
                        synchronized (this){
                                if (!flag){
                                     this.wait();
                                }
                                // printFoo.run() outputs "foo". Do not change or remove this line.
                                printFoo.run();
                                flag=false;
                                this.notifyAll();

                        }

                }
        }

        public void bar(Runnable printBar) throws InterruptedException {

                for (int i = 0; i < n; i++) {
                        synchronized (this) {
                                if (flag){
                                        this.wait();
                                }
                                // printBar.run() outputs "bar". Do not change or remove this line.
                                printBar.run();
                                flag = true;
                                this.notifyAll();
                        }
                }
        }
}