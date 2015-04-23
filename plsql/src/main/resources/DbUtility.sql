CREATE OR REPLACE PACKAGE DbUtility
AS

  FUNCTION getTableNameFromSynonym(psynonym_name IN VARCHAR2)
    RETURN VARCHAR2;
  FUNCTION getTableNameIfExists(psynonym_name IN VARCHAR2)
    RETURN VARCHAR2;
  FUNCTION getOwnerName(psynonym_name_or_table_name IN VARCHAR2)
    RETURN VARCHAR2;
  PROCEDURE getobjectcolumnsdef(pobject_name VARCHAR2, pcolumnlist OUT tstringlist, pdatatypelist OUT tstringlist);

END DbUtility;
/

CREATE OR REPLACE PACKAGE BODY DbUtility
AS

  FUNCTION getTableNameFromSynonym(psynonym_name IN VARCHAR2)
    RETURN VARCHAR2 AS
    vtable_name VARCHAR2(32);
    BEGIN
      SELECT table_name
      INTO vtable_name
      FROM user_synonyms
      WHERE synonym_name = psynonym_name;
      RETURN vtable_name;
      EXCEPTION WHEN no_data_found THEN
      RETURN NULL;
    END;

  FUNCTION getOwnerName(psynonym_name_or_table_name IN VARCHAR2)
    RETURN VARCHAR2 AS
    vowner_name VARCHAR2(32);
    vtable_name VARCHAR2(32);
    BEGIN
      vtable_name := getTableNameFromSynonym(upper(psynonym_name_or_table_name));
      IF (vtable_name IS NULL)
      THEN
        RETURN SYS_CONTEXT('USERENV', 'current_user');
      ELSE
        SELECT table_owner
        INTO vowner_name
        FROM user_synonyms
        WHERE synonym_name = psynonym_name_or_table_name;
        RETURN vowner_name;
      END IF;
      EXCEPTION WHEN no_data_found THEN
      RETURN NULL;
    END;

  FUNCTION getTableNameIfExists(psynonym_name IN VARCHAR2)
    RETURN VARCHAR2 AS
    vtable_name VARCHAR2(32);
    BEGIN
      vtable_name := getTableNameFromSynonym(upper(psynonym_name));
      IF (vtable_name IS NULL)
      THEN
        RETURN psynonym_name;
      END IF;
      RETURN vtable_name;
    END;

  PROCEDURE getobjectcolumnsdef(pobject_name      VARCHAR2,
                                pcolumnlist   OUT tstringlist,
                                pdatatypelist OUT tstringlist) IS
    vsql          VARCHAR2(32000);
    vtype         VARCHAR2(106);
    vobject_type  VARCHAR2(19);
    vtable_name   VARCHAR2(30);
      invalid_table EXCEPTION;
    vcollist      TSTRINGLIST;
    vdatatypelist TSTRINGLIST;
    vtype_name    VARCHAR2(30);
    TYPE tsqlquery IS REF CURSOR;
    sqlquery      tsqlquery;
    vcolumn_name  VARCHAR2(30);
    vdata_type    VARCHAR2(106);
    vdata_length  NUMBER;
    vprecision    NUMBER;
    vscale        NUMBER;
    vchar_length  NUMBER;
    vschema_name  VARCHAR2(32);
    BEGIN
      vschema_name := SYS_CONTEXT('USERENV', 'current_user');
      pcolumnlist := TSTRINGLIST();
      pdatatypelist := TSTRINGLIST();
      BEGIN
        SELECT object_type
        INTO
          vobject_type
        FROM
          user_objects
        WHERE
          object_name = pobject_name AND object_type IN ('TABLE', 'SYNONYM', 'TYPE');
        EXCEPTION WHEN no_data_found THEN
        RETURN;
      END;
      IF vobject_type = 'SYNONYM'
      THEN
        vtable_name := gettablenamefromsynonym(pobject_name);
        SELECT table_owner
        INTO vschema_name
        FROM user_synonyms
        WHERE synonym_name = pobject_name;
      ELSIF vobject_type = 'TABLE'
        THEN
          vtable_name := pobject_name;
      ELSE
        vtype_name := pobject_name;
      END IF;
      IF vtable_name IS NOT NULL AND vobject_type IN ('TABLE', 'SYNONYM')
      THEN
        OPEN sqlquery FOR SELECT
                            column_name,
                            data_type,
                            data_length,
                            data_precision,
                            data_scale,
                            char_length
                          FROM all_tab_columns
                          WHERE table_name = vtable_name AND owner = vschema_name
                          ORDER BY column_id;
      ELSE
        OPEN sqlquery FOR SELECT
                            attr_name,
                            attr_type_name,
                            length,
                            precision,
                            scale,
                            length
                          FROM user_type_attrs
                          WHERE type_name = vtype_name
                          ORDER BY attr_no;
      END IF;
      LOOP
        FETCH sqlquery INTO vcolumn_name, vdata_type, vdata_length, vprecision, vscale, vchar_length;
        EXIT WHEN sqlquery%NOTFOUND;
        pcolumnlist.extend;
        pdatatypelist.extend;
        IF vdata_type = 'NUMBER'
        THEN
          vtype := vdata_type || '(' || nvl(
              vprecision, 18) || ',' || nvl(vscale, 0) || ')';
        ELSIF vdata_type = 'DATE' OR vdata_type = 'BLOB'
          THEN
            vtype := vdata_type;
        ELSIF vdata_type = 'TIMESTAMP(6)'
          THEN
            vtype := vdata_type;
        ELSIF vdata_type = 'CHAR'
          THEN
            vtype := vdata_type || '(' || vchar_length || ')';
        ELSE
          vtype := vdata_type || '(' || vdata_length || ')';
        END IF;
        pcolumnlist(pcolumnlist.last) := vcolumn_name;
        pdatatypelist(pdatatypelist.last) := vtype;
      END LOOP;
      CLOSE sqlquery;

    END;

END DbUtility;
/