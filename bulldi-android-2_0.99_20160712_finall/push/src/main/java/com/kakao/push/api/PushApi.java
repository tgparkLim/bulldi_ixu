/**
 * Copyright 2014 Daum Kakao Corp.
 *
 * Redistribution and modification in source or binary forms are not permitted without specific prior written permission. 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kakao.push.api;

import com.kakao.auth.SingleNetworkTask;
import com.kakao.auth.network.response.ApiResponse.BlankApiResponse;
import com.kakao.network.response.ResponseData;
import com.kakao.push.request.DeregisterPushTokenRequest;
import com.kakao.push.request.GetPushTokensRequest;
import com.kakao.push.request.RegisterPushTokenRequest;
import com.kakao.push.request.SendPushRequest;
import com.kakao.push.response.GetPushTokenResponse;
import com.kakao.push.response.RegisterPushTokenResponse;

/**
 * Bloking으로 동작하며, push에 관련된 내부 API콜을 한다.
 * @author leoshin, created at 15. 8. 10..
 */
public class PushApi {

    /**
     * 현 기기의 푸시 토큰을 등록한다.
     * 푸시 토큰 등록 후 푸시 토큰 삭제하기 전 또는 만료되기 전까지 서버에서 관리되어 푸시 메시지를 받을 수 있다.
     * @param pushToken 등록할 푸시 토큰
     * @param deviceId 한 사용자가 여러 기기를 사용할 수 있기 때문에 기기에 대한 유일한 id도 필요
     */
    public static RegisterPushTokenResponse registerPushToken(final String pushToken, final String deviceId) throws Exception {
        SingleNetworkTask networkTask = new SingleNetworkTask();
        ResponseData result = networkTask.requestApi(new RegisterPushTokenRequest(pushToken, deviceId));
        return new RegisterPushTokenResponse(result);
    }

    /**
     * 현 사용자 ID로 등록된 모든 푸시토큰 정보를 반환한다.
     */
    public static GetPushTokenResponse getPushTokens() throws Exception {
        SingleNetworkTask networkTask = new SingleNetworkTask();
        ResponseData result = networkTask.requestApi(new GetPushTokensRequest());
        return new GetPushTokenResponse(result);
    }

    /**
     * 사용자의 해당 기기의 푸시 토큰을 삭제한다. 대게 로그아웃시에 사용할 수 있다.
     * @param deviceId 해당기기의 푸시 토큰만 삭제하기 위해 기기 id 필요
     */
    public static void deregisterPushToken(final String deviceId) throws Exception {
        SingleNetworkTask newtworkTask = new SingleNetworkTask();
        ResponseData result = newtworkTask.requestApi(new DeregisterPushTokenRequest(deviceId));
        new BlankApiResponse(result);
    }

    /**
     * 자기 자신에게 푸시 메시지를 전송한다. 테스트 용도로만 사용할 수 있다. 다른 사람에게 푸시를 보내기 위해서는 서버에서 어드민키로 REST API를 사용해야한다.
     * @param pushMessage 보낼 푸시 메시지
     * @param deviceId 푸시 메시지를 보낼 기기의 id
     */
    public static void sendPushMessage(final String pushMessage, final String deviceId) throws Exception {
        SingleNetworkTask newtworkTask = new SingleNetworkTask();
        ResponseData result = newtworkTask.requestApi(new SendPushRequest(pushMessage, deviceId));
        new BlankApiResponse(result);
    }
}
