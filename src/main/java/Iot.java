import org.eclipse.paho.client.mqttv3.*;

public class Iot implements MqttCallback {
    MqttClient client;
    DBlink link = new DBlink();
    final String im_sensor_t = "factory/image_sensor";
    final String lv_sensor_t = "factory/level_sensor";
    final String ph_sensor_t = "factory/photo_sensor";
    final String model_t = "factory/model";
    //конструктор подключается к серверу
    public Iot(String clientid){
        try {
            client = new MqttClient("tcp://localhost:1883", clientid);
            client.connect();
            client.setCallback(this);
        } catch (MqttException e){
            System.err.println(e);
        }
    }

    ///метод публикации
    public void publish(String level_sensor, String image_sensor, String photo_sensor){
        MqttMessage lv_msg = new MqttMessage(level_sensor.getBytes());
        MqttMessage im_msg = new MqttMessage(image_sensor.getBytes());
        MqttMessage ph_msg = new MqttMessage(photo_sensor.getBytes());
        //MqttMessage mo_msg = new MqttMessage(model.getBytes());

        try {
            client.publish(lv_sensor_t, lv_msg);
            client.publish(im_sensor_t, im_msg);
            client.publish(ph_sensor_t, ph_msg);
            //client.publish(model_t, mo_msg);
            System.out.println("Message published");
        } catch (MqttException e){
            System.err.println(e);
        }
    }

    //метод подписчика
    public void subscribe() {
        try {
            client.subscribe(im_sensor_t);
            client.subscribe(lv_sensor_t);
            client.subscribe(ph_sensor_t);
            client.subscribe(model_t);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        // TODO Auto-generated method stub

    }

    //переопределенный метод реакции на сообщение
    @Override
    public void messageArrived(String topic, MqttMessage message)
            throws Exception {
        System.out.println("Message sent to parsing: " + message);
        //парсим сообщение
        String query = messageParse(topic, message);
        System.out.println("Send query: " + query);
        //отправляем в бд
        link.executeQuery(query);
    }

    //парсим пришедшие данные
    private String messageParse(String topic, MqttMessage message){
        String[] msg = message.toString().split("#");
        String query;
        switch (topic) {
            case "factory/image_sensor" -> {
                query = "INSERT INTO factory.image_sensor (imageSensorId, total, good, bad, imSDate) VALUES (" +
                        msg[0] + "," + msg[1] + "," + msg[2] + "," + msg[3] + "," + msg[4] + ")";
                return query;
            }
            /*case "factory/model" -> {
                query = "INSERT INTO factory.model (modelId, nameOfModel, coordXModel, coordYModel, squareModel) VALUES (" +
                        msg[0] + "," + msg[1] + "," + msg[2] + "," + msg[3] + "," + msg[4] + ")";
                return query;
            }*/
            case "factory/level_sensor" -> {
                query = "INSERT INTO factory.level_sensor (levelSensorId, criticalLevel, isCritical, level, lSDate) VALUES (" +
                        msg[0] + "," + msg[1] + "," + msg[2] + ","+ msg[3] + "," + msg[4] + ")";
                return query;
            }
            case "factory/photo_sensor" -> {
                query = "INSERT INTO factory.photo_sensor (photoSensorId, isDetectedObject, levelObject, pSDate) VALUES (" +
                        msg[0] + "," + msg[1] + "," + msg[2] + "," + msg[3] + ")";
                return query;
            }
            default -> {
                System.err.println("Topic loss");
                return null;
            }
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // TODO Auto-generated method stub

    }

}