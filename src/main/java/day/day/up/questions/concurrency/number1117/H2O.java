package day.day.up.questions.concurrency.number1117;

import java.util.function.IntConsumer;

class H2O {

        private volatile int hCount;
        private volatile int oCount;

        public H2O() {
                hCount=0;
                oCount=0;

        }

        public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
                synchronized (this) {
                        while (!(hCount<=2*oCount)){
                            this.wait();
                        }
                        // releaseHydrogen.run() outputs "H". Do not change or remove this line.
                        releaseHydrogen.run();
                        hCount++;
                        this.notifyAll();

                }
        }

        public void oxygen(Runnable releaseOxygen) throws InterruptedException {
                synchronized (this) {
                        while (!(hCount>2*oCount)){
                                this.wait();
                        }
                        // releaseOxygen.run() outputs "O". Do not change or remove this line.
                        releaseOxygen.run();
                        oCount++;
                        this.notifyAll();
                }
        }
}