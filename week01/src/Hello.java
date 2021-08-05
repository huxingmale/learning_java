/**
 * @Description:
 * @ProjectName: week01
 * @Package: PACKAGE_NAME
 * @ClassName: Hello
 * @Author: huxing
 * Date: 2021-06-28
 * Time: 09:07
 */
public class Hello {

    private static String test;

    static {
        test = "这是一个测试方法工具类";
    }

    public static void printTest(){
        System.out.println("打印测试字符串: " + test);
    }

    /**
     * @Description: 欢迎来到Hello
     * @Author: huxing
     * @Date: 2021-06-28 09:30
     * @return: void
     **/
    public void hello(){
        System.out.println("Hello, Welcome you, I'm Create A Xar FILE");
    }

    /**
     * @Description: 4则运算测试
     * @Author: huxing
     * @Date: 2021-06-28 09:30
     * @return: void
     **/
    public void test(){
        // int类型 1
        int    num1 = 1;
        // double类型 2
        double num2 = 2.0D;
        // long类型 3
        long   num3 = 3L;
        // byte类型4
        byte   num4 = 4;
        // 加运算
        if ("".length() < 10){
            System.out.println("加法运算: " + (num2 + num3));
        }

        for (int i = 0; i< num1; i++){
            System.out.println("乘法运算: " + (num1 * num4));
        }
    }
}
