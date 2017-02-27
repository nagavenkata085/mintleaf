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



 
 