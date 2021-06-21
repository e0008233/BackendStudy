package day.day.up.questions.concurrency.number1195;

import java.util.function.IntConsumer;

class FizzBuzz {

        private int n;
        private int currentNumber = 1;
        private final Object mutex = new Object();

        public FizzBuzz(int n) {
                this.n = n;
        }

        // printFizz.run() outputs "fizz".
        public void fizz(Runnable printFizz) throws InterruptedException {
                synchronized (mutex) {
                        while (currentNumber <= n) {
                                if (currentNumber % 3 != 0 || currentNumber % 5 == 0) {
                                        mutex.wait();
                                        continue;
                                }
                                printFizz.run();
                                currentNumber += 1;
                                mutex.notifyAll();
                        }
                }
        }

        // printBuzz.run() outputs "buzz".
        public void buzz(Runnable printBuzz) throws InterruptedException {
                synchronized (mutex) {
                        while (currentNumber <= n) {
                                if (currentNumber % 5 != 0 || currentNumber % 3 == 0) {
                                        mutex.wait();
                                        continue;
                                }
                                printBuzz.run();
                                currentNumber += 1;
                                mutex.notifyAll();
                        }
                }
        }

        // printFizzBuzz.run() outputs "fizzbuzz".
        public void fizzbuzz(Runnable printFizzBuzz) throws InterruptedException {
                synchronized (mutex) {
                        while (currentNumber <= n) {
                                if (currentNumber % 15 != 0) {
                                        mutex.wait();
                                        continue;
                                }
                                printFizzBuzz.run();
                                currentNumber += 1;
                                mutex.notifyAll();
                        }
                }
        }

        // printNumber.accept(x) outputs "x", where x is an integer.
        public void number(IntConsumer printNumber) throws InterruptedException {
                synchronized (mutex) {
                        while (currentNumber <= n) {
                                if (currentNumber % 3 == 0 || currentNumber % 5 == 0) {
                                        mutex.wait();
                                        continue;
                                }
                                printNumber.accept(currentNumber);
                                currentNumber += 1;
                                mutex.notifyAll();
                        }
                }
        }
}