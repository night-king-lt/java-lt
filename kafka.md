# 安装zookeeper
  运行 zkserver.cmd
# 安装kafka

运行命令：
cd E:\soft\kafka_2.12-2.6.0\
bin\windows\kafka-server-start.bat config\server.properties

 #创建 topic
 cd E:\soft\kafka_2.12-2.6.0\bin\windows
 kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic flink—test
 
 # 打开一个producer
 cd E:\soft\kafka_2.12-2.6.0\bin\windows
 kafka-console-producer.bat --broker-list localhost:9092 --topic test
 
 # 打开一个consumer
  cd E:\soft\kafka_2.12-2.6.0\bin\windows
 
 kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic test --from-beginning
