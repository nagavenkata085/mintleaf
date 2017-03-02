---
title: Mintleaf Documentation
 
toc_footers:  
  - <a href='http://getmintleaf.org'>getmintleaf.org</a>
  - <a href='https://github.com/senips/mintleaf'>Source @ github</a>
 

 
includes:
  

search: true
---

# Introduction

Welcome to the Mintleaf! Mintleaf is a light weight framework tool helps you to advance your database developement on continuous integration / continuous delivery model as easy as possible.

### Features
- Database Migration (either from command line or programatic approach)
- Ability to write automated tests and run them on migrated database schemas, objects, data integrity checks during CI/CD
- Seamless Test life cycle management such as setup, teardown mock data, schema and any database objects using changesets
- Transfer/Copy data between datbases
- Nothing more but to use Plain old SQL that you know of

**_Before we deep dive into the Mintleaf, lets look at what migration means and how it works._**

## Migration at a glance
Database migraton refers to the management of incremental, reversible changes to relational database schemas. A schema migration is performed on a database whenever it is necessary to update or revert that database's schema to some newer or older version.  Look at the below diagram which shows you the schema changes over a period of time during a Agile Software Developement Life Cycle.   Every schema changes during a developement sprint will be applied to QA Databases and Prod Database at the end of sprint release.  
  
  
![Database Migration](/images/basicflow.png)   

When developing software applications backed by a database, developers typically develop the application source code in tandem with an evolving database schema. The code typically has rigid expectations of what columns, tables and constraints are present in the database schema whenever it needs to interact with one, so only the version of database schema against which the code was developed is considered fully compatible with that version of source code.

#### Version Control Systems v.s Database Migration Versions?
Teams of software developers usually use version control systems to manage and collaborate on changes made to versions of source code. Different developers can develop on divergent, relatively older or newer branches of the same source code to make changes and additions during development.  So version control system by itself does not maintain database versions but data migration tools does it for you.  So using Mintleaf you can maintain your database versions either upgrade to a newer version or degrade it a older version. 

## Why MintLeaf?

Schema migration to a production database is always a risky task. Production databases are usually huge, old, and full of surprise things like the following 
    
- Assumptions on certian data conditions 
- Implied dependencies which no body knows
- Unclean / stale data which are hard to resolve 
- Direct schema patches for bug fixes 
- Direct performance fixes
and so on....

 
>So for these reasons, a solid migration process needs high level of discipline through build, test and deploy strategy is a way to go as like shown in below diagram where **Mintleaf** serves as a tool and as a framework to help an organisation move forward to next level of Agile Database Developement.

![Mintleaf](/images/overall.png)   

 
So again, welcome to the Mintleaf!  Mintleaf is a light weight framework tool helps you to advance your database developement on continuous integration / continuous delivery model as easy as possible.

 
# Understanding Mintleaf

Let's look at the core concepts behind Mintleaf design so that you will be able to understand and use it accordingly before you move onto technical section.  Here is the simple steps to get you started quick.


## Changesets
 

 Changesets are basically contains one ore more changes that are to be applied during a database migration.  Mintleaf changesets are stored in plain old sql files but changeset information is described using sql comment lines as shown below 
 
```sql
 -- <ChangeSet id="{a-change-set-id}" delimiter=";" userdata="" />
```
where,
 
Parameter | Description
--------- | -----------
id | The ID of the changeset and it should be unique
delimiter | Sql DDL/DML statement delimiter
userdata | user defined columns that one can store additional meta data of the changeset. 


 
**_For example, a file 'abcdb-changeset.sql' contains the following two changesets: create schema and create tables_**

 
```sql
-- <ChangeSet id="create schema" delimiter=";" userdata="" />
DROP SCHEMA IF EXISTS ABCDB;
CREATE SCHEMA IF NOT EXISTS ABCDB;
 
-- <ChangeSet id="create tables" delimiter=";" userdata="" /> 
CREATE TABLE IF NOT EXISTS ABCDB.USERS
(
  USERID      INT NOT NULL,
  USERNAME    VARCHAR2(50),
  RATE        NUMBER(12, 2),
  CREATE_TIME DATE DEFAULT sysdate,
  CONSTRAINT PK_USERID PRIMARY KEY (USERID)
);
 
``` 
 
 
<!--Tips: -->
<!--- Create a folder-->
<!--- Under that new folder, just create plain old sql file with an extension .sql-->
<!--- Edit that sql file to create a new changeset by starting a single line comment inside that sql file-->
<!--- Add your changes (DDL/DML scripts) after changeset comment line declaration-->
<!--- And repeat as much as changeset that you like and you can create as many as sql files as you like to spread them up-->



 
<aside class="notice"> 
It's very easy to add changeset definition on your exising sql files if you would like.  So nothing much to do apart from this comment line declaration. 
</aside>


## Configuration file

Version profile is a configuration file which contains list of changesets to be applied for a particular version of a database migration.  Typically you will have all versions from the beginning to the latest.   So the Mintleaf looks for a default version profile at the folder where it is being execution location.

 **mintleaf.yml**

```yml
databases:
    - database:
        id : abcdb
        type : h2|oracle|sqlserver|postgres|mysql|sybase
        url :  jdbc:h2:file:./target/ABCDB;mv_store=false;
        username : 
        password : 

versions:
    
    - version:
        id : abcdb-v1
        ver : 1.0
        changesets:  
            - create schema
            - create tables        
        location:
            -  path/*.sql
        

   
        
   
```

 
# Creating test data

Before run your test you should load your test data to target test database so that your tests can be run for to prove to be working with developer code changes. So generally, your test scenearios may be involved with one or more database tables that needs to be populated with data in order to verify developer code changes either stored procedure side or application side. Mintleaf simplifies the test data creation process as easy as possible.  Here are the possible ways that one can think of creating test data with Mintleaf, 

- Create your own data set in a csv file format and load it to your target test database
- Or copy data from existing database to a csv file and later use it for loading in to your target test database 
- Or directly copy data from one database to another database/your target test database
 
So over all you can use csv file and database-to-database copy of data for your tests.  
 
 
<aside class="warning"> Mintleaf copy data process is intended for to use for test data creation purpose only so it is not meant for production use   
</aside>

### _Usage:_

**mintleaf -config=&lt;[configuration file](#configuration-file)&gt; -targetdb=&lt;db id&gt; -file=&lt;csv file to save&gt; -task=&lt;dbtocsv|csvtodb|dbtodb&gt; -targetsql=&lt;a select query&gt;**


## Database to CSV

For example, if we want to dump data from a table called HRDB.USERS in abcd-db to a CSV file then you run like the following 
 
```shell

> mintleaf --config=connection.yml  targetdb=abcd-db -file=abcd-db.csv -task=dbtocsv targetsql="select * from HRDB.USERS"

running...done.
results stored in users.csv, 2cols and 4 rows copied
|ID|NAME| 
|1|Allen| 
|2|Jim| 
|3|Gary|
|4|Sen|


```
  
```java
 
        //connect to a database, below is an example for H2 database but you can change to any database
        DataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:h2:file:./target/H2DbTests;mv_store=false");
        ds.setDriverClassName("org.h2.Driver");

        //create a database context and call exportDataTo
        DbContext dbContext = new H2DbContextImpl(ds);         
        File f = new File("users.csv");                
        h2DbContext.exportDataTo(new CsvExport(new FileWriter(f)), "select * from ABCDB.USERS", null);
        
```  
 
  
## CSV to Database

Suppose you have a data in a CSV file called abcd.csv and want to load it in to a table called HRDB.USERS then you run like the following command.  Note that CSV file must have a header row as first which contains column names. Please note that whatever the column name you define in CSV file will be used exactly in your targetsql argument as shown below.  Here target sql is a template and the CSV column names are prefixed and suffixed with dollar sign as variables.   One great thing about using a template approach here is that it enables use of builtin functions of the database inside your insert statement some instances like any date formatting functions or math functions and so on.   


```shell

> mintleaf --config=connection.yml  targetdb=abcd-db -file=abcd.csv -task=csvtodb targetsql="INSERT INTO HRDB.USERS (ID, NAME) VALUES ($ID$, $NAME$)"

```
 
```java
 
        //connect to a database, below is an example for H2 database but you can change to any database
        DataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:h2:file:./target/H2DbTests;mv_store=false");
        ds.setDriverClassName("org.h2.Driver");

        //create a database context and call importDataFrom
        Reader reader = new FileReader(new File("users.csv"));
        DbContext dbContext = new H2DbContextImpl(ds);
        h2DbContext.importDataFrom(new CsvImportSource(reader), "INSERT INTO ABCDB.USERS (ID, USERNAME) VALUES($USERID$, '$USERNAME$'");      
        
```  
  
 
 
## Database to Database
 
```shell

> mintleaf --config=connection.yml  targetdb=abcd-db -sourcedb=poppy-db -task=dbtodb targetsql="INSERT INTO HRDB.USERS (ID, NAME) VALUES ($ID$, '$NAME$')" sourcesql="SELECT ID, NAME FROM HRDB.USERS"

```
  
 
 
```java
 
        //connect to a database, below is an example for H2 database but you can change to any database
        DataSource targetDbContext = new BasicDataSource();
        targetDbContext.setUrl("jdbc:h2:file:./target/H2DbTests;mv_store=false");
        targetDbContext.setDriverClassName("org.h2.Driver");
        
        DataSource sourceDbContext = new BasicDataSource();
        sourceDbContext.setUrl("jdbc:oracle:thin:@localhost:1521:xe");  // set your database connection detail here
        sourceDbContext.setDriverClassName("oracle.jdbc.driver.OracleDriver");

        //create a database context and call importDataFrom    
        FluentJdbc source = sourceDbContext.newQuery().withSql("SELECT * FROM ABCDB.USERS");

        targetDbContext.importDataFrom(new DbImportSource(source.getResultSet()),
                "INSERT INTO ABCDDB.USERS_IMPORT_TABLE (USERID, USERNAME) VALUES ($USERID$, '$USERNAME$')");
                              
        
```  
  



