/**
 *
 */
package com.google.rconclient.rcon;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Random;

/**
 * This class implements the communication with MineCraft using the RCon
 * protocol.
 *
 * @author vincent
 */
public class RCon {

    /**
     * The value of a command packet type.
     */
    private static final int COMMAND_TYPE = 2;

    /**
     * The value of a login packet type.
     */
    private static final int LOGIN_TYPE = 3;

    /**
     * The request id that will be used for this communication channel.
     */
    private final int requestId;

    /**
     * The socket used for the communication.
     */
    private final Socket socket;

    /**
     * The input stream for reading the data from MineCraft.
     */
    private final InputStream inputStream;

    /**
     * The output stream for sending the data to MineCraft.
     */
    private final OutputStream outputStream;

    /**
     * The object to be used for synchronization of the communication using the
     * socket. This means also the inputStream and outputStream.
     */
    private final Object syncObject = new Object();

    private boolean shutdown = true;

    /**
     * Create a new communication channel to MineCraft using the RCon protocol.
     * The channel will try to connect to the defined host and port and initiate
     * the login sequence for authentication with MineCraft.
     *
     * @param host     The name or IP address of the host.
     * @param port     The port.
     * @param password The password.
     * @throws IOException             Some sort of I/O exception occurred.
     * @throws AuthenticationException The authentication using the password failed.
     */
    public RCon(final String host, final int port, final char[] password) throws IOException, AuthenticationException {
        super();
        final Random random = new Random();
        requestId = random.nextInt();
        socket = new Socket(host, port);
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();
        final byte[] passwordBytes = new byte[password.length];
        for (int i = 0; i < password.length; i++) {
            passwordBytes[i] = (byte) password[i];
        }
        final byte[] response = send(LOGIN_TYPE, passwordBytes);
        for (int i = 0; i < passwordBytes.length; i++) {
            passwordBytes[i] = 0;
        }
        assert response.length == 0;
        shutdown = false;
    }

    public boolean isShutdown() {
        return shutdown;
    }

    /**
     * Close the communication channel to MineCraft. After that, no
     * communication is possible anymore.
     *
     * @throws IOException Some sort of I/O exception occurred.
     */
    public void close() throws IOException {
        synchronized (syncObject) {
            if (!socket.isClosed()) {
                socket.close();
                shutdown = true;
            }
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("RCon [requestId=");
        builder.append(requestId);
        builder.append(", socket=");
        builder.append(socket);
        builder.append("]");
        return builder.toString();
    }

    /**
     * Send data to MineCraft and return is't response.
     *
     * @param type    The type of the packet to use.<br>
     *                COMMAND_TYPE: send a command packet.<br>
     *                LOGIN_TYPE: send a login packet.
     * @param payload The data to send.
     * @return The response.
     * @throws IOException                 Some sort of I/O exception occurred.
     * @throws IncorrectRequestIdException The request id was not as expected.
     */
    private byte[] send(final int type, final byte[] payload) throws IOException, IncorrectRequestIdException {
        final byte[] receivedPayload;
        synchronized (syncObject) {
            // Send the command.
            final int sendLength = 4 + 4 + payload.length + 2;
            final byte[] sendBytes = new byte[4 + sendLength];
            final ByteBuffer sendBuffer = ByteBuffer.wrap(sendBytes);
            sendBuffer.order(ByteOrder.LITTLE_ENDIAN);
            sendBuffer.putInt(sendLength);
            sendBuffer.putInt(requestId);
            sendBuffer.putInt(type);
            sendBuffer.put(payload);
            sendBuffer.put((byte) 0).put((byte) 0);
            outputStream.write(sendBytes);
            outputStream.flush();

            // Receive the response.
            final byte[] receivedBytes = new byte[4096];
            final int receivedBytesLength = inputStream.read(receivedBytes);
            final ByteBuffer receivedBuffer = ByteBuffer.wrap(receivedBytes, 0, receivedBytesLength);
            receivedBuffer.order(ByteOrder.LITTLE_ENDIAN);
            final int receivedLength = receivedBuffer.getInt();
            final int receivedRequestId = receivedBuffer.getInt();
            @SuppressWarnings("unused")
            final int receivedType = receivedBuffer.getInt();
            receivedPayload = new byte[receivedLength - 4 - 4 - 2];
            receivedBuffer.get(receivedPayload);
            receivedBuffer.get(new byte[2]);
            if (receivedRequestId != requestId) {
                final IncorrectRequestIdException exception = new IncorrectRequestIdException(receivedRequestId);
                throw exception;
            }
        }
        return receivedPayload;
    }

    /**
     * Send data to MineCraft and return is't response.
     *
     * @param type    The type of the packet to use.<br>
     *                COMMAND_TYPE: send a command packet.<br>
     *                LOGIN_TYPE: send a login packet.
     * @param payload The data to send.
     * @return The response.
     * @throws IOException                 Some sort of I/O exception occurred.
     * @throws IncorrectRequestIdException The request id was not as expected.
     */
    private String send(final int type, final String payload) throws IOException, IncorrectRequestIdException {
        final String responsePayload = new String(send(type, payload.getBytes(Charset.forName("US-ASCII"))),
                Charset.forName("US-ASCII"));
        return responsePayload;
    }

    /**
     * Send a command to MineCraft and return its' response. This method will
     * always send the command using the COMMAND_TYPE packet type.
     *
     * @param payload The data to send.
     * @return The response.
     * @throws IOException                 Some sort of I/O exception occurred.
     * @throws IncorrectRequestIdException The request id was not as expected.
     */
    public String send(final String payload) throws IOException, IncorrectRequestIdException {
        final String response = send(COMMAND_TYPE, payload);
        return response;
    }

    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }

}