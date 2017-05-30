package com.szdd.qianxun.message.baichuan.im;

import com.alibaba.wxlib.util.SimpleKVStore;

/**
 * 工程的一些持久化存储
 */
public class DemoSimpleKVStore {
    private static String NEED_SOUND = "need_sound";//是否静音
    private static String NEED_VIBRATION = "need_vibration";//是否震动
    private static String NEED_NOTE = "need_note";//是否通知栏

    public static int getNeedSound() {
        return SimpleKVStore.getIntPrefs(NEED_SOUND, 1);
    }

    public static void setNeedSound(int value) {
        SimpleKVStore.setIntPrefs(NEED_SOUND, value);
    }

    public static int getNeedVibration() {
        return SimpleKVStore.getIntPrefs(NEED_VIBRATION, 1);
    }

    public static void setNeedVibration(int value) {
        SimpleKVStore.setIntPrefs(NEED_VIBRATION, value);
    }

    public static int getNeedNote() {
        return SimpleKVStore.getIntPrefs(NEED_NOTE, 1);
    }

    public static void setNeedNote(int value) {
        NotificationInitSampleHelper.setNeedNote(value == 1 ? true : false);
        SimpleKVStore.setIntPrefs(NEED_NOTE, value);
    }
}
