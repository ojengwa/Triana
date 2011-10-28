package org.trianacode.enactment.logging.rabbit;

import com.rabbitmq.client.*;
import org.trianacode.enactment.logging.Loggers;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: ian
 * Date: 18/08/2011
 * Time: 11:18
 * To change this template use File | Settings | File Templates.
 */
public class RabbitHandler {

    private static Channel channel = null;
    private static Connection connection = null;
    private String queueName = null;

    private static RabbitHandler rabbitHandler = new RabbitHandler();

    public static RabbitHandler getRabbitHandler() {
        return rabbitHandler;
    }

    private RabbitHandler() {
    }

    public void initConnection(String host, int port, String username, String password, String queueName) throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        if (username != null && password != null) {
            factory.setUsername(username);
            factory.setPassword(password);
        }
        factory.setPort(port);
        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.addShutdownListener(new ShutdownListener() {
            @Override
            public void shutdownCompleted(ShutdownSignalException e) {
                try {
                    Loggers.DEV_LOGGER.debug("Channel shutdown, tidying up");
                    closeAll();
                } catch (IOException e1) {
                    Loggers.DEV_LOGGER.debug("Error shutting down Rabbit channel/connection");
                }
            }
        });
        channel.queueDeclare(queueName, false, false, false, null);
        this.queueName = queueName;
        System.out.println("Connection made " + channel.toString());
    }

    private void closeAll() throws IOException {
        channel.close();
        connection.close();
        channel = null;
        connection = null;
    }

    public void sendLog(String string) throws IOException {
        if (isReady()) {
            channel.basicPublish("", queueName, null, string.getBytes());
        } else {
            Loggers.CONFIG_LOGGER.error("Error publishing stampede log");
        }

    }

    public boolean isReady() {
        return !(channel == null || !channel.isOpen() || queueName == null);
    }
}
