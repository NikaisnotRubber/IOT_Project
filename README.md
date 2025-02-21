## 項目簡介
利用 Web 開發技術實現一個 IoT 設備管理平台。該平台將允許用戶註冊、登錄，並提供物聯網設備管理、用戶
認證、設備管理、數據查詢和統計等功能。
- 遵守 Spring MVC 框架，並且拆分為用戶認證和授權、設備管理資料查詢和統計以及 MQTT 模塊四個模塊，使用 Spring Security 進行用戶認證和授權。處理用戶註冊、登錄和頒發 JWT 令牌。使用 Spring Data JPA 管理設備信息的創建、編輯和刪除。使用 RESTful API 以支持前端設備管理、數據查詢以及設備統計數據操作。使用 Mosquitto 接收設備數據並將其存儲到數據庫（使用 MyBatis 與資料庫進行交互）中，並且通過 Docker實現系統的容器化和編排。

## 運行方法
進入`docker`資料夾
```shell
 docker-compose up -d
```

訪問前端網址 http://localhost:5173



**各目錄功能**

`./backend/`: 項目後端,為前端提供數據庫訪問

`./frontend/`: 項目前端, 提供網頁訪問

`./iotsim/`: 物聯網模擬器

`./mosquitto/`: `mqtt broker`

`./mqttsub/`: 項目後端, 連接`mosquitto` 獲取信息並存入數據庫

`./mysql/`:  數據庫

