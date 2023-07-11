package sheet_int.com;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesResponse;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

public class GoogleSheetsLiveTest_write {
    private static Sheets sheetsService;
    private static String SPREADSHEET_ID = "1NamFYLEufZ4u4keO1vFENZFneoqBdSywvPXUUhuw7Hw";
    private static final String APPLICATION_NAME = "Google Sheets Example";
    

    
  @BeforeClass
    public static void setup() throws GeneralSecurityException, IOException {
        sheetsService = SheetsServiceUtil.getSheetsService();
    }
  @Test(priority = 1)
  public void whenWriteSheet_thenReadSheetOk() throws IOException, GeneralSecurityException {
      Sheets sheetsService = SheetsServiceUtil.getSheetsService();

      List<List<Object>> values = new ArrayList<>();
      values.add(Arrays.asList((Object) "Name", "Age", "Email"));
      values.add(Arrays.asList((Object) "John Doe", 30, "johndoe@example.com"));
      values.add(Arrays.asList((Object) "Jane Smith", 28, "janesmith@example.com"));
      values.add(Arrays.asList((Object) "Tom Johnson", 35, "tomjohnson@example.com"));
      values.add(Arrays.asList((Object) "rajesh", 25, "rajesh@example.com"));
      
      List<List<Object>> values1 = new ArrayList<>();
      for (List<Object> stringRow : values) {
          List<Object> objectRow = new ArrayList<>(stringRow);
          values1.add(objectRow);
      }

      BatchUpdateValuesRequest requestBody = new BatchUpdateValuesRequest();
      requestBody.setValueInputOption("USER_ENTERED");
      requestBody.setData(Arrays.asList(new ValueRange().setRange("Sheet1!A1:C5").setValues(values)));

      
      
      sheetsService.spreadsheets().values().batchUpdate(SPREADSHEET_ID, requestBody).execute();
	}
  

	  @Test(priority = 2)
	    public void multirange() throws IOException {
	        List<List<String>> stringValues = Arrays.asList(
	            Arrays.asList("January Total", "=B2+B3"),
	            Arrays.asList("February Total", "=B5+B6")
	        );

	        List<List<Object>> objectValues = stringValues.stream()
	            .map(row -> new ArrayList<Object>(row))
	            .collect(Collectors.toList());

	        List<ValueRange> data = new ArrayList<>();
	        data.add(new ValueRange()
	            .setRange("D1")
	            .setValues(objectValues.subList(0, 1)));
	        data.add(new ValueRange()
	            .setRange("D4")
	            .setValues(objectValues.subList(1, 2)));

	        BatchUpdateValuesRequest batchBody = new BatchUpdateValuesRequest()
	            .setValueInputOption("USER_ENTERED")
	            .setData(data);

	        BatchUpdateValuesResponse batchResult = sheetsService.spreadsheets().values()
	            .batchUpdate(SPREADSHEET_ID, batchBody)
	            .execute();
	    }
	  
	  @Test(priority = 3)
	  public void appending_method() throws IOException {
		  ValueRange appendBody = new ValueRange()
				  .setValues(Arrays.asList(
				    Arrays.asList("Total", "=E1+E4")));
				AppendValuesResponse appendResult = sheetsService.spreadsheets().values()
				  .append(SPREADSHEET_ID, "A1", appendBody)
				  .setValueInputOption("USER_ENTERED")
				  .setInsertDataOption("INSERT_ROWS")
				  .setIncludeValuesInResponse(true)
				  .execute();
				        
				ValueRange total = appendResult.getUpdates().getUpdatedData();
				System.out.println("app sucessful");
				
	  }
	  
	  @Test(priority =4)
	  public void read_value_from_sheet() throws IOException {
		  List<String> ranges = Arrays.asList("E1","E4");
		  BatchGetValuesResponse readResult = sheetsService.spreadsheets().values()
		    .batchGet(SPREADSHEET_ID)
		    .setRanges(ranges)
		    .execute();
		      
		  ValueRange januaryTotal = readResult.getValueRanges().get(0);
		  assertEquals("58", januaryTotal.getValues().get(0).get(0));
		  

		 
	  }
	  @Test(priority = 5)
	  public void create_sheet() throws IOException {
	     
	     Spreadsheet spreadsheet = new Spreadsheet()
	    	        .setProperties(new SpreadsheetProperties().setTitle("My Spreadsheet"));
	   

	    	    Spreadsheet result = sheetsService.spreadsheets().create(spreadsheet).execute();

	    	    // Use 'result' to access information about the created spreadsheet if needed
	    	    System.out.println("Spreadsheet created. ID: " + result.getSpreadsheetId());
	  }
	  @Test(priority=6)
	public void new_sheet() {
		 try {
	            // Define properties of the new sheet
	            SheetProperties newSheetProperties = new SheetProperties()
	                    .setTitle("Second Sheet");

	            // Create the new sheet
	            Sheet newSheet = new Sheet().setProperties(newSheetProperties);

	            // Retrieve the existing sheets of the spreadsheet
	            Spreadsheet spreadsheet = sheetsService.spreadsheets().get(SPREADSHEET_ID).execute();
	            List<Sheet> existingSheets = spreadsheet.getSheets();

	            // Add the new sheet to the list of existing sheets
	            List<Request> requests = new ArrayList<>();
	            requests.add(new Request()
	                    .setAddSheet(new AddSheetRequest()
	                            .setProperties(newSheetProperties)));

	            // Update the spreadsheet with the new sheet
	            BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest()
	                    .setRequests(requests);

	            sheetsService.spreadsheets().batchUpdate(SPREADSHEET_ID, batchUpdateRequest).execute();

	            System.out.println("Second sheet created successfully.");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}
	  
  }
 
