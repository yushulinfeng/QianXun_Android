package com.szdd.qianxun.message.baichuan.im;

import android.support.v4.app.Fragment;

import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMConversationListOperation;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWConversationType;
import com.alibaba.mobileim.conversation.YWCustomConversationBody;
import com.szdd.qianxun.R;

import java.util.ArrayList;

/**
 * 最近会话界面的逻辑定制
 */
public class ConversationListOperationCustomSample extends IMConversationListOperation {

    public ConversationListOperationCustomSample(Pointcut pointcut) {
        super(pointcut);
    }

    /**
     * 返回自定义会话和群会话的头像 url
     * 该方法只适用设置自定义会话和群会话的头像，设置单聊会话头像请参考 UserProfileSampleHelper
     *
     * @param fragment
     * @param conversation 会话 可以通过 conversation.getConversationId拿到用户设置的会话id以根据不同的逻辑显示不同的头像
     * @return
     */
    @Override
    public String getConversationHeadPath(Fragment fragment,
                                          YWConversation conversation) {
        if (conversation.getConversationType() == YWConversationType.Custom) {
//            return "http://tp2.sinaimg.cn/1721410501/50/40033657718/0";
        }
        return "";
    }

    /**
     * 返回自定义会话和群会话的默认头像 如返回本地的 R.drawable.test
     * 该方法只适用设置自定义会话和群会话的头像，设置单聊会话头像请参考UserProfileSampleHelper}     * @param fragment
     *
     * @param conversation
     * @return
     */
    @Override
    public int getConversationDefaultHead(Fragment fragment,
                                          YWConversation conversation) {
        if (conversation.getConversationType() == YWConversationType.Custom) {
            YWCustomConversationBody body = (YWCustomConversationBody) conversation.getConversationBody();
            String conversationId = body.getIdentity();
//            if(conversationId.equals(FragmentTabs.SYSTEM_TRIBE_CONVERSATION)){
//                return R.drawable.aliwx_tribe_head_default;
//            }else  if(conversationId.equals(FragmentTabs.SYSTEM_FRIEND_REQ_CONVERSATION)){
//                return R.drawable.aliwx_head_default;
//            }else{
            return R.drawable.aliwx_head_default;
//            }
        }
        return 0;
    }

    /**
     * 返回自定义会话的名称
     *
     * @param fragment
     * @param conversation
     * @return 这里的myconversatoin或者custom_view_conversation是调用sdk方法插入的自定义会话id.
     * 具体参考CustomConversationHelper
     */
    @Override
    public String getConversationName(Fragment fragment,
                                      YWConversation conversation) {
//        if (conversation.getConversationBody() instanceof YWCustomConversationBody) {
//            YWCustomConversationBody body = (YWCustomConversationBody) conversation.getConversationBody();
//            if (body.getIdentity().equals(FragmentTabs.SYSTEM_TRIBE_CONVERSATION)) {
//                return "群系统消息";
//            }else if (body.getIdentity().equals(FragmentTabs.SYSTEM_FRIEND_REQ_CONVERSATION)) {
//                return "联系人系统消息";
//            } else if(body.getIdentity().equals("custom_view_conversation")) {
//                return "自定义View展示的会话";
//            }
//        }
        return null;
    }

    /**
     * 返回会话长按弹出的对话框列表（默认有置顶和删除）
     *
     * @return
     */
    @Override
    public ArrayList<String> getLongClickMenuList(Fragment fragment,
                                                  YWConversation conversation) {
        ArrayList<String> list = new ArrayList<String>();

        //这里可以添加菜单项
        //list.add("test1");
        //list.add("test2");

        return list;
    }

    /**
     * 定制会话点击事件，该方法可以定制所有的会话类型
     *
     * @param fragment     会话列表fragment
     * @param conversation 当前点击的会话对象
     * @return true: 使用用户自定义的点击事件  false：使用SDK默认的点击事件
     */
    @Override
    public boolean onItemClick(Fragment fragment, YWConversation conversation) {
//        YWConversationType type = conversation.getConversationType();
//        if (type == YWConversationType.P2P){
//            //TODO 单聊会话点击事件
//            if()
//            Toast.makeText(fragment.getActivity(),conversation.getConversationId(),Toast.LENGTH_SHORT).show();
//            Toast.makeText(fragment.getActivity(),conversation.getMessageSender()+"",Toast.LENGTH_SHORT).show();

//        YWP2PConversationBody conversationBody = (YWP2PConversationBody) conversation
//                .getConversationBody();
//        Log.e("EEEEEE","1");
//            String id=conversation.getConversationId();
//            id=id.substring(id.length()-11,id.length());
//            Toast.makeText(fragment.getActivity(), conversationBody.getContact().getUserId(),Toast.LENGTH_SHORT).show();
//
//            if(id.equals("12345678910"))
//            return true;
//        }
// else if (type == YWConversationType.Tribe){
//            //TODO 群会话点击事件
//            return true;
//        } else if (type == YWConversationType.Custom){
//            //TODO 自定义会话点击事件
//            return true;
//        }
        return false;
    }

    /**
     * 定制会话长按事件，该方法可以定制所有的会话类型
     *
     * @param fragment     会话列表fragment
     * @param conversation 当前点击的会话对象
     * @return true: 使用用户自定义的长按事件  false：使用SDK默认的长按事件
     */
    @Override
    public boolean onConversationItemLongClick(Fragment fragment, YWConversation conversation) {
//        YWConversationType type = conversation.getConversationType();
//        if (type == YWConversationType.P2P){
//            //TODO 单聊会话长按事件
//            return true;
//        } else if (type == YWConversationType.Tribe){
//            //TODO 群会话长按事件
//            return true;
//        } else if (type == YWConversationType.Custom){
//            //TODO 自定义会话长按事件
//            return true;
//        }
        return false;
    }
}
