import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

/**
 * @Description: GC日志解读分析工具类
 * @ProjectName: week02
 * @Package: PACKAGE_NAME
 * @ClassName: GCLogAnalysis
 * @Author: huxing
 * @DateTime: 2021-08-10 上午10:23
 */
public class GCLogAnalysis {

    private static Random random = new Random();

    public static void main(String[] args) {
        // 当前毫秒时间戳
        long startMillis = System.currentTimeMillis();
        // 持续运行毫秒数
        long timeOutMillis = TimeUnit.SECONDS.toMillis(1);
        // 结束时间戳
        long endMillis = startMillis + timeOutMillis;
        //TODO: 这个是啥类？
        LongAdder counter = new LongAdder();
        System.out.println("正在执行...");
        // 缓存一部分对象进入老年代
        int cacheSize = 2000;
        Object[] cacheGarbage = new Object[cacheSize];
        // 在此时间内持续循环
        while (System.currentTimeMillis() < endMillis){
            // 生成垃圾对象
            Object garbage = generatGarbage(100 * 1024);
            counter.increment();
            int randomIndex = random.nextInt(2 * cacheSize);
            if (randomIndex < cacheSize){
                cacheGarbage[randomIndex] = garbage;
            }
        }
        System.out.println("执行结束！共生成对象次数: " + counter.longValue());
    }

    /**
     * @Description: 生成垃圾对象
     * @Author: huxing
     * @param max
     * @return java.lang.Object
     * @Date: 2021/8/10 上午10:29
     **/
    private static Object generatGarbage(int max){
        int randomSize = random.nextInt(max);
        int type = randomSize % 4;
        Object result = null;
        switch (type){
            case 0:
                result = new int[randomSize];
                break;
            case 1:
                result = new byte[randomSize];
                break;
            case 2:
                result = new double[randomSize];
                break;
            default:
                StringBuilder builder = new StringBuilder();
                String randomString = "randomString-Anything";
                while (builder.length() < randomSize){
                    builder.append(randomString);
                    builder.append(max);
                    builder.append(randomSize);
                }
                result = builder.toString();
                break;
        }
        return result;
    }
}
