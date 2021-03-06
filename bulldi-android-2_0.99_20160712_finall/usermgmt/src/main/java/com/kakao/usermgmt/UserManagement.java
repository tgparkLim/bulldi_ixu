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
package com.kakao.usermgmt;

import com.kakao.auth.ApiResponseCallback;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.network.tasks.KakaoResultTask;
import com.kakao.network.tasks.KakaoTaskQueue;
import com.kakao.usermgmt.api.UserApi;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.usermgmt.response.AgeAuthResponse;
import com.kakao.usermgmt.response.model.UserProfile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UserManagement API 요청을 담당한다.
 * @author MJ
 */
public class UserManagement {

    /**
     * 가입 요청
     * @param callback signup 요청 결과에 대한 callback
     * @param properties 가입시 받은 사용자 정보
     */
    public static void requestSignup(final ApiResponseCallback<Long> callback, final Map<String, String> properties) {
        KakaoTaskQueue.getInstance().addTask(new KakaoResultTask<Long>(callback) {
            @Override
            public Long call() throws Exception {
                return UserApi.requestSignup(properties).getUserId();
            }
        });
    }

    /**
     * 로그아웃 요청
     * @param callback logout 요청 결과에 대한 callback
     */
    public static void requestLogout(final LogoutResponseCallback callback) {
        KakaoTaskQueue.getInstance().addTask(new KakaoResultTask<Long>(callback) {
            @Override
            public Long call() throws Exception {
                return UserApi.requestLogout().getUserId();
            }
        });
    }

    /**
     * Unlink 요청
     * @param callback unlink 요청 결과에 대한 handler
     */
    public static void requestUnlink(final UnLinkResponseCallback callback) {
        KakaoTaskQueue.getInstance().addTask(new KakaoResultTask<Long>(callback) {
            @Override
            public Long call() throws Exception {
                return UserApi.requestUnlink().getUserId();
            }
        });
    }

    /**
     * @param callback updateProfile 요청 결과에 대한 callback
     * @param nickName 사용자 이름
     * @param thumbnailImagePath 사용자 profile image thumbnail image path
     * @param profileImage 사용자의 profile image path
     * @param properties 저장할 사용자 extra 정보
     */
    public static void requestUpdateProfile(final ApiResponseCallback<Long> callback, final String nickName, final String thumbnailImagePath, final String profileImage,  final Map<String, String> properties) {
        KakaoTaskQueue.getInstance().addTask(new KakaoResultTask<Long>(callback) {
            @Override
            public Long call() throws Exception {
                Map<String, String> userProperties = properties;
                if (userProperties == null) {
                    userProperties = new HashMap<String, String>();
                }

                properties.put(StringSet.nickname, nickName);
                properties.put(StringSet.thumbnail_image, thumbnailImagePath);
                properties.put(StringSet.profile_image, profileImage);
                return UserApi.requestUpdateProfile(userProperties).getUserId();
            }
        });
    }

    /**
     * 사용자정보 저장 요청
     * @param callback updateProfile 요청 결과에 대한 callback
     * @param properties 저장할 사용자 정보
     */
    public static void requestUpdateProfile(final ApiResponseCallback<Long> callback, final Map<String, String> properties) {
        KakaoTaskQueue.getInstance().addTask(new KakaoResultTask<Long>(callback) {
            @Override
            public Long call() throws Exception {
                return UserApi.requestUpdateProfile(properties).getUserId();
            }
        });
    }

    /**
     * 사용자정보 요청
     * @param callback me 요청 결과에 대한 callback
     */
    public static void requestMe(final MeResponseCallback callback) {
        requestMe(callback, null, false);
    }

    /**
     * 사용자 정보 일부나 이미지 리소스를 https로 받고 싶은 경우의 사용자정보 요청
     * @param callback me 요청 결과에 대한 callback
     * @param propertyKeys 사용자 정보의 키 리스트
     * @param secureResource 이미지 url을 https로 반환할지 여부
     */
    public static void requestMe(final MeResponseCallback callback, final List<String> propertyKeys, final boolean secureResource) {
        KakaoTaskQueue.getInstance().addTask(new KakaoResultTask<UserProfile>(callback) {
            @Override
            public UserProfile call() throws Exception {
                return UserApi.requestMe(propertyKeys, secureResource).getUserProfile();
            }
        });
    }

    /**
     * 토큰으로 인증날짜와 CI값을 얻는다. 게임 사업부가 저장하고 있는 정보를 내려준다.
     * (제휴를 통해 권한이 부여된 특정 앱에서만 호출이 가능합니다.)
     * @param callback 요청 결과에 대한 callback
     */
    public static void requestCheckAgeAuth(final ResponseCallback<AgeAuthResponse> callback) {
        KakaoTaskQueue.getInstance().addTask(new KakaoResultTask<AgeAuthResponse>(callback) {
            @Override
            public AgeAuthResponse call() throws Exception {
                return UserApi.requestCheckAgeAuth();
            }
        });
    }
}
