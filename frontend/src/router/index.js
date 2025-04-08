import { createRouter, createWebHashHistory } from "vue-router";
import store from '@/store';
import { showConfirmDialog } from 'vant';
import LayOut from "@/pages/Layout";
import Notice from "@/pages/Notice/MyNotice.vue";
import Search from "@/pages/Search/SearchPage.vue";
import Login from "@/pages/Login/index.vue";
import Market from '@/pages/Market/index.vue'
import Consignment from '@/pages/Market/Consignment/consignmentPage.vue'
import SaleDetail from "@/pages/Market/SaleDetail/SaleDetail.vue";
import announceDetail from "@/pages/Market/AnnounceDetail/announceDetail.vue";
import User from "@/pages/Layout/user.vue";
import Wallet from "@/pages/Wallet/MyWallet.vue";
import Community from "@/pages/Layout/community.vue";
import Home from "@/pages/Layout/home.vue";
import FillConsignmentPrice from "@/pages/FillConsignmentPrice/FillConsignmentPrice.vue";
import BuyPage from "@/pages/BuyPage/BuyPage.vue";

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
  {
    path: "/market",
    component: Market,
    meta:{
      requiresAuth: true
    }
  },
  {
    path: '/consignment',
    component: Consignment,
    meta: {
      keepAlive: false // 不缓存详情页
    }
  },
  {
    path: '/saleDetail/:nftId/:instanceNumber',
    component:SaleDetail,
    props:true
  },
  {
    path: '/announceDetail/:nftId/:announceId',
    component:announceDetail,
    props:true
  },
  {
    path: "/wallet",
    name:'Wallet',
    component: Wallet,
    meta: {
      keepAlive: false // 不缓存详情页
    }
  },
  {
    path:"/fillConsignmentPrice",
    component: FillConsignmentPrice,
    meta: {
      keepAlive: false // 不缓存详情页
    }
  },
  {
    path:"/buyPage",
    name:'BuyPage',
    component: BuyPage,
    meta: {
      keepAlive: false // 不缓存详情页
    }
  }


];

const router = createRouter({
  history: createWebHashHistory(),
  routes
});
//路由守卫
router.beforeEach((to, from, next) => {
  const token = store.getters.getToken;
  const protectedRoutes = ['/search', '/notice', '/market','/consignment','/saleDetail','/announceDetail','/wallet','/fillConsignmentPrice','/buyPage'];

  if (protectedRoutes.includes(to.path) && !token) {
    showConfirmDialog({
      title: '提示',
      message: '您尚未登录，是否前往登录页面？',
      confirmButtonText: '去登录',
      cancelButtonText: '再逛逛',
    })
      .then(() => {
        next('/login');
      })
      .catch(() => {
        next(false);
      });
  } else {
    next();
  }
});
export default router;