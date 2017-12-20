package nia.chapter7;

import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 永远不要将一个长时间运行的任务放入到执行队列中，因为它会阻塞同一线程上的其他任务
 * 阻塞调用。
 * <p>
 * Listing 7.2 Scheduling a task with a ScheduledExecutorService
 * <p>
 * Listing 7.3 Scheduling a task with EventLoop
 * <p>
 * Listing 7.4 Scheduling a recurring task with EventLoop
 * <p>
 * Listing 7.5 Canceling a task using ScheduledFuture
 */
public class ScheduleExamples {
    private static final Channel CHANNEL_FROM_SOMEWHERE = new NioSocketChannel();

    /**
     * ScheduledExecutorService 实现具有局限性，在大量任务被紧凑的调用时，将会是一个瓶颈
     * <p>
     * Listing 7.2 Scheduling a task with a ScheduledExecutorService
     */
    public static void schedule() {
        // 创建一个10个线程的线程池
        ScheduledExecutorService executor =
                Executors.newScheduledThreadPool(10);

        // 创建一个Runnable,以供调度稍后执行
        ScheduledFuture<?> future = executor.schedule(
                new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Now it is 60 seconds later");
                    }
                }, 60, TimeUnit.SECONDS); // 延迟60秒执行
        //...
        executor.shutdown(); // 关闭连接池，释放资源
    }

    /**
     * EventLoop 方式
     * Listing 7.3 Scheduling a task with EventLoop
     */
    public static void scheduleViaEventLoop() {
        Channel ch = CHANNEL_FROM_SOMEWHERE; // get reference from somewhere

        // 创建一个Runnable,以供调度稍后执行
        ScheduledFuture<?> future = ch.eventLoop().schedule(
                new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("60 seconds later");
                    }
                }, 60, TimeUnit.SECONDS); // 60秒之后执行
    }

    /**
     * EventLoop 方式
     * Listing 7.4 Scheduling a recurring task with EventLoop
     */
    public static void scheduleFixedViaEventLoop() {
        Channel ch = CHANNEL_FROM_SOMEWHERE; // get reference from somewhere
        ScheduledFuture<?> future = ch.eventLoop().scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Run every 60 seconds");
                    }
                }, 60, 60, TimeUnit.SECONDS); // 60秒之后执行，并且以后每隔60秒运行
    }

    /**
     * Listing 7.5 Canceling a task using ScheduledFuture
     */
    public static void cancelingTaskUsingScheduledFuture() {
        Channel ch = CHANNEL_FROM_SOMEWHERE; // get reference from somewhere
        ScheduledFuture<?> future = ch.eventLoop().scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Run every 60 seconds");
                    }
                }, 60, 60, TimeUnit.SECONDS);
        // Some other code that runs...
        boolean mayInterruptIfRunning = false;
        future.cancel(mayInterruptIfRunning); // 取消该任务，防止它再次运行
    }
}
