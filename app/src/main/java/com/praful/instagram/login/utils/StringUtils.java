package com.praful.instagram.login.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class StringUtils {
	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	public static String streamToString(InputStream is) throws IOException {
		String str = "";

		if (is != null) {
			StringBuilder sb = new StringBuilder();
			String line;

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));

				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}

				reader.close();
			} finally {
				is.close();
			}

			str = sb.toString();
		}

		return str;
	}
}






 ========================================================================================================================================
	
	private static Context ctx;
    private static FileInputStream stream;
    private static CipherInputStream cis;
    private static AssetFileDescriptor fileDescriptor;
    private static String[] images = new String[0];
    private static ArrayList<String> listImages;


public static ArrayList<Bitmap> decryptDataFrame(Context applicationContext) throws IOException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException
    {
        ArrayList<Bitmap> arrBitmapList=new ArrayList<>();
        try {
            ctx = applicationContext;
            AssetManager mngr = ctx.getAssets();
            images = mngr.list("standImage");
            listImages = new ArrayList<>(Arrays.asList(images));

            for (int i = 0; i < images.length; i++)
            {
                fileDescriptor = mngr.openFd("standImage/" + listImages.get(i));
                stream = fileDescriptor.createInputStream();
                SecretKeySpec sks = new SecretKeySpec(DCStaticMethod.generateAPIKEY().getBytes(), "AES");
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, sks);
                cis = new CipherInputStream(stream, cipher);
                Bitmap mBitmap = BitmapFactory.decodeStream(cis, null, null);
                arrBitmapList.add(mBitmap);
                cis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrBitmapList;
    }
	
========================================================================================================================================	
	
	
	
	
	
	
	
	
========================================================================================================================================
	public static Context ctx;
    public static FileInputStream stream;
    public static CipherOutputStream cos;
    public static AssetFileDescriptor fileDescriptor;
    public static String[] arrimages = new String[0];
    public static ArrayList<String> listImages;
    public static String folder_main = "StandEncryptMedia";
    public static int countI = 0;
    public static int Size = 0;
    Context context;
    OnAsyncEncMediaResponse caller;
    ProgressDialog pDialog = null;
    String TAG = "AsyncEncMedia";
    //public static String strKey = "HvX4JuGQciR9lv7q";
    public static String strKey = "0905AIzaSyBHupT0";
	
	public static void encrypt(Context applicationContext) throws IOException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException {

        ctx = applicationContext;
        AssetManager mngr = ctx.getAssets();
        arrimages = mngr.list("standImage");
        listImages = new ArrayList<String>(Arrays.asList(arrimages));
        for (countI = 0; countI < arrimages.length; countI++) {

            fileDescriptor = mngr.openFd("standImage/" + listImages.get(countI));

            File extStore = new File(Environment.getExternalStorageDirectory(), folder_main);
            if (!extStore.exists()) {
                extStore.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(extStore + "/" + arrimages[countI]);
            stream = fileDescriptor.createInputStream();
            SecretKeySpec sks = new SecretKeySpec(strKey.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, sks);
            cos = new CipherOutputStream(fos, cipher);
            int b;
            byte[] d = new byte[8];
            while ((b = stream.read(d)) != -1) {
                cos.write(d, 0, b);
            }
            cos.flush();
            cos.close();
            stream.close();
            Log.e("encrypted ", "--->" + "done");
        }
    }

========================================================================================================================================








