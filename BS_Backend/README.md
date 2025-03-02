# 物聯網設備後台管理系統

> ZJU BS Backend  
>
> 後端框架:     SpringBoot  + MyBatis  +  MySQL
>
> 項目管理:     Maven

## 運行方式

### 編寫配置文件

在目錄`src/main/resources`下新建`application.properties`文件，內容參考`application_template.properties`，僅需修改`EDIT ME`中帶尖括號的信息即可（注意有兩個地方，分別是數據庫連接設置和JWT秘鑰設置）。

其中，第一個 `EDIT ME` 塊主要是SpringBoot整合MyBatis需要的數據庫連接配置，你可以參考這篇文章進行配置 [Spring Boot整合Mybatis](https://blog.csdn.net/junR_980218/article/details/124805813) 

第一個 `EDIT ME` 塊主要是用於生成 `JWT` 令牌的加密秘鑰，你可以任取一串字符串作為秘鑰

```properties
#-----EDIT ME-----#
# 數據庫連接配置
spring.datasource.driver-class-name=<your_datasource_driver-class-name>
# 數據庫連接URL
spring.datasource.url=<your_datasource_url>
# 數據庫用戶名
spring.datasource.username=<your_datasource_username>
# 數據庫密碼
spring.datasource.password=<your_datasource_password>
#------------------#

#-----EDIT ME-----#
# JWT 配置 （任意字符串秘鑰）
jwt.secretKey=<"your_token_secret_key">
#------------------#

# MyBatis 配置
# 使用標準輸出作為 MyBatis 的日誌實現
mybatis.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl
# 將下划線分隔的命名轉換為駝峰命名（例如，a_column -> aColumn）
mybatis.configuration.map-underscore-to-camel-case=true
# 掃描 MyBatis 類型別名的包
mybatis.type-aliases-package=com.param.bs_backend.pojo

# 日誌配置
# 根日誌級別
logging.level.root=info
# 解除以下行的注釋以設置不同包的特定日誌級別
# logging.level.com.param.bs_backend.controller=DEBUG
logging.level.com.param.bs_backend.service=DEBUG
logging.level.com.param.bs_backend.pojo=DEBUG

# MQTT配置信息
mqtt.url=tcp://127.0.0.1:1883
mqtt.username=admin
mqtt.password=public
mqtt.client.id=consumer-id
mqtt.default.topic=topic

```

### Notes

- 在啓動應用程序之前，請確保數據庫服務器能夠連接成功，你可以打開IDE或者相應的數據庫軟件來檢測連接是否成功。

### 編譯運行

項目使用`maven`進行依賴管理，請自行參考`maven`的使用方法。或使用`IntelliJ IDEA`打開項目，使用`IDEA`自帶的`maven`插件進行依賴的更新。當所有依賴更新完之後點擊運行即可啓動後端項目（這之前得先保證上一步的配置文件配置無誤）。

或者你也可以參考其他方式運行`maven`項目，可以參考這個 [總結啓動maven項目的3種方式](https://blog.csdn.net/qq_43392001/article/details/99625275)

### 數據庫建表語句

數據庫的建表語句存儲在在目錄`src/main/resources`下，文件名為 `bs_database.sql`