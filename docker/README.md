本目錄為項目部署目錄,使用docker快速部署網站

安裝最新版本docker, 在當前目錄下執行
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

`./mysql/`:  資料庫

