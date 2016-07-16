
-- <ChangeSet name="create employee table" delimiter="/" />

CREATE TABLE EMPLOYEE 
    ( 
     
     emp_id NUMBER (18)  NOT NULL ,      
     NAME VARCHAR2 (60 CHAR)  NOT NULL
    
    )  
/

begin
insert into employee values(10,'EMP1');
insert into employee values(20,'EMP2');
end;
/


-- <ChangeSet name="drop employee table" delimiter="/" />


begin
execute immediate 'drop table employee purge';
exception when others then null;
end;
/	 
