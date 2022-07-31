package md.dev.plugin.documentation.table;

import java.util.ArrayList;
import java.util.List;

public class DescriptionRow {
    private final int length;
    private final List<Object> cells;


    public DescriptionRow(int length) {
        this.length = length;
        cells = new ArrayList<>();
    }


    public void addCell(TableCell tableCell) {
        if (cells.size() == length) {
            throw new IndexOutOfBoundsException();
        }
        cells.add(tableCell);
    }


    public void addInnerTable(DescriptionTable innerTable) {
        if (cells.size() == length) {
            throw new IndexOutOfBoundsException();
        }
        innerTable.setOuter(false);
        cells.add(innerTable);
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<tr>\n");
        cells.forEach(builder::append);
        builder.append("</tr>\n");
        return builder.toString();
    }
}
