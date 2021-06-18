package day.day.up.questions.concurrency.number1114;

import java.util.concurrent.CountDownLatch;

class Foo {
        CountDownLatch firstOne;
        CountDownLatch secondOne;
        public Foo() {
                firstOne = new CountDownLatch(1);
                secondOne = new CountDownLatch(1);
        }

        public void first(Runnable printFirst) throws InterruptedException {

                // printFirst.run() outputs "first". Do not change or remove this line.
                printFirst.run();
                firstOne.countDown();
        }

        public void second(Runnable printSecond) throws InterruptedException {
                firstOne.await();
                // printSecond.run() outputs "second". Do not change or remove this line.
                printSecond.run();
                secondOne.countDown();
        }

        public void third(Runnable printThird) throws InterruptedException {
                secondOne.await();
                // printThird.run() outputs "third". Do not change or remove this line.
                printThird.run();
        }
}