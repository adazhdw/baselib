package com.adazhdw.net

import android.os.Handler
import android.os.Looper
import okhttp3.internal.threadFactory
import java.util.concurrent.*

object ExecutorUtils {

    private const val THREAD_COUNT = 3

    val diskIO: Executor = DiskIOThreadExecutor()

    /**
     * FixedThreadPool
     * Executors.newFixedThreadPool(int nThreads, ThreadFactory threadFactory)
     * 这类线程池只有核心线程，数量固定且不会被回收，等待队列的长度没有上限。
     * 这个线程池的特点就是线程数量固定，适用于负载较重的服务器，可以通过这种线程池来限制线程数量；线程不会被回收，也有比较高的响应速度。
     * 但是等待队列没有上限，在任务过多时有可能发生OOM。
     *
     * CacheThreadPool
     * Executors.newCachedThreadPool(ThreadFactory threadFactory)
     * 这类线程池没有核心线程，而且线程数量上限为Integer.MAX_VALUE，可以认为没有上限。等待队列没有长度，每一个任务到来都会分配一个线程来执行。
     * 这类线程池的特点就是每个任务都会被马上执行，在任务数量过大时可能会创建大量的线程导致系统OOM。但是在一些任务数量多但执行时间短的情景下比较适用。
     *
     * SingleThreadExecutor
     * Executors.newSingleThreadExecutor(ThreadFactory threadFactory)
     * 只有一个线程的线程池，等待队列没有上限，每个任务都会按照顺序被执行。适用于对任务执行顺序有严格要求的场景。
     *
     * ScheduledThreadPoolExecutor
     * Executors.newScheduledThreadPool(int corePoolSize, ThreadFactory threadFactory)
     * 和ScheduledThreadPoolExecutor原生默认的构造器差别不大
     *
     * SingleThreadScheduledPoolExecutor
     * Executors.newSingleThreadScheduledExecutor(ThreadFactory threadFactory)
     * 控制线程只有一个。每个任务会按照顺序先后被执行
     *
     * 阻塞队列的类型
     * LinkedBlockingQueue、ArrayBlockingQueue、SychronizeQueue和PriorityBlockingQueue，其次还有比较少用的DelayedWorkQueue
     * 1、LinkedBlockingQueue、ArrayBlockingQueue都是普通的阻塞队列，尾插头出；区别是后者在创建的时候必须指定长度。
     * 2、SychronizeQueue是一个没有容量的队列，每一个插入到这个队列的任务必须马上找到可以执行的线程，如果没有则拒绝执行。
     * 3、PriorityBlockingQueue是具有优先级的阻塞队列，里面的任务会根据设置的优先级进行排序。所以优先级低的任务可能会一直被优先级高的任务顶下去而得不到执行。
     * 4、DelayedWorkQueue则非常像Handler中的MessageQueue了，可以给任务设置延时。
     */
    val networkIO = ThreadPoolExecutor(
        THREAD_COUNT, THREAD_COUNT, 0, TimeUnit.SECONDS,
        LinkedBlockingDeque(), threadFactory("com.adazhdw.net.ExecutorUtils-networkExecutor", false)
    )

    val mainThread = MainThreadExecutor()

    class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }

    class DiskIOThreadExecutor : Executor {

        private val diskIO = Executors.newSingleThreadExecutor()

        override fun execute(command: Runnable) {
            diskIO.execute(command)
        }
    }
}
