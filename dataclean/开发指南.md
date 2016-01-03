本文档主要用于说明开发与部署hadoop程序的正确姿势。

# 开发Map-Reduce程序

### 1. hadoop-client

首先说明一点，如果要开发map-reduce程序，完成对指定数据集的分析并得到reduce结果，**不需要安装hadoop**

这是因为，hadoop本身分为多个模块：
- map-reduce模块，负责执行map和reduce任务
- hdfs
- yarn

在开发阶段，完全可以不使用hdfs（使用本地的磁盘系统），也不使用yarn（使用默认的简单任务调度实现）。
而map-reduce模块以及相关的任务提交代码都包含在叫做hadoop-client的jar包里（可以使用maven下载）

也就是说，**只要在项目中使用maven配置好对hadoop-client的依赖，你就足以执行map-reduce任务了**。
执行效果应该完全等同于安装hadoop后执行单机模式。

> 当然，如果不用maven下载的hadoop-client包，你也可以直接跑到hadoop根目录下的share/hadoop/mapreduce把里面的jar包加到项目依赖里，也是可以的

### 2. ToolRunner

那么具体该如何执行map-reduce任务？可以这么干：
1. 写好map和reduce代码
2. 写一个main方法
3. 在main方法里创建一个job，设置mapper,reducer,输入输出路径等参数（很多资料都会提到，此处按下不表）
4. 执行该java程序

这样做显然是可以的，但是存在着一个问题：如何制定配置信息？
这里的配置信息主要指的是：任务使用什么文件系统？任务使用什么调度系统以及它的端口是什么？等等。这些都是与具体任务相关的配置信息

> 与任务无关的配置信息比如：hdfs各个块备份几分（hdfs-site.xml），集群中有哪些机器（slaves）
> 这些都是启动hdfs或者yarn时需要的配置信息，与具体任务无关

最合理的做法就是把这些信息放在一个配置文件里面，然后读取这些配置信息传给job。但是解析配置文件是一件很麻烦的事情，不想做。所以可以用ToolRunner。

*以下请参考paper.Driver类*

用ToolRunner来接收main的入参（也就是命令行参数），ToolRunner就会去解析命令行参数中对它有意义的参数，包括读取并解析配置文件，然后再调用我们实际的run方法。run()中调用getConf()就可以获取到配置文件中的信息，不用看直接传给job就可以了。

使用ToolRunner的另一个作用是，命令行的配置参数会被ToolRunner吃掉，比如有命令行参数：
`-conf local.xml input output`
这里实际上有四个字符串，ToolRunner会解析到`-conf`这个字符串，判断`local.xml`就是配置文件的路径，于是把这两个参数全部吃掉。这样在run()方法中得到的入参就只剩下`input output`了，使用args[0]和args[1]就可以取出来，方便很多

总而言之，**使用ToolRunner来启动我们的map-reduce程序，就可以很方便的指定配置文件了**

### 3. 开发流程

总结一下我们（在eclipse中）的开发流程：（参考paper.Driver类）
0. 准备好项目依赖(就是hadoop-client)
1. 编写map和reduce代码
2. 准备好配置文件（可以参考config/local.xml和localhost.xml）
3. 在main方法中使用ToolRunner驱动，并在run()中写好剩下的代码
4. 设置启动程序时的命令行参数，包括配置文件位置，输入目录，输出目录
5. 启动执行程序（如果输出目录里有内容要先删除）
6. 在输出目录中得到输出结果

命令行参数可以在Run Configurations -> Arguments -> Program Arguments里添加，如
```
-conf src/main/resources/config/hadoop-local.xml    # 配置文件
src/main/resources/input/bibtex                     # bibtex的输入目录
target/output                                       # 输出目录
```
然后在Tool.run方法中使用args[0]和args[1]就可以取出输入和输出参数，并传入job中。


# 部署分布式Map-Reduce程序

之前说的是如何开发一个map-reduce程序，这一节将说明如何将开发好的map-reduce程序部署到分布式/伪分布式集群上。在集群上启用hdfs和yarn服务。

### 1. 代码打包

和开发环境的不同之处在于，部署环境是有多台机器的。因此这里就涉及到代码在集群中各台机器上的分发。Hadoop使用jar包的形式来分发代码。因此首先要将我们的代码打包。在项目根目录下使用`mvn package`即可把项目打包到target目录下。

### 2. 启动Hadoop环境

首先准备好Linux环境，建议使用ubuntu14.04 LTS。OSX也可以运行，但是可能会出现一些小问题需要绕开，因此不推荐

其实启动Hadoop是一个错误的说法。严格来说启动的应该是hdfs和yarn服务。
在hadoop的etc目录下对hdfs-site.xml和yarn-site.xml进行配置，比如：
```
hdfs-site.xml:
<configuration>
    <property>
        <name>dfs.replication</name>
        <value>1</value>
    </property>
</configuration>

yarn-site.xml:
<configuration>
    <property>
        <name>yarn.resourcemanager.hostname</name>
        <value>localhost</value>
    </property>
    <property>
        <name>yarn.nodemanager.aux-services</name>
        <value>mapreduce_shuffle</value>
    </property>
</configuration>
```

为了启动hdfs，首先要对hdfs文件系统进行格式化，执行`hdfs namenode -format`
然后分别启动hdfs和yarn服务，使用jps检查是否已经成功启动。

> 默认情况下，hdfs会把文件系统初始化到/tmp目录下，这样每次重启就会清空所有数据，对于测试来说是比较方便的
> 如果需要保留，请在yarn-site.xml中额外配置hdfs文件系统的路径

再次提醒，这些配置是启动hdfs和yarn服务使用的，启动后hdfs和yarn将会作为守护进程存在。
我们的map-reduce程序只有在执行时用的配置文件（如-conf hadoop-localhost.xml）中显示指定使用hdfs和yarn才会与这些服务连接，否则跑的还是单机模式，会无视掉已经开启的hdfs和yarn服务。

在conf目录下已经准备好了hadoop-local.xml和hadoop-localhost.xml文件，分别是单机模式（全部使用本机资源）和伪分布式（使用hdfs和yarn）的配置文件，在启动map-reduce程序的时候切换使用即可。

### 3. 准备数据

比如在linux下某目录是这样的：
- your-packaged-code.jar # 打包后的代码文件
- hadoop-localhost.xml   # map-reduce任务的配置文件
- input                  # 输入数据文件夹

这一步我们要把数据放到hdfs文件系统里，然后map-reduce才能够从hdfs中取出数据进行计算。

我们先说一下关于路径的问题。比如输入路径写的是`input`，在代码中使用`new Path("input")`来创建。这个文件夹到底在哪里取决于你的文件系统是什么。如果使用的是本地文件系统，那么hadoop会在你的classpath里指定的路径中找input。如果你用的是hdfs，那么hadoop会去找`hdfs://localhost/user/${USER}/input`这个目录。

因此如果使用hdfs，在准备输入数据的时候，`/user/${USER}`这个目录需要自己创建，然后把input目录放进去。这样hadoop才会找到输入目录。

参考命令：
`hadoop fs -mkdir -p /user/${USER}`
`hadoop fs -copyFromLocal input /user/${USER}`

### 4. 执行

还是上一节的目录结构。

参考命令：
`hadoop jar your-packaged-code.jar -conf hadoop-localhost.xml input output`
这条命令提交了程序jar包，指定了配置属性以及输入输出目录。

注意这里配置文件指的是当前目录中的配置文件，而input和output指的是hdfs中/下的路径。
因此这个map-reduce任务的输入数据路径是`hdfs://localhost/user/${USER}/input`，而输出是`hdfs://localhost/user/${USER}/output`

执行完毕后就可以在输出路径中找到计算结果了。

> 执行运算的机器内存不要太小，实测500M的内存明显是不够用的。一旦内存吃完就会非常卡，然后任务失败还查不出原因

# 安利时间

为了使用的Linux环境，可以安装Linux的虚拟机。但是更好的方案建议使用vagrant。
vagrant本质上就是一个virtualbox的控制器，提供很方便的操作虚拟机的接口。

```
machine:~ me$ mkdir vagrant-test && cd vagrant-test     # 创建测试目录
machine:vagrant-test me$ vagrant init ubuntu14.04       # 自动下载ubuntu镜像
machine:vagrant-test me$ vagrant up                     # 调用virtualbox启动该ubuntu镜像

# 等几秒...

machine:vagrant-test me$                                # 启动完毕，回到命令行
machine:vagrant-test me$ vagrant ssh                    # 使用内建的ssh连接启动的虚拟机

vagarnt@ubuntu:~$                                       # 成功进入虚拟机环境，可以为所欲为了
```

推荐下载试用。

The End.
