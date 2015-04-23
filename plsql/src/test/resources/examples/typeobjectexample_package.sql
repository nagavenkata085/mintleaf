create or replace package typeobjectexample_package
as

procedure setPerson(person in TPerson); 
function getPerson return TPerson;

end typeobjectexample_package;

/
 
create or replace
package body typeobjectexample_package
as

mvperson TPerson;

procedure setPerson(person in TPerson)
as
begin
  mvperson := person;
end;

function getPerson return TPerson
as
begin
  return mvperson;
end;	

end typeobjectexample_package;

/
 

