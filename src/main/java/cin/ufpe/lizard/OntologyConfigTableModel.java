package cin.ufpe.lizard;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import cin.ufpe.lizard.config.SourceConfig;

public class OntologyConfigTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	
	private List<SourceConfig> sourceConfigList;
	
	public OntologyConfigTableModel(List<SourceConfig> sourceConfigList) {
		this.sourceConfigList = sourceConfigList; 
	}
	
	@Override
	public int getRowCount() {
		return sourceConfigList.size();
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0: return sourceConfigList.get(rowIndex).getName();
		case 1: return sourceConfigList.get(rowIndex).getDescription();
		case 2: return sourceConfigList.get(rowIndex).isGlobal() ? "Sim" : "-";
		}
		return "-";
	}
	
	@Override
	public String getColumnName(int column) {
		switch (column) {
		case 0: return "Nome";
		case 1: return "Descrição";
		case 2: return "Global";
		}
		return "-";
	}
	
}
