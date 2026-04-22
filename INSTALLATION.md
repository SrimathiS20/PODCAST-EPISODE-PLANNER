# Installation & Setup Guide

## System Requirements

- **Java Development Kit (JDK)** 17 or higher
- **Maven** 3.8 or higher

## Step 1: Install Java

### Windows
1. Download JDK 17 from [Oracle](https://www.oracle.com/java/technologies/downloads/) or use OpenJDK
2. Run the installer
3. Set JAVA_HOME environment variable:
   - Right-click "This PC" → Properties
   - Click "Advanced system settings"
   - Click "Environment Variables"
   - New → JAVA_HOME = `C:\Program Files\Java\jdk-17` (your JDK path)

Verify installation:
```bash
java -version
javac -version
```

### Mac
```bash
brew install openjdk@17
```

### Linux (Ubuntu)
```bash
sudo apt install openjdk-17-jdk
```

## Step 2: Install Maven

### Windows
1. Download Maven from [Apache Maven](https://maven.apache.org/download.cgi)
2. Extract to a folder (e.g., `C:\maven`)
3. Set M2_HOME and add to PATH:
   - JAVA_HOME: `C:\Program Files\Java\jdk-17`
   - M2_HOME: `C:\maven\apache-maven-3.x.x`
   - PATH: Add `%M2_HOME%\bin`

Verify installation:
```bash
mvn -version
```

### Mac
```bash
brew install maven
```

### Linux (Ubuntu)
```bash
sudo apt install maven
```

## Step 3: Build the Project

Navigate to project directory:
```bash
cd c:\Users\ChirumamillaNagaVenk\ai-kata
```

Build:
```bash
mvn clean install
```

This will:
1. Download dependencies
2. Compile source code
3. Run tests
4. Package into JAR

Expected output:
```
[INFO] BUILD SUCCESS
[INFO] Total time: XX.XXXs
```

## Step 4: Run the Application

Option 1: Using Maven
```bash
mvn spring-boot:run
```

Option 2: Run JAR directly
```bash
java -jar target/agent-orchestration-1.0.0.jar
```

You'll see:
```
======================================================================
🎙️  PODCAST AGENT ORCHESTRATION SYSTEM STARTED
======================================================================
Available Endpoints:
  POST /api/orchestration/start           - Start new workflow
  POST /api/orchestration/generate-questions - Generate questions
  POST /api/orchestration/generate-script    - Generate script
  POST /api/orchestration/run-all            - Run all agents
  GET  /api/orchestration/health             - Health check
======================================================================
```

## Step 5: Verify It's Running

In another terminal:
```bash
curl http://localhost:8080/api/orchestration/health
```

Response:
```
Orchestration service is running
```

## Troubleshooting

### "mvn is not recognized"
**Solution:** Maven is not in PATH
1. Install Maven (see Step 2)
2. Add `%M2_HOME%\bin` to PATH
3. Restart terminal
4. Run `mvn -version` to verify

### "error: cannot find symbol"
**Solution:** Java version is too old
1. Check: `java -version`
2. Must be Java 17+
3. Update JAVA_HOME if needed

### Port 8080 already in use
**Solution:** Change port
- Edit `src/main/resources/application.properties`
- Change: `server.port=8080` → `server.port=8081`
- Rebuild and run

### "BUILD FAILURE" during compile
**Solution:** Check error message
1. Ensure all JDK files compiled correctly
2. Check for syntax errors in source files
3. Run `mvn clean` then rebuild
4. Check console for specific error

## IDE Setup (Optional)

### IntelliJ IDEA
1. Open project
2. File → Project Structure
3. Set Project SDK to JDK 17
4. Maven should auto-detect

### VS Code
1. Install extensions:
   - Extension Pack for Java
   - Spring Boot Extension Pack
2. Open project folder
3. Run via Maven in terminal

### Eclipse
1. Install Eclipse IDE for Java Developers
2. File → Open Projects from File System
3. Select project folder
4. Eclipse auto-imports Maven

## Environment Variables Summary

After installation, verify these are set:

```bash
JAVA_HOME = C:\Program Files\Java\jdk-17
M2_HOME = C:\maven\apache-maven-3.8.x
PATH = ... ;%JAVA_HOME%\bin;%M2_HOME%\bin
```

Check:
```bash
echo %JAVA_HOME%
echo %M2_HOME%
java -version
mvn -version
```

## Complete Setup Checklist

- [ ] Java 17+ installed (`java -version`)
- [ ] Maven 3.8+ installed (`mvn -version`)
- [ ] JAVA_HOME environment variable set
- [ ] M2_HOME environment variable set
- [ ] `%M2_HOME%\bin` in PATH
- [ ] Navigate to project directory
- [ ] Run `mvn clean install`
- [ ] Run `mvn spring-boot:run`
- [ ] Test with `curl http://localhost:8080/api/orchestration/health`

## What's Included

After building, you get:

- ✅ **Source Code**: Clean, well-documented Java classes
- ✅ **Tests**: Unit tests for Context Agent decision logic
- ✅ **Documentation**: README, API examples, architecture explanation
- ✅ **Configuration**: Spring Boot application.properties
- ✅ **Database**: H2 in-memory database for development
- ✅ **REST API**: Ready-to-use endpoints

## Next Steps After Setup

1. **Run the application**: `mvn spring-boot:run`
2. **Test the API**: Use curl commands from QUICK_START.md
3. **Understand the code**: Read ARCHITECTURE_EXPLANATION.md
4. **View database**: Access H2 console at http://localhost:8080/h2-console
5. **Run tests**: `mvn test`
6. **Modify for your needs**: Update agents with your logic

## Getting Help

If you encounter issues:

1. **Check the error message** - Most errors are self-explanatory
2. **Review QUICK_START.md** - Common issues and solutions
3. **Check Maven output** - Run with `-X` flag for detailed output:
   ```bash
   mvn clean install -X
   ```
4. **Verify installation** - Run the troubleshooting checks above

---

**You're all set! 🚀**

After completing these steps, follow the QUICK_START.md guide to run and test the application.
