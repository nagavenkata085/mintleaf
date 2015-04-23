--this requires. testddl.sql , see datarollbacktest.java for usage 
DELETE TABLE2;
INSERT INTO TABLE2 VALUES  (  1, 'scott'  );
INSERT INTO TABLE2 VALUES  (  2, 'tiger'  );
INSERT INTO TABLE2 VALUES  (  3, 'tiger'  );
INSERT INTO TABLE2 VALUES  (  4, 'tiger'  );
INSERT INTO TABLE2 VALUES  (  5, 'tiger'  ); 
