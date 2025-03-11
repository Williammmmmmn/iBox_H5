import {createApp} from 'vue'
import App from './App.vue'
import Vant from 'vant'
import 'vant/lib/index.css'
import router from './router'
import store from './store'; // 引入 Vuex Store

const app = createApp(App);
app.use(Vant);
app.use(router);
app.use(store);
app.mount('#app')
