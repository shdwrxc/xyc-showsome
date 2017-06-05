package org.xyc.showsome.pecan.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Semaphore {

    private int limit;
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public Semaphore(int limit) {
        lock.lock();
        try {
            this.limit = limit;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void acquire() throws Exception{
        lock.lock();
        try {
            while (limit <= 0) {
                condition.await();
            }
            --limit;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void release() throws Exception {
        lock.lock();
        try {
            ++limit;
            condition.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }
}
