
create or replace package StringExample
as
  FUNCTION replace_Space_With_DollarSign(pstr IN varchar2) return varchar2;   
end StringExample;

/
show err 

create or replace
package body StringExample
as
  FUNCTION replace_Space_With_DollarSign(pstr IN varchar2) return varchar2
  as
    vstr varchar2(4000);
  begin
    vstr := translate(pstr,' ','$');
    return trim(vstr);
  end;
end StringExample;

/

