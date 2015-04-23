create type TPerson as OBJECT  (
    id number,
 	first_name varchar2(32),
 	last_name varchar2(32),
    MEMBER PROCEDURE setCustomData 		
)
NOT FINAL; 
/

create or replace type body TPerson 
as

MEMBER PROCEDURE setCustomData
as
begin
	SELF.id:=98;	
	SELF.first_name:=SELF.first_name || 'senthil';
	SELF.last_name:=SELF.last_name||'maruth';
	typeobjectexample_package.setPerson(SELF);
end;

end;
/
