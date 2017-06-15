package com.szdd.qianxun.tools.file;

/**
 * Created by DELL on 2016/4/1.
 */
public class ServiceType {

    public static int getServiceCount() {
        return 15;
    }

    public static String getServiceType(int i) {
        String str = "";
        switch (i) {
            case 0:
                str = "手绘素描";
                break;
            case 1:
                str = "陪玩运动";
                break;
            case 2:
                str = "摄影PS";
                break;
            case 3:
                str = "心灵氧吧";
                break;
            case 4:
                str = "手工DIY";
                break;
            case 5:
                str = "才艺小屋";
                break;
            case 6:
                str = "学霸课桌";
                break;
            case 7:
                str = "乐活族";
                break;
            case 8:
                str = "美妆达人";
                break;
            case 9:
                str = "商业街";
                break;
            case 10:
                str = "租赁园";
                break;
            case 11:
                str = "闲置宝贝";
                break;
            case 12:
                str = "零食水果";
                break;
            case 13:
                str = "代办跑腿";
                break;
            case 14:
                str = "其他";
                break;
        }
        return str;
    }

    public static String getServiceTypeText(int i) {
        String str = "";
        switch (i) {
            case 0:
                str = "手绘我心，素描人生";
                break;
            case 1:
                str = "寻找好基友告别单身狗";
                break;
            case 2:
                str = "艺术与现实，永远是两个样子。";
                break;
            case 3:
                str = "让心灵做一次有氧呼吸";
                break;
            case 4:
                str = "有人靠脸吃饭，我靠手吃饭！";
                break;
            case 5:
                str = "我演奏的不是寂寞，是传说呀！";
                break;
            case 6:
                str = "学霸做同桌，考试带你过";
                break;
            case 7:
                str = "乐享生活，逛吃全国";
                break;
            case 8:
                str = "点一下，你就是全世界";
                break;
            case 9:
                str = "给我一个口袋给你整个世界";
                break;
            case 10:
                str = "宝贝的每一秒都忽然充满意义";
                break;
            case 11:
                str = "你闲置的正是我需要的\n交换让彼此生活更美好";
                break;
            case 12:
                str = "吃饱这一顿再去减肥可好？";
                break;
            case 13:
                str = "用我的加速度成全你生活的速度！";
                break;
            case 14:
                str = "先捂脸不给看，猜中我就告诉你！";
                break;
        }
        return str;
    }

    public static String getServiceWay(int i) {
        String str = "";
        switch (i) {
            case 1:
                str = "线上";
                break;
            case 2:
                str = "指定";
                break;
            case 3:
                str = "邮寄";
                break;
            case 4:
                str = "上门";
                break;
        }
        return str;
    }
}
