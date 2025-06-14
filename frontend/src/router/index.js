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
import Wallet from "@/pages/MyPersonal/Wallet/MyWallet.vue";
import MyOrder from "@/pages/MyPersonal/Order/MyOrder.vue";
import OrderDetail from "@/pages/MyPersonal/Order/OrderDetail.vue";
import Community from "@/pages/Layout/community.vue";
import Home from "@/pages/Layout/home.vue";
import FillConsignmentPrice from "@/pages/FillConsignmentPrice/FillConsignmentPrice.vue";
import BuyPage from "@/pages/BuyPage/BuyPage.vue";
import SellPurchaseRequests from "@/pages/Market/SellPurchaseRequests/SellPurchaseRequests.vue";
import LaunchPurchaseRequests from "@/pages/Market/LaunchPurchaseRequests/LaunchPurchaseRequests.vue";
import MySettings from "@/pages/MyPersonal/Settings/MySettings.vue";
import UserInfo from "@/pages/MyPersonal/Settings/UserInfo.vue";
import EditNickname from "@/pages/MyPersonal/Settings/EditNickname.vue";
import EditPhone from "@/pages/MyPersonal/Settings/EditPhone.vue";
import OfficialPurchase from "@/pages/BuyPage/OfficialPurchase.vue";


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
    path: '/saleDetail',
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
    path: "/myOrder",
    name:'MyOrder',
    component: MyOrder,
    meta: {
      keepAlive: false // 不缓存详情页
    }
  },
  {
    path: "/mySettings",
    name:'MySettings',
    component: MySettings,
    meta: {
      keepAlive: false // 不缓存详情页
    }
  },
  {
    path: "/userInfo",
    name:'UserInfo',
    component: UserInfo,
    meta: {
      keepAlive: false // 不缓存详情页
    }
  },
  {
    path:"/editNickname",
    name:'EditNickname',
    component: EditNickname,
    meta: {
      keepAlive: false // 不缓存详情页
    }  
  },
  {
    path:"/editPhone",
    name:'EditPhone',
    component: EditPhone,
    meta: {
      keepAlive: false // 不缓存详情页
    }
  },
  {
    path:"/orderDetail",
    name:'OrderDetail',
    component: OrderDetail,
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
  },
  {
    path:"/sellPurchaseRequests",
    name:'SellPurchaseRequests',
    component: SellPurchaseRequests,
    meta: {
      keepAlive: false // 不缓存详情页
    }
  },
  {
    path:"/launchPurchaseRequests",
    name:'LaunchPurchaseRequests',
    component: LaunchPurchaseRequests,
    meta: {
      keepAlive: false // 不缓存详情页
    }
  },
  {
    path: "/officialPurchase",
    name: 'OfficialPurchase',
    component: OfficialPurchase,
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
  const protectedRoutes = ['/search', '/notice', '/market','/consignment','/saleDetail','/announceDetail','/wallet','/fillConsignmentPrice','/buyPage','/sellPurchaseRequests',
    '/launchPurchaseRequests','/myOrder','/orderDetail','/mySettings','/officialPurchase'];

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