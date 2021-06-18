package day.day.up.questions.concurrency.number1116;

import java.util.function.IntConsumer;

class ZeroEvenOdd {
        private int n;
        private volatile boolean isZero;
        private volatile boolean isOdd;
        public ZeroEvenOdd(int n) {
                this.n = n;
                isZero = true;
                isOdd =false;
        }

        // printNumber.accept(x) outputs "x", where x is an integer.
        public void zero(IntConsumer printNumber) throws InterruptedException {
                for (int i=0;i<n;i++){
                        synchronized (this){
                                if (!isZero){
                                        this.wait();
                                }
                                printNumber.accept(0);
                                isZero = false;
                                isOdd=!isOdd;
                                this.notifyAll();
                        }
                }
        }

        public void even(IntConsumer printNumber) throws InterruptedException {
                for(int i=2;i<=n;i=i+2){
                        synchronized (this){
                                while (isZero||isOdd){
                                        this.wait();
                                }
                                if (i%2==0) {
                                        printNumber.accept(i);

                                }
                                isZero = true;
                                // isOdd=!isOdd;
                                this.notifyAll();
                        }
                }
        }

        public void odd(IntConsumer printNumber) throws InterruptedException {
                for(int i=1;i<=n;i=i+2){
                        synchronized (this){
                                while (isZero||!isOdd){
                                        this.wait();
                                }
                                if (i%2==1) {
                                        printNumber.accept(i);
                                }
                                isZero = true;
                                // isOdd=!isOdd;
                                this.notifyAll();
                        }
                }
        }
}