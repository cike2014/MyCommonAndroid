package com.mmednet.common.okhttp.builder;

import com.mmednet.common.okhttp.OkHttpUtils;
import com.mmednet.common.okhttp.request.OtherRequest;
import com.mmednet.common.okhttp.request.RequestCall;

/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers,id).build();
    }
}
