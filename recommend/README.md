# 准备工作

###首先启动hbase，建立一张表

```
bin/start-hbase.sh
bin/habase shell
```

hbase shell中

```
# 表名, 列族名
create 'cite', 'paper' 
create 'feedback', 'like', 'dislike'
```

###准备数据
运行src/main/java/mapred/Driver，argument为src/main/resources/input