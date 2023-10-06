# Makefile for compiling Java source files and putting class files in bin directory

# Define the Java compiler and flags
JAVAC := javac
JAVAC_FLAGS := -d bin -sourcepath src
JVM := java
# Define the source and build directories
SRC_DIR := src
BIN_DIR := bin
PACKAGE = MonteCarloMini
# Define the source files (use wildcard to include all Java files)
SOURCES := $(wildcard $(SRC_DIR)/MonteCarloMini/*.java) $(wildcard $(SRC_DIR)/OtherPackage/*.java)

# Define the target for building all Java files
all: $(SOURCES:.java=.class)

# Rule to compile Java source files to class files
%.class: %.java
	$(JAVAC) $(JAVAC_FLAGS) $<

# Rule to clean up generated class files
clean:
	rm -rf $(BIN_DIR)/*

run:
	$(JVM) -cp $(BIN_DIR) $(PACKAGE).MonteCarloMinimizationParallel $(ARGS)

# Declare 'all' and 'clean' as phony targets
.PHONY: all clean
