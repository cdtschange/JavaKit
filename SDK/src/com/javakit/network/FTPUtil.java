package com.javakit.network;

import com.javakit.data.exception.NetException;
import com.javakit.data.log.Log;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by cdts on 29/03/2017.
 */
public class FTPUtil {
    private String userName;     //登录名
    private String password;     //密码
    private String ftpHostName;  //ftp地址
    private int port;       //端口
    private FTPClient ftpClient = new FTPClient();
    private OutputStream os = null;
    private InputStream is = null;

    private Pattern pattern = Pattern.compile("\\d{8}");    //8位数字

    /**
     *
     * @param url FTP服务器hostname
     * @param port FTP服务器端口
     * @param username FTP登录账号
     * @param password FTP登录密码
     */
    public FTPUtil(String url, int port, String username, String password){
        super();
        this.ftpHostName = url;
        this.port = port;
        this.userName = username;
        this.password = password;
    }
    /**
     * 建立链接
     */
    private void connect() throws NetException {
        try {
            Log.info("开始链接...");
            ftpClient.connect(ftpHostName, port);
            int reply = ftpClient.getReplyCode();   //ftp响应码
            if(!FTPReply.isPositiveCompletion(reply)){  //ftp拒绝链接
                Log.error("ftp拒绝链接...");
                ftpClient.disconnect();
            }
            ftpClient.login(userName, password);
            ftpClient.enterLocalPassiveMode();       //设置被动模式    通知server端开通端口传输数据
            ftpClient.setBufferSize(256);
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.setControlEncoding("utf-8");
            Log.info("登录成功");
        } catch (Exception e) {
            throw NetException.BusinessException("连接FTP失败", e);
        }
    }
    /**
     * 退出FTP登录关闭链接   并  关闭输入输出流
     */
    private void close() throws NetException {
        try {
            if(is!=null){
                is.close();
            }
            if(os!=null){
                os.flush();
                os.close();
            }
            ftpClient.logout();
            Log.info("退出登录！");
            ftpClient.disconnect();
            Log.info("关闭链接！");
        } catch (Exception e) {
            throw NetException.BusinessException("关闭FTP失败", e);
        }
    }

    /**
     * 上传文件
     * @param ftpPath   FTP服务器保存目录
     * @param fileName  上传到FTP服务器上的文件名
     * @param input 输入流
     */
    public void uploadFile(String ftpPath,String fileName, InputStream input) throws NetException {
        connect();
        uploadFileByDo(ftpPath,fileName, input);
        close();
    }
    /**
     * 上传文件
     * @param ftpPath   FTP服务器保存目录
     * @param fileName  上传到FTP服务器上的文件名
     * @param input 输入流
     */
    private void uploadFileByDo(String ftpPath, String fileName, InputStream input) throws NetException {
        try {
            is = input;
            if(!createDirecroty(ftpPath)) {
                throw NetException.BusinessException("创建FTP文件夹失败");
            }
            if(ftpClient.changeWorkingDirectory(ftpPath)){
                ftpClient.storeFile(fileName, is);
                is.close();
            }else{
                Log.info("上传FTP服务器路径有误！");
                throw NetException.BusinessException("上传FTP服务器路径有误");
            }
        } catch (Exception e) {
            throw NetException.BusinessException("上传FTP文件失败", e);
        } finally{
            try {
                if(is!=null){
                    is.close();
                }
            } catch (Exception e) {
                throw NetException.BusinessException("关闭FTP失败",  e);
            }
        }
    }

    /**
     * 下载文件
     * @param ftpFileName
     * @param downloadDate
     */
    public void downloadFiles(String ftpFileName,String downloadDate){
        connect();
        downloadFileByDate(ftpFileName,downloadDate);
        close();
    }

    /**
     * 下载指定文件
     * @param ftpFileName
     * @param downloadDate
     */
    private void downloadFileByDate(String ftpFileName,String downloadDate){
        try {
            if(isDir(ftpFileName)){        //文件夹
                String[] names = ftpClient.listNames();
                for(int i=0;i<names.length;i++){
                    System.out.println(names[i] + "--------------");
                    if(pattern.matcher(names[i]).matches()){   //如果是8位数字的文件夹
                        downloadFileByDate(ftpFileName + "/" + downloadDate,downloadDate);  //指定文件夹
                        ftpClient.changeToParentDirectory();
                        break;
                    }
                    if(isDir(names[i])){
                        downloadFileByDate(ftpFileName + "/" + names[i],downloadDate);
                        ftpClient.changeToParentDirectory();
                    }else{
                        is = ftpClient.retrieveFileStream(names[i]);     //取出文件转成输入流
                        zipByFile(ftpFileName+"/"+names[i],is);                             //压缩文件
                        //在retrieveFileStream后面加上completePendingCommand，changeWorkingDirectory才能正常输出
                        //而且completePendingCommand一定要在is.close()之后，否则容易程序死掉，坑爹啊；
                        ftpClient.completePendingCommand();
                        //ftpClient.changeToParentDirectory();
                    }
//                    //测试
//                    if("04".equals(names[i])){
//                        break;
//                    }
                }
            } else {    //文件
                System.out.println(ftpFileName + "-------------------------");
                is = ftpClient.retrieveFileStream(ftpFileName);
                zipByFile(ftpFileName,is);                             //压缩文件
                ftpClient.completePendingCommand();
                ftpClient.changeToParentDirectory();
            }
            //os.flush();
            Log.info("下载成功！");
        } catch (IOException e) {
            Log.error("下载失败！",e);
        }
    }
    /**
     * @param ftpFileName
     */
    private void downloadFileOrDir(String ftpFileName){
        try {
            if(isDir(ftpFileName)){        //文件夹
                String[] names = ftpClient.listNames();
                for(int i=0;i<names.length;i++){
                    System.out.println(names[i] + "--------------");
                    if(isDir(names[i])){
                        downloadFileOrDir(ftpFileName + "/" + names[i]);
                        ftpClient.changeToParentDirectory();
                    }else{
                        File loadFile = new File(ftpFileName + File.separator
                                + names[i]);
                        os = new FileOutputStream(loadFile);
                        ftpClient.retrieveFile(names[i], os);
                    }
                }
            } else {    //文件
                File file = new File(ftpFileName);
                os = new FileOutputStream(file);
                ftpClient.retrieveFile(file.getName(), os);
                ftpClient.changeToParentDirectory();
            }
            Log.info("下载成功！");
        } catch (IOException e) {
            Log.error("下载失败！",e);
        }
    }

    /**
     * 判断是否是目录
     * @param fileName
     * @return
     */
    public boolean isDir(String fileName){
        try {
            // 切换目录，若当前是目录则返回true,否则返回true。
            boolean falg = ftpClient.changeWorkingDirectory(fileName);
            return falg;
        } catch (IOException e) {
            Log.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 压缩文件
     */
    private void zipByFile(String fileName,InputStream is){
        try {
            // System.out.println(fileName+"==============");
            ((ZipOutputStream) os).putNextEntry(new ZipEntry(fileName));
            // 设置注释
            //zip.setComment("hello");
            int temp = 0;
            while((temp = is.read()) != -1){
                os.write(temp);
            }
            is.close();
            Log.info("压缩成功！");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.error(e.getMessage(), e);

        } finally{
            try {
                if(is!=null){
                    is.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    //创建多层目录文件，如果有ftp服务器已存在该文件，则不创建，如果无，则创建
    private boolean createDirecroty(String dirname) throws IOException {
        boolean success = true;
        String directory = dirname + "/";
//        String directory = remote.substring(0, remote.lastIndexOf("/") + 1);
        // 如果远程目录不存在，则递归创建远程服务器目录
        if (!directory.equalsIgnoreCase("/") && !ftpClient.changeWorkingDirectory(new String(directory))) {
            int start = 0;
            int end = 0;
            if (directory.startsWith("/")) {
                start = 1;
            } else {
                start = 0;
            }
            end = directory.indexOf("/", start);
            String path = "";
            String paths = "";
            while (true) {

                String subDirectory = new String(dirname.substring(start, end).getBytes("GBK"), "iso-8859-1");
                path = path + "/" + subDirectory;
                if (!existFile(path)) {
                    if (ftpClient.makeDirectory(subDirectory)) {
                        ftpClient.changeWorkingDirectory(subDirectory);
                    } else {
                        Log.debug("创建目录[" + subDirectory + "]失败");
                        ftpClient.changeWorkingDirectory(subDirectory);
                    }
                } else {
                    ftpClient.changeWorkingDirectory(subDirectory);
                }

                paths = paths + "/" + subDirectory;
                start = end + 1;
                end = directory.indexOf("/", start);
                // 检查所有目录是否创建完毕
                if (end <= start) {
                    break;
                }
            }
        }
        return success;
    }
    //判断ftp服务器文件是否存在
    public boolean existFile(String path) throws IOException {
        boolean flag = false;
        FTPFile[] ftpFileArr = ftpClient.listFiles(path);
        if (ftpFileArr.length > 0) {
            flag = true;
        }
        return flag;
    }




    public String getUserName() {
        return userName;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public String getFtpHostName() {
        return ftpHostName;
    }


    public void setFtpHostName(String ftpHostName) {
        this.ftpHostName = ftpHostName;
    }


    public int getPort() {
        return port;
    }


    public void setPort(int port) {
        this.port = port;
    }

    public OutputStream getOs() {
        return os;
    }
    public void setOs(OutputStream os) {
        this.os = os;
    }

}
