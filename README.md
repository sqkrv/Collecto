# Collecto

## Installation

No specific installation required.
However, your computer should already have java installed. (Check with `java --version`. The version must be 11.x.x or higher)
Once the project archive is unarchived there are two files jar `Server.jar` and `Client.jar`, `README.md` (this document) and `Source` folder.
- `Server.jar` is needed to run a server
- `Client.jar` runs the client to connect to the server and play the game
- `JavaDoc` folder contains generated JavaDoc for the source code of this project
- `Source` folder contains source code of the project

## Usage
> To run `.jar` files you can use `java -jar "filename.jar"`

To host games by yourself run `Server.jar`. Port can be specified as an argument or will be prompted in the terminal. If the port is incorrect you will be prompted to enter a new port via the terminal.
The server notifies you if it has successfully started.  
After the server has successfully started you can start the Client — `Client.jar`.
Host address and port can be specified as arguments where the first argument is the host address and the second one is the port.
After that the client tries to connect to the specified host.
If the connection was unsuccessful Client notifies via the terminal and asks for new input of host address and port.
After a successful connection, you are prompted to enter a username. If the server accepts username you will be able to play games. For all client-side commands type `HELP`. 