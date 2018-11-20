package com.embestkit.test;


public class Test1{

	public static void main(String[] args) {
		String str="02 69 00 01 00 68";
		String str1="026900010068";
		/*System.out.println(str16toStr(str1));
		
		System.out.println(strTo16Str(str));
		
		System.out.println(hexStringToString(str1));
		
		Integer x = Integer.parseInt("69",16);
		System.out.println(x);*/
		
	}
	
	/**
	 * 将16进制转为字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String str16toStr(String hexstr) {

		String str="0123456789ABCDEF";
		char[] hexs=hexstr.toCharArray();
		byte[] bytes=new byte[hexstr.length()/2];
		int n;
		for (int i = 0; i < bytes.length; i++) {
			n=str.indexOf(hexs[2*i])*16;
			n+=str.indexOf(hexs[2*i+1]);
			bytes[i]=(byte) (n & 0xff);
		}
		return new String(bytes);
	}

	/**
	 * 将字符串转为16进制
	 * 
	 * @param b
	 * @return
	 */
	public static String strTo16Str(String str) {
		char[] chars = "0123456789ABCDEF".toCharArray();
		StringBuilder sb = new StringBuilder("");
		byte[] bs = str.getBytes();
		int bit;
		for (int i = 0; i < bs.length; i++) {
			bit = (bs[i] & 0x0f0) >> 4;
			sb.append(chars[bit]);
			bit = bs[i] & 0x0f;
			sb.append(chars[bit]);
		}
		return sb.toString().trim();
	}
	
	
	public static String hexStringToString(String s) {  
        if (s == null || s.equals("")) {  
            return null;  
        }  
        s = s.replace(" ", "");  
        byte[] baKeyword = new byte[s.length() / 2];  
        for (int i = 0; i < baKeyword.length; i++) {  
            try {  
                baKeyword[i] = (byte) (0xff & Integer.parseInt(  
                        s.substring(i * 2, i * 2 + 2), 16));  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
        try {  
            s = new String(baKeyword, "gbk");  
            new String();  
        } catch (Exception e1) {  
            e1.printStackTrace();  
        }  
        return s;  
    }  

}
