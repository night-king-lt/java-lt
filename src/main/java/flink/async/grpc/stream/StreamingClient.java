package flink.async.grpc.stream;

import flink.async.AsyncIODemo;
import flink.async.SampleAsyncFunction;
import flink.async.grpc.protojava.GreeterGrpc;
import flink.async.grpc.protojava.HelloRequest;
import io.grpc.ManagedChannel;
import io.grpc.netty.NettyChannelBuilder;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.async.AsyncFunction;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Author liu.teng
 * @Date 2020/11/5 11:29
 * @Version 1.0
 */
public class StreamingClient {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(30000);
        env.setParallelism(1);
        final int taskNum = 1;
        final long timeout = 40000;

        DataStream<byte[]> inputStream = env.addSource(new HelloWorldSource());
        AsyncFunction<byte[], Boolean> function = new HelloWorldAsyncFunction();

        DataStream<Boolean> result = AsyncDataStream.unorderedWait(
                inputStream,
                function,
                timeout,
                TimeUnit.MILLISECONDS,
                10).setParallelism(taskNum);

        result.print();

        env.execute("Async IO Demo");
    }

    static class HelloWorldSource implements SourceFunction<byte[]>{

        @Override
        public void run(SourceContext<byte[]> sourceContext) throws Exception {
            String[] names = {"flink", "hadoop", "spark"};
            while (true){
                String name = names[(int) (Math.random() * 2)];
                HelloRequest request = HelloRequest.newBuilder().setName(name).build();
                sourceContext.collect(request.toByteArray());
            }
        }

        @Override
        public void cancel() {

        }
    }

    static class HelloWorldAsyncFunction extends RichAsyncFunction<byte[], Boolean>{

        public ManagedChannel channel;
        public GreeterGrpc.GreeterStub grpcServer;
        private static final String[] ipArray = {"127.0.0.1:50051"};

        @Override
        public void open(Configuration parameters) throws Exception {
            int index = (int) (Math.random() * ipArray.length);
            System.out.println("ip adress: " + ipArray[index]);
            channel = NettyChannelBuilder.forTarget(ipArray[index])
                    .maxInboundMessageSize(100 * 1024 * 1024)
                    .usePlaintext()
                    .build();
            grpcServer = GreeterGrpc.newStub(channel);
        }

        @Override
        public void asyncInvoke(byte[] bytes, ResultFuture<Boolean> resultFuture) throws Exception {

        }
    }
}
