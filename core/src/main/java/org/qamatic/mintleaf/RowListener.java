package org.qamatic.mintleaf;

/**
 * Created by senips on 7/12/16.
 */
public interface RowListener {

    void rowData(int row, Object[] values);



    DbColumn getMetaData(int col);

}
