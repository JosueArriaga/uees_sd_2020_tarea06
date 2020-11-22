/**
 Java ECHO client with UDP sockets example
 */

import java.io.*;
import java.net.*;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Collecter
{
    public static void main(String args[])
    {
        try {

            while (true) {
                InetAddress host = InetAddress.getByName("192.168.100.74");
                int port=9999;

                System.out.println("por favor, seleccione una opcion");
                System.out.println("1. escenario 1  ");
                System.out.println("2. escenario 2  ");
                System.out.println("3. escenario 3  ");
                System.out.println("4. escenario 4  ");
                System.out.println("5. escenario 5  ");
                System.out.println("6. escenario 6  ");
                System.out.println("7. normal");
                System.out.println("8. exit");

                Scanner sc = new Scanner(System.in);
                int selection = sc.nextInt();

		boolean quit =false;
                switch (selection) {
                    case 1:
                       escenario1(host,port);
                       break;

                    case 2:

                        escenario2(host,port);
                        break;
                    case 3:

                        escenario3(host,port);
                        break;
                    case 4:
                        escenario4(host,port);
                        break;
                    case 5:
                        escenario5(host,port);
                        break;
                    case 6:
                        escenario6(host,port);
                        break;
                    case 7:
                        normal(host,port);
                        break;
                    case 8:
			quit=true;
                        break;

                    default:
                        System.out.println("other number");
                }
                if(quit) break;
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }









    }

    //simple function to echo data to terminal

    private static void normal(InetAddress host,int port) throws IOException, InterruptedException {


        try{
            //declare client socket
            //declarar el enchufe del cliente
            DatagramSocket ClienteSocket = new DatagramSocket();
            ClienteSocket.setSoTimeout(10000);
            Random rand = new Random();
            while (true)
            {
                //generate sensor and send it
                //Generar el sensor y enviarlo
                byte[] enviarsensor = new byte[1024];

                String sensor="sensor"+(rand.nextInt(4)+1);
                enviarsensor = sensor.getBytes();
                DatagramPacket sensorDP = new DatagramPacket(enviarsensor, enviarsensor.length, host, port);
                ClienteSocket.send(sensorDP);

                // Generar valand enviarlo
                //generate  value and send it
                byte[] enviandolectura = new byte[1024];
                float lecuraF= (float) (rand.nextInt(10)+ rand.nextDouble());
                //convete it from float to string
                //lo transmiten de la float a la string
                String  lecura = String.valueOf(lecuraF);
                enviandolectura = lecura.getBytes();
                DatagramPacket lecuraDP = new DatagramPacket(enviandolectura, enviandolectura.length, host, port);
                ClienteSocket.send(lecuraDP);
                //getting time of the collector
                //obtener el tiempo de la collector
                byte[] enviartiempoC = new byte[1024];
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                String time=dtf.format(now);
                enviartiempoC = time.getBytes();
                DatagramPacket timeDP = new DatagramPacket(enviartiempoC, enviartiempoC.length, host, port);
                ClienteSocket.send(timeDP);
                //getting response from the server
                //obteniendo respuesta del servidor
                byte[] Responde = new byte[1024];
                DatagramPacket confirm = new DatagramPacket(Responde, Responde.length);
                ClienteSocket.receive(confirm);
                String s = new String(confirm.getData());
                System.out.println(s.trim());
                //check if there is lost data or duplicate
                //comprobar si hay datos perdidos o duplicados
                if (s.trim().equals("perdido") || s.trim().equals("duplicado"))
                {
                    float nuevolecuraF= (float) (rand.nextInt(10)+ rand.nextDouble());
                    String  nuevolecura = String.valueOf(nuevolecuraF);
                    enviandolectura = nuevolecura.getBytes();
                    DatagramPacket nuevolecuraDP = new DatagramPacket(enviandolectura, enviandolectura.length, host, port);
                    ClienteSocket.send(nuevolecuraDP);
                    byte[] nuevoResponde = new byte[1024];

                    DatagramPacket nuevoconfirm = new DatagramPacket(nuevoResponde, nuevoResponde.length);
                    ClienteSocket.receive(nuevoconfirm);
                    s = new String(nuevoconfirm.getData());
                    System.out.println(s.trim());
                }

                TimeUnit.SECONDS.sleep(5);





            }
        }
        catch (SocketTimeoutException e) {
            // timeout exception.
            System.out.println("Tiempo de espera alcanzado, la conexión con el servidor puede haber fallado");
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


    }
    private static void escenario1(InetAddress host,int port)  {





        try {
            //declare client socket
            //declarar el enchufe del cliente
            DatagramSocket ClienteSocket = new DatagramSocket();

            Random rand = new Random();
            while (true) {
                //generate sensor and send it
                //Generar el sensor y enviarlo
                byte[] enviarsensor = new byte[1024];

                String sensor = "sensor" + (rand.nextInt(4) + 1);
                enviarsensor = sensor.getBytes();
                DatagramPacket sensorDP = new DatagramPacket(enviarsensor, enviarsensor.length, host, port);
                ClienteSocket.send(sensorDP);

                // Generar valand enviarlo
                //generate  value and send it
                byte[] enviandolectura = new byte[1024];
                float lecuraF = 10;
                //convete it from float to string
                //lo transmiten de la float a la string
                String lecura = String.valueOf(lecuraF);
                enviandolectura = lecura.getBytes();
                DatagramPacket lecuraDP = new DatagramPacket(enviandolectura, enviandolectura.length, host, port);
                ClienteSocket.send(lecuraDP);
                //getting time of the collector
                //obtener el tiempo de la collector
                byte[] enviartiempoC = new byte[1024];
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                String time = dtf.format(now);
                enviartiempoC = time.getBytes();
                DatagramPacket timeDP = new DatagramPacket(enviartiempoC, enviartiempoC.length, host, port);
                ClienteSocket.send(timeDP);
                //getting response from the server
                //obteniendo respuesta del servidor
                byte[] Responde = new byte[1024];
                DatagramPacket confirm = new DatagramPacket(Responde, Responde.length);
                ClienteSocket.receive(confirm);
                String s = new String(confirm.getData());
                System.out.println(s.trim());
                //check if there is lost data or duplicate
                //comprobar si hay datos perdidos o duplicados


                if (s.trim().equals("perdido")) {

                    float nuevolecuraF = (float) (rand.nextInt(10) + rand.nextDouble());
                    String nuevolecura = String.valueOf(nuevolecuraF);
                    enviandolectura = nuevolecura.getBytes();
                    DatagramPacket nuevolecuraDP = new DatagramPacket(enviandolectura, enviandolectura.length, host, port);
                    ClienteSocket.send(nuevolecuraDP);
                    byte[] nuevoResponde = new byte[1024];

                    DatagramPacket nuevoconfirm = new DatagramPacket(nuevoResponde, nuevoResponde.length);
                    ClienteSocket.receive(nuevoconfirm);
                    s = new String(nuevoconfirm.getData());
                    System.out.println(s.trim());

                }
                if ( s.trim().equals("duplicado")) {

                    float nuevolecuraF = (float) (rand.nextInt(10) + rand.nextDouble());
                    String nuevolecura = String.valueOf(nuevolecuraF);
                    enviandolectura = nuevolecura.getBytes();
                    DatagramPacket nuevolecuraDP = new DatagramPacket(enviandolectura, enviandolectura.length, host, port);
                    ClienteSocket.send(nuevolecuraDP);
                    byte[] nuevoResponde = new byte[1024];

                    DatagramPacket nuevoconfirm = new DatagramPacket(nuevoResponde, nuevoResponde.length);
                    ClienteSocket.receive(nuevoconfirm);
                    s = new String(confirm.getData());
                    System.out.println(s.trim());
                }
                TimeUnit.SECONDS.sleep(2);
               break;


            }
        }

        catch (SocketTimeoutException e) {
            // timeout exception.
            System.out.println("Tiempo de espera alcanzado, la conexión con el servidor puede haber fallado");
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


    }
    private static void escenario2(InetAddress host,int port) throws IOException, InterruptedException {

        try {


            //declare client socket
            //declarar el enchufe del cliente
            DatagramSocket ClienteSocket = new DatagramSocket();

            Random rand = new Random();
            while (true) {
                Instant start = Instant.now();
                TimeUnit.SECONDS.sleep(4);
                Instant finish = Instant.now();
                long timeElapsed = Duration.between(start, finish).toSeconds();
                boolean check = false;
                if (timeElapsed > 3) {
                    System.out.println("Su pedido puede estar esperando en alguna cola para ser entregado mas tarde.");
                    check = true;
                }
                //generate sensor and send it
                //Generar el sensor y enviarlo
                byte[] enviarsensor = new byte[1024];

                String sensor = "sensor" + (rand.nextInt(4) + 1);
                enviarsensor = sensor.getBytes();
                DatagramPacket sensorDP = new DatagramPacket(enviarsensor, enviarsensor.length, host, port);
                ClienteSocket.send(sensorDP);

                // Generar valand enviarlo
                //generate  value and send it
                byte[] enviandolectura = new byte[1024];
                float lecuraF = (float) (rand.nextInt(10) + rand.nextDouble());
                //convete it from float to string
                //lo transmiten de la float a la string
                String lecura = String.valueOf(lecuraF);
                enviandolectura = lecura.getBytes();
                DatagramPacket lecuraDP = new DatagramPacket(enviandolectura, enviandolectura.length, host, port);
                ClienteSocket.send(lecuraDP);
                //getting time of the collector
                //obtener el tiempo de la collector
                byte[] enviartiempoC = new byte[1024];
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                String time = dtf.format(now);
                enviartiempoC = time.getBytes();
                DatagramPacket timeDP = new DatagramPacket(enviartiempoC, enviartiempoC.length, host, port);
                ClienteSocket.send(timeDP);
                //getting response from the server
                //obteniendo respuesta del servidor
                byte[] Responde = new byte[1024];
                DatagramPacket confirm = new DatagramPacket(Responde, Responde.length);
                ClienteSocket.receive(confirm);
                String s = new String(confirm.getData());
                System.out.println(s.trim());
                //check if there is lost data or duplicate
                //comprobar si hay datos perdidos o duplicados


                if (s.trim().equals("perdido")) {

                    float nuevolecuraF = (float) (rand.nextInt(10) + rand.nextDouble());
                    String nuevolecura = String.valueOf(nuevolecuraF);
                    enviandolectura = nuevolecura.getBytes();
                    DatagramPacket nuevolecuraDP = new DatagramPacket(enviandolectura, enviandolectura.length, host, port);
                    ClienteSocket.send(nuevolecuraDP);
                    byte[] nuevoResponde = new byte[1024];

                    DatagramPacket nuevoconfirm = new DatagramPacket(nuevoResponde, nuevoResponde.length);
                    ClienteSocket.receive(nuevoconfirm);
                    s = new String(confirm.getData());
                    System.out.println(s.trim());
                    break;
                }
                if (s.trim().equals("perdido") || s.trim().equals("duplicado")) {

                    float nuevolecuraF = (float) (rand.nextInt(10) + rand.nextDouble());
                    String nuevolecura = String.valueOf(nuevolecuraF);
                    enviandolectura = nuevolecura.getBytes();
                    DatagramPacket nuevolecuraDP = new DatagramPacket(enviandolectura, enviandolectura.length, host, port);
                    ClienteSocket.send(nuevolecuraDP);
                    byte[] nuevoResponde = new byte[1024];

                    DatagramPacket nuevoconfirm = new DatagramPacket(nuevoResponde, nuevoResponde.length);
                    ClienteSocket.receive(nuevoconfirm);
                    s = new String(nuevoconfirm.getData());
                    System.out.println(s.trim());
                }


                if (check)
                    break;
                TimeUnit.SECONDS.sleep(5);


            }
        }


        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
    private static void escenario3(InetAddress host,int port)  {







        try {
            //declare client socket
            //declarar el enchufe del cliente
            DatagramSocket ClienteSocket = new DatagramSocket();


            Random rand = new Random();
            while (true)
            { ClienteSocket.setSoTimeout(1);
                
                //generate sensor and send it
                //Generar el sensor y enviarlo
                byte[] enviarsensor = new byte[1024];


                String sensor="sensor"+(rand.nextInt(4)+1);
                enviarsensor = sensor.getBytes();
                DatagramPacket sensorDP = new DatagramPacket(enviarsensor, enviarsensor.length, host, port);
                TimeUnit.SECONDS.sleep(3);
                ClienteSocket.send(sensorDP);

                // Generar valand enviarlo
                //generate  value and send it
                byte[] enviandolectura = new byte[1024];
                float lecuraF= (float) (rand.nextInt(10)+ rand.nextDouble());
                //convete it from float to string
                //lo transmiten de la float a la string
                String  lecura = String.valueOf(lecuraF);
                enviandolectura = lecura.getBytes();
                DatagramPacket lecuraDP = new DatagramPacket(enviandolectura, enviandolectura.length, host, port);
                ClienteSocket.send(lecuraDP);
                //getting time of the collector
                //obtener el tiempo de la collector
                byte[] enviartiempoC = new byte[1024];
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                String time=dtf.format(now);
                enviartiempoC = time.getBytes();
                DatagramPacket timeDP = new DatagramPacket(enviartiempoC, enviartiempoC.length, host, port);
                ClienteSocket.send(timeDP);
                //getting response from the server
                //obteniendo respuesta del servidor
                byte[] Responde = new byte[1024];
                DatagramPacket confirm = new DatagramPacket(Responde, Responde.length);
                ClienteSocket.receive(confirm);
                String s = new String(confirm.getData());
                System.out.println(s.trim());
                //check if there is lost data or duplicate
                //comprobar si hay datos perdidos o duplicados
                if (s.trim().equals("perdido") || s.trim().equals("duplicado"))
                {
                    float nuevolecuraF= (float) (rand.nextInt(10)+ rand.nextDouble());
                    String  nuevolecura = String.valueOf(nuevolecuraF);
                    enviandolectura = nuevolecura.getBytes();
                    DatagramPacket nuevolecuraDP = new DatagramPacket(enviandolectura, enviandolectura.length, host, port);
                    ClienteSocket.send(nuevolecuraDP);
                    byte[] nuevoResponde = new byte[1024];

                    DatagramPacket nuevoconfirm = new DatagramPacket(nuevoResponde, nuevoResponde.length);
                    ClienteSocket.receive(nuevoconfirm);
                    s = new String(nuevoconfirm.getData());
                    System.out.println(s.trim());
                }

                TimeUnit.SECONDS.sleep(5);











            }




        } catch (SocketTimeoutException e) {
            // timeout exception.
            System.out.println("Tiempo de espera alcanzado, la conexion con el servidor puede haber fallado");


        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    private static void escenario4(InetAddress host,int port) throws IOException, InterruptedException {


        try{
            //declare client socket
            //declarar el enchufe del cliente
            DatagramSocket ClienteSocket = new DatagramSocket();

            Random rand = new Random();
            while (true)
            {
                Instant start = Instant.now();
                TimeUnit.SECONDS.sleep(2);
                Instant finish = Instant.now();
                long timeElapsed = Duration.between(start, finish).toSeconds();
                boolean check = false;
                if (timeElapsed > 1) {
                    System.out.println("la respuesta puede llegar tarde");
                    check = true;
                }
                TimeUnit.SECONDS.sleep(4);
                //generate sensor and send it
                //Generar el sensor y enviarlo
                byte[] enviarsensor = new byte[1024];

                String sensor="sensor"+(rand.nextInt(4)+1);
                enviarsensor = sensor.getBytes();
                DatagramPacket sensorDP = new DatagramPacket(enviarsensor, enviarsensor.length, host, port);
                ClienteSocket.send(sensorDP);

                // Generar valand enviarlo
                //generate  value and send it
                byte[] enviandolectura = new byte[1024];
                float lecuraF= (float) (rand.nextInt(10)+ rand.nextDouble());
                //convete it from float to string
                //lo transmiten de la float a la string
                String  lecura = String.valueOf(lecuraF);
                enviandolectura = lecura.getBytes();
                DatagramPacket lecuraDP = new DatagramPacket(enviandolectura, enviandolectura.length, host, port);
                ClienteSocket.send(lecuraDP);
                //getting time of the collector
                //obtener el tiempo de la collector
                byte[] enviartiempoC = new byte[1024];
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                String time=dtf.format(now);
                enviartiempoC = time.getBytes();
                DatagramPacket timeDP = new DatagramPacket(enviartiempoC, enviartiempoC.length, host, port);
                ClienteSocket.send(timeDP);
                //getting response from the server
                //obteniendo respuesta del servidor
                byte[] Responde = new byte[1024];
                DatagramPacket confirm = new DatagramPacket(Responde, Responde.length);
                ClienteSocket.receive(confirm);
                String s = new String(confirm.getData());
                System.out.println(s.trim());
                //check if there is lost data or duplicate
                //comprobar si hay datos perdidos o duplicados
                if (s.trim().equals("perdido") || s.trim().equals("duplicado"))
                {
                    float nuevolecuraF= (float) (rand.nextInt(10)+ rand.nextDouble());
                    String  nuevolecura = String.valueOf(nuevolecuraF);
                    enviandolectura = nuevolecura.getBytes();
                    DatagramPacket nuevolecuraDP = new DatagramPacket(enviandolectura, enviandolectura.length, host, port);
                    ClienteSocket.send(nuevolecuraDP);
                    byte[] nuevoResponde = new byte[1024];

                    DatagramPacket nuevoconfirm = new DatagramPacket(nuevoResponde, nuevoResponde.length);
                    ClienteSocket.receive(nuevoconfirm);
                    s = new String(nuevoconfirm.getData());
                    System.out.println(s.trim());
                }

                TimeUnit.SECONDS.sleep(5);


            break;


            }
        }

        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


    }
    private static void escenario5(InetAddress host,int port) throws IOException, InterruptedException {


        try{

            //declare client socket
            //declarar el enchufe del cliente
            DatagramSocket ClienteSocket = new DatagramSocket();
            ClienteSocket.setSoTimeout(10000);
            Random rand = new Random();
            while (true)
            {

                //generate sensor and send it
                //Generar el sensor y enviarlo
                byte[] enviarsensor = new byte[1024];

                String sensor="sensor"+(rand.nextInt(4)+1);
                enviarsensor = sensor.getBytes();
                DatagramPacket sensorDP = new DatagramPacket(enviarsensor, enviarsensor.length, host, port);
                ClienteSocket.send(sensorDP);

                // Generar valand enviarlo
                //generate  value and send it
                byte[] enviandolectura = new byte[1024];
                float lecuraF= 10;
                //convete it from float to string
                //lo transmiten de la float a la string
                String  lecura = String.valueOf(lecuraF);
                enviandolectura = lecura.getBytes();
                DatagramPacket lecuraDP = new DatagramPacket(enviandolectura, enviandolectura.length, host, port);
                ClienteSocket.send(lecuraDP);
                //getting time of the collector
                //obtener el tiempo de la collector
                byte[] enviartiempoC = new byte[1024];
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                String time=dtf.format(now);
                enviartiempoC = time.getBytes();
                DatagramPacket timeDP = new DatagramPacket(enviartiempoC, enviartiempoC.length, host, port);
                ClienteSocket.send(timeDP);
                //getting response from the server
                //obteniendo respuesta del servidor
                byte[] Responde = new byte[1024];
                DatagramPacket confirm = new DatagramPacket(Responde, Responde.length);
                ClienteSocket.receive(confirm);
                String s = new String(confirm.getData());

                Instant start = Instant.now();
                TimeUnit.SECONDS.sleep(4);
                Instant finish = Instant.now();
                long timeElapsed = Duration.between(start, finish).toSeconds();
                if (timeElapsed>3 || s.trim().equals("lost"))
                {
                    System.out.println("El nodo remoto ha procesado su pedido, pero la respuesta se puede perder");

                }
                TimeUnit.SECONDS.sleep(4);
                //check if there is lost data or duplicate
                //comprobar si hay datos perdidos o duplicados
                if (s.trim().equals("perdido") || s.trim().equals("duplicado"))
                {
                    System.out.println(s.trim());
                    float nuevolecuraF= (float) (rand.nextInt(10)+ rand.nextDouble());
                    String  nuevolecura = String.valueOf(nuevolecuraF);
                    enviandolectura = nuevolecura.getBytes();
                    DatagramPacket nuevolecuraDP = new DatagramPacket(enviandolectura, enviandolectura.length, host, port);
                    ClienteSocket.send(nuevolecuraDP);
                    byte[] nuevoResponde = new byte[1024];

                    DatagramPacket nuevoconfirm = new DatagramPacket(nuevoResponde, nuevoResponde.length);
                    ClienteSocket.receive(nuevoconfirm);
                    s = new String(nuevoconfirm.getData());
                    System.out.println(s.trim());
                }


                break;




            }
        }
        catch (SocketTimeoutException e) {
            // timeout exception.
            System.out.println("Tiempo de espera alcanzado, la conexión con el servidor puede haber fallado");
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


    }
    private static void escenario6(InetAddress host,int port) throws IOException, InterruptedException {


        try{

            //declare client socket
            //declarar el enchufe del cliente
            DatagramSocket ClienteSocket = new DatagramSocket();
            ClienteSocket.setSoTimeout(10000);
            Random rand = new Random();
            while (true)
            {
                boolean check=false;
                //generate sensor and send it
                //Generar el sensor y enviarlo
                byte[] enviarsensor = new byte[1024];

                String sensor="sensor"+(rand.nextInt(4)+1);
                enviarsensor = sensor.getBytes();
                DatagramPacket sensorDP = new DatagramPacket(enviarsensor, enviarsensor.length, host, port);
                ClienteSocket.send(sensorDP);

                // Generar valand enviarlo
                //generate  value and send it
                byte[] enviandolectura = new byte[1024];
                float lecuraF= (float) (rand.nextInt(10)+ rand.nextDouble());
                //convete it from float to string
                //lo transmiten de la float a la string
                String  lecura = String.valueOf(lecuraF);
                enviandolectura = lecura.getBytes();
                DatagramPacket lecuraDP = new DatagramPacket(enviandolectura, enviandolectura.length, host, port);
                ClienteSocket.send(lecuraDP);
                //getting time of the collector
                //obtener el tiempo de la collector
                byte[] enviartiempoC = new byte[1024];
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                String time=dtf.format(now);
                enviartiempoC = time.getBytes();
                DatagramPacket timeDP = new DatagramPacket(enviartiempoC, enviartiempoC.length, host, port);
                ClienteSocket.send(timeDP);
                //getting response from the server
                //obteniendo respuesta del servidor
                byte[] Responde = new byte[1024];
                DatagramPacket confirm = new DatagramPacket(Responde, Responde.length);
                ClienteSocket.receive(confirm);
                String s = new String(confirm.getData());

                Instant start = Instant.now();
                TimeUnit.SECONDS.sleep(2);
                Instant finish = Instant.now();
                long timeElapsed = Duration.between(start, finish).toSeconds();
                if (timeElapsed>1)
                {
                    System.out.println("El nodo remoto ha procesado su pedido, pero la respuesta estar retrasada.");
                    check=true;
                }

                TimeUnit.SECONDS.sleep(3);

                //check if there is lost data or duplicate
                //comprobar si hay datos perdidos o duplicados
                if (s.trim().equals("perdido") || s.trim().equals("duplicado"))
                {
                    float nuevolecuraF= (float) (rand.nextInt(10)+ rand.nextDouble());
                    String  nuevolecura = String.valueOf(nuevolecuraF);
                    enviandolectura = nuevolecura.getBytes();
                    DatagramPacket nuevolecuraDP = new DatagramPacket(enviandolectura, enviandolectura.length, host, port);
                    ClienteSocket.send(nuevolecuraDP);
                    byte[] nuevoResponde = new byte[1024];

                    DatagramPacket nuevoconfirm = new DatagramPacket(nuevoResponde, nuevoResponde.length);
                    ClienteSocket.receive(nuevoconfirm);
                    s = new String(nuevoconfirm.getData());
                    s="";
                    System.out.println(s.trim());
                }
                s="";
                System.out.println(s.trim());

                    break;






            }
        }
        catch (SocketTimeoutException e) {
            // timeout exception.
            System.out.println("Tiempo de espera alcanzado, la conexión con el servidor puede haber fallado");
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


    }



}
