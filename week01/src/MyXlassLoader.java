import java.io.*;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @Description: 字节码加载工具类
 * @ProjectName: week01
 * @Package: PACKAGE_NAME
 * @ClassName: MyXlassLoader
 * @Author: huxing
 * Date: 2021-06-28
 * Time: 09:33
 */
public class MyXlassLoader extends ClassLoader {

    /**
     * @Description:  main方法
     * @Author: huxing
     * @Date: 2021-06-28 10:35
     * @param args:
     * @return: void
     **/
    public static void main(String[] args) throws Exception{
        // 加载类名
        final String className = "Hello";
        // 初始化对象
        MyXlassLoader xlassLoader = new MyXlassLoader();
        // 加载xlass文件
        Class<?> classz = xlassLoader.loadClass(className);
        // 打印方法名
        for (Method method:classz.getMethods()){
            System.out.println(classz.getSimpleName() + "." + method.getName());
        }
        // 调用Hello方法
        xlassLoader.invoke(classz, "hello");
        // 调用test方法
        xlassLoader.invoke(classz, "test");
    }

    /**
     * @Description: 通过文件路径加载Hello.xlass文件
     * @Author: huxing
     * @param name
     * @return java.lang.Class<?>
     * @Date: 2021/8/5 下午5:04
     **/
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        // TODO: 通过字节码模式加载类, 1: 文件形式, 2: 压缩包形式
        return loadByteCodeFile(name, 2);
    }

    /**
     * @Description:   字节码加载类
     * @Author: huxing
     * @param name  加载类名
     * @param load  加载模式
     * @return java.lang.Class<?>
     * @Date: 2021/8/6 下午3:05
     **/
    private Class<?> loadByteCodeFile(String name, int load) throws ClassNotFoundException{
        // 当前文件路径名
        String resourcePath =System.getProperty("user.dir") + "/doc/";
        // 文件后缀后缀名
        final String suffix = ".xlass";
        if (load == 1){
            // 通过文件模式加载字节码
            return this.loadByFile(resourcePath + name + suffix, name);
        } else if (load == 2){
            // 通过压缩包模式加载字节码
            return this.loadByXar(name + suffix, name);
        } else {
            return super.findClass(name);
        }
    }

    /**
     * @Description: 通过压缩包加载字节码类
     * @Author: huxing
     * @param fileName  文件名
     * @param className 类名
     * @return java.lang.Class<?>
     * @Date: 2021/8/6 下午3:12
     **/
    private Class<?> loadByXar(String fileName, String className) throws ClassNotFoundException{
        System.out.println("通过压缩包加载字节码类文件");
        // 压缩文件路径
        String filePath = System.getProperty("user.dir") + "/doc/";
        // 文件输入流
        FileInputStream input = null;
        //获取ZIP输入流(一定要指定字符集Charset.forName("GBK")否则会报java.lang.IllegalArgumentException: MALFORMED)
        ZipInputStream zipInputStream = null;
        //定义ZipEntry置为null,避免由于重复调用zipInputStream.getNextEntry造成的不必要的问题
        ZipEntry ze = null;
        try {
            input = new FileInputStream(filePath + "xlass.xar");
            zipInputStream = new ZipInputStream(new BufferedInputStream(input),
                    Charset.forName("GBK"));
            //循环遍历
            while ((ze = zipInputStream.getNextEntry()) != null) {
                System.out.println("文件名：" + ze.getName() + " 文件大小：" + ze.getSize() + " bytes");
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                // 读取到了文件
                if (Objects.equals(fileName, ze.getName())){
                    //文件输出流
                    int len;
                    byte[] buf = new byte[1024];
                    while ((len = zipInputStream.read(buf)) != -1) {
                        bos.write(buf, 0, len);
                    }
                    byte[] byteArray = bos.toByteArray();
                    System.out.println("字节码长度: " + byteArray.length);
                    // 解析处理字节数组
                    byte[] classByte = decode(byteArray);
                    // 根据字节加载类 TODO: 注意这里是类名，不要跟文件名搞混了
                    return defineClass(className, classByte, 0, classByte.length);
                }
            }
            return null;
        } catch (Exception ex){
            throw new ClassNotFoundException(className, ex);
        } finally {
            // 关闭流
            try {
                zipInputStream.closeEntry();
                MyXlassLoader.close(input);
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    /**
     * @Description: 通过文件加载字节码
     * @Author: huxing
     * @param fileName   文件名
     * @param className  类名
     * @return java.lang.Class<?>
     * @Date: 2021/8/6 下午3:09
     **/
    private Class<?> loadByFile(String fileName, String className) throws ClassNotFoundException{
        System.out.println("通过路径加载字节码类文件");
        // 读入文件流
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(fileName);
            // 字节流长度
            int length = inputStream.available();
            // 定义字节数组
            byte[] byteArray = new byte[length];
            // 读取字节数组
            inputStream.read(byteArray);
            // 解析处理字节数组
            byte[] classByte = decode(byteArray);
            // 根据字节加载类
            return defineClass(className, classByte, 0, length);
        } catch (IOException ex){
            throw new ClassNotFoundException(className, ex);
        } finally {
            close(inputStream);
        }
    }

    /**
     * @Description:   调用指定类的指定方法
     * @Author: huxing
     * @Date: 2021-06-28 09:36
     * @param classz:   class类字节码
     * @param methodName:  方法名
     * @return: void
     **/
    private void invoke(Class<?> classz, String methodName) throws Exception{
        // 初始化创建类对象
        Object instance = classz.getDeclaredConstructor().newInstance();
        // 初始化创建类方法
        Method method = classz.getMethod(methodName);
        // 调用方法
        method.invoke(instance);
    }

    /**
     * @Description:   字节码加密
     * @Author: huxing
     * @Date: 2021-06-28 09:49
     * @param byteArray:
     * @return: byte[]
     **/
    public static byte[] encode(byte[] byteArray){
        // 定义一个解密字节数组长度
        byte[] targetArray = new byte[byteArray.length];
        // 转义长度
        for (int i=0; i<byteArray.length; i++){
            targetArray[i] = (byte)(255 - byteArray[i]);
        }
        // 返回解密后字节长度
        return targetArray;
    }

    /**
     * @Description:  字节码解密
     * @Author: huxing
     * @Date: 2021-06-28 09:47
     * @param byteArray:
     * @return: byte[]
     **/
    public static byte[] decode(byte[] byteArray){
        // 定义一个解密字节数组长度
        byte[] targetArray = new byte[byteArray.length];
        // 转义长度
        for (int i=0; i<byteArray.length; i++){
            targetArray[i] = (byte)(255 - byteArray[i]);
        }
        // 返回解密后字节长度
        return targetArray;
    }

    /**
     * @Description:  关闭IO流
     * @Author: huxing
     * @Date: 2021-06-28 09:43
     * @param res:
     * @return: void
     **/
    public static void close(Closeable res){
        if (null != res){
            try {
                res.close();
            } catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }
}
