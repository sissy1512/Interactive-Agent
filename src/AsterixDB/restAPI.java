package AsterixDB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class restAPI {
	public static final String REST_ENDPOINT = "http://localhost:19002";
	public static String URI_INFO_PATH = null;
	
	public void setInfoPath(String info){
		assert (info.equals("query") || info.equals("ddl") || info.equals("update"));
		URI_INFO_PATH = "/" + info + "?" + info + "=";
	}
	
	public String query(String query){
		String endpoint = REST_ENDPOINT + URI_INFO_PATH + query;
		HttpURLConnection request = null;
		BufferedReader rd = null;
		StringBuilder response = null;
		//JsonElement jse = null;

		try{
			URL endpointUrl = new URL(endpoint);
			request = (HttpURLConnection)endpointUrl.openConnection();
		//	request.setRequestMethod("POST");			
			request.connect();
	
			rd  = new BufferedReader(new InputStreamReader(request.getInputStream()));
			response = new StringBuilder();
			String line = null;
			while ((line = rd.readLine()) != null){
				response.append(line + '\n');
			}
			//System.out.println(response);
			return (new String(response));				
		} catch (MalformedURLException e) {
			System.out.println("Exception: " + e.getMessage());
			return null;
			//e.printStackTrace();
		} catch (ProtocolException e) {
			System.out.println("Exception: " + e.getMessage());
			return null;
			//e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Exception: " + e.getMessage());
			return null;
			//e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			return null;
			//e.printStackTrace();
		} finally {
			try{
				request.disconnect();
			} catch(Exception e){
			}
	
			if(rd != null){
				try{
					rd.close();
				} catch(IOException ex){
				}
				rd = null;
			}
		}
//		System.out.println(response);
		

		
		
//		JsonParserFactory factory=JsonParserFactory.getInstance();
//		JSONParser parser=factory.newJsonParser();
//		Map jsonData= parser.parseJson(new String(response));
////		ArrayList<String> arr = (ArrayList<String>)jsonData.get("results");
////		for(String s: arr){
////			Map contents = parser.parseJson(s);
////			ArrayList value= (ArrayList)contents.get("DatasetName");
////			System.out.println(value);
////		}
//
//		List al= (List)jsonData.get("results");
//		String dName=(String) ((Map)al.get(0)).get("DatasetName");
//		System.out.println(dName);
		
	}
}
