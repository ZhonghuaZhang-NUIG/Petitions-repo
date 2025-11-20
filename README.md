# zhonghuaspetitions

Simple Spring Boot petitions app (in-memory). Creates, lists, searches and signs petitions.

How to run (macOS / zsh):

```bash
# from the project root
cd "/Users/nancymacbook/Desktop/UG CSS/2025 sem03/2526-CT5171 Cloud DevOps/assignment01/zhonghuaspetitions"
mvn spring-boot:run
```

Or build and run the jar:

```bash
mvn -DskipTests package
java -jar target/zhonghuaspetitions-0.0.1-SNAPSHOT.jar
```

Then open http://localhost:8080 in your browser.

Notes:
- Data is in-memory (Java lists) and reset on restart.
- Pages: /petitions (list), /petitions/create, /petitions/search, /petitions/{id}
