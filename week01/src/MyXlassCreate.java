import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        // 创建一个xlass文件
        xlassCreate.createXlass(ZipUtil.filePath);
        // 生成xar压缩文件包
        xlassCreate.createXar();
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
            // 读入文件字节流 TODO: 前面这里忘记加了
            inputStream.read(byteArray);
            byte[] classzArray = MyXlassLoader.encode(byteArray);
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
     * @Description: 生成xar压缩文件包
     * @Author: huxing
     * @param
     * @Date: 2021/8/5 下午5:27
     **/
    private void createXar(){
        // 压缩文件名
        String zipName = ZipUtil.filePath + "xlass.zip";
        // 压缩文件包名
        File zipFile = new File(zipName);
        // 打包文件
        List<File> fileList = new ArrayList<>();
        fileList.add(new File(ZipUtil.filePath + "Hello.xlass"));
        ZipUtil.toZip(fileList, zipFile);
        // 修改文件名
        File xarFile = new File(ZipUtil.filePath + "xlass.xar");
        // 修改文件名
        if (zipFile.renameTo(xarFile)){
            System.out.println("文件打包压缩成功");
        }
    }
}
