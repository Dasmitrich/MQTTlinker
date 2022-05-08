import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Emulator {
    ArrayList<String> level_sensor = new ArrayList();
    ArrayList<String> image_sensor = new ArrayList();
    ArrayList<String> photo_sensor = new ArrayList();
    ArrayList<String> model = new ArrayList();
    String[] modarray = {"'white'", "'black'", "'diabetic'", "'porous'"};
    Random rand = new Random();

    //генерируем псевдоданные в конструкторе
    public Emulator() {
        for (int i = 0; i < 20; i++) {
            level_sensor.add(rand.nextInt( 1,4) + "#" + rand.nextInt( 201)
                    + "#" + rand.nextInt(2) + "#" + rand.nextInt(211)
                    +"#'2021-12-" + new Random().nextInt(1,32) +" "+
                    rand.nextInt(8,20)+":"+ rand.nextInt(0,61)+":00'");

            image_sensor.add( rand.nextInt( 1,4) + "#" + rand.nextInt( 101)
                    + "#" + rand.nextInt(1000) + "#" + rand.nextInt(391)
                    +"#'2021-12-" + rand.nextInt(1,32) +" "+
                    rand.nextInt(8,20)+":"+ rand.nextInt(0,61)+":00'");

            photo_sensor.add(rand.nextInt( 1, 4) + "#" + rand.nextInt( 2)
                    + "#" + rand.nextInt(101) + "#'2021-12-" + rand.nextInt(1, 32) +" "+
                    rand.nextInt(8,20)+":"+ rand.nextInt(61)+":00'");
        }
    }

    //эмулируем передачу данных по mqtt
    public void publishEmulate(){
        Iot iot = new Iot("Pub");
        for(int i=0; i< 15; i++) {
            iot.publish(level_sensor.get(i), image_sensor.get(i), photo_sensor.get(i));
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                System.err.println("Emulator failed "+e);
            }
        }
    }
}
