create or replace package employee_package
as

    
procedure getEmployee(pid in number,emprec out employee%rowtype) ; 
end employee_package;

/
 
create or replace
package body employee_package
as

procedure getEmployee(pid in number,emprec out employee%rowtype) 
as

begin
select *  into emprec from employee where emp_id=pid;   
end;

end employee_package;

/
 

