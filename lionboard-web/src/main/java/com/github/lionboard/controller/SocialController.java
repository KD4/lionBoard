package com.github.lionboard.controller;

import com.github.lionboard.error.InvalidCmtException;
import com.github.lionboard.error.InvalidUserException;
import com.github.lionboard.model.User;
import com.github.lionboard.service.LionBoardService;
import common.OAuthHeaderTwitter;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Lion.k on 16. 2. 7..
 */

@Controller
@RequestMapping("/auth")
@PropertySource("classpath:/config/local.properties")
public class SocialController {

    @Resource
    private Environment environment;

    @Autowired
    LionBoardService lionBoardService;


    @RequestMapping(value = "/facebook")
    public String facebookOAuth(HttpSession session)  {

        session.setAttribute("facebookAppKey", environment.getProperty("facebook.app.id"));
        session.setAttribute("appSecret", environment.getProperty("facebook.app.secret"));

        session.setAttribute("url", "http://www.facebook.com/dialog/oauth?client_id=" + environment.getProperty("facebook.app.id")
                + "&redirect_uri="+environment.getProperty("callback.host")+"/auth/facebook_success&scope=public_profile,email");

        return "redirect:"+(String)session.getAttribute("url");

    }

    @RequestMapping(value = "/facebook_success")
    public String processFacebookSignIn(HttpSession session, HttpServletRequest request) throws ClientProtocolException,IOException {
        String code = request.getParameter("code");
        String accessToken = "";
        String result = "";
        if (StringUtils.isNotEmpty(code)) {
            HttpGet get = new HttpGet("https://graph.facebook.com/oauth/access_token" + "?client_id=" + environment.getProperty("facebook.app.id")
                    + "&client_secret=" + environment.getProperty("facebook.app.secret") + "&redirect_uri="+environment.getProperty("callback.host")+"/auth/facebook_success"
                    + "&code=" + code);

            HttpClient http = HttpClientBuilder.create().build();
            result = http.execute(get, new BasicResponseHandler());
            accessToken = result.substring(result.indexOf("=") + 1);

        }
        URL url = null;
        URLConnection urlConnection = null;
        String sUrl = "https://graph.facebook.com/me?fields=id,name,email,gender,picture.type(large),birthday,location&access_token=";

        url = new URL(sUrl + accessToken);

        urlConnection = url.openConnection();

        InputStream ist = urlConnection.getInputStream();
        InputStreamReader isr = new InputStreamReader(ist);
        BufferedReader br = new BufferedReader(isr);
        String nextLine = br.readLine();
        JSONObject jObject = new JSONObject(nextLine);

        User user = new User();
        user.setIdentity(jObject.getString("id"));
        user.setEmail(jObject.getString("email"));
        user.setName(jObject.getString("name"));
        user.setProfileUrl(jObject.getJSONObject("picture").getJSONObject("data").getString("url"));
        user.setRoles("ROLE_USER");
        user.setIsOAuth("T");
        user.setPassword("");

        User existedUser = lionBoardService.existUserByIdentity(user.getIdentity());
        if(existedUser == null){
            lionBoardService.addUser(user);
            lionBoardService.securityLogin(user);
        }else{
            lionBoardService.securityLogin(user);
        }
        return "redirect:/index";
    }

    @RequestMapping(value = "/twitter")
    public String twitter_access(HttpSession session) throws Exception{

        OAuthHeaderTwitter twitter_api = new OAuthHeaderTwitter();

        // 트위터 앱 생성후 받은 토큰
        twitter_api.setConsumer_key(environment.getProperty("twitter.app.id"));
        twitter_api.setConsumer_secret(environment.getProperty("twitter.app.secret"));



        // 인증 요청 토큰 생성
        twitter_api.setRequestToken();

        String requestToken = twitter_api.getRequestToken().getToken();
        String requestTokenSecret = twitter_api.getRequestToken().getTokenSecret();

        System.out.println("after redirecting, request Token is " + requestToken);

        //Todo : store the requestToken and secret to redis cache.

        String authenticationUrl = twitter_api.getRequestToken().getAuthenticationURL();


        // 인증url 리다이렉트
        return "redirect:"+authenticationUrl;

    }

    @RequestMapping(value = "/twitter_success")
    public String twitter_success(HttpSession session, HttpServletRequest request) throws Exception {

        // 트위터에서 받은 인증토큰
        String oauthToken = request.getParameter("oauth_token"); // requestToken 토큰과 같은 값이여야 한다. (보안)
        String oauthVerifier = request.getParameter("oauth_verifier"); // 트위터에서 인증요청하면서 생성한 토큰

        //Todo : Get the requestToken and secret from redis cache.
        String requestToken = (String) session.getAttribute("requestToken");
        String requestTokenSecret = (String) session.getAttribute("requestTokenSecret");
        String contact = null;

        // 보안상 토큰이 일치하는 지 비교.
        if (requestToken.equals(oauthToken)) {

            OAuthHeaderTwitter twitter_api = new OAuthHeaderTwitter();
            twitter_api.setConsumer_key("oi9uv7nIxPqcTTKu3WpOQeW4C");
            twitter_api.setConsumer_secret("d26Hq4TMBo2uNOplbI5hbmKObXKB4BUgl335ODOYNfymawVkv3");


            // 인증된 토큰 생성
            twitter_api.setAccessToken(requestToken, requestTokenSecret, oauthVerifier);

            // 인증된 토큰을 변수에 담는다.
            String accessToken = twitter_api.getAccessToken().getToken();
            String accessTokenSecret = twitter_api.getAccessToken().getTokenSecret();


            User user = new User();
            user.setIdentity(Long.toString(twitter_api.getTwitter().verifyCredentials().getId()));
            user.setEmail(Long.toString(twitter_api.getTwitter().verifyCredentials().getId()));
            user.setName(twitter_api.getTwitter().verifyCredentials().getName());
            user.setProfileUrl(twitter_api.getTwitter().verifyCredentials().getOriginalProfileImageURL());
            user.setRoles("ROLE_USER");
            user.setIsOAuth("T");
            user.setPassword("");

            User existedUser = lionBoardService.existUserByIdentity(user.getIdentity());
            if (existedUser == null) {
                lionBoardService.addUser(user);
                lionBoardService.securityLogin(user);
            } else {
                lionBoardService.securityLogin(user);
            }

            return "redirect:/index";
        }else{
            throw new InvalidUserException("twitter login fail.");
        }

    }

    //TODO facebook,twitter,kakao,daum 각 provider 구별 방법
    //TODO normal 로그인을 시도하는 유저가 Oauth 유저면 예외처리
    //TODO email 인증처리

}