package com.embestkit.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpUtil {

	
	public interface HttpCallbackListener{
		//网络访问完成
		void finish(String response);
		//网络错误
		void onError(Exception e);
	}
	
	//get数据
	public static void sendHttpResquest(final String address,final HttpCallbackListener listener){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO 自动生成的方法存根
				HttpURLConnection connection=null;
				try {
					URL url=new URL(address);
					connection=(HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					connection.setDoInput(true);
					connection.setDoOutput(true);
					InputStream in=connection.getInputStream();
					BufferedReader reader=new BufferedReader(new InputStreamReader(in));
					StringBuilder response=new StringBuilder();
					String line;
					while ((line=reader.readLine())!=null) {
						response.append(line);
					}
					System.out.println(response.toString()+"数据");
					if(listener!=null){
						//回调
						listener.finish(response.toString());
						
					}
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					
					if(listener!=null){
						//回调
						listener.onError(e);
						System.out.println("网络连接失败");
						
						
					}
				}finally{
					if(connection!=null){
						//回调
						connection.disconnect();
						
					}
				}
				
			}
		}).start();
	}
	
	/* 
	 * data的数据封装如：username=admin&password=123456
	 * @see java.lang.Runnable#run()
	 */
	//Post请求
	public static void postHttpResquest(final String address,final String data,final HttpCallbackListener listener){
		System.out.println(address+"?"+data);
		new Thread(new Runnable() {
		
			@Override
			public void run() {
				// TODO 自动生成的方法存根
				HttpURLConnection connection=null;
				try {
					URL url=new URL(address);
					connection=(HttpURLConnection) url.openConnection();
					connection.setRequestMethod("POST");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					connection.setDoInput(true);
					connection.setDoOutput(true);
					connection.connect();
					DataOutputStream out=new DataOutputStream(connection.getOutputStream());
					out.writeBytes(data);
					/*PrintWriter writer=new PrintWriter(connection.getOutputStream());
					writer.write(data);
					writer.flush();
					writer.close();*/
					InputStream in=connection.getInputStream();
					BufferedReader reader=new BufferedReader(new InputStreamReader(in));
					StringBuilder response=new StringBuilder();
					String line;
					while ((line=reader.readLine())!=null) {
						response.append(line);
					}
					System.out.println("数据是"+response.toString());
					if(listener!=null){
						//回调
						listener.finish(response.toString());
					}
					
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					if(listener!=null){
						//回调
						listener.onError(e);
						try {
							System.out.println("网络连接失败"+connection.getResponseCode());
						} catch (IOException e1) {
							// TODO 自动生成的 catch 块
							e1.printStackTrace();
						}
						
					}
				}finally{
					if(connection!=null){
						//回调
						connection.disconnect();
						
					}
				}
				
			}
		}).start();
	}
	
}
