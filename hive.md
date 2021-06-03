# Hive之严格模式（strict mode）
 开启严格模式：
 set hive.mapred.mode=strict;
 关闭严格模式：
 set hive.mapred.mode=undefined;
 严格模式严格在哪里
 1. 对分区表的查询必须使用到分区相关的字段
 2. order by必须带limit
 3. 禁止笛卡尔积查询（join必须有on连接条件）
 
 # sort by 和 order by区别
 
 order by：
 order by会对所给的全部数据进行全局排序，并且只会“叫醒”一个reducer干活。它就像一个糊涂蛋一样，不管来多少数据，
 都只启动一个reducer来处理。因此，数据量小还可以，但数据量一旦变大order  by就会变得异常吃力，甚至“罢工”。
 
 sort by：
 sort by是局部排序。相比order  by的懒惰糊涂，sort by正好相反，它不但非常勤快，而且具备分身功能。
 sort by会根据数据量的大小启动一到多个reducer来干活，并且，它会在进入reduce之前为每个reducer都产生一个排序文件。
 这样的好处是提高了全局排序的效率。

distribute by：
distribute by的功能是：distribute by 控制map结果的分发，它会将具有相同字段的map输出分发到一个reduce节点上做处理。
即就是，某种情况下，我们需要控制某个特定行到某个reducer中，这种操作一般是为后续可能发生的聚集操作做准备。
举个例子： select year, memony from table distribute by year sort by year asc, memony desc;
查询结果：  2019 40
          2019 20
          2020 30
          2020 20
          2020 10
          2021 50
          2021 40

Cluster By：
当 Distribute by 和 Sort by 字段相同时，可以使用Cluster by方式。
Cluster by 除了具有 Distribute by 的功能外还兼具 Sort by 的功能。但是只能升序排序，不能指定排序规则为ASC或者DESC。