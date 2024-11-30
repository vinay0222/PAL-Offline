package com.idreameducation.ipreppal.deviceIdService;

/**
 * OAID获取回调
 *
 * @author 大定府羡民（1032694760@qq.com）
 * @since 2020/8/20
 */
public interface IGetter {

    /**
     * 成功获取到OAID
     *
     * @param result OAID
     */
    void onOAIDGetComplete(String result);

    /**
     * OAID获取失败（不正常或获取不到）
     *
     * @param error 异常信息
     */
    void onOAIDGetError(Exception error);

}
