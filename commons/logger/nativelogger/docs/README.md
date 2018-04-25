## 说明

Android Native Logger

## Android native logger

### 依赖库

* slf4j-api-xxx.jar

### 编译

参考 [slf-logger 编译说明](../src/main/jni/libslflogger/README.md) 初始化依赖环境。

双击源码路径下的 build.bat 脚本编译，双击 clean.bat 脚本执行清理。

### 使用

```java
import org.hibate.logger.nativelogger.LoggerContext;
import org.hibate.logger.nativelogger.LoggerContextFactory;
import org.hibate.logger.nativelogger.LoggerOverSlf4j;
import org.hibate.logger.nativelogger.Priority;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LogbackConfigurator.configure(this);        // 配置 Logback
        LoggerContext loggerContext = LoggerContextFactory.getLoggerContext();
        loggerContext.setPriority(Priority.DEBUG);  // 设置日志输出级别
        loggerContext.setLineLoggable(true);        // 是否输出文件/行号信息
        loggerContext.addLoggerListener(new LoggerOverSlf4j()); // 将底层日志链接到 Slf4j 中输出
    }
}
```

备注: 调用 `org.hibate.logger.nativelogger.LoggerContext` 任意方法都会自行加载底层库。