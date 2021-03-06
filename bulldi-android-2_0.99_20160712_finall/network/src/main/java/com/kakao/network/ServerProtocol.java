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
package com.kakao.network;

import com.kakao.util.helper.log.Logger.DeployPhase;

/**
 * @author MJ
 */
public final class ServerProtocol {
    private static final DeployPhase DEPLOY_PHASE = DeployPhase.current();
    public static final String AUTH_AUTHORITY = initAuthAuthority();
    public static final String AGE_AUTH_AUTHORITY = initAgeAuthAuthority();
    public static final String API_AUTHORITY = initAPIAuthority();

    //Authorization: Bearer
    public static final String AUTHORIZATION_HEADER_KEY ="Authorization";
    public static final String AUTHORIZATION_BEARER ="Bearer";
    public static final String AUTHORIZATION_HEADER_DELIMITER = " ";

    // oauth url
    public static final String AUTHORIZE_CODE_PATH = "oauth/authorize";
    public static final String ACCESS_TOKEN_PATH = "oauth/token";
    public static final String ACCESS_AGE_AUTH_PATH = "ageauths/main.html";

    // api url
    private static final String API_VERSION = "v1";
    // usermgmt
    public static final String USER_ME_PATH = API_VERSION + "/user/me";
    public static final String USER_LOGOUT_PATH = API_VERSION + "/user/logout";
    public static final String USER_SIGNUP_PATH = API_VERSION + "/user/signup";
    public static final String USER_UNLINK_PATH = API_VERSION + "/user/unlink";
    public static final String USER_UPDATE_PROFILE_PATH = API_VERSION + "/user/update_profile";
    public static final String USER_ACCESS_TOKEN_INFO_PATH = API_VERSION + "/user/access_token_info";
    public static final String USER_AGE_AUTH = API_VERSION + "/user/ageauth";

    // push
    public static final String PUSH_REGISTER_PATH = API_VERSION + "/push/register";
    public static final String PUSH_TOKENS_PATH = API_VERSION + "/push/tokens";
    public static final String PUSH_DEREGISTER_PATH = API_VERSION + "/push/deregister";
    public static final String PUSH_SEND_PATH = API_VERSION + "/push/send";

    //POST
    public static final String STORY_MULTI_UPLOAD_PATH = API_VERSION + "/api/story/upload/multi";
    public static final String STORY_POST_NOTE_PATH = API_VERSION + "/api/story/post/note";
    public static final String STORY_POST_PHOTO_PATH = API_VERSION + "/api/story/post/photo";
    public static final String STORY_POST_LINK_PATH = API_VERSION + "/api/story/post/link";
    //GET
    public static final String STORY_PROFILE_PATH = API_VERSION + "/api/story/profile";
    public static final String STORY_ACTIVITIES_PATH = API_VERSION + "/api/story/mystories";
    public static final String STORY_ACTIVITY_PATH = API_VERSION + "/api/story/mystory";
    public static final String STORY_LINK_SCRAPPER_PATH = API_VERSION + "/api/story/linkinfo";
    public static final String IS_STORY_USER_PATH = API_VERSION + "/api/story/isstoryuser";
    //DELETE
    public static final String STORY_DELETE_ACTIVITY_PATH = API_VERSION + "/api/story/delete/mystory";

    //talk
    public static final String TALK_PROFILE_PATH = API_VERSION + "/api/talk/profile";
    public static final String TALK_MESSAGE_SEND = API_VERSION + "/api/talk/message/send";
    public static final String TALK_CHAT_LIST_PATH = API_VERSION + "/api/talk/chats";

    // friends and operation
    public static final String GET_FRIENDS_PATH = API_VERSION + "/friends";
    public static final String GET_FRIENDS_OPERATION_PATH = API_VERSION + "/friends/operation";

    // storage
    public static final String STORAGE_UPLOAD_IMAGE = API_VERSION + "/storage/image/upload";

    private static String initAuthAuthority() {
        switch (DEPLOY_PHASE) {
            case Local:
                return "localhost:";
            case Alpha:
                return "alpha-kauth.kakao.com";
            case Sandbox:
                return "sandbox-kauth.kakao.com";
            case Beta:
                return "beta-kauth.kakao.com";
            case Release:
                return "kauth.kakao.com";
            default:
                return null;
        }
    }

    private static String initAPIAuthority() {
        switch (DEPLOY_PHASE) {
            case Local:
                return "localhost:";
            case Alpha:
                return "alpha-kapi.kakao.com";
            case Sandbox:
                return "sandbox-kapi.kakao.com";
            case Beta:
                return "beta-kapi.kakao.com";
            case Release:
                return "kapi.kakao.com";
            default:
                return null;
        }
    }

    private static String initAgeAuthAuthority() {
        switch (DEPLOY_PHASE) {
            case Local:
                return "localhost:";
            case Alpha:
                return "alpha-auth.kakao.com";
            case Sandbox:
                return "sandbox-auth.kakao.com";
            case Beta:
                return "beta-auth.kakao.com";
            case Release:
                return "auth.kakao.com";
            default:
                return null;
        }
    }
}
