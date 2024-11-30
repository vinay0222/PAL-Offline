package com.idreameducation.ipreppal.deviceIdService;


import android.util.Log;

/**
 * 调试日志工具类
 *
 * @author 贵州山魈羡民 (1032694760@qq.com)
 * @since 2021/3/26 21:34
 */
public final class OAIDLog {
    private static final String TAG = "OAID";
    private static boolean enable = false;

    private OAIDLog() {
        super();
    }

    /**
     * 启用调试日志
     */
    public static void enable() {
        enable = true;
    }

    /**
     * 打印调试日志
     *
     * @param log 日志信息
     */
    public static void print(Object log) {
        if (!enable) {
            return;
        }
        if (log == null) {
            log = "<null>";
        }
        Log.d(TAG, log.toString());
    }

}
