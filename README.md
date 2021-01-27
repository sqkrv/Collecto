# Collecto

## Installation

No specific installation required.
However, your computer should already have java installed. (Check with `java --version`. The version must be 11.x.x or higher)
Once the project archive is unarchived there are two files jar `Server.jar` and `Client.jar`, `README.md` (this document) and `Source` folder.
- `Server.jar` is needed to run a server
- `Client.jar` runs the client to connect to the server and play the game
- `JavaDoc` folder contains generated JavaDoc for the source code of this project
- `Source` folder contains source code of the project

### Windows
On Windows you may need to execute some commands to get colours to work.
1. Execute in CMD with Admin rights: `reg add HKEY_CURRENT_USER\Console /v VirtualTerminalLevel /t REG_DWORD /d 0x00000001 /f`
2. Open PowerShell and execute command: `[Console]::OutputEncoding = [System.Text.Encoding]::UTF8`

## Usage
> To run `.jar` files you can use `java -jar "filename.jar"`  
> *On Windows you may need to include `-Dfile.encoding=UTF-8` parameter after the command above to have string like `java -jar "filename.jar" -Dfile.encoding=UTF-8`* 

To host games by yourself run `Server.jar`. Port can be specified as an argument or will be prompted in the terminal. If the port is incorrect you will be prompted to enter a new port via the terminal.
The server notifies you if it has successfully started.  
After the server has successfully started you can start the Client — `Client.jar`.
The host address and port can be specified as arguments where the first argument is the host address and the second one is the port.
The client then tries to connect to the specified host.
If the connection was unsuccessful the client notifies the user via the terminal and asks for new input of the host address and port.
After a successful connection, you are prompted to enter a username. If the server accepts username you will be able to play games.  
If the username is already taken or does not conform to the rules, you will be prompted for a different username.
For all client-side commands type `HELP`.
