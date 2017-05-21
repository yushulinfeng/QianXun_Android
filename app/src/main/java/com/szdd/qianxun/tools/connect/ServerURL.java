package com.szdd.qianxun.tools.connect;

import com.szdd.qianxun.message.msg_tool.ParamTool;

public class ServerURL {
    private static final boolean IS_TEST_MODE = false;// 是否为测试模式
    private static final String SERVER_IP = "http://120.27.25.61";
    //服务器IP地址http://121.42.157.195:8080//new : http://120.27.25.61/

    // 登录注册相关
    public static final String USER_EXIST_CHECK = "userRegister_checkPNumber.action";
    public static final String REGISTER = "userRegister_regist.action";
    public static final String LOGIN = "userLogin_login.action";
    public static final String BASE_INFO = "user_getInfoByUsername.action";
    public static final String CODE_FORGET_LOGIN = "userLogin_loginByCheckCode.action";
    public static final String CODE_FORGET_ALTER = "user_changePwd.action";
    public static final String LOGOUT = "userLogin_logout.action";
    // 用户信息相关
    public static final String USER_SCORE = "";// 用户积分处理
    public static final String USER_COLLECT = "";// 关注的好友
    public static final String CREDIT_ID = "userVerify_saveUser.action";// 身份认证网址
    public static final String CREDIT_SDU = "userVerify_saveStu.action";// 学生认证网址
    public static final String ALTER_INFO = "user_updateInfoById.action";// 修改个人信息

    public static final String BUSINESS_MAIN_ROUND = "businessService_getLocalByPage.action";// 获取附近
    public static final String BUSINESS_MAIN_LAST = "businessService_getLatestByPage.action";// 获取最近
    public static final String BUSINESS_MAIN_HOT = "businessService_getHotByPage.action";// 获取综合项目//暂不修改
    public static final String BUSINESS_LAST = "businessService_getLatestByCategory.action";// 获取最新项目//暂不修改
    public static final String BUSINESS_ROUND = "businessService_getLocalByCategory.action";// 获取附近项目//改
    public static final String BUSINESS_COLLECT = "user_getAllFavoriteServiceByUserId.action";// 获取收藏
    public static final String GET_ID_BY_NAME = "user_getIdByUsername.action";

    public static final String ALIPAY_SIGN = "alipay_signature.action";
    public static final String ALIPAY_CONFIRM = "userGetService_confirmPayed.action";
    public static final String GETMYCASH = "trade_getBalance.action";
    public static final String GETCASHRECORD = "trade_getRecordByUserId.action";
    public static final String ALIPAY_GETCASH = "trade_getCash.action";

    // PostRequest类中向后抬发送需求的url
    public static final String POST_REQUEST_URL = "userRequest_save.action";
    // 删除需求的接口
    public static final String DELETE_REQUEST_URL = "userRequest_delete.action";
    // 获取分类好的需求
    public static final String GET_REQUEST_URL = "userRequest_getRelated.action";
    public static final String RECEIVED_PEOPLE_NUMBER = "userRequest_getReceivedUsersByReqId.action";
    public static final String COMMENT_URL = "userComment_save.action";

    //卖家已售服务界面已完成接口
    public static final String ALL_ORDER_FINISHSELLED_URL = "userGetService_getFinishedByStatusAndId.action";

    // ///////////////////////////////////////////////////////////////

    // 附近需求带有类型
    public static final String LOCAL_REQUEST_TYPE_URL = "userRequest_getLocalByType.action";
    // 附近需求
    public static final String LOCAL_REQUEST_URL = "userRequest_getLocal.action";
    // 同意接单
    public static final String LET_SOMEONE_TO_DO = "userRequest_takeOrder.action";
    // 需求详细界面
    public static final String REQUEST_DETAIL_URL = "userRequest_getById.action";
    // 服务详细界面
    public static final String SERVICE_DETAIL_URL = "businessService_getById.action";
    // 服务详细界面收藏者
    public static final String SERVICE_DETAIL_COLLECTOR_URL = "businessService_getFavoritePeople.action";
    // 服务详细界面添加收藏者
    public static final String SERVICE_DETAIL_ADDCOLLECTOR_URL = "user_addFavoriteService.action";
    // 服务详细界面删除收藏者
    public static final String SERVICE_DETAIL_CANCELCOLLECTOR_URL = "user_cancelFavoriteService.action";
    //买家已买服务界面
    public static final String ALL_ORDER_BOUGHT_URL = "userGetService_getUserBought.action";
    //卖家已售服务界面
    public static final String ALL_ORDER_SELLED_URL = "userGetService_getBusinessSelled.action";
    //用户要求退款
    public static final String ALL_ORDER_REFUNDIP_URL = "userGetService_refundInProgress.action";
    //买家取消预约
    public static final String ALL_ORDER_DELETE_URL = "userGetService_delete.action";
    //商家同意预约
    public static final String ALL_ORDER_TAKEORDER_URL = "userGetService_takeOrder.action";
    //商家拒绝预约
    public static final String ALL_ORDER_REFUSEORDER_URL = "userGetService_refuseToTakeOrder.action";
    //商家同意退款
    public static final String ALL_ORDER_CONFIRMREFUND_URL = "userGetService_confirmRefundInProgress.action";
    //商家拒绝退款
    public static final String ALL_ORDER_REFUSEREFUND_URL = "userGetService_refuseRefundInProgress.action";
    //商家完成服务
    public static final String ALL_ORDER_FINISHORDER_URL = "userGetService_finishService.action";
    //催一下
    public static final String REQUEST_HURRY = "userRequest_reminder.action";
    // 举报
    public static final String REPORT_URL = "userRequest_report.action";
    // imageview的图片
    public static final String IMAGES_URL = "prop_getEightlink.action";
    // 报名有关
    public static final String SIGN_UP_URL = "userRequest_signUp.action";
    // 取消报名
    public static final String CANCEL_SIGN_URL = "userRequest_cancelSign.action";
    // 发单人确认需求是否完成
    public static final String ENSURE_IF_ISFINISHED_1 = "userRequest_finishRequest.action";
    // 接单人确认任务已经完成
    public static final String CONFIRM_ISFINISHED_1 = "userRequest_pushFinish.action";
    //发服务
    public static final String POST_SERVICE = "businessService_save.action";

    //预约是=时得到数据
    public static final String GET_SETVICE_ORDER = "businessService_getById.action";
    //预定服务
    public static final String ORDER_SERVICE = "userGetService_save.action";
    //买家看的订单详情
    public static final String GET_ORDER_BUY = "userGetService_getByIdFromUserAngles.action";
    //卖家看的订单详情
    public static final String GET_ORDER_SALE = "userGetService_getById.action";
    //接单
    public static final String ORDER_SALE_AGREE = "userGetService_takeOrder.action";
    //拒绝
    public static final String ORDER_SALE_REJECT = "userGetService_refuseToTakeOrder.action";
    // //////////////////////////////////////

    // 广告台图文区一级界面
    public static final String GET_BRIEF = "userComment_getBrief.action";
    // 广告台图文区二级界面,获取内容
    public static final String GET_DETAIL = "userRequest_getByCommentId.action";
    // 广告台图文区二级界面点赞
    public static final String ADD_GREAT = "userComment_addGreat";
    // 广告台视频区一级界面
    public static final String GET_VIDEO = "video_getVideoByPage.action";
    // 发动态
    public static final String POST_DYNAMIC = "dynamic_save.action";
    //广告台图文区一级界面动态
    public static final String DYNAMIC_GET_BRIEF = "dynamic_getBriefByPage.action";
    //获得动态详情
    public static final String GET_DYNAMIC_DETAIL = "dynamic_getById.action";
    //动态顶
    public static final String DYNAMIC_SUPPORT = "dynamic_addSupport.action";
    //动态踩
    public static final String DYNAMIC_TRAMPLE = "dynamic_addTrample.action";
    //个人主页个人信息
    public static final String HOMEPAGE_PERSONAL_DETAIL = "user_getInfoById.action";
    //个人主页动态
    public static final String HOMEPAGE_DYNAMIC = "dynamic_getBriefByUserId.action";
    //个人主页服务
    public static final String HOMEPAGE_SERVICE = "businessService_getBriefByUserId.action";
    //个人主页需求
    public static final String HOMEPAGE_REQUEST = "userRequest_getAllByUserId.action";
    //个人主页关注
    public static final String HOMEPAGE_ATTENTION = "user_concernPeople.action";
    //个人主页取消关注
    public static final String HOMEPAGE_CANCEL_ATTENTION = "user_cancelConcern.action";
    //删除动态
    public static final String DELETE_DYNAMIC = "dynamic_delete.action";
    //删除服务
    public static final String DELETE_SERVICE="businessService_shield.action";
    //获取个人关注的人
    public static final String GET_ALL_ATTENTION = "user_getAllConcernPeopleById.action";
    //修改背景图片
    public static final String ALTER_BACKGROUND = "user_updateUserBackGroundImage.action";
    //修改头像
    public static final String ALTER_HEADICON = "user_updateUserHeadIcon.action";

    //首页顶部
    public static final String FIRST_PAGE_TOP = "prop_getMainPageImages.action";
    //首页中部
    public static final String FIRST_PAGE_CENTER = "prop_getSmallImages.action";

    /**
     * 获取服务器IP地址
     *
     * @return 服务器IP地址（http开头）
     */
    public static String getIP() {
        // 使用在线参数的IP
        String value = ParamTool.getParam("sever_ip");
        if (value.equals("") || value.equals("0"))
            return SERVER_IP;
        return "http://" + value;
    }

    /**
     * 获取完整URL，包括前缀和后缀
     *
     * @param static_url ServerURL中配置的地址
     * @return 服务器完整地址前缀
     */
    public static String getSignedURL(String static_url) {
        return getIP() + "/qianxun/" + static_url + ConnectSign.getSignURL();
    }

    /**
     * 是否为测试模式
     *
     * @return true：测试模式；false：非测试模式。
     */
    public static boolean isTest() {
        return IS_TEST_MODE;
    }
}
