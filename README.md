# Android 通用模块

## 说明

module                   | description
-------------------------|--------------------------------
commons-beanutils-bridge | Apache 通用组件 bridge for android
commons-beanutils        | Apache commons beanutils for android
commons-digester3        | Apache commons digester for android
commons-logger-logback   | Slf4j + logback for android
commons-logger-nativelogger | Android JNI 层日志框架

部分模块的 `build.gradle` 中包含 `buildJar` 任务，可通过执行该任务将该模块打包成单独的 jar 文件。

## Demo

module                   | description
-------------------------|--------------------------------
demo-logger              | slf4j + logback + nativelogger 使用示例
