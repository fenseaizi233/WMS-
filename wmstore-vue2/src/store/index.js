import Vue from "vue";
import Vuex from "vuex";
import createPersistedState from "vuex-persistedstate";
Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    CurUser: {},
    Menu: [],
    hasRoutes: false,
  },
  mutations: {
    setMenu(state, data) {
      (state.Menu = data), localStorage.setItem("Menu", data);
    },
    setUser(state, data) {
      (state.CurUser = data), localStorage.setItem("CurUser", data);
    },
    changeRouteStates(state, hasRoutes) {
      state.hasRoutes = hasRoutes;
    },
    resetState(state) {
      (state.Menu = []), (state.CurUser = {});
      state.hasRoutes = false;
      localStorage.removeItem("CurUser");
      localStorage.removeItem("Menu");
    },
  },
  getters: {
    getMenu(state) {
      return state.Menu;
    },
  },
  plugins: [createPersistedState()],
});
