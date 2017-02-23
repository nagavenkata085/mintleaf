
-- <ChangeSet id="create_test_table" delimiter=";" />

CREATE TABLE TABLE1 
    ( 
     
     ID NUMBER (18)  NOT NULL ,      
     NAME VARCHAR2 (60 CHAR)  NOT NULL
    
    )  
;

 
CREATE TABLE TABLE2 
    ( 
      ID NUMBER (18)  NOT NULL ,      
     NAME VARCHAR2 (60 CHAR)  NOT NULL
    )  
;


 
insert into TABLE1 values(1,'aa');

insert into TABLE1 values(2,'bb');

insert into TABLE1 values(3,'cc');