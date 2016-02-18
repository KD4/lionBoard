package com.github.lionboard.common;

/**
 * Created by Lion.k on 16. 2. 9..
 */

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class OAuthHeaderTwitter {

    private String consumer_key;

    public void setConsumer_key(String consumer_key) {
        this.consumer_key = consumer_key;
    }

    private String consumer_secret;

    public void setConsumer_secret(String consumer_secret) {
        this.consumer_secret = consumer_secret;
    }

    private RequestToken requestToken = null;
    private AccessToken accessToken = null;
    private Twitter twitter = null;


    public OAuthHeaderTwitter() {
        twitter = new TwitterFactory().getInstance();
    }
    public Twitter getTwitter(){
        return twitter;
    }

    // 인증 요청 토큰 생성
    public void setRequestToken(String callbackHost)  throws TwitterException{
        twitter.setOAuthConsumer(this.consumer_key, this.consumer_secret);
        requestToken = twitter.getOAuthRequestToken(callbackHost+"/auth/twitter_success");

    }
    public RequestToken getRequestToken(){
        return requestToken;
    }


    // 인증된 토큰 생성
    public void setAccessToken(String request_token, String request_tokensecret, String oauth_verifier)	 {
        try {
            twitter.setOAuthConsumer(this.consumer_key, this.consumer_secret);
            accessToken = twitter.getOAuthAccessToken(new RequestToken(request_token, request_tokensecret),
                    oauth_verifier);
        } catch (TwitterException te) {
            System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + te);
        }

        twitter.setOAuthAccessToken(accessToken);
    }
    public AccessToken getAccessToken(){
        return accessToken;
    }

    // 재인증 처리
    public void signIn(String access_token, String access_tokensecret) {

        twitter.setOAuthConsumer(this.consumer_key, this.consumer_secret);

        twitter.setOAuthAccessToken(new AccessToken(access_token, access_tokensecret));
    }

}
