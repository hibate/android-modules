# Android 通用模块

## 说明

module                   | description                             | 传送带
-------------------------|-----------------------------------------|-----------
commons-beanutils-bridge | Apache 通用组件 bridge for android      | [点击查看](commons/beanutils-bridge)
commons-beanutils        | Apache commons beanutils for android    | [点击查看](commons/beanutils)
commons-digester3        | Apache commons digester for android     | [点击查看](commons/digester3)
commons-logger-logback   | Slf4j + logback for android             | [点击查看](commons/logger/logback)
commons-logger-nativelogger | Android JNI 层日志框架               | [点击查看](commons/logger/nativelogger)

部分模块的 `build.gradle` 中包含 `buildJar` 任务，可通过执行该任务将该模块打包成单独的 jar 文件。

## Demo

module                   | description                             | 传送带
-------------------------|-----------------------------------------|-----------
demo-logger              | slf4j + logback + nativelogger 使用示例 | [点击查看](demos/logger)
