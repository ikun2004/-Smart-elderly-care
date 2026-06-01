package cn.yangeit.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WechatService {
    // 登录
    private static final String REQUEST_URL = "https://api.weixin.qq.com/sns/jscode2session";
    // 获取token
    private static final String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";
    // 获取手机号
    private static final String PHONE_REQUEST_URL = "https://api.weixin.qq.com/wxa/business/getuserphonenumber";

    private String appId = "wx56f970623887f89a"; // 请替换为实际appid
    private String appSecret = "6d2d8afc48f168e47419a85979805edb"; // 请替换为实际appsecret

    /**
     * 获取openid
     * @param code 微信临时登录凭证
     * @return openid
     */
    public String getOpenid(String code) {
        Map<String, Object> params = getAppConfig();
        params.put("js_code", code);
        params.put("grant_type", "authorization_code");

        String result = HttpUtil.get(REQUEST_URL, params);
        JSONObject jsonObject = JSONUtil.parseObj(result);

        // 更健壮的错误处理
        if (jsonObject.containsKey("errcode") && jsonObject.getInt("errcode") != 0) {
            throw new RuntimeException("微信接口错误: " + jsonObject.getStr("errmsg"));
        }
        return jsonObject.getStr("openid");
    }

    /**
     * 获取用户手机号
     * @param phoneCode 微信手机号授权凭证
     * @return 手机号
     */
    public String getPhoneNumber(String phoneCode) {
        String token = getToken();
        String url = PHONE_REQUEST_URL + "?access_token=" + token;

        Map<String, Object> params = new HashMap<>();
        params.put("code", phoneCode);

        String result = HttpUtil.post(url, JSONUtil.toJsonStr(params));
        JSONObject jsonObject = JSONUtil.parseObj(result);

        // 优化错误处理逻辑
        Integer errcode = jsonObject.getInt("errcode");
        if (errcode != null && errcode != 0) {
            throw new RuntimeException("获取手机号失败: " + jsonObject.getStr("errmsg"));
        }

        // 修正字段名：微信返回的是 purePhoneNumber
        return jsonObject.getJSONObject("phone_info").getStr("purePhoneNumber");
    }

    /**
     * 获取微信AccessToken
     * @return access_token
     */
    private String getToken() {
        Map<String, Object> params = getAppConfig();
        params.put("grant_type", "client_credential");

        String result = HttpUtil.get(TOKEN_URL, params);
        JSONObject jsonObject = JSONUtil.parseObj(result);

        // 更健壮的错误处理
        if (jsonObject.containsKey("errcode") && jsonObject.getInt("errcode") != 0) {
            throw new RuntimeException("获取Token失败: " + jsonObject.getStr("errmsg"));
        }
        return jsonObject.getStr("access_token");
    }

    /**
     * 获取微信应用配置
     * @return 配置参数Map
     */
    private Map<String, Object> getAppConfig() {
        Map<String, Object> params = new HashMap<>();
        params.put("appid", appId);
        params.put("secret", appSecret);
        return params;
    }
}