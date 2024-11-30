package com.idreameducation.ipreppal.deviceIdService;

/**
 * OAID接口
 *
 * @author 大定府羡民（1032694760@qq.com）
 * @since 2020/5/30
 */
public interface IOAID {

    /**
     * 是否支持OAID
     *
     * @return 支持则返回true，不支持则返回false
     */
    boolean supported();

    /**
     * 异步获取OAID
     *
     * @param getter 回调
     */
    void doGet(IGetter getter);

}
