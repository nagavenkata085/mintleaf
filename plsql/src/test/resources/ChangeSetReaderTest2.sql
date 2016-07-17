
-- <ChangeSet id=delete tables" delimiter="/" />

declare
   c int;
begin
   select count(*) into c from user_tables where table_name = upper('table1');
   if c = 1 then
      execute immediate 'drop table TABLE1';
   end if;
end; 

/

 
declare
   c int;
begin
   select count(*) into c from user_tables where table_name = upper('table2');
   if c = 1 then
      execute immediate 'drop table TABLE2';
   end if;
end; 


/

-- <ChangeSet id=create some tables" delimiter=";" />

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

