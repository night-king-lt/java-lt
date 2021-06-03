package async;

import org.apache.commons.lang.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class CompletableFutureTest {
    public static void main(String[] args) throws Exception {
        handleList();
    }

    /**
     *  没有指定Executor的方法会使用ForkJoinPool.commonPool() 作为它的线程池执行异步代码。
     *  使用共享线程池将会有个弊端，一旦有任务被阻塞，将会造成其他任务没机会执行。
     *
     *  如果指定线程池，则使用指定的线程池运行。以下所有的方法都类同。
     *
     * runAsync方法不支持返回值。
     * supplyAsync可以支持返回值。
     */
    public static void runAsync() throws Exception {
        ExecutorService pool = Executors.newCachedThreadPool();
        // 无返回值
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
            }
        }, pool);
        future.whenComplete((t, action) -> {
            System.out.println(Thread.currentThread().getName() + " run end ...");
            System.out.println("执行完成: " + t);
            pool.shutdown();
        });

        // 有返回值
        CompletableFuture<Long> future2 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
            }
            return System.currentTimeMillis();
        }, pool);

        future2.whenComplete((t, action) -> {
            System.out.println(Thread.currentThread().getName() + " run end ...");
            System.out.println("执行完成: " + t);
            pool.shutdown();
        });
        // main线程，阻塞等待 future2 线程的结果
        System.out.println("future2 result: " + future2.get());
        System.out.println(Thread.currentThread().getName() + " run end ...");
    }

    public static void callback() throws Exception {
        // 创建一个缓存线程池
        ExecutorService pool = Executors.newCachedThreadPool();
        // 有返回值
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
            }
            return "cjx";
        }, pool);

        CompletableFuture<String> result = future.thenApply(name -> {
            String r = "hello " + name;
            System.out.println(r);
            pool.shutdown();
            return r;
        });

        System.out.println(Thread.currentThread().getName() + " run end ...");
    }

    /**
     *  当CompletableFuture的计算结果完成，或者抛出异常的时候，可以执行特定的Action
     *
     * whenComplete 和 whenCompleteAsync 的区别：
     *  whenComplete：是执行当前任务的线程执行继续执行 whenComplete 的任务。
     *  whenCompleteAsync：是执行把 whenCompleteAsync 这个任务继续提交给线程池来进行执行。
     *
     * @throws Exception
     */
    public static void whenComplete() throws Exception {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
            }
            if(new Random().nextInt()%2>=0) {
                int i = 12/0;
            }
            System.out.println("run end ...");
        });

        // 计算结果完成时 回调方法
        future.whenComplete((t, action) -> System.out.println("执行完成！"))
            // 抛出异常时 回调方法
            .exceptionally(t -> {
                System.out.println("执行失败！"+ t.getMessage());
                return null;
            });

        TimeUnit.SECONDS.sleep(2);
    }

    /**
     *  当一个线程依赖另一个线程时，可以使用 thenApply 方法来把这两个线程串行化。
     *  @throws Exception
     */
    public static void thenApply() throws Exception {
        CompletableFuture<Long> future = CompletableFuture.supplyAsync(() -> {
            long result = new Random().nextInt(100);
            System.out.println("result1 = " + result);
            return result;
        }).thenApply(t -> {
            long result = t * 5;
            System.out.println("result2 = " + result);
            return result;
        });
        System.out.println(future.get());
    }

    /**
     *   handle 是执行任务完成时对结果的处理。
     *    handle 方法和 thenApply 方法处理方式基本一样。不同的是 handle 是在任务完成后再执行，还可以处理异常的任务。
     *    thenApply 只可以执行正常的任务，任务出现异常则不执行 thenApply 方法。
     *
     * @throws Exception
     */
    public static void handle() throws Exception {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            int i= 10/0;
            return new Random().nextInt(10);
        }).thenApply(t -> {
            int result = t * 5;
            System.out.println("result2 = " + result);
            return result;
        }).handle((param, throwable) -> {
            int result = -1;
            if(throwable == null){
                result = param * 2;
            }else{
                System.out.println(throwable.getMessage());
            }
            return result;
        });
        System.out.println(future.get());
    }

    /**
     *  join 多个线程的返回结果，整体耗时以最长的来定
     * @throws Exception
     */
    public static void handleList() throws Exception{
        // 创建一个缓存线程池
        ExecutorService pool = Executors.newCachedThreadPool();
        long start = System.currentTimeMillis();
        CompletableFuture<String> result = CompletableFuture.supplyAsync(() -> RandomStringUtils.randomAlphabetic(1));
        for (int i = 0; i < 20; i++){
            int finalI = i;
            result = result.thenCombineAsync(CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(finalI * 1000);
                    System.out.println("线程：" + Thread.currentThread().getName() + " sleep: " + finalI + "s");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return RandomStringUtils.randomAlphabetic(1);
            }, pool), (r1, r2) -> r1 + " = " + r2);
        }
        System.out.println("result: " + result.get(200, TimeUnit.SECONDS));
        // 整体请求时间以最长的来定
        System.out.println("senconds:" + (System.currentTimeMillis() - start)/1000);

    }

    public static void thenCombineAsync() throws Exception{
        long start = System.currentTimeMillis();
        CompletableFuture<String> result = CompletableFuture.supplyAsync(() -> {
            try {
                // 这里模拟微服务A的查询接口
                System.out.println("A======" + Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return RandomStringUtils.randomAlphabetic(10);
        }).thenCombineAsync(
                CompletableFuture.supplyAsync(() -> {
                    try {
                        // 这里模拟微服务B的查询接口
                        System.out.println("completablefutureB======" + Thread.currentThread().getName());
                        TimeUnit.SECONDS.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return RandomStringUtils.randomAlphabetic(20);
                })
                , (r1, r2) -> {
                    // 合并两个查询结果
                    return r1 + " = " + r2;
                });


        System.out.println("main======" + Thread.currentThread().getName());
        System.out.println(result.get());
        // 整体请求时间以最长的来定
        System.out.println("senconds:" + (System.currentTimeMillis() - start));
    }
}
