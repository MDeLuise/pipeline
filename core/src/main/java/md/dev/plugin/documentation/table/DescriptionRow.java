package md.dev.plugin.documentation.table;

import java.util.ArrayList;
import java.util.List;

public class DescriptionRow {
    private final int LENGTH;
    private final List<Object> CELLS;


    public DescriptionRow(int length) {
        LENGTH = length;
        CELLS = new ArrayList<>();
    }

    public void addCell(TableCell tableCell) {
        if (CELLS.size() == LENGTH) {
            throw new IndexOutOfBoundsException();
        }
        CELLS.add(tableCell);
    }

    public void addInnerTable(DescriptionTable innerTable) {
        if (CELLS.size() == LENGTH) {
            throw new IndexOutOfBoundsException();
        }
        innerTable.setOuter(false);
        CELLS.add(innerTable);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("<tr>\n");
        CELLS.forEach(builder::append);
        builder.append("</tr>\n");
        return builder.toString();
    }
}
