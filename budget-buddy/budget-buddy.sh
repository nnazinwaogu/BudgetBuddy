#!/bin/bash

echo "=============================="
echo "BudgetBuddy Launcher"
echo "=============================="
echo

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed or not in PATH."
    echo
    echo "Please install Java 21 or later from:"
    echo "  https://adoptium.net/"
    echo
    read -rp "Press Enter to exit..."
    exit 1
fi

# Locate the JAR file
JAR_NAME="budget-buddy.jar"
JAR_PATH=""

# Check current directory
if [[ -f "$JAR_NAME" ]]; then
    JAR_PATH="$JAR_NAME"
fi

# Check target directory
if [[ -z "$JAR_PATH" && -f "target/$JAR_NAME" ]]; then
    JAR_PATH="target/$JAR_NAME"
fi

# Check parent directory
if [[ -z "$JAR_PATH" && -f "../$JAR_NAME" ]]; then
    JAR_PATH="../$JAR_NAME"
fi

if [[ -z "$JAR_PATH" ]]; then
    echo "ERROR: $JAR_NAME not found."
    echo
    echo "Please ensure you are in the BudgetBuddy project directory"
    echo "or that the JAR file exists in the current folder or target/ subfolder."
    echo
    echo "You can build the project with:"
    echo "  ./mvnw package -DskipTests"
    echo
    read -rp "Press Enter to exit..."
    exit 1
fi

echo "Starting BudgetBuddy..."
echo
java -jar "$JAR_PATH"

EXIT_CODE=$?
if [[ $EXIT_CODE -ne 0 ]]; then
    echo
    echo "Application exited with error code $EXIT_CODE."
    echo
    echo "Note: BudgetBuddy requires Java 21 or later."
    echo "If you see a JNI error, please update your Java installation."
    echo "Download from: https://adoptium.net/"
    echo
    read -rp "Press Enter to exit..."
    exit $EXIT_CODE
fi
