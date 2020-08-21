# 编译pb
protoc  --proto_path=src/main/java/proto/message --java_out=src/main/java/ src/main/java/proto/message/*.proto