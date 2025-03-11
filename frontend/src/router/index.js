import {createRouter, createWebHashHistory} from "vue-router";
import LayOut from "@/pages/Layout";
import Notice from "@/pages/Notice/MyNotice.vue";
import Search from "@/pages/Search/SearchPage.vue";
import Login from "@/pages/Login/index.vue";

import User from "@/pages/Layout/user.vue";
import Community from "@/pages/Layout/community.vue";
import Home from "@/pages/Layout/home.vue";

const routes = [
    {
        path: "/",
        component: LayOut,
        redirect: "/home",
        children: [
            {
                path: "/home", // 默认子路由，路径为 /home
                component: Home
            },
            {
                path: "/community",
                component: Community
            },
            {
                path: "/user",
                component: User
            },

        ]
    },
    {
        path: "/login",
        component: Login,
    },
    {
        path: "/search",
        component: Search,
    },
    {
        path: "/notice",
        component: Notice,
    },


];

const router = createRouter({
    history: createWebHashHistory(),
    routes
});

export default router;