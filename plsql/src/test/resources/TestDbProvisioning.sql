
CREATE OR REPLACE PACKAGE TestDbProvisioning AUTHID CURRENT_USER AS


procedure dropApplicationUser (pusername in varchar2);
procedure createApplicationUser (pusername in varchar2, puserPassword varchar2);

END;

/

CREATE OR REPLACE PACKAGE BODY TestDbProvisioning AS

	procedure terminateSession(pusername in varchar2) IS 
	begin
   		for REC in
	       (SELECT s.SID, s.SERIAL#
	        from v$session s
	        where s.username = upper(pusername)                  
	       ) LOOP    
         execute immediate 'alter system kill session ''' || rec.sid || ', ' ||
                            rec.serial# || '''' ;
	   end loop;   
	end ;
  
	procedure dropApplicationUser (pusername in varchar2)  is 
	 c int;
	 serialNumber NUMBER;
	 sid NUMBER;
	begin  	
-- 	   execute immediate 'alter session set container=PDB1 ';
	   terminateSession(pusername);
		
	   select count(*) into c from all_users where username = upper(pusername);
	   if c = 1 then
	      execute immediate 'drop user '||pusername || ' cascade';     
	   end if;
-- 	  execute immediate 'alter session set container=CDB$ROOT ';
	end;
	

	

 	procedure createApplicationUser (pusername in varchar2, puserPassword in varchar2)  is 
	 c int;
	begin  		  
	  -- execute immediate 'alter session set container=PDB1 ';
	    execute immediate 'create user ' || pusername ||
		' identified by ' || puserPassword ;

	    execute immediate 'grant connect, resource to ' || pusername;
	    execute immediate 'grant create table, create session, create view, create any context,create sequence, create trigger
		     , alter session, administer database trigger, create role, create job
		     , create materialized view ,create synonym to '|| pusername;
	   	--execute immediate 'grant select on v_$session to ' || pusername;
   		 
	end;

END;
/


