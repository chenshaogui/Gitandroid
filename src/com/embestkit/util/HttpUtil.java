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
		//����������
		void finish(String response);
		//�������
		void onError(Exception e);
	}
	
	//get����
	public static void sendHttpResquest(final String address,final HttpCallbackListener listener){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO �Զ����ɵķ������
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
					System.out.println(response.toString()+"����");
					if(listener!=null){
						//�ص�
						listener.finish(response.toString());
						
					}
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					
					if(listener!=null){
						//�ص�
						listener.onError(e);
						System.out.println("��������ʧ��");
						
						
					}
				}finally{
					if(connection!=null){
						//�ص�
						connection.disconnect();
						
					}
				}
				
			}
		}).start();
	}
	
	/* 
	 * data�����ݷ�װ�磺username=admin&password=123456
	 * @see java.lang.Runnable#run()
	 */
	//Post����
	public static void postHttpResquest(final String address,final String data,final HttpCallbackListener listener){
		System.out.println(address+"?"+data);
		new Thread(new Runnable() {
		
			@Override
			public void run() {
				// TODO �Զ����ɵķ������
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
					System.out.println("������"+response.toString());
					if(listener!=null){
						//�ص�
						listener.finish(response.toString());
					}
					
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					if(listener!=null){
						//�ص�
						listener.onError(e);
						try {
							System.out.println("��������ʧ��"+connection.getResponseCode());
						} catch (IOException e1) {
							// TODO �Զ����ɵ� catch ��
							e1.printStackTrace();
						}
						
					}
				}finally{
					if(connection!=null){
						//�ص�
						connection.disconnect();
						
					}
				}
				
			}
		}).start();
	}
	
}
