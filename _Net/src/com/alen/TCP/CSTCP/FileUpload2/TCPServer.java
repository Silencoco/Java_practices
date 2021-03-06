package com.alen.TCP.CSTCP.FileUpload2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * 读取客户端上传的文件，保存到服务器的硬盘，给客户端回写“上传成功”
 *
 * 实现步骤：
 * 1、创建一个服务器ServerSocket对象，和系统要指定的端口号
 * 2、使用ServerSocket对象中的方法accept，获取到请求的客户端Socket对象
 * 3、使用Socket对象中的方法getInputStream，获取到网络字节输入流InputStream对象
 * 4、判断文件夹是否存在，不存在则创建一个
 * 5、创建本地字节输出流FileOutputStream对象，构造方法中绑定要输出的目的地
 * 6、使用网络字节输入流InputStream对象中的方法read，读取客户端上传的文件
 * 7、使用本地字节输出流FileOutputStream对象中的方法write。将上传的文件写入到服务器硬盘
 * 8、使用socket对象中的getOutputStream，获取OutputStream对象
 * 9、给客户端回写“上传成功”
 * 10、释放资源
 */
public class TCPServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8888);
        /**
         * 让服务一直处于监听状态（死循环accept）
         * 有一个客户端上传文件，就保存文件
         */
        while (true){
            Socket socket = serverSocket.accept();

            /**
             * 使用多线程技术，提高效率
             * 有一个客户端上传文件，我就开启一个线程完成文件的上传
             */
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        //完成文件的上传
                        InputStream is = socket.getInputStream();
                        File file = new File("D:\\upload");
                        if (!file.exists()){
                            file.mkdir();
                        }
                        /**
                         * 自定义一个文件的命名规则：防止同名的文件被覆盖
                         * 规则：域名+毫秒值+随机数
                         */
                        String fileName = "silencoco"+System.currentTimeMillis()+new Random().nextInt(999999)+".jpg";
                        //绑定要输出的目的地
                        FileOutputStream fos = new FileOutputStream(file+"\\"+fileName);
                        //读取客户端上传的文件
                        byte[] bytes = new byte[1024];
                        int len=0;
                        while ((len = is.read(bytes))!=-1){
                            //把读取到的文件保存到服务器上
                            fos.write(bytes,0,len);
                        }
                        //给客户端回写信息
                        socket.getOutputStream().write("上传成功".getBytes());

                        fos.close();
                        socket.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                }
            }).start();
        }
    }
}
