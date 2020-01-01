package cn.enjoy.xxtomcat;

import java.lang.ref.WeakReference;

/**
 * @author 享学课堂[Roy老师]
 */
public class Test {
    public static void main(String[] args) {
//        WeakRef weakRef = new Test().new WeakRef();
        final WeakRef weakRef = new Test().new WeakRef();
        WeakReference<WeakRef> weakCar = new WeakReference<WeakRef>(weakRef);
        System.out.println("111");
        System.gc();
        for (int i = 0; i < 1000; i++) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("222");
    }

    class WeakRef{
        int a = 1;
        @Override
        protected void finalize() throws Throwable {
            System.out.println("我被回收了");
        }
    }
}
