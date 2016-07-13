package org.qamatic.mintleaf.dbsupportimpls.h2;

import org.qamatic.mintleaf.core.BaseDbContext;
import org.qamatic.mintleaf.interfaces.H2DbContext;

import javax.sql.DataSource;

/**
 * Created by senips on 7/12/16.
 */
public class H2DbContextImpl extends BaseDbContext implements H2DbContext{
    public H2DbContextImpl(DataSource datasource) {
        super(datasource);
    }
    
    
    public void iterateTableByQuery(String tableName, String columns, String whereClause, RowListner listener) throws SQLException {
        TableMetaData metaData = getObjectMetaData(tableName);



    }

    @Override
    public TableMetaData getObjectMetaData(String objectName) throws SQLException {
        final TableMetaData metaData = new TableMetaData();
        if(objectName != null) {
            objectName = objectName.toUpperCase();
        }

        String[] splits = objectName.split(Pattern.quote("."));

        metaData.setObjectName(objectName);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(this.getDataSource());
        String sql = String.format("select * from information_schema.columns where TABLE_SCHEMA ='%s' and TABLE_NAME='%s'", splits[0], splits[1]);
        jdbcTemplate.query(sql, new RowMapper() {
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                Column colMetaData = new Column();
                colMetaData.setColumnName(rs.getString("COLUMN_NAME"));
                colMetaData.setNullable(rs.getString("IS_NULLABLE")!="NO");
                colMetaData.setColumnSize(1);
                colMetaData.setDatatype(rs.getInt("DATA_TYPE"));

                colMetaData.setTypeName(rs.getString("TYPE_NAME"));
                colMetaData.setColumnSize(rs.getInt("CHARACTER_OCTET_LENGTH"));

                if(rs.getString("TYPE_NAME").equals("CHAR")) {
                  //  colMetaData.setColumnSize(rs.getInt("CHARACTER_OCTET_LENGTH"));
                } else if(rs.getString("TYPE_NAME").equals("DECIMAL")) {
                    colMetaData.setColumnSize(rs.getInt("NUMERIC_PRECISION"));
                }
                colMetaData.setDecimalDigits(rs.getInt("NUMERIC_SCALE"));

                metaData.add(colMetaData);
                return null;
            }
        });
        return metaData;
    }
}
