/**
 * @Description: 字节码
 * @ProjectName: week01
 * @Package: PACKAGE_NAME
 * @ClassName: HelloByteCode
 * @Author: huxing
 * @DateTime: 2021-08-07 下午3:17
 */
public class HelloByteCode {

    public static void main(String[] args) {
        HelloByteCode obj = new HelloByteCode();
        obj.test();
    }

    public void test() {
        int num1 = 1;
        double num2 = 2.0D;
        long num3 = 3L;
        byte num4 = 4;
        if ("".length() < 10) {
            System.out.println("加法运算: " + (num2 + (double)num3));
        }

        for(int i = 0; i < num1; ++i) {
            System.out.println("乘法运算: " + num1 * num4);
        }

    }
}
