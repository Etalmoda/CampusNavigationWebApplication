# Campus Navigator

This navigator was a 6 week long group project done in CS400 at the University of Wisconsin - Madison.

This is a Java web application that helps users navigate the UW-Madison campus by finding optimal walking routes between locations. The application stores campus buildings and pathways as a graph data structure, with nodes representing locations and weighted edges representing walking times in seconds. Using Dijkstra's shortest path algorithm, it calculates and displays the quickest routes through an interactive web interface.
The project starts a local HTTP server that serves dynamic HTML pages, allowing users to input start and destination points and receive step-by-step directions with total travel times. It also features the ability to find the longest possible shortest path from any given starting location. The codebase includes comprehensive JUnit 5 tests to ensure reliability and correctness of the graph algorithms and web functionality.

## How to Run

Make sure Java11+ is installed

### Mac/Linux

Compile the project:
```bash
javac -cp .:junit5.jar *.java
```

Start the server:
```bash
java WebApp 8888
```

Open browser and go to: http://localhost:8888

### Windows

Compile the project:
```cmd
javac -cp .;junit5.jar *.java
```

Start the server:
```cmd
java WebApp 8888
```

Open browser and go to: http://localhost:8888

## Running Tests

### Mac/Linux

Compile first (if not already done):
```bash
javac -cp .:junit5.jar *.java
```

Run backend tests:
```bash
java -jar junit5.jar -cp . -c BackendTests
```

Run frontend tests:
```bash
java -jar junit5.jar -cp . -c FrontendTests
```

### Windows

Compile first (if not already done):
```cmd
javac -cp .;junit5.jar *.java
```

Run backend tests:
```cmd
java -jar junit5.jar -cp . -c BackendTests
```

Run frontend tests:
```cmd
java -jar junit5.jar -cp . -c FrontendTests
```

## Troubleshooting

If you get "Address already in use" error:

### Mac/Linux
Find the process using port 8888:
```bash
lsof -i :8888
```

Kill the process (replace `<PID>` with actual process ID):
```bash
kill <PID>
```

### Windows
Find the process using port 8888:
```cmd
netstat -ano | findstr :8888
```

Kill the process (replace `<PID>` with actual process ID):
```cmd
taskkill /PID <PID> /F
```

## Makefile Commands (Designed for Mac/Linux)

- `make startServer` - Compile and start the web server on port 8888
- `make runAllTests` - Compile and run all JUnit tests  
- `make clean` - Remove compiled class files
