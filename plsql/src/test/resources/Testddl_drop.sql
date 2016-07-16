
-- <ChangeSet name="drop_test_table" delimiter="/" />

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


 

