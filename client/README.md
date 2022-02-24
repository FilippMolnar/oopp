While the project can be run out of the box via Gradle, running it from within Eclipse seems to require adding the following as *VM* commands:

    --module-path="/Users/seb/Downloads/javafx-sdk-17.0.2/lib" --add-modules=javafx.controls,javafx.fxml


### Connecting to a remote server

To connect to a remote server, open the file called _ServerUtils.java_ in the _client/src/main/java/client/utils_ folder. This file contains basic utilities to communicate with the server.
All utilities are contained in the class _ServerUtils_, which has a field _SERVER_. This field allows you to specify the private (or public) IP address of the server you want to connect to.

#### Example:
You want to run a server on the localhost of device **A** and you want to connect to this server using device **B**. To find the IP address of **A**, you can run one of the following commands on that device to find the private ip address:

##### Linux:
    `ip a | grep inet`

##### Windows:
    `ipconfig`

##### Mac OS:
    `ifconfig | grep inet`

The local ip address will likely look like 192.x.x.x or 172.x.x.x where x is any number smaller than or equal to 255.
Now change the SERVER variable on device **B** (the client) to the IP-address you just found on device **A**. Run `gradle bootrun` on A and run `gradle run` on **B**. **B** should now be connected to **A**.
