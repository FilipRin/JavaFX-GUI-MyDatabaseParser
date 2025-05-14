# JavaFX-GUI-MyDatabaseParser
Database parser made in eclipse using JavaFX

---------------------------------------------------------
- JavaFX project/task for university, application for managing databases (parser for different files: sql,txt,etc..). It was requested to have in mind object-oriented design and use as much of libraries that Java offers (collections, algorithms, regex, lamba statements, iterators,etc..), but also have a clean code and comments on a lot of places.
- User starts the application and first stage/window is shown. There user can choose to make a new DB or import data from existing one (must be sql or txt file).
- After that user is sent to main stage where he has a text area for all tables in DB (on top), text area that shows results of executed statement (middle), text field where user writes sql-ish statement, next to that there is button for executing statement and text area where info on execution is shown (explanation of action that happend or error info). Lastly, there are two buttons for exporting DB in either user-defined format or sql format. If at any moment user wants to exit app without exporting (saving) his DB, alert window will pop up and remind user to export before exiting or his DB will be lost.
- Statements that are supported are SELECT, DELETE, INSERT, DROP TABLE, SHOW TABLES, UPDATE.
- Also as requested Table class contains documentational comments, and in folder doc there is generated documentation for that class using Javadoc tool.
