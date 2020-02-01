package org.superbapps.integrations.jira.infra.oauth;

import com.google.api.client.auth.oauth.OAuthParameters;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class JiraOAuthClient {

	private final JiraOAuthTokenFactory oAuthGetAccessTokenFactory;
	private final String                authorizationUrl;

	public JiraOAuthClient(String homeUrl) {
		oAuthGetAccessTokenFactory = new JiraOAuthTokenFactory( homeUrl );
		authorizationUrl = homeUrl + "/plugins/servlet/oauth/authorize";
	}

	/**
	 * Creates OAuthParameters used to make authorized request to JIRA
	 */
	public OAuthParameters getParameters(String tmpToken, String secret, String consumerKey, String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
		JiraOAuthGetAccessToken oAuthAccessToken = oAuthGetAccessTokenFactory.getJiraOAuthGetAccessToken(
				tmpToken, secret, consumerKey, privateKey );
		oAuthAccessToken.verifier = secret;
		return oAuthAccessToken.createParameters();
	}

}
