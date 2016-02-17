package com.github.lionboard.controller;

import com.github.lionboard.error.InvalidUserException;
import com.github.lionboard.model.User;
import com.github.lionboard.service.LionBoardService;
import com.github.lionboard.common.OAuthHeaderTwitter;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import twitter4j.TwitterException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

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
    public String accessFacebook()  {
        return "redirect:http://www.facebook.com/dialog/oauth?client_id=" + environment.getProperty("facebook.app.id")+"&redirect_uri="+environment.getProperty("callback.host")+"/auth/facebook_success&scope=public_profile,email";
    }

    @RequestMapping(value = "/facebook_success")
    public String progressFacebook(HttpSession session, HttpServletRequest request) throws ClientProtocolException,IOException {
        String accessToken  = getFBAccessToken(request.getParameter("code"));

        User user = getFBUserInfo(accessToken);

        progressSignIn(user);

        return "redirect:/index";
    }

    @RequestMapping(value = "/kakao")
    public String accessKakao()  {
        return "redirect:https://kauth.kakao.com/oauth/authorize?client_id="+environment.getProperty("kakao.app.id")+"&redirect_uri="+environment.getProperty("kakao.callback")+"&response_type=code";
    }

    @RequestMapping(value = "/kakao_success")
    public String progressKakao(HttpServletRequest request) throws ClientProtocolException,IOException {
        String code = request.getParameter("code");

        String accessToken = getKakaoAccessToken(code);

        User user = getKakaoUserInfo(accessToken);

        progressSignIn(user);

        return "redirect:/index";
    }


    @RequestMapping(value = "/daum")
    public String accessDaum()  {
        return "redirect:https://apis.daum.net/oauth2/authorize?client_id="+environment.getProperty("daum.app.id")+"&redirect_uri="+environment.getProperty("daum.callback")+"&response_type=code";
    }

    @RequestMapping(value = "/daum_success")
    public String progressDaum(HttpServletRequest request) throws ClientProtocolException,IOException {
        String code = request.getParameter("code"); //{authorize_code}

        String accessToken = getDaumAccessToken(code);

        User user = getDaumUserInfo(accessToken);

        progressSignIn(user);

        return "redirect:/index";
    }


    @RequestMapping(value = "/twitter")
    public String accessTwitter(HttpSession session) throws Exception{

        OAuthHeaderTwitter twitter_api = new OAuthHeaderTwitter();

        // 트위터 앱 생성후 받은 토큰
        twitter_api.setConsumer_key(environment.getProperty("twitter.app.id"));
        twitter_api.setConsumer_secret(environment.getProperty("twitter.app.secret"));

        // 인증 요청 토큰 생성
        twitter_api.setRequestToken(environment.getProperty("callback.host"));
        String requestToken = twitter_api.getRequestToken().getToken();
        String requestTokenSecret = twitter_api.getRequestToken().getTokenSecret();

        session.setAttribute("requestToken",requestToken);
        session.setAttribute("requestTokenSecret",requestTokenSecret);
        String authenticationUrl = twitter_api.getRequestToken().getAuthenticationURL();


        // 인증url 리다이렉트
        return "redirect:"+authenticationUrl;

    }

    @RequestMapping(value = "/twitter_success")
    public String progressTwitter(HttpSession session, HttpServletRequest request) throws Exception {

        // 트위터에서 받은 인증토큰
        String oauthToken = request.getParameter("oauth_token"); // requestToken 토큰과 같은 값이여야 한다. (보안)
        String oauthVerifier = request.getParameter("oauth_verifier"); // 트위터에서 인증요청하면서 생성한 토큰
        String requestToken = (String) session.getAttribute("requestToken");
        String requestTokenSecret = (String) session.getAttribute("requestTokenSecret");
        // 보안상 토큰이 일치하는 지 비교.
        if (requestToken.equals(oauthToken)) {
            OAuthHeaderTwitter twitter_api = setTwitterAccessToken(oauthVerifier, requestToken, requestTokenSecret);
            User user = getTwitterUserInfo(twitter_api);
            progressSignIn(user);
            return "redirect:/index";
        }else{
            throw new InvalidUserException("twitter login fail. the oauth token is broken.");
        }

    }





    private void progressSignIn(User user) {
        User existedUser = lionBoardService.existUserByIdentity(user.getIdentity());
        if(existedUser == null){
            lionBoardService.addUser(user);
            lionBoardService.securityLogin(user);
        }else{
            lionBoardService.securityLogin(user);
        }
    }

    private User getFBUserInfo(String accessToken) throws IOException {
        URL url = null;
        URLConnection urlConnection = null;
        String sUrl = "https://graph.facebook.com/me?fields=id,name,email,picture.type(large)&access_token=";

        url = new URL(sUrl + accessToken);

        urlConnection = url.openConnection();

        InputStream ist = urlConnection.getInputStream();
        InputStreamReader isr = new InputStreamReader(ist);
        BufferedReader br = new BufferedReader(isr);
        String nextLine = br.readLine();
        JSONObject jObject = new JSONObject(nextLine);

        User user = new User();
        user.setIdentity("FB"+jObject.getString("id"));
        user.setEmail(jObject.getString("email"));
        user.setName(jObject.getString("name"));
        user.setProfileUrl(jObject.getJSONObject("picture").getJSONObject("data").getString("url"));
        user.setRoles("ROLE_USER");
        user.setIsOAuth("T");
        user.setPassword("");
        return user;
    }

    private String getFBAccessToken(String code) throws IOException {
        String accessToken = "";
        if (StringUtils.isNotEmpty(code)) {
            HttpGet get = new HttpGet("https://graph.facebook.com/oauth/access_token" + "?client_id=" + environment.getProperty("facebook.app.id")
                    + "&client_secret=" + environment.getProperty("facebook.app.secret") + "&redirect_uri="+environment.getProperty("callback.host")+"/auth/facebook_success"
                    + "&code=" + code);

            HttpClient http = HttpClientBuilder.create().build();
            String result = http.execute(get, new BasicResponseHandler());
            accessToken = result.substring(result.indexOf("=") + 1);
        }
        return accessToken;
    }




    private User getTwitterUserInfo(OAuthHeaderTwitter twitter_api) throws TwitterException {
        User user = new User();
        user.setIdentity("TW"+Long.toString(twitter_api.getTwitter().verifyCredentials().getId()));
        user.setEmail(Long.toString(twitter_api.getTwitter().verifyCredentials().getId()));
        user.setName(twitter_api.getTwitter().verifyCredentials().getName());
        user.setProfileUrl(twitter_api.getTwitter().verifyCredentials().getOriginalProfileImageURL());
        user.setRoles("ROLE_USER");
        user.setIsOAuth("T");
        user.setPassword("");
        return user;
    }

    private OAuthHeaderTwitter setTwitterAccessToken(String oauthVerifier, String requestToken, String requestTokenSecret) {
        OAuthHeaderTwitter twitter_api = new OAuthHeaderTwitter();
        twitter_api.setConsumer_key(environment.getProperty("twitter.app.id"));
        twitter_api.setConsumer_secret(environment.getProperty("twitter.app.secret"));


        // 인증된 토큰 생성
        twitter_api.setAccessToken(requestToken, requestTokenSecret, oauthVerifier);
        return twitter_api;
    }



    private User getKakaoUserInfo(String accessToken) throws IOException {
        BufferedReader rd = null;
        InputStreamReader isr = null;
        User user = new User();
        try {


        HttpGet get = new HttpGet("https://kapi.kakao.com/v1/user/me");
        get.setHeader("Authorization","Bearer "+accessToken);
        HttpClient http = HttpClientBuilder.create().build();


        final HttpResponse responseForUserInfo = http.execute(get);
        final int responseCodeForUserInfo = responseForUserInfo.getStatusLine().getStatusCode();

        System.out.println("Response Code : " + responseCodeForUserInfo );


        isr = new InputStreamReader(responseForUserInfo.getEntity().getContent());
        rd = new BufferedReader(isr);
        final StringBuffer bufferForUserInfo = new StringBuffer();
        String lineForUserInfo;
        while ((lineForUserInfo = rd.readLine()) != null) {
            bufferForUserInfo.append(lineForUserInfo);
        }

        System.out.println(bufferForUserInfo);
        JSONObject jObjectForUserInfo = new JSONObject(bufferForUserInfo.toString());
        System.out.println(jObjectForUserInfo);
        user.setIdentity("KA"+String.valueOf(jObjectForUserInfo.getInt("id")));
        user.setEmail(String.valueOf(jObjectForUserInfo.getInt("id")));
        user.setName(jObjectForUserInfo.getJSONObject("properties").getString("nickname"));
        user.setProfileUrl(jObjectForUserInfo.getJSONObject("properties").getString("profile_image"));
        user.setRoles("ROLE_USER");
        user.setIsOAuth("T");
        user.setPassword("");
        if(1 > user.getProfileUrl().trim().length()){
            user.setProfileUrl("http://t1.daumcdn.net/osa/tech/i1.daumcdn.jpg");
        }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // clear resources
            if (rd != null) {
                try {
                    rd.close();
                } catch(Exception ignore) {
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch(Exception ignore) {
                }
            }
            return user;
        }

    }

    private String getKakaoAccessToken(String code) throws IOException {
        BufferedReader rd = null;
        InputStreamReader isr = null;
        String accessToken = "";
        try {
        final HttpPost post = new HttpPost("https://kauth.kakao.com/oauth/token");

        final ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        postParameters.add(new BasicNameValuePair("grant_type", "authorization_code"));
        postParameters.add(new BasicNameValuePair("client_id", environment.getProperty("kakao.app.id")));
        postParameters.add(new BasicNameValuePair("redirect_uri", environment.getProperty("kakao.callback")));
        postParameters.add(new BasicNameValuePair("code", code));

        post.setEntity(new UrlEncodedFormEntity(postParameters));

        HttpClient client = HttpClientBuilder.create().build();


        final HttpResponse response = client.execute(post);


        final int responseCode = response.getStatusLine().getStatusCode();



        isr = new InputStreamReader(response.getEntity().getContent());
        rd = new BufferedReader(isr);
        final StringBuffer buffer = new StringBuffer();
        String line;
        while ((line = rd.readLine()) != null) {
            buffer.append(line);
        }

        JSONObject jObject = new JSONObject(buffer.toString());

        accessToken = jObject.getString("access_token");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // clear resources
            if (rd != null) {
                try {
                    rd.close();
                } catch(Exception ignore) {
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch(Exception ignore) {
                }
            }
            return accessToken;
        }
    }



    private User getDaumUserInfo(String accessToken) throws IOException {
        BufferedReader rd = null;
        InputStreamReader isr = null;
        User user = new User();

        try{
            HttpGet get = new HttpGet("https://apis.daum.net/user/v1/show.json?access_token="+accessToken);
            HttpClient http = HttpClientBuilder.create().build();


            final HttpResponse responseForUserInfo = http.execute(get);
            final int responseCodeForUserInfo = responseForUserInfo.getStatusLine().getStatusCode();

            System.out.println("Response Code : " + responseCodeForUserInfo );


            isr = new InputStreamReader(responseForUserInfo.getEntity().getContent());
            rd = new BufferedReader(isr);
            final StringBuffer bufferForUserInfo = new StringBuffer();
            String lineForUserInfo;
            while ((lineForUserInfo = rd.readLine()) != null) {
                bufferForUserInfo.append(lineForUserInfo);
            }

            System.out.println(bufferForUserInfo);
            JSONObject jObjectForUserInfo = new JSONObject(bufferForUserInfo.toString());
            System.out.println(jObjectForUserInfo);

            user.setIdentity("DM" + String.valueOf(jObjectForUserInfo.getJSONObject("result").getInt("id")));
            user.setEmail(String.valueOf(jObjectForUserInfo.getJSONObject("result").getInt("id")));
            user.setName(jObjectForUserInfo.getJSONObject("result").getString("nickname"));
            user.setProfileUrl(jObjectForUserInfo.getJSONObject("result").getString("imagePath"));
            user.setRoles("ROLE_USER");
            user.setIsOAuth("T");
            user.setPassword("");


            if(1 > user.getProfileUrl().trim().length()){
                user.setProfileUrl("http://t1.daumcdn.net/osa/tech/i1.daumcdn.jpg");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // clear resources
            if (rd != null) {
                try {
                    rd.close();
                } catch(Exception ignore) {
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch(Exception ignore) {
                }
            }
            return user;
        }
    }

    private String getDaumAccessToken(String code) throws IOException {
        String accessToken="";
        BufferedReader rd = null;
        InputStreamReader isr = null;
        try{
        final HttpPost post = new HttpPost("https://apis.daum.net/oauth2/token");

        final ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        postParameters.add(new BasicNameValuePair("client_id", environment.getProperty("daum.app.id")));
        postParameters.add(new BasicNameValuePair("client_secret", environment.getProperty("daum.app.secret")));
        postParameters.add(new BasicNameValuePair("redirect_uri", environment.getProperty("daum.callback")));
        postParameters.add(new BasicNameValuePair("code", code));
        postParameters.add(new BasicNameValuePair("grant_type", "authorization_code"));

        post.setEntity(new UrlEncodedFormEntity(postParameters));

        HttpClient client = HttpClientBuilder.create().build();


        final HttpResponse response = client.execute(post);


        final int responseCode = response.getStatusLine().getStatusCode();

        System.out.println("Post parameters : " + postParameters);
        System.out.println("Response Code : " + responseCode);


        isr = new InputStreamReader(response.getEntity().getContent());
        rd = new BufferedReader(isr);
        final StringBuffer buffer = new StringBuffer();
        String line;
        while ((line = rd.readLine()) != null) {
            buffer.append(line);
        }
        JSONObject jObject = new JSONObject(buffer.toString());
        accessToken = jObject.getString("access_token");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // clear resources
            if (rd != null) {
                try {
                    rd.close();
                } catch(Exception ignore) {
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch(Exception ignore) {
                }
            }
            return accessToken;
        }
    }


    //TODO facebook,twitter,kakao,daum 각 provider 구별 방법
    //TODO email 인증처리

}