COMP 3005 Final Project: Health & Fitness Club Management System

Video Demonstration: https://youtu.be/7AnQ3uOdeg4
---

Project Structure:
* /app: Contains the Java source code (FitnessClubApp.java) 
* /sql: Contains the database creation scripts (DDL.sql) and sample data (DML.sql)
* /docs: Contains the project report and ER diagram

How to Run
1. download postgresql jar from https://jdbc.postgresql.org/download/ and place it in the /app folder before running.
2. open pgAdmin and create a database named `fitness_club` in PostgreSQL
2. run DDL.sql then DML.sql
3. open terminal and go to the /app directory 
4. run "javac fitnessCenter.java"
5. java -cp ".;postgresql-42.7.2.jar" fitnessCenter
(Note: if your using a different driver please change the filename in the command above)

Testing for user accounts:
Member: rachelg@gmail.com
Trainer: janesmith@gmail.com

Admin password: admin123
