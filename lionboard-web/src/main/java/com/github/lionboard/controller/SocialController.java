package com.github.lionboard.controller;

import com.github.lionboard.model.User;
import com.github.lionboard.service.LionBoardService;
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
        System.out.println(nextLine);
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

    //TODO facebook,twitter,kakao,daum 각 provider 구별 방법
    //TODO normal 로그인을 시도하는 유저가 Oauth 유저면 예외처리
    //TODO email 인증처리

}