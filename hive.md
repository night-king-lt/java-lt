# Hive之严格模式（strict mode）

#开启严格模式：
 set hive.mapred.mode=strict;
#关闭严格模式：
 set hive.mapred.mode=undefined;
 
# 严格模式严格在哪里
 1. 对分区表的查询必须使用到分区相关的字段
 2. order by必须带limit
 3. 禁止笛卡尔积查询（join必须有on连接条件）