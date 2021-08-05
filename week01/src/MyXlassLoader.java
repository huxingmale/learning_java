import java.io.*;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
        // 文件路径
        String filePath=System.getProperty("user.dir") + "/doc/";
        // 打印记录
        System.out.println("文件路径: " + filePath);
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
        // 默认采用xar包模式加载
        return loadByXar(name);
        // 通过直接读取文件的方式加载
//        return loadByFile(name);
    }

    /**
     * @Description: 通过读取路径文件加载xlass文件
     * @Author: huxing
     * @param name
     * @return java.lang.Class<?>
     * @Date: 2021/8/5 下午5:04
     **/
    private Class<?> loadByXar(String name) throws ClassNotFoundException{
        // 当前路径名 如果输入带包名做路径转换
        String resourcePath = name.replace(".", "/");
        // 后缀名
        final String suffix = ".xlass";
        // 加载文件
        return reloadXarFile(resourcePath + suffix);
    }

    /**
     * @Description: 通过xar压缩包加载文件类文件
     * @Author: huxing
     * @param name
     * @return java.lang.Class<?>
     * @Date: 2021/8/5 下午5:06
     **/
    private Class<?> loadByFile(String name) throws ClassNotFoundException{
        // 当前路径名 如果输入带包名做路径转换
        String resourcePath = name.replace(".", "/");
        // 后缀名
        final String suffix = ".xlass";
        // 读入文件流
        InputStream inputStream = this.getClass().getResourceAsStream(resourcePath + suffix);
        try {
            // 字节流长度
            int length = inputStream.available();
            // 定义字节数组
            byte[] byteArray = new byte[length];
            // 读取字节数组
            inputStream.read(byteArray);
            // 解析处理字节数组
            byte[] classByte = decode(byteArray);
            // 根据字节加载类
            return defineClass(name, classByte, 0, length);
        } catch (IOException ex){
            throw new ClassNotFoundException(name, ex);
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
     * @Description:  字节码解密
     * @Author: huxing
     * @Date: 2021-06-28 09:47
     * @param byteArray:
     * @return: byte[]
     **/
    private static byte[] decode(byte[] byteArray){
        // 定义一个解密字节数组长度
        byte[] targetArray = new byte[byteArray.length];
        // 转义长度
        for (int i=0; i<byteArray.length; i++){
            targetArray[i] = (byte)((byteArray[i] & 0XFF) << 8);
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

    /**
     * @Description: 读取xar内的文件
     * @Author: huxing
     * @param
     * @Date: 2021/8/5 下午4:36
     **/
    @SuppressWarnings("all")
    private Class<?> reloadXarFile(String fileName) throws ClassNotFoundException{
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
                // 读取到了文件
                if (Objects.equals(fileName, ze.getName())){
                    //读取
                    BufferedReader br = new BufferedReader(new
                            InputStreamReader(zipInputStream, StandardCharsets.UTF_8));
                    String line;
                    //内容不为空，输出
                    while ((line = br.readLine()) != null) {
                        System.out.println(line);
                    }
                    // 解析处理字节数组
                    byte[] classByte = decode(line.getBytes(StandardCharsets.UTF_8));
                    // 根据字节加载类
                    return defineClass(ze.getName(), classByte, 0, classByte.length);
                }
            }
            // 压缩包内没有文件 返回空
            return null;
        } catch (Exception ex){
            throw new ClassNotFoundException(fileName, ex);
        } finally {
            // 关闭流
            try {
                zipInputStream.closeEntry();
                input.close();
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
}
