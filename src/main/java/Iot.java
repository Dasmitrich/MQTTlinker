import org.eclipse.paho.client.mqttv3.*;

public class Iot implements MqttCallback {
    MqttClient client;
    DBlink link = new DBlink();
    final String im_sensor = "factory/image_sensor";
    final String lv_sensor = "factory/level_sensor";
    final String ph_sensor = "factory/photo_sensor";
    final String object = "factory/object";

    public Iot(String clientid){
        try {
            client = new MqttClient("tcp://localhost:1883", clientid);
            client.connect();
            client.setCallback(this);
        } catch (MqttException e){
            System.err.println(e);
        }
    }
    public void publish(String level_sensor){
        MqttMessage message = new MqttMessage(level_sensor.getBytes());
        try {
            client.publish(lv_sensor, message);
            System.out.println("Message published");
        } catch (MqttException e){
            System.err.println(e);
        }
    }

    public void subscribe() {
        try {
            client.subscribe(im_sensor);
            client.subscribe(lv_sensor);
            client.subscribe(ph_sensor);
            client.subscribe(object);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        // TODO Auto-generated method stub

    }

    @Override
    public void messageArrived(String topic, MqttMessage message)
            throws Exception {
        System.out.println("Message sent to parsing: " + message);
        String query = messageParse(topic, message);
        System.out.println("Send query: " + query);
        link.executeQuery(query);
    }

    private String messageParse(String topic, MqttMessage message){
        String msg[] = message.toString().split("#");
        String query = null;
        switch (topic) {
            case "factory/image_sensor" -> {
                query = "INSERT INTO factory.image_sensor (imageSensorId, total, good, bad) VALUES ( " +
                        msg[0] + "," + msg[1] + "," + msg[2] + "," + msg[3] + ")";
                return query;
            }
            case "factory/model" -> {
                query = "INSERT INTO model (modelId, nameOfModel, coordXModel, coodYModel, squareModel) VALUES (" +
                        msg[0] + ",'" + msg[1] + "'," + msg[2] + "," + msg[3] + "," + msg[4] + ")";
                return query;
            }
            case "factory/object" -> {
                query = "INSERT INTO object (objectId, modelId, coordX, coodY, square) VALUES (" +
                        msg[0] + "," + msg[1] + "," + msg[2] + "," + msg[3] + "," + msg[4] + ")";
                return query;
            }
            case "factory/level_sensor" -> {
                query = "INSERT INTO factory.level_sensor (levelSensorId, criticalLevel, isCritical) VALUES (" +
                        msg[0] + "," + msg[1] + "," + msg[2] + ")";
                return query;
            }
            case "factory/photo_sensor" -> {
                query = "INSERT INTO factory.photo_sensor (photoSensorId, isDetectedObject, levelObject) VALUES (" +
                        msg[0] + "," + msg[1] + "," + msg[2] + ")";
                return query;
            }
            default -> {
                return null;
            }
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // TODO Auto-generated method stub

    }

}