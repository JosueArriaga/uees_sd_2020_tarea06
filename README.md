# Deber de Sistemas Distribuidos 


El objetivo de esta tarea es simular la comunicación entre un agente colector de mediciones de sensores y un servidor que recibirá 
las mediciones y las almacenará en una base de datos. La comunicación entre los dos módulos se debe realizar mediante protocolo UDP, 
se deberá implementar las medidas necesarias para manejar efectivamente cualquier tipo de error de red. Además, las aplicaciones deberán 
incluir código que permita simular errores de la red, tanto en el emisor como en el receptor.

# Requerimientos 
### Agente colector
- sudo apt-get update                                     
- sudo apt install net-tools                              
- sudo apt install default-jdk                            
- sudo apt install git
### Agente Servidor
- sudo apt-get update
- sudo apt install net-tools
- sudo apt install default-jdk
- sudo apt install git
- sudo apt install mysql-server
- sudo mysql_secure_installation

# Server.java
Este aplicativo contiene la conexión en el puerto UDP 9999, ademas posee la respectiva conexion a mysql donde fue creada la base de datos con el nombre uees, misma que contiene nombres con las distintas columnas del deber. Se recibe los respectivos datos en cada conexion con el agente colector, los datos validos de los sensores los almacenará en la base de datos, este aplicativo crea un archivo de texto con el nombre 'log.txt' donde se almacena cada registro con su respectivo de mensaje de resultado. Ademas, previene la escritura de datos repetidos.
#### Ejecucion del aplicativo
- Se procede ir a la ruta donde se encuentra los archivos del deber
- Se abre la terminal de linux
- En la terminal se procede a poner el comando java -cp mysql.jar Se ejecuta la respectiva libreria de mysql
- Procedentemente del comando server.java  este mismo ejecutará la clase clase java del agente servidor
# Collecter.java
Este aplicativo contiene las respectivas funcionalidades que complementan los escenarios, Se crean sensores con un ID al selecionar cada uno de los escenarios, en cada iteracion el colector tomara el timestamp(la fecha y hora del servidor en ese momento) y por cada sensor generará aleatoriamente un valor que será utilizado como la medición; Este mismo enviara cada lectura al servidor mendiante un mensaje UDP, tendra la respectiva validaciones para los errores o mensajes duplicados. El colector realizara iteraciones indefinidas hasta que estas sean canceladas por el operador.
### Ejecucion del aplicativo 
- Se procede a clonar el repositorio con el comando "git clone"[url_del_repositorio]"
- Se dirige a la carpeta con el comando "cd [nombre_del_repositorio]"
- Se ejecuta la siguiente linea de codigo para ejecutar la clase del colector: java  Collecter.java 
# Referencias
- https://www.binarytides.com/udp-socket-programming-in-java/
- https://www.edureka.co/blog/connect-mysql-database-in-java
