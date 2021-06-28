import java.io.*;
import java.lang.reflect.Method;

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
        String filePath = xlassLoader.getClass().getClassLoader().getResource("").getPath();
        System.out.println("文件路径: " + filePath);
        // 创建一个xlass文件
        xlassLoader.createXlass(filePath);
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

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException{
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
     * @Description: 创建xlass类文件
     * @Author: huxing
     * @Date: 2021-06-28 09:54
     * @return: void
     **/
    protected void createXlass(String filePath){
        // 初始化文件输入流
        FileInputStream inputStream = null;
        // 初始化文件输出流
        FileOutputStream outputStream = null;
        try {
            // 加载Hello.class文件
            inputStream = new FileInputStream(filePath + "Hello.class");
            int length = inputStream.available();
            byte[] byteArray = new byte[length];
            byte[] classzArray = MyXlassLoader.encode(byteArray);
            // 写入Hello.xlass文件
            outputStream = new FileOutputStream(filePath + "Hello.xlass");
            outputStream.write(classzArray);
        } catch (IOException ex){
            ex.printStackTrace();
        }finally {
            MyXlassLoader.close(inputStream);
            MyXlassLoader.close(outputStream);
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
//            targetArray[i] = (byte)(255 - byteArray[i]);
            targetArray[i] = (byte)((byteArray[i] & 0XFF) << 8);
        }
        // 返回解密后字节长度
        return targetArray;
    }

    /**
     * @Description:   字节码加密
     * @Author: huxing
     * @Date: 2021-06-28 09:49
     * @param byteArray:
     * @return: byte[]
     **/
    private static byte[] encode(byte[] byteArray){
        // 定义一个解密字节数组长度
        byte[] targetArray = new byte[byteArray.length];
        // 转义长度
        for (int i=0; i<byteArray.length; i++){
//            targetArray[i] = (byte)(255 - byteArray[i]);
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
    private static void close(Closeable res){
        if (null != res){
            try {
                res.close();
            } catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }
}
