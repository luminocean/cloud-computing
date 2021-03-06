# 代码启动说明

Driver启动是需要命令行参数的。

参数格式：
`-conf [CONFIG_FILE_PATH] -D table=[HBASE_TABLE_NAME] [INPUT_PATH * n] [OUTPUT_PATH]`

其中
- `CONFIG_FILE_PATH`是配置文件的路径。在Eclipse中就是相对于项目根目录的路径
- `INPUT_PATH`可以有多个，在代码中可以使用`args[0]`,`args[1]`等来获取。同样在Eclipse中也是相对于项目根目录的路径
- `-D table`属性用于指定要写入的HBase的表名。指定了table以后就不需要指定`OUTPUT_PATH`。
- 如果不指定`-D table`属性，表示使用默认行为，即输出到文本文件里。此时必须指定`OUTPUT_PATH`，且应该放在最后一个

在Eclipse中，参数可以在Run Configurations -> Arguments -> Program Arguments里添加

# HBASE建表
create 'table','apa','bibtex','chicago','mla'
# 坑

- 有些文件多了换行，导致map会读进来空行，这个要处理

- bibtex-04.txt第97行什么鬼，而且还缺数据，只有156个（数据间空两行）

- bibtex-06.txt缺一条

- bibtex-10.txt与12.txt使用的是windows的\r\n换行符

- bibtex-12.txt多了一条数据

- apa-4.txt，apa-10.txt与apa-12.txt使用\r\n换行符

- apa-4.txt和chicago-4.txt使用的火(utf)星(16)编码

- chicago和mla引用使用的是全角双引号

- 存在文献重复的现象，一种是因为同一个关键字下，网站给出的文献存在重复，另一种因为文献是跨领域的，在不同组的文本中同时存在

- chicago-4和mla-4的转成utf-8之后仍然有乱码，会导致解析结果错误
