// ISerialConfigParams.aidl
package com.evideostb.training.chenhuan.aidl;
import com.evideostb.training.chenhuan.aidl.IOutDataStream;
import com.evideostb.training.chenhuan.aidl.SerialConfigParams;
import com.evideostb.training.chenhuan.aidl.OutDataStream;

// Declare any non-default types here with import statements

interface ISerialConfigParams {
    /**
     *客户端对服务端调用的方法                                                                                                         Map getMap(in String country, in Product product);
    **/
    void setConfigParams(in SerialConfigParams serailConfig);

    /**
     * 服务端获取客户端参数
    **/
    SerialConfigParams getConfigParams();

    /**
     *用来注册回调的对象
    **/
    void registerCallback(in IOutDataStream callback);
    void unregisterCallback(in IOutDataStream callback);

}
