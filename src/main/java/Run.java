public class Run extends Thread{
    public static void main(String[] args){
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                Iot iot = new Iot("Sub");
                iot.subscribe();
            }
        });
        t1.start();

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                new Emulator().publishEmulate();
            }
        });
        t2.start();
    }
}
