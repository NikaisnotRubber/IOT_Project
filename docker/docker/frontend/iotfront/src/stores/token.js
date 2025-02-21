//定义store
import {defineStore} from 'pinia'
import {ref} from 'vue'

//存储token的store
export const useTokenStore = defineStore('token',()=>{
    //定义状态的内容

    //响应式变量
    const token = ref('')

    //定义一个函数,修改token的值
    const setToken = (newToken)=>{
        token.value = newToken
    }

    //函数,移除token的值
    const removeToken = ()=>{
        token.value=''
    }

    return {
        token,setToken,removeToken
    }
},{
    persist:true//持久化存储
});