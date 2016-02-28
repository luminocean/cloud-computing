本次作业为文献搜索

# 准备工作

首先启动hbase，建立两张表
```
bin/start-hbase.sh

# hbase shell中

create 'cite', 'src' # 表名, 列族名
create 'index', 'title'
```

设计的两张表分别为cite和index

cite用于记录文献文本，row key就是文献的title，只有一个列族src，src里面也只有一个列content。
cite记录的是bibtex格式的文献原文。作用是根据检索出来的title来获取其原始文本。

index用于存储倒排索引，row key是从输入文件里拆分出来的一个个英文单词。也只有一个列族title。
这里的一个设计是，**直接把这个单词对应的文献标题作为列名**。
如果一个单词有几万条关联文献，那么这个单词行就有几万个列，形如`title:hello world`, `title:a quick brown fox jumps over a lazy dog`等等

每个列的值留空就行了。

> 解释一下，现在的需求是想在某一个词语的行里存下所有与之相关的文献列表。原来的想法是使用一个counter，每次添加就递增counter作为列名，按照title:1, title:2这样插入值。
但是可能会存在多个reducer，而且一个reducer的reduce方法可能会被调用好几次，这样的话写一个不会冲突的counter比较困难。
因此干脆直接把title作为列名，连重复的问题都解决了。

# 建立索引

直接执行`core.Driver`即可使用map reduce来读取输入文本，解析bibtex后把原文和倒排索引插入cite和index表中。
