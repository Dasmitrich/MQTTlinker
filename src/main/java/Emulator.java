import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Emulator {
    ArrayList<String> level_sensor = new ArrayList();

    //генерируем псевдоданные в конструкторе
    public Emulator() {
        for (int i = 0; i < 15; i++) {
            level_sensor.add(i + "#" + new Random().nextInt(2, 100)
                    + "#" + new Random().nextInt(1, 70));
            System.out.println(level_sensor.get(i));
        }
    }

    //эмулируем передачу данных по mqtt
    public void publishEmulate(){
        Iot iot = new Iot("Pub");
        for(int i=0; i< 15; i++) {
            iot.publish(level_sensor.get(i));
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                System.err.println("Emulator failed "+e);
            }
        }
    }
}
