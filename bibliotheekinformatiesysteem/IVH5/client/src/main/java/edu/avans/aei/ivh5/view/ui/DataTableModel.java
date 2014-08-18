package edu.avans.aei.ivh5.view.ui;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;

/**
 * This class provides a data model for the JTable component in the UI. Using this data model
 * enables us to quickly perform operations on the table containing member information. 
 * 
 * @author Robin Schellius
 *
 */
@SuppressWarnings("serial")
class DataTableModel extends AbstractTableModel {

	Vector<String[]> cache; // will hold String[] objects . . .
	int colCount;
	String[] headers;

	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(DataTableModel.class);

	/**
	 * 
	 */
	public DataTableModel() {
		logger.debug("Constructor");

		cache = new Vector<String[]>();
		colCount = 4;
		headers = new String[] {"Nr", "Voornaam", "Achternaam", "Bibliotheek"};
	}

	/**
	 * 
	 */
	public String getColumnName(int i) { 
		return headers[i];
	}

	/**
	 * 
	 */
	public int getColumnCount() {
		return colCount;
	}

	/**
	 * 
	 */
	public int getRowCount() {
		return cache.size();
	}

	/**
	 * 
	 */
	public Object getValueAt(int row, int col) {
		return ((String[]) cache.elementAt(row))[col];
	}

	/**
	 * 
	 * @param header
	 */
	public void setTableHeader(String[] header) {
		logger.debug("setTableHeader");

		headers = new String[colCount];
		for (int h = 0; h < colCount; h++) {
			headers[h] = header[h];
		}

	}

	/**
	 * 
	 * @param rs
	 */
	public void setValues(String[][] rs) {
		logger.debug("setValues");

		cache = new Vector<String[]>();
		try {
			int i = 0;
			while (i < rs.length) {
				String[] record = new String[colCount];
				for (int j = 0; j < colCount; j++) {
					record[j] = rs[i][j];
				}
				cache.addElement(record);
				i++;
			}
			fireTableChanged(null); // notify everyone that we have a new table.
		} catch (Exception e) {
			cache = new Vector<String[]>(); // blank it out and keep going.
			e.printStackTrace();
		}
	}
}