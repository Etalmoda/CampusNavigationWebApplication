# Campus Navigator

This navigator was a 7 week long group project done in CS400 at the University of Wisconsin - Madison.

A web application that helps users navigate campus by finding the shortest walking paths between locations. Campus locations are stored as nodes in a graph data structure, with walking paths as weighted edges representing travel time in seconds. Built with Java, it uses Dijkstra's algorithm to calculate optimal routes through this graph and displays them through an interactive web interface. Users can find shortest paths between any two campus locations, view step-by-step directions with travel times, and discover the longest possible shortest path from any starting point.

## How to Connect

1. **Start the server:**
```bash
make startServer
```
Go to:
http://localhost:8888

## Makefile Commands

- `make startServer` - Compile and start the web server on port 8888
- `make runAllTests` - Compile and run all JUnit tests  
- `make clean` - Remove compiled class files

## Troubleshooting

If you get "Address already in use":
```bash
lsof -i :8888
```

```bash
kill <PID>
```
