import Vue from 'vue'
import App from './App.vue'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import axios from 'axios';
import VueRouter from 'vue-router'
import Vuex from 'vuex'
import router from './router'
import store from './store'
Vue.config.productionTip = false
Vue.use(ElementUI)
Vue.use(Vuex)
Vue.use(VueRouter)
Vue.prototype.$axios=axios;
Vue.prototype.$httpUrl='http://localhost:8844'
new Vue({
  render: h => h(App),
  router,
  store
}).$mount('#app')
