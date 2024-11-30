/*
 * Copyright (c) 2016-present 贵州纳雍穿青人李裕江<1032694760@qq.com>
 *
 * The software is licensed under the Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *     http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 * PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package com.idreameducation.ipreppal.deviceIdService.impl;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.Log;

import com.idreameducation.ipreppal.deviceIdService.IGetter;
import com.idreameducation.ipreppal.deviceIdService.IOAID;
import com.idreameducation.ipreppal.deviceIdService.OAIDLog;


/**
 * @author 贵州山野羡民（1032694760@qq.com）
 * @since 2021/8/26 17:09
 */
public class FreemeImpl implements IOAID {
    private final Context context;

    public FreemeImpl(Context context) {
        this.context = context;
    }

    @Override
    public boolean supported() {
        if (context == null) {
            return false;
        }
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo("com.android.creator", 0);
            return pi != null;
        } catch (Exception e) {
            OAIDLog.print(e);
            return false;
        }
    }

    @Override
    public void doGet(final IGetter getter) {
        Log.e("TAG","因奇怪缘由，注释掉此处代码");
       /* if (context == null || getter == null) {
            return;
        }
        Intent intent = new Intent("android.service.action.msa");
        intent.setPackage("com.android.creator");
        OAIDService.bind(context, intent, getter, new OAIDService.RemoteCaller() {
            @Override
            public String callRemoteInterface(IBinder service) throws OAIDException, RemoteException {
                IdsSupplier anInterface = null;// = IdsSupplier.asInterface(service);
                if (anInterface == null) {
                    throw new OAIDException("IdsSupplier is null");
                }
                return anInterface.getOAID();

            }
        });*/
    }

}
