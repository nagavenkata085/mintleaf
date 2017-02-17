-- <ChangeSet id="create schema" delimiter=";" />

create SCHEMA mintleaf;

-- <ChangeSet id="create tables" delimiter=";" />



CREATE TABLE mintleaf.TABLE1
    ( 
     
     ID NUMBER (18)  NOT NULL ,      
     NAME VARCHAR2 (60 CHAR)  NOT NULL
    
    )  
;

 
CREATE TABLE mintleaf.TABLE2
    ( 
      ID NUMBER (18)  NOT NULL ,      
     NAME VARCHAR2 (60 CHAR)  NOT NULL
    )  
;


-- <ChangeSet id="drop tables" delimiter=";" />

drop table mintleaf.TABLE1;

drop table mintleaf.TABLE2;


-- <ChangeSet id="clean db" delimiter=";" />
drop schema mintleaf;