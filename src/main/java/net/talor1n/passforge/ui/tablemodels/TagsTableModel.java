package net.talor1n.passforge.ui.tablemodels;

import net.talor1n.passforge.entity.Tag;

import javax.swing.*;
import javax.swing.table.*;
import java.util.Map;
import java.util.Set;

public class TagsTableModel extends AbstractTableModel {

    private final String[] columnNames = {"Тэг", "Выбрано"};
    private final Map<JCheckBox, Tag> checkboxTagMap;

    public TagsTableModel(Map<JCheckBox, Tag> checkboxTagMap) {
        this.checkboxTagMap = checkboxTagMap;
    }

    @Override
    public int getRowCount() {
        return checkboxTagMap.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Tag tag = getTagAt(rowIndex);
        switch (columnIndex) {
            case 0:
                return tag.getName();
            case 1:
                return getCheckBoxForTag(tag).isSelected(); // Возвращаем Boolean вместо JCheckBox
            default:
                return null;
        }
    }

    private Tag getTagAt(int index) {
        Set<Map.Entry<JCheckBox, Tag>> entrySet = checkboxTagMap.entrySet();
        Map.Entry<JCheckBox, Tag> entry = (Map.Entry<JCheckBox, Tag>) entrySet.toArray()[index];
        return entry.getValue();
    }

    private JCheckBox getCheckBoxForTag(Tag tag) {
        for (Map.Entry<JCheckBox, Tag> entry : checkboxTagMap.entrySet()) {
            if (entry.getValue().equals(tag)) {
                return entry.getKey();
            }
        }
        return new JCheckBox();
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 1 && aValue instanceof Boolean) {
            Tag tag = getTagAt(rowIndex);
            JCheckBox checkBox = getCheckBoxForTag(tag);
            checkBox.setSelected((Boolean) aValue);
            fireTableCellUpdated(rowIndex, columnIndex); // Уведомляем таблицу об изменении
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 1) {
            return Boolean.class; // Изменяем на Boolean вместо JCheckBox
        }
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 1; // Делаем колонку с чекбоксами редактируемой
    }

    public boolean getCheckBoxState(Tag tag) {
        for (Map.Entry<JCheckBox, Tag> entry : checkboxTagMap.entrySet()) {
            if (entry.getValue().equals(tag)) {
                return entry.getKey().isSelected();
            }
        }
        return false;
    }
}