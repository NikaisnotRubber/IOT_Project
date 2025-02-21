<template>
    <div id="container"></div>
</template>
 
<script setup>
import AMapLoader from '@amap/amap-jsapi-loader';
import {ref, watch} from "vue";
import {getNewPointsMsgService}from '@/api/msg.js';

 
function initMap() {
    window._AMapSecurityConfig = {
        securityJsCode: '05ceb7b0cd1739804611d3e5ff9dcc74',
    }
    AMapLoader.load({
        key:"119fac2eec4dde18c52ff7ed05459f16", // 申请好的Web端开发者Key，首次调用 load 时必填
        version:"2.0", // 指定要加载的 JSAPI 的版本，缺省时默认为 1.4.15
    }).then((AMap)=>{
        const map = new AMap.Map("container",{  //设置地图容器id
            resizeEnable: true, // 是否监控地图容器尺寸变化
        });
        // 添加插件
        AMap.plugin(["AMap.ToolBar", "AMap.Scale", "AMap.HawkEye","AMap.Geolocation","AMap.MapType","AMap.MouseTool"], function () {
            //异步同时加载多个插件
            // 添加地图插件
            map.addControl(new AMap.Scale()); // 显示当前地图中心的比例尺
            map.addControl(new AMap.HawkEye()); // 显示缩略图
        });
        
        const pointsMsg = ref([
            {
            clientId: '',
            info: '',
            value: 0,
            alert: 0,
            lng: 0.0,
            lat: 0.0,
            timestamp: 0
            }
        ]);
        
        const getpointsMsg = async ()=>{
            let res = await getNewPointsMsgService();
            pointsMsg.value = res.data;

            //合并经纬度,添加position属性,映射状态
            for (let i = 0; i < pointsMsg.value.length; i++) {
                pointsMsg.value[i].position = [pointsMsg.value[i].lng, pointsMsg.value[i].lat];
                pointsMsg.value[i].state = pointsMsg.value[i].alert == 0 ? '正常' : '异常';
            }
        }
        getpointsMsg();
        watch(pointsMsg, (newVal, oldVal) => {
            for (var i = 0; i < newVal.length; i++) {
                const marker = new AMap.Marker({
                    position: newVal[i].position,
                    map: map
                });
                marker.content = '<p>设备id: '+newVal[i].clientId +'</p>'+
                                 '<p>设备信息: '+newVal[i].info +'</p>'+
                                 '<p>设备值: '+newVal[i].value +'</p>'+
                                 '<p>设备状态: '+newVal[i].state +'</p>';
                marker.on('click', markerClick);
                marker.emit('click', {target: marker});
            }
        })
        const infoWindow = new AMap.InfoWindow({offset: new AMap.Pixel(0, -30)});

        function markerClick(e) {
            infoWindow.setContent(e.target.content);
            infoWindow.open(map, e.target.getPosition());
        }
        map.setFitView();
    }).catch(e=>{
        console.log(e);
    })
}
 
initMap()
</script>
 
<style>
#container{
    padding:0px;
    margin: 0px;
    width: 100%;
    height: 800px;
}
</style>