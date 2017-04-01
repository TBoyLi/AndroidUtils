/**
 * Copyright 2016 smartbetter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.smartbetter.utilslibrary;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

/**
 * Created by gc on 2017/2/21.
 */
public class DevicesUtils {

    private DevicesUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获取系统版本
     *
     * @return
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取设备型号
     *
     * @return
     */
    public static String getDevicesModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取设备ID
     *
     * @return
     */
    public static String getDevicesId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    /**
     * 获取日期
     *
     * @return
     */
    public static String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

    /**
     * 获取 SD 卡路径
     *
     * @param context
     * @param paramString
     * @return
     */
    public static String getSDCacheDir(Context context, String paramString) {
        String absoultePath = "";
        if ("mounted".equals(Environment.getExternalStorageState()))
            absoultePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        if (absoultePath == null) {
            File cacheDir = context.getCacheDir();
            if (cacheDir != null) {
                if (cacheDir.exists())
                    absoultePath = cacheDir.getPath();
            }
        }
        return absoultePath + File.separator + paramString;
    }

    /**
     * 获取mac地址
     *
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String mac = info.getMacAddress();
        return mac;
    }

    /**
     * 获取设备IP
     *
     * @return
     */
    public static String getDevicesIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
                    .hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
                        .hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}