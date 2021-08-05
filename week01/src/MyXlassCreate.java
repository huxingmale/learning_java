import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @Description: xlcass文件创建
 * @ProjectName: week01
 * @Package: PACKAGE_NAME
 * @ClassName: MyXlassCreate
 * @Author: huxing
 * @DateTime: 2021-08-05 下午4:11
 */
public class MyXlassCreate {

    public static void main(String[] args) {
        // 初始化对象
        MyXlassCreate xlassCreate = new MyXlassCreate();
        // 文件路径
        String filePath = xlassCreate.getClass().getClassLoader().getResource("").getPath();
        // 创建一个xlass文件
        xlassCreate.createXlass(filePath);
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
            byte[] classzArray = MyXlassCreate.encode(byteArray);
            // 写入Hello.xlass文件
            String outFilePath = System.getProperty("user.dir") + "/doc/";
            outputStream = new FileOutputStream(outFilePath + "Hello.xlass");
            outputStream.write(classzArray);
        } catch (IOException ex){
            ex.printStackTrace();
        }finally {
            MyXlassLoader.close(inputStream);
            MyXlassLoader.close(outputStream);
        }
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
            targetArray[i] = (byte)((byteArray[i] & 0XFF) << 8);
        }
        // 返回解密后字节长度
        return targetArray;
    }
}
