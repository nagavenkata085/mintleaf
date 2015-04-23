
-- <sqlpart name="create_test_table" delimiter="/" />

CREATE TABLE EMP
    ( 
     EMPLOYEE_NUMBER VARCHAR2 (60 CHAR)  NOT NULL , 
     FIRST_NAME VARCHAR2 (60 CHAR)  NOT NULL , 
     LAST_NAME VARCHAR2 (60 CHAR)  NOT NULL , 
     HIRE_DATE VARCHAR2 (60 CHAR)  NOT NULL  
    ) 
/

-- <sqlpart name="insert_into_table" delimiter="/" />

INSERT INTO EMP VALUES ('1429','Bob','Martin','10-Oct-1975')

/

-- <sqlpart name="insert_into_table2" delimiter="/" />


INSERT INTO EMP VALUES ('9924','Bill','Mitchell','19-Dec-1966')


/


-- <sqlpart name="drop_test_table" delimiter="/" />

declare
  cursor ix is
    select *
      from user_objects
     where object_type in ('TABLE', 'VIEW')
	 and object_name like '%EMP%';
begin
 for x in ix loop
   execute immediate('drop '||x.object_type||' '||x.object_name);
 end loop;
end;
/

