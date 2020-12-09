# 编译pb
protoc  --proto_path=src/main/java/proto/message --java_out=src/main/java/ src/main/java/proto/message/*.proto


# 编译 proto rpc
protoc --plugin=protoc-gen-grpc-java=protoc-gen-grpc-java-1.6.1-windows-x86_64.exe --grpc-java_out=src/main/java/ --proto_path=src/main/java/flink/async/grpc/proto/ src/main/java/flink/async/grpc/proto/helloworld.proto
protoc --proto_path=src/main/java/flink/async/grpc/proto/ --java_out=src/main/java/ src/main/java/flink/async/grpc/proto/helloworld.proto