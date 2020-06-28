package async;

import java.util.concurrent.*;

public class CompletableFutureTest {
    public static void main(String[] args) throws Exception {
       test1();
    }

    public static void test1() throws Exception{
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        for (int i = 0; i < 4; i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep((long)(Math.random()*10000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    completableFuture.complete("h");
                }
            }).start();
        }

        System.out.println(completableFuture.get(10, TimeUnit.SECONDS));
    }

    public static void test2() throws Exception{
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                System.out.println("executorService 是否为守护线程 :" + Thread.currentThread().isDaemon());
                return null;
            }
        });

        final CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("this is lambda supplyAsync");
            System.out.println("supplyAsync 是否为守护线程 " + Thread.currentThread().isDaemon());
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("this lambda is executed by forkJoinPool");
            return "result1";
        });
        final CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("this is task with executor");
            System.out.println("supplyAsync 使用executorService 时是否为守护线程 : " + Thread.currentThread().isDaemon());
            return "result2";
        }, executorService);
        System.out.println(completableFuture.get());
        System.out.println(future.get());
        executorService.shutdown();

    }
}
