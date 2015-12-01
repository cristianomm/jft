/**
 * 
 */
package com.cmm.jft.ui.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;


/**
 * <p><code>GenericTableModel.java</code></p>
 * TableModel para objetos, deve receber a classe do objeto a ser armazenado
 * na tabela para que possam ser conhecidos os campos que serao mostrados no grid.
 * Os campos que serao mostrados no grid devem ser definidos atraves do metodo
 * <code>addAlias()</code>
 * 
 * @author Cristiano M Martins
 * @version 27/01/2014 23:22:47
 *
 */
public class GenericTableModel<T> extends AbstractTableModel {

	private class TableAlias{

		private int position;
		private String fieldName;
		private String fieldAlias;
		private boolean visible;

		/**
		 * @param position
		 * @param fieldName
		 * @param fieldAlias
		 * @param visible
		 */
		public TableAlias(int position, String fieldName, String fieldAlias, boolean visible) {
			super();
			this.position = position;
			this.fieldName = fieldName;
			this.fieldAlias = fieldAlias;
			this.visible = visible;
		}

	}


	private Class typeClass;
	private List<T> lines;
	private List<String> columns;
	private Map<String, TableAlias> gridFields;

	/**
	 * 
	 */
	public GenericTableModel(Class typeClass) {
		this.typeClass = typeClass;
		this.lines = new ArrayList<T>();
		this.columns = new ArrayList<String>();
		this.gridFields = new HashMap<String, TableAlias>();
		retrieveColumns();
	}

	public GenericTableModel(List<T> list, Class typeClass) {
		this.typeClass = typeClass;
		this.lines = new ArrayList<T>(list);
		this.columns = new ArrayList<String>();
		this.gridFields = new HashMap<String, TableAlias>();
		retrieveColumns();
	}

	private void retrieveColumns() {

		int idx=0;
		for(Field f : typeClass.getDeclaredFields()) {
			f.setAccessible(true);
			addAlias(f.getName(), f.getName(), false, idx);
		}
		rebuildColumns();
	}


	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return columns.size();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		return lines.size();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		Object ret = null;

		if(columnIndex>=0 && columnIndex<columns.size()) {
			T t = lines.get(rowIndex);
			String col = columns.get(columnIndex);

			for(TableAlias ta : gridFields.values()) {
				if(ta.fieldAlias.equalsIgnoreCase(col)) {
					for(Field f: typeClass.getDeclaredFields()) {
						f.setAccessible(true);
						if(ta.fieldName.equalsIgnoreCase(f.getName())) {

							try {
								return f.get(t);

							} catch (IllegalArgumentException
									| IllegalAccessException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}

		/*
	for(Method m: t.getClass().getDeclaredMethods()) {
	    if(m.isAnnotationPresent(ColumnField.class)) {
		ColumnField cf = m.getAnnotation(ColumnField.class);
		if(col.equalsIgnoreCase(cf.name())) {
		    try {
			ret = m.invoke(t, null);
			Formatter f =  FormatterFactory.getFormatter(cf.formatter());
			ret = f.format(ret);
			break;
		    } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		    }
		}
	    }
	}
		 */

		return ret;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
	 */
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int column) {
		return columns.get(column);
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {

		Class ret = String.class;
		if(columnIndex>=0 && columnIndex<columns.size()) {
			String col = columns.get(columnIndex);

			for(TableAlias ta : gridFields.values()) {
				if(ta.fieldAlias.equalsIgnoreCase(col)) {
					for(Field f: typeClass.getDeclaredFields()) {
						f.setAccessible(true);
						if(ta.fieldName.equalsIgnoreCase(f.getName())) {
							return f.getType();
						}
					}
				}
			}
		}
		return ret;
	}

	public void addColumn(String columnName) {
		columns.add(columnName);
	}

	public void addColumn(String columnName, int index) {
		columns.add(index, columnName);
	}

	public T getValue(int rowIndex) {
		return lines.get(rowIndex);
	}

	public List<T> getValues(){
		return lines;
	}

	public void setValues(Collection<T> lines) {
		if(lines!=null) {
			this.lines.clear();
			this.lines.addAll(lines);
			fireTableRowsInserted(0, getRowCount()-1);
		}
	}

	public void addValue(T t) {
		lines.add(t);
		int lastIdx = getRowCount() -1;
		fireTableRowsInserted(lastIdx, lastIdx);
	}

	public void remove(int rowIndex) {
		if(FormUtils.getInstance().indexInRange(lines, rowIndex)) {
			lines.remove(rowIndex);
			fireTableRowsDeleted(rowIndex, rowIndex);
		}
	}

	public void addValues(List<T> values) {
		int lastSize = getRowCount();
		lines.addAll(values);
		fireTableRowsInserted(lastSize, getRowCount()-1);
	}

	public void clearTable() {
		lines.clear();
		fireTableDataChanged();
	}

	public boolean isEmpty() {
		return lines.isEmpty();
	}

	public void showColumnWithAlias(String fieldName, String fieldAlias) {
		showColumn(fieldName);
		setAlias(fieldName, fieldAlias);
	}

	public void showColumn(String fieldName) {
		if(gridFields.containsKey(fieldName)) {
			gridFields.get(fieldName).visible = true;
		}
	}

	public void setAlias(String fieldName, String fieldAlias) {
		if(gridFields.containsKey(fieldName)) {
			gridFields.get(fieldName).fieldAlias = fieldAlias;
		}
	}

	public void addAlias(String fieldName, String fieldAlias, boolean visible, int position) {
		gridFields.put(fieldName, new TableAlias(position, fieldName, fieldAlias, visible));
	}

	public void rebuildColumns() {
		columns.clear();
		for(TableAlias ta : gridFields.values()) {
			if(ta.visible) {
				columns.add(ta.position, ta.fieldAlias);
			}
		}

	}

}
