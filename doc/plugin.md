# Plugin Design

## 目标

以jar包为单位管理扩展业务，扩展业务包，此处称为Plugin。

Plugin有2种：
- `Pattern` + `Extension`
- `Partner` + `Extension`

## 现有解决方案

| 方案               | pros                                               | cons                                                         |
| ------------------ | -------------------------------------------------- | ------------------------------------------------------------ |
| Java 9 的模块化    | requires/exports/transitive                        |                                                              |
| OSGi               | 热替换是通过创建新对象实例，然后替换引用的方式实现 | 重，复杂，基于Bundle开发，难以驾驭；要解决有状态的问题；OSGi和非OSGi混用非常麻烦；间接依赖很难把控 |
| Pandora(OSGi)      |                                                    |                                                              |
| SOFAArk(OSGi)      |                                                    |                                                              |
| 自己做ClassLoader  |                                                    |                                                              |
| Java Agent(Arthas) |                                                    | 只能修改方法体，不能增加类：redefine                         |
| 脚本语言           |                                                    |                                                              |


## Scenarios

- 可以自己 AOP 吗
- 打印日志
