# 代码说明

- main入口在paper.Driver里面

- 为了让MapReduce程序与其他部分尽可能解耦合，输入输出路径通过命令行参数传入，而不是写死在代码里。
因此请在Run Configurations -> Arguments -> Program Arguments里添加参数，如
```
-conf 
src/main/resources/config/hadoop-local.xml 
src/main/resources/input/bibtex
target/output
```
在Tool.run方法中(Driver.java)使用args[0]和args[1]就可以取出输入和输出参数

> -conf选项用来传入配置文件，不知道会不会用到，反正先写在这里。这个选项会被ToolRunner吃掉，因此不会占用run方法入参的数目

- 如果map-reduce执行有异常，可以在log4j.properties中将调试级别降低以输出更多的log

> FATAL > ERROR > INFO > DEBUG

# 坑

- 有些文件多了换行，导致map会读进来空行，这个要处理

- bibtex-04.txt第97行什么鬼，而且还缺数据，只有156个（数据间空两行）

- bibtex-06.txt缺一条

- bibtex-10.txt与12.txt使用的是windows的\r\n换行符
