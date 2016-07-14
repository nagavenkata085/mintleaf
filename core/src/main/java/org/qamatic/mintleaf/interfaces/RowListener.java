package org.qamatic.mintleaf.interfaces;

/**
 * Created by senips on 7/12/16.
 */
public interface RowListener {

    void rowData(int row, Object[] values);



    Column getMetaData(int col);

}
