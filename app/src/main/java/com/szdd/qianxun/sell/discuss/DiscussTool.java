package com.szdd.qianxun.sell.discuss;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.szdd.qianxun.message.baichuan.mine.BaiChuanUtils;
import com.szdd.qianxun.message.msg_tool.ManagerTool;
import com.szdd.qianxun.message.msg_tool.UserStateTool;
import com.szdd.qianxun.tools.connect.ServerURL;
import com.umeng.comm.core.CommunitySDK;
import com.umeng.comm.core.beans.CommUser;
import com.umeng.comm.core.beans.Comment;
import com.umeng.comm.core.beans.FeedItem;
import com.umeng.comm.core.beans.Topic;
import com.umeng.comm.core.constants.ErrorCode;
import com.umeng.comm.core.impl.CommunityFactory;
import com.umeng.comm.core.listeners.Listeners;
import com.umeng.comm.core.login.LoginListener;
import com.umeng.comm.core.nets.Response;
import com.umeng.comm.core.nets.responses.CommentResponse;
import com.umeng.comm.core.nets.responses.FeedItemResponse;
import com.umeng.comm.core.nets.responses.PortraitUploadResponse;
import com.umeng.comm.core.nets.responses.PostCommentResponse;
import com.umeng.comm.core.utils.CommonUtils;

import java.util.ArrayList;

/**
 * 此处使用单例类来写，相对于全部静态方法，具有更好的封装性
 * 学习了面向对象，确实是非常有用的，尤其在优化方面……
 */
//登录，修改资料
//获取评论详情（建议外部获取，intent传递）
//获取回复列表
//发布评论（需要后台存储ID值，通知）
//发布回复
public class DiscussTool {
    private static final String TOPIC_ID = "574fc5b655c4007925471e81";//话题ID，FEED依托于话题
    private static DiscussTool discussTool = new DiscussTool();
    private CommunitySDK mCommSDK;

    private DiscussTool() {
    }

    public static DiscussTool getInstance() {
        return discussTool;
    }

    /**
     * 初始化SDK
     */
    public void initSDK(Context context) {
        // 获取SDK实例
        mCommSDK = CommunityFactory.getCommSDK(context);
        //注：取消用户名的长度和特殊字符限制不能起到作用
    }

    /**
     * 用户是否已登录评论系统
     * 因为有些用户的昵称是不合法的，所以需要在显示时处理。之后的用户已在注册时进行约束。
     * 就是不显示出来。禁止回复即可。已经验证，未登录，可获取，但是不可回复。
     */
    public boolean isDiscussLogin(Context context) {
        //登录成功后,用该方法判断用户时候已经登录
        return CommonUtils.isLogin(context);
    }

    private boolean isSuccess(Response response) {
        return response.errCode == ErrorCode.NO_ERROR;
    }

    /**
     * 登录评论系统。
     * 使用我们后台返回的userID
     */
    public void loginDiscuss(Context context, String userId, String nickName,
                             boolean isMan, String headUrl) {
        if (!ManagerTool.isManagerLogin())
            if (!UserStateTool.isLoginNow(context))
                return;
        if (TextUtils.isEmpty(userId))
            return;
        //构建用户
        CommUser user = new CommUser();
        //4位之内，因为昵称是1-15位的，还需要有1位下划线，保证不超过20位
        user.id = BaiChuanUtils.getUserName(userId);//同阿里百川
        long user_id_long = Long.parseLong(userId) % 10000;
        user.name = nickName + "_" + user_id_long;
        if (ManagerTool.isManagerLogin())
            user.name = nickName;
        user.gender = isMan ? CommUser.Gender.MALE : CommUser.Gender.FEMALE;
        user.iconUrl = headUrl;
        if (ServerURL.isTest()) {
            Log.e("EEEEEEEEEE", "DISCUSS-" + "login-before");
            Log.e("EEEEEEEEEE", "DISCUSS-" + "login-id:" + user.id);
            Log.e("EEEEEEEEEE", "DISCUSS-" + "login-name:" + user.name);
            Log.e("EEEEEEEEEE", "DISCUSS-" + "login-iconUrl:" + user.iconUrl);
        }
        //登录
        mCommSDK.loginToUmengServerBySelfAccount(context, user, new LoginListener() {
            public void onStart() {
            }

            //成败都不用管，可以后期判断登陆状态
            public void onComplete(int stCode, CommUser commUser) {
                if (ServerURL.isTest()) {
                    if (ErrorCode.NO_ERROR == stCode)
                        Log.e("EEEEEEEEEE", "DISCUSS-" + "login-success");
                    else //登录失败
                        Log.e("EEEEEEEEEE", "DISCUSS-" + "login-error");
                }
            }
        });
    }

    /**
     * 更新用户信息，请确保用户已经登录。
     * 信息不变的请传入null
     */
    public void updateUserInfo(Context context, String userId,
                               String nickName, boolean isMan, String headUrl) {
        if (!UserStateTool.isLoginNow(context) || !isDiscussLogin(context))
            return;
        if (!TextUtils.isEmpty(nickName)) {//更新昵称
            if (TextUtils.isEmpty(userId))
                return;
            //构建用户
            CommUser user = new CommUser();
            //4位之内，因为昵称是1-15位的，还需要有1位下划线，保证不超过20位
            long user_id_long = Long.parseLong(userId) % 10000;
            user.id = BaiChuanUtils.getUserName(user_id_long);
            user.name = nickName + "_" + userId;
            user.gender = isMan ? CommUser.Gender.MALE : CommUser.Gender.FEMALE;
            user.iconUrl = headUrl;
            //更新，成败就不必管了
            mCommSDK.updateUserProfile(user, new Listeners.CommListener() {
                public void onStart() {
                }

                public void onComplete(Response response) {
                    if (ServerURL.isTest()) {
                        if (isSuccess(response)) //是否成功
                            Log.e("EEEEEEEEEE", "DISCUSS-" + "alert-success");
                        else
                            Log.e("EEEEEEEEEE", "DISCUSS-" + "alert-fail");
                    }
                }
            });
        } else if (!TextUtils.isEmpty(headUrl)) {//更新头像。成败就不必管了，没有意义。不要传入null。
            mCommSDK.updateUserProtrait(headUrl, new Listeners
                    .SimpleFetchListener<PortraitUploadResponse>() {
                public void onComplete(PortraitUploadResponse
                                               portraitUploadResponse) {
                    if (ServerURL.isTest()) {
                        if (isSuccess(portraitUploadResponse)) //是否成功
                            Log.e("EEEEEEEEEE", "DISCUSS-" + "alertIcon-success");
                        else
                            Log.e("EEEEEEEEEE", "DISCUSS-" + "alertIcon-fail");
                    }
                }
            });
        }
    }

    //仅更新头像
    public void updateUserInfo(Context context, String headUrl) {
        updateUserInfo(context, null, null, true, headUrl);
    }

    /**
     * 发布评论
     */
    public void postDiscuss(Context context, String title, String text,
                            final DiscussPostListener listener) {
        if (!UserStateTool.isLoginNow(context) || !isDiscussLogin(context)) {
            if (ServerURL.isTest())
                Log.e("EEEEEEEEEE", "DISCUSS-" + "feed-fail-pleaseLogin");
            if (listener != null)
                listener.OnResponse(null);
            return;
        }
        FeedItem item = new FeedItem();
        item.title = title;
        item.text = text;
        Topic topic = new Topic();
        topic.id = TOPIC_ID;
        item.topics.add(topic);
        mCommSDK.postFeed(item, new Listeners.SimpleFetchListener<FeedItemResponse>() {
            @Override
            public void onComplete(FeedItemResponse feedItemResponse) {
                if (isSuccess(feedItemResponse)) {//是否成功
                    if (ServerURL.isTest()) {
                        Log.e("EEEEEEEEEE", "DISCUSS-" + "feed-success");
                        Log.e("EEEEEEEEEE", "DISCUSS-" + "feed-id:" + feedItemResponse.result.id);
                    }
                    if (listener != null)
                        listener.OnResponse(feedItemResponse.result.id);
                } else {
                    if (ServerURL.isTest())
                        Log.e("EEEEEEEEEE", "DISCUSS-" + "feed-fail");
                    if (listener != null)
                        listener.OnResponse(null);
                }
            }
        });
    }

    /**
     * 获取评论
     */
    public void getDiscuss(String discussId, final DiscussGetListener listener) {
        mCommSDK.fetchFeedWithId(discussId, new Listeners.FetchListener<FeedItemResponse>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onComplete(FeedItemResponse feedItemResponse) {
                if (isSuccess(feedItemResponse)) {
                    FeedItem item = feedItemResponse.result;
                    if (ServerURL.isTest()) {
                        Log.e("EEEEEEEEEE", "DISCUSS-" + "feedget-success");
                        Log.e("EEEEEEEEEE", "DISCUSS-" + "feedget:" + item.toString());
                        Log.e("EEEEEEEEEE", "DISCUSS-" + "feedget-title:" + item.title);
                        Log.e("EEEEEEEEEE", "DISCUSS-" + "feedget-text:" + item.text);
                        Log.e("EEEEEEEEEE", "DISCUSS-" + "feedget-count:" + item.commentCount);
                        Log.e("EEEEEEEEEE", "DISCUSS-" + "feedget-userid:" + item.creator.sourceUid);
                    }
                    if (listener != null) {
                        String userId = BaiChuanUtils.getUserId(item.creator.sourceUid) + "";//-1也可以
                        listener.OnResponse(userId, item.title, item.text, item.commentCount);
                    }
                } else {
                    if (ServerURL.isTest())
                        Log.e("EEEEEEEEEE", "DISCUSS-" + "feedget-fail");
                    if (listener != null)
                        listener.OnResponse(null, null, null, 0);
                }
            }
        });
    }

    /**
     * 获取评论的回复
     */
    public void getReply(String discussId, final ReplyGetListener listener) {
        mCommSDK.fetchFeedComments(discussId, new Listeners.SimpleFetchListener<CommentResponse>() {
            @Override
            public void onComplete(CommentResponse commentResponse) {
                if (isSuccess(commentResponse)) {//是否成功
                    if (ServerURL.isTest()) {
                        Log.e("EEEEEEEEEE", "DISCUSS-" + "comment-success");
                        Log.e("EEEEEEEEEE", "DISCUSS-" + "comment:" + commentResponse.result.size());
                    }
                    //单独写一个类，AnDiscussReply，包含相关信息
                    ArrayList<AnDiscussReply> list = new ArrayList<AnDiscussReply>();
                    for (Comment comment : commentResponse.result) {
                        if (ServerURL.isTest()) {
                            Log.e("EEEEEEEEEE", "DISCUSS-" + "comment:" + comment.toString());
                            Log.e("EEEEEEEEEE", "DISCUSS-" + "comment-id:" + comment.id);
                        }
                        AnDiscussReply item = new AnDiscussReply.Builder()
                                .userId(comment.creator.sourceUid)
                                .userHead(comment.creator.iconUrl)
                                .userName(comment.creator.name)
                                .replyId(comment.id)
                                .discussId(comment.feedId)
                                .replyFloor(comment.floor)
                                .replyText(comment.text)
                                .replyTime(comment.createTime)
                                .build();
                        list.add(item);
                    }
                    if (listener != null)
                        listener.OnResponse(list);
                } else {
                    if (ServerURL.isTest())
                        Log.e("EEEEEEEEEE", "DISCUSS-" + "comment-fail");
                    if (listener != null)
                        listener.OnResponse(null);
                }
            }
        });
    }

    /**
     * 回复评论
     */
    //回复某人，其实可以变通的写，discussId+AnDiscussReply+text，通过自定义text头信息等处理。
    //暂时不提供回复某个用户这个接口。
    public void replayDiscuss(Context context, String discussId, String text,
                              final ReplyPostListener listener) {
        if (!UserStateTool.isLoginNow(context) || !isDiscussLogin(context)) {
            if (ServerURL.isTest())
                Log.e("EEEEEEEEEE", "DISCUSS-" + "reply-fail-pleaseLogin");
            if (listener != null)
                listener.OnResponse(null, null);
            return;
        }
        Comment comment = new Comment();
        comment.feedId = discussId;
        comment.text = text;
        mCommSDK.postCommentforResult(comment, new Listeners
                .FetchListener<PostCommentResponse>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onComplete(PostCommentResponse postCommentResponse) {
                if (isSuccess(postCommentResponse)) {//是否成功
                    if (ServerURL.isTest()) {
                        Log.e("EEEEEEEEEE", "DISCUSS-" + "replay-success");
                        Log.e("EEEEEEEEEE", "DISCUSS-" + "replay-id:"
                                + postCommentResponse.getComment().id);//便于立即删除
                    }
                    if (listener != null) {
                        Comment comment = postCommentResponse.getComment();
                        AnDiscussReply item = new AnDiscussReply.Builder()
                                .userId(comment.creator.sourceUid)
                                .userHead(comment.creator.iconUrl)
                                .userName(comment.creator.name)
                                .replyId(comment.id)
                                .discussId(comment.feedId)
                                .replyFloor(comment.floor)
                                .replyText(comment.text)
                                .replyTime(comment.createTime)
                                .build();
                        listener.OnResponse(postCommentResponse.getComment().id, item);
                    }
                } else {
                    if (ServerURL.isTest())
                        Log.e("EEEEEEEEEE", "DISCUSS-" + "replay-fail");
                    if (listener != null)
                        listener.OnResponse(null, null);
                }
            }
        });
    }

    /**
     * 删除回复(除了管理员外和评论的发布者外，其他人只能删除自己的)
     */
    public void deleteReply(Context context, String discussId, String replyId,
                            final ReplyDelListener listener) {
        if (!ManagerTool.isManagerLogin())
            if (!UserStateTool.isLoginNow(context) || !isDiscussLogin(context)) {
                if (ServerURL.isTest())
                    Log.e("EEEEEEEEEE", "DISCUSS-" + "delReply-fail-pleaseLogin");
                if (listener != null)
                    listener.OnResponse(false);
                return;
            }
        mCommSDK.deleteComment(discussId, replyId, new Listeners.CommListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onComplete(Response response) {
                if (isSuccess(response)) {//是否成功
                    if (ServerURL.isTest()) {
                        Log.e("EEEEEEEEEE", "DISCUSS-" + "replay-success");
                    }
                    if (listener != null)
                        listener.OnResponse(true);
                } else {
                    if (ServerURL.isTest())
                        Log.e("EEEEEEEEEE", "DISCUSS-" + "replay-fail");
                    if (listener != null)
                        listener.OnResponse(false);
                }
            }
        });
    }

    //interface

    /**
     * 评论获取监听
     */
    public interface DiscussGetListener {
        /**
         * 获取评论内容
         *
         * @param authorId   发布者Id，是我们服务器的id，错误回调null
         * @param title      标题，错误回调null
         * @param text       内容，错误回调null
         * @param replyCount 回复数量
         */
        public void OnResponse(String authorId, String title, String text, int replyCount);
    }

    /**
     * 评论发布状态监听
     */
    public interface DiscussPostListener {
        /**
         * 发布初始评论接口
         *
         * @param discussId 评论的ID，失败将回调null
         */
        public void OnResponse(String discussId);
    }

    /**
     * 回复获取监听
     */
    public interface ReplyGetListener {
        /**
         * 获取回复列表
         *
         * @param list 回复列表，错误回调null
         */
        public void OnResponse(ArrayList<AnDiscussReply> list);
    }

    /**
     * 回复发布监听
     */
    public interface ReplyPostListener {
        /**
         * 回复评论信息
         *
         * @param replyId 回复的信息的ID，失败回调null
         * @param reply   刚才发布的内容，便于直接处理，失败回调null
         */
        public void OnResponse(String replyId, AnDiscussReply reply);
    }

    /**
     * 回复删除监听
     */
    public interface ReplyDelListener {
        /**
         * 回复评论信息
         *
         * @param state true删除成功
         */
        public void OnResponse(boolean state);
    }

}
