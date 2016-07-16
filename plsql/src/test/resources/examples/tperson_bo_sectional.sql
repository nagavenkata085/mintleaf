
-- <ChangeSet name="type" delimiter="/" />

,MEMBER function getFirstName return varchar2
 


-- <ChangeSet name="typebody" delimiter="/" />


MEMBER function getFirstName return varchar2 is
begin	
	return SELF.first_name;	
end;
	 
