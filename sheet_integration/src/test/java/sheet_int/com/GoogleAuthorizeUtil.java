package sheet_int.com;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;

public class GoogleAuthorizeUtil {
	
    // build GoogleClientSecrets from JSON file

    	 private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    	    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    	    public static Credential authorize() throws IOException, GeneralSecurityException {
    	        // Load client secrets JSON file
    	        InputStream in = GoogleAuthorizeUtil.class.getResourceAsStream("/first.json");
    	        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

    	        // Set up authorization flow
    	        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
    	                GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, clientSecrets,
    	                Arrays.asList(SheetsScopes.SPREADSHEETS))
    	                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
    	                .build();

    	        // Authorize
    	        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        
    }
}


