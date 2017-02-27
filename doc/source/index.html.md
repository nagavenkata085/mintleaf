---
title: Mintleaf Documentation
 
toc_footers:  
  - <a href='http://getmintleaf.org'>getmintleaf.org</a>

includes:
  

search: true
---

# Introduction

Welcome to the Mintleaf! Mintleaf is a light weight framework tool helps you to advance your database developement on continuous integration / continuous delivery model as easy as possible.


- Database Migration (either from command line or programatic approach)
- Ability to write automated tests and run them on migrated database schemas, objects, data integrity checks during CI/CD
- Seamless Test life cycle management such as setup, teardown mock data, schema and any database objects using changesets
- Transfer/Copy data between datbases
- Nothing more but to use Plain old SQL that you know of

# Database Migration

## What is migration means?
Database migraton refers to the management of incremental, reversible changes to relational database schemas. A schema migration is performed on a database whenever it is necessary to update or revert that database's schema to some newer or older version.  Look at the below diagram which shows you the schema changes over a period of time during a Agile Software Developement Life Cycle.   Every schema changes during a developement sprint will be applied to QA Databases and Prod Database at the end of sprint release.    

![Alt text](/images/basicflow.png)   


```sql
create schema ABCDB;
create table A;

```

## Change sets
 
 >A file *abcdb-changeset-demo.sql* contains the following two changesets create schema and create tables
 
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

 Changesets are basically contains one ore more changes that are to be applied during database migration.  Mintleaf changesets are stored in plain old sql files but described in comment lines 
 
 <aside class="notice">
      <code> -- &lt;ChangeSet id=&quot;{a-change-set-id}&quot; delimiter=&quot;;&quot; delimiter=&quot;&quot; /&gt; </code> 
 </aside>
 
 
### changeset parameters
 
Parameter | Description
--------- | -----------
id | The ID of the changeset
delimiter | sql script ending delimiter
userdata | user defined columns that one can store additional meta data of the changeset


# Create Test Data
