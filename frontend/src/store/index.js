import { createStore } from "vuex";
// 创建 Vuex Store
const store = createStore({
    state: {
      token: localStorage.getItem('token') || null, // 从 localStorage 读取 token
      userInfo: JSON.parse(localStorage.getItem('userInfo')) || null, // 从 localStorage 读取用户信息
    },
    mutations: {
      setToken(state, token) {
        state.token = token;
        localStorage.setItem('token', token); // 保存到 localStorage
      },
      setUserInfo(state, userInfo) {
        state.userInfo = userInfo;
        localStorage.setItem('userInfo', JSON.stringify(userInfo)); // 保存到 localStorage
      },
    },
    actions: {
      login({ commit }, { token, userInfo }) {
        commit('setToken', token);
        commit('setUserInfo', userInfo);
      },
      logout({ commit }) {
        commit('setToken', null);
        commit('setUserInfo', null);
        localStorage.removeItem('token'); // 清除 localStorage 中的 token
        localStorage.removeItem('userInfo'); // 清除 localStorage 中的用户信息
      },
    },
    getters: {
      getToken(state) {
        return state.token;
      },
      getUserInfo(state) {
        return state.userInfo;
      },
    },
  });
  export default store;