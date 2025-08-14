# Makefile for P213 Integration Assignment

# Variables
JUNIT=./junit5.jar
JFLAGS=-cp .:$(JUNIT)

# Default rule: show help
all:
	@echo "Please specify a target: startServer, runAllTests, or clean"

# Compile and start the server
startServer:
	javac $(JFLAGS) *.java
	java WebApp 8888 & 

# Compile and run JUnit 5 tests
runAllTests:
	javac $(JFLAGS) *.java
	java -jar $(JUNIT) -cp . -c BackendTests && java -jar $(JUNIT) -cp . -c FrontendTests

# Clean up class files
clean:
	rm -f *.class
