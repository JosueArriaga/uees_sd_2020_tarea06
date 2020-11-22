/**
 Java ECHO server with UDP sockets example
 */

import java.io.*;
import java.net.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
public class server
{
    public static void main(String args[])
    {


        try
        {


            //1. creating a server socket, parameter is local port number
            DatagramSocket  sock = new DatagramSocket(9999);



            //2. Wait for an incoming data
            System.out.println("Esperando los datos entrantes");


            Connection con=DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/uees","root","root1234");


            col(con,sock);
            //communication loop

        }


        catch(IOException | SQLException  | InterruptedException e)
        {
            System.err.println("IOException " + e);
        }
    }

    //simple function to echo data to terminal

    public static void col(Connection conexion, DatagramSocket servidorSocket) throws  InterruptedException {


        while (true) {
            try {
                //recibiendo los valores del colector
                byte[] sensorReceptor = new byte[1024];
                servidorSocket.setSoTimeout(10000);
                DatagramPacket Sensorpaquete = new DatagramPacket(sensorReceptor, sensorReceptor.length);
                servidorSocket.receive(Sensorpaquete);
                String sensor = new String(Sensorpaquete.getData());


                    byte[] lecuraReceptor = new byte[1024];

                    DatagramPacket lecturaPaquete = new DatagramPacket(lecuraReceptor, lecuraReceptor.length);
                    servidorSocket.receive(lecturaPaquete);
                    String lecturaStr = new String(lecturaPaquete.getData());
                    float lectura=Float.parseFloat(lecturaStr.trim());
                    byte[] timeReceptor = new byte[1024];

                    DatagramPacket timePaquete = new DatagramPacket(timeReceptor, timeReceptor.length);
                    servidorSocket.receive(timePaquete);
                    String time = new String(timePaquete.getData());
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();

                    String timeservidor=dtf.format(now);
                //para comprobar si hay algún duplicado o se ha perdido
                boolean compruebeError=false;





                    try {

                        //   si el valor más de 7 se pierde
                        if (lectura >= 7) {
                            byte[] responder = new byte[1024];
                            String send = "perdido";

                            responder = send.getBytes();
                            DatagramPacket responderPaquete = new DatagramPacket(responder, responder.length, timePaquete.getAddress(), timePaquete.getPort());
                            servidorSocket.send(responderPaquete);
                            File file = new File("log.txt");
                            FileWriter fr = new FileWriter(file, true);
                            fr.write(sensor.trim() + " / " + lectura + " / " + time.trim() + " / " + timeservidor.trim() + " / " + send + "\n");
                            fr.close();
                            compruebeError=true;


                        } else {
                            //comprobando si hay un duplicate
                            Statement stmt = conexion.createStatement();
                            ResultSet rs = stmt.executeQuery("SELECT * FROM sensor WHERE sensorID = '" + sensor.trim() + "' AND tiempoS = '" + timeservidor.trim() + "' AND tiempoC= '" + time.trim() + "' AND (lectura LIKE " + lectura + " OR lectura=" + lectura + "  )");

                            //si el duplicado es excitado
                            if (rs.next()) {
                                System.out.println("duplicado");

                                byte[] responder = new byte[1024];


                                String send = "duplicado";
                                compruebeError = true;
                                responder = send.getBytes();
                                DatagramPacket responderPaquete = new DatagramPacket(responder, responder.length, timePaquete.getAddress(), timePaquete.getPort());
                                servidorSocket.send(responderPaquete);
                                File file = new File("log.txt");
                                FileWriter fr = new FileWriter(file, true);
                                fr.write(sensor.trim() + " / " + lectura + " / " + time.trim() + " / " + timeservidor.trim() + " / " + send + "\n");
                                fr.close();

                                //si el duplicado no excita
                            } else if (!rs.next()) {
                                Statement stmt1 = conexion.createStatement();
                                stmt1.executeUpdate("insert into sensor values('" + sensor.trim() + "'," + lectura + ",'" + time.trim() + "','" + timeservidor.trim() + "')");
                                byte[] responder = new byte[1024];
                                String send = "OK";
                                responder = send.getBytes();
                                DatagramPacket responderPaquete = new DatagramPacket(responder, responder.length, timePaquete.getAddress(), timePaquete.getPort());
                                servidorSocket.send(responderPaquete);
                                File file = new File("log.txt");
                                FileWriter fr = new FileWriter(file, true);
                                fr.write(sensor.trim() + " / " + lectura + " / " + time.trim() + " / " + timeservidor.trim() + " / " + send + "\n");
                                fr.close();


                            }
                        }//si hay alguna perdida o duplicado
                            if (compruebeError)
                            {
                                byte[] nuevorecevinglecura = new byte[1024];

                                DatagramPacket nuevolecturaPA = new DatagramPacket(nuevorecevinglecura, nuevorecevinglecura.length);
                                servidorSocket.receive(nuevolecturaPA);
                                String nuevolecturaS = new String(nuevolecturaPA.getData());
                                float nuevolectura=Float.parseFloat(nuevolecturaS.trim());
                                Statement stmt1 = conexion.createStatement();
                                stmt1.executeUpdate("insert into sensor values('" + sensor.trim() + "'," + nuevolectura + ",'" + time.trim() + "','" + timeservidor.trim() + "')");
                                byte[] responder = new byte[1024];
                                String send = "perdido o el duplicado se ha manejado ";
                                responder = send.getBytes();
                                DatagramPacket responderPaquete = new DatagramPacket(responder, responder.length, timePaquete.getAddress(), timePaquete.getPort());
                                servidorSocket.send(responderPaquete);
                                File file = new File("log.txt");
                                FileWriter fr = new FileWriter(file, true);
                                fr.write(sensor.trim() + " / " + nuevolectura + " / " + time.trim() + " / " + timeservidor.trim() + " / " + send + "\n");
                                fr.close();

                            }

                        } catch(Exception e){
                            System.out.println(e);
                        }



                }


            catch (SocketTimeoutException e) {
                // timeout exception.
                System.out.println("Timeout reached!!! " + e);
                TimeUnit.SECONDS.sleep(5);

            }catch (SocketException e1) {
                // TODO Auto-generated catch block
                //e1.printStackTrace();
                System.out.println("Socket closed " + e1);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


    }
}
