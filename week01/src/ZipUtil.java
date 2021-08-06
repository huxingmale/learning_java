import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @Description:
 * @ProjectName: week01
 * @Package: PACKAGE_NAME
 * @ClassName: ZipUtil
 * @Author: huxing
 * @DateTime: 2021-08-05 下午4:19
 */
public class ZipUtil {

    public static final int BUFFER_SIZE = 4096;

    /** 压缩文件路径 **/
    public static final String filePath = System.getProperty("user.dir") + "/doc/";

    /**
     * 把文件集合打成zip压缩包
     * @param srcFiles 压缩文件集合
     * @param zipFile  zip文件名
     * @throws RuntimeException 异常
     */
    public static void toZip(List<File> srcFiles, File zipFile) throws RuntimeException {
        long start = System.currentTimeMillis();
        if(zipFile == null){
            System.out.println("压缩包文件名为空！");
            return;
        }
        if(!zipFile.getName().endsWith(".zip")){
            System.out.println("压缩包文件名异常，zipFile= " + zipFile.getPath());
            return;
        }
        ZipOutputStream zos = null;
        try {
            FileOutputStream out = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(out);
            for (File srcFile : srcFiles) {
                byte[] buf = new byte[BUFFER_SIZE];
                zos.putNextEntry(new ZipEntry(srcFile.getName()));
                int len;
                FileInputStream in = new FileInputStream(srcFile);
                while ((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                zos.setComment("我是注释");
                zos.closeEntry();
                in.close();
                out.close();
            }
            long end = System.currentTimeMillis();
            System.out.println("压缩完成，耗时：" + (end - start) + " ms");
        } catch (Exception e) {
            System.out.println("ZipUtil toZip exception, " + e.getMessage());
            throw new RuntimeException("zipFile error from ZipUtils", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    System.out.println("ZipUtil toZip close exception, " + e.getMessage());
                }
            }
        }
    }

    public static void unZip(String filePath) throws Exception{
        //读取压缩文件
        ZipInputStream in = new ZipInputStream(new FileInputStream(filePath + "xlass.xar"));
        //zip文件实体类
        ZipEntry entry;
        //遍历压缩文件内部 文件数量
        while((entry = in.getNextEntry()) != null){
            if(!entry.isDirectory()){
                //文件输出流
                FileOutputStream out = new FileOutputStream( filePath + entry.getName());
                BufferedOutputStream bos = new BufferedOutputStream(out);
                int len;
                byte[] buf = new byte[1024];
                while ((len = in.read(buf)) != -1) {
                    bos.write(buf, 0, len);
                }
                // 关流顺序，先打开的后关闭
                bos.close();
                out.close();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        // 压缩文件名
        String zipName = filePath + "xlass.zip";
        // 压缩文件包名
        File zipFile = new File(zipName);
        // 打包文件
        List<File> fileList = new ArrayList<>();
        fileList.add(new File(filePath + "Hello.xlass"));
        ZipUtil.toZip(fileList, zipFile);
        // 修改文件名
        File xarFile = new File(filePath + "xlass.xar");
        // 修改文件名
        zipFile.renameTo(xarFile);
    }
}
