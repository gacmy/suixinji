package gac.com.richedittextadvance;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by lenovo on 2016/4/1.
 */
public class BitmapUtils {

    public static boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals( Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
    public static File saveBitmap2file(Bitmap bmp) {
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        int quality = 100;
        OutputStream stream = null;
        File file = null;
        try {
            file = getOutputMediaFile(1);
            stream = new FileOutputStream(file.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bmp.compress(format, quality, stream);
        return file;
    }

    public static File getMediaFile(){
        File sdCardDir = Environment.getExternalStorageDirectory();
        File mediaStorageDir = new File(sdCardDir + File.separator + "Vbox"
                + File.separator + "VboxApp");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + "kaoqin" + ".jpg");
        return mediaFile;
    }

    @SuppressLint("SimpleDateFormat")
    public static File getOutputMediaFile(int type) {
        File sdCardDir = Environment.getExternalStorageDirectory();
        File mediaStorageDir = new File(sdCardDir + File.separator + "Vbox"
                + File.separator + "VboxApp");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        File mediaFile;
        if (type == 1) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + "car_easy_headimg" + ".jpg");
        } else {
            return null;
        }
        return mediaFile;
    }

    //打开系统相册
    public static int PHOTO_REQUEST_GALLERY = 0x00;
    public static void gallery(Activity context) {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        context.startActivityForResult(intent,PHOTO_REQUEST_GALLERY);
    }
    //获取打开系统相册返回的uri
    public static Uri getGalleryUri(Intent data,int requestCode){
        Uri uri = null;
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                 uri = data.getData();
            }
        }
        return uri;
    }

    public static int PHOTO_REQUEST_CAMERA = 0x01;
    public static String PHOTO_FILE_NAME = "camera_pic.jpg";
    //调用系统照相功能
    public static void camera(Activity context) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 判断存储卡是否可以用，可用进行存储 解决照相机返回的图片太小
        if (BitmapUtils.hasSdcard()) {
            //调用照相机 保存图片 返回的intent 为null
            intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME)));
        }
        context.startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
    }
    //获取照相功能的uri
    public static Uri getCameraUri(int requestCode){
        File tempFile = null;
        Uri uri = null;
        if (requestCode == PHOTO_REQUEST_CAMERA) {
            if (BitmapUtils.hasSdcard()) {
                tempFile = new File(Environment.getExternalStorageDirectory(),PHOTO_FILE_NAME);
                uri = Uri.fromFile(tempFile);//将保存的图片文件 转换成uri的格式
            }
        }
        return uri;
    }


    public static int PHOTO_REQUEST_CUT = 0x02;
    //调用系统剪切图片的功能
    public static  void crop(Uri uri,Activity context) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
        context.startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    //获取系统剪切后返回的图片
    public static Bitmap getCropBitmap(int requestCode,Intent data) {
        Bitmap bitmap = null;
        if (requestCode == PHOTO_REQUEST_CUT) {
            if(data != null){
                bitmap = data.getParcelableExtra("data");
            }
        }
        return bitmap;
    }


    /**
     * 获取文件的路径
     *通过uri 获取文件路径
     * @param
     * @return
     */

    public static final String CONTENT = "content";
    public static final String FILE = "file";
    public static String getFilePath(Context context, Uri uri) {
        String filePath = null;
        if (CONTENT.equalsIgnoreCase(uri.getScheme())) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[] { MediaStore.Images.Media.DATA }, null, null, null);
            if (null == cursor) {
                return null;
            }
            try {
                if (cursor.moveToNext()) {
                    filePath = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));
                }
            } finally {
                cursor.close();
            }
        }
        // 从文件中选择
        if (FILE.equalsIgnoreCase(uri.getScheme())) {
            filePath = uri.getPath();
        }
        return filePath;
    }
    //文件转换为uri格式
    public static Uri file2Uri(File file){
        return  Uri.fromFile(file);
    }

    public static void recyleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            Log.e("gac","bitmap recyle");
            bitmap.recycle();
            System.gc();
        }else{
            Log.e("gac","bitmap not recyle");
        }
    }
//    //上传考勤头像
//    public static String uploadKaoqinTx(String userid,String image, String strUrl,Activity context) {
//        String result = null;
//        String  BOUNDARY =  UUID.randomUUID().toString();  //边界标识   随机生成
//        String PREFIX = "--" , LINE_END = "\r\n";
//        String CONTENT_TYPE = "multipart/form-data";   //内容类型
//        try {
//            strUrl=strUrl+"?userid="+userid;
//            URL url = new URL(strUrl);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setReadTimeout(Constant.TIME_OUT);
//            conn.setConnectTimeout(Constant.TIME_OUT);
//            conn.setDoInput(true);  //允许输入流
//            conn.setDoOutput(true); //允许输出流
//            conn.setUseCaches(false);  //不允许使用缓存
//            conn.setRequestMethod("POST");  //请求方式
//            conn.setRequestProperty("Charset", Constant.CHARSET);  //设置编码
//            conn.setRequestProperty("connection", "keep-alive");
//            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
//            File file=new File(image);
//            if(file!=null)
//            {
//
//                DataOutputStream dos = new DataOutputStream( conn.getOutputStream());
//                StringBuffer sb = new StringBuffer();
//                sb.append(PREFIX);
//                sb.append(BOUNDARY);
//                sb.append(LINE_END);
//                sb.append("Content-Disposition: form-data; name=\"image\"; filename=\"" + file.getName() + "\"" + LINE_END);
//                sb.append("Content-Type: application/octet-stream; charset=" + Constant.CHARSET + LINE_END);
//                sb.append(LINE_END);
//                dos.write(sb.toString().getBytes());
//                FileInputStream is = new FileInputStream(file);
//                long tempCount = 0;
//                long totalCount =is.available();
//                byte[] bytes = new byte[1024];
//                int len = 0;
//               // Log.e("gac","totalcount:"+totalCount);
//                while((len=is.read(bytes))!=-1)
//                {
//                    tempCount+=len;
//                    dos.write(bytes, 0, len);
//                    //发送进度条
//                    Message msg = new Message();
//                    //Log.e("gac","tempCount:"+tempCount);
//                    float rate = (float)tempCount/(float)totalCount;
//                    //Log.e("gac","rate:"+rate);
//                    int percents = (int)(rate*100);
//                    //Log.e("gac","percents:"+percents);
//                    msg.obj =percents;
//                    //Log.e("gac","sendMessage");
//                    UploadkaoqinHeadActivity activity = (UploadkaoqinHeadActivity)context;
//                    activity.handler.sendMessage(msg);
//
//                }
//                is.close();
//                dos.write(LINE_END.getBytes());
//                byte[] end_data = (PREFIX+BOUNDARY+PREFIX+LINE_END).getBytes();
//                dos.write(end_data);
//                dos.flush();
//                int res = conn.getResponseCode();
//                InputStream input =  conn.getInputStream();
//                StringBuffer sb1= new StringBuffer();
//                int ss ;
//                while((ss=input.read())!=-1)
//                {
//                    sb1.append((char)ss);
//                }
//                result = sb1.toString();
//            }
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }


//    public static String uploadFile(String userid,String image, String strUrl) {
//        String result = null;
//        String  BOUNDARY =  UUID.randomUUID().toString();  //边界标识   随机生成
//        String PREFIX = "--" , LINE_END = "\r\n";
//        String CONTENT_TYPE = "multipart/form-data";   //内容类型
//        try {
//            strUrl=strUrl+"?userid="+userid;
//            URL url = new URL(strUrl);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setReadTimeout(Constant.TIME_OUT);
//            conn.setConnectTimeout(Constant.TIME_OUT);
//            conn.setDoInput(true);  //允许输入流
//            conn.setDoOutput(true); //允许输出流
//            conn.setUseCaches(false);  //不允许使用缓存
//            conn.setRequestMethod("POST");  //请求方式
//            conn.setRequestProperty("Charset", Constant.CHARSET);  //设置编码
//            conn.setRequestProperty("connection", "keep-alive");
//            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
//            File file=new File(image);
//            if(file!=null)
//            {
//                DataOutputStream dos = new DataOutputStream( conn.getOutputStream());
//                StringBuffer sb = new StringBuffer();
//                sb.append(PREFIX);
//                sb.append(BOUNDARY);
//                sb.append(LINE_END);
//                sb.append("Content-Disposition: form-data; name=\"image\"; filename=\""+file.getName()+"\""+LINE_END);
//                sb.append("Content-Type: application/octet-stream; charset="+Constant.CHARSET+LINE_END);
//                sb.append(LINE_END);
//                dos.write(sb.toString().getBytes());
//                InputStream is = new FileInputStream(file);
//                byte[] bytes = new byte[1024];
//                int len = 0;
//                while((len=is.read(bytes))!=-1)
//                {
//                    dos.write(bytes, 0, len);
//                }
//                is.close();
//                dos.write(LINE_END.getBytes());
//                byte[] end_data = (PREFIX+BOUNDARY+PREFIX+LINE_END).getBytes();
//                dos.write(end_data);
//                dos.flush();
//
//                int res = conn.getResponseCode();
//                InputStream input =  conn.getInputStream();
//                StringBuffer sb1= new StringBuffer();
//                int ss ;
//                while((ss=input.read())!=-1)
//                {
//                    sb1.append((char)ss);
//                }
//                result = sb1.toString();
//                input.close();
//                dos.close();
//            }
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
}
