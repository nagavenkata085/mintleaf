
-- <ChangeSet id=type" delimiter="/" />

,MEMBER function getFirstName return varchar2
 


-- <ChangeSet id=typebody" delimiter="/" />


MEMBER function getFirstName return varchar2 is
begin	
	return SELF.first_name;	
end;
	 
