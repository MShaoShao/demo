package com.example.demo.javatrain.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 小苗学java5-java锁的训练与学习
 * 当前最新的java锁分为以下几种：
 * 1、乐观锁和悲观锁 区别：线程需不需要锁住同步资源   悲观锁：需要 乐观锁：不需要
 * 2、自旋锁和适应性自旋锁  都属于在锁住同步资源失败后，线程不堵塞的锁
 * 3、无锁、偏向锁(BiasedLock)、轻量级锁和重量级锁（锁状态）在多个线程竞争同步资源的区别：
 *    3.1 无锁：不锁住资源，多个线程中只有一个能修改资源成功，其他线程会重试
 *    3.2 偏向锁：同一个线程执行同步资源时自动获取资源
 *    3.3 轻量级锁：多个线程竞争同步资源时，没有获取资源的线程自旋等待锁释放
 *    3.4 重量级锁：多个线程竞争同步资源时，没有获取资源的线程阻塞等待唤醒
 * 4、公平锁和非公平锁   区别：
 *    4.1 公平锁在多个线程竞争锁时需要排队
 *    4.2 非公平锁则在多个线程竞争锁时，先尝试插队，插队失败在排队
 * 5、可重入锁和非可重入锁  区别：
 *    5.1 可重入锁允许一个线程的多个流程获取同一把锁
 *    5.2 非可重入锁不允许一个线程的多个流程获取同一把锁
 * 6、共享锁和排他锁：
 *    6.1 共享锁：允许多个线程共享一把锁
 *    6.2 排他锁：不允许多个线程共享一把锁
 *
 * @author MiaoShaoDong
 * @date 14:30 2020/6/24
 */
@Slf4j
public class LockTrain {
    /**
     * -----------------------------悲观锁调用方式------------------
     */
    public synchronized void testMethod() {
        //synchronized关键字 操作同步资源
    }

    /**
     * ReentrantLock 需保证多个线程使用同一个锁
     */
    private ReentrantLock lock = new ReentrantLock();
    public void modifyPublicResource(){
        lock.lock();
        try {
            //同步资源操作
        }catch (Exception e){
            e.getStackTrace();
        }finally {
            lock.unlock();
        }
    }

    /**
     * -----------------------------乐观锁调用方式------------------
     * 需保证多个线程使用同一个AtomicInteger
     * 主要实现算法CAS(比较与交换)
     */
    private void happyLock(){
        AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.incrementAndGet();
    }
}

