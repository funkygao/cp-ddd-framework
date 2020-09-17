# Java Dynamic Load Jar

## Options

- OSGi
- ClassLoader
- Java Agent Instruments
- Dynamic compiling

## OSGi

从一开始发布OSGI，引以为傲的两大特点：模块化与热部署（动态化）。

使用Bundle做模块化管理，osgi做的很成熟。但是热部署并没有想象的那么好用。

### Bundle

OSGi的核心组件Bundle，与java中jar包的差别就是元数据配置。

- Bundle-Activator
- Bundle-Classpath
- Bundle-ManifestVersion
- Bundle-Name
- Bundle-SymbolicName
- Bundle-Version
- Export-Package
- Import-Package
- Require-Bundle
- Fragment-Host

### OSGI组件的热部署

一般Java的热部署（在不重启JVM的情况下替换class文件）只能适用于方法体的修改。

如果是更大的类结构的修改则需要自定义类加载器。

动态化在java界更明确的词是hot deployment，而hot deployment的实现并不容易。

即使你采用OSGi，但也不代表你的应用就具备了hot deployment的能力。

在hot deployment上，完美的结果就是当更新完成后，新的执行请求就在新的代码逻辑上正确的执行，就像没发生过更新这回事样，但实际要做到这样的效果，远没这么容易，即使是基于OSGi也同样如此。

OSGi以Bundle为粒度来实现动态化：如果要更新一个类，需要做的是更新整个Bundle。

更新的方法有两种：
- 直接update该Bundle(在MANIFEST.MF中增加Bundle-UpdateLocation来指定Bundle更新时所使用的文件)
- 先uninstall旧的Bundle，然后再安装并启动新的Bundle

无论是哪种方法，对于OSGi的应用而言，问题就在于package的类的改变以及Bundle中OSGi服务实现的改变。

在Equinox中，当update一个Bundle时，如果这个Bundle中有对外暴露的package，如果这个Bundle是singleton模式，在update后仍然保留了同样的Bundle SymbolicName的话，其实是无法update成功的，会报出一个已经有相同的Singleton的Bundle存在，因此update这种方法仅适用于没有对外暴露package的Bundle。

通过update方式来完成Bundle的更新受到了很大的限制，毕竟大部分时候Bundle都是singleton的，并且在更新的时候也是不会去改变其Bundle SymbolicName。

因此，在Equinox中要实现Bundle的更新，通常都使用另外一种方法，就是uninstall，然后再install并start更新后的Bundle。

[reference](https://blog.csdn.net/huakai_sun/article/details/78112493)


## ClassLoader

jar变化后，使用新的`ClassLoader`统一加载，原有的unreacheable classes and ClassLoader will be GC'ed, with uncertain when.

面临Germ区变大隐患。

## Java Agent Instruments

不支持增加方法，不支持method signature change，不支持增加类。

## Dynamic compiling
