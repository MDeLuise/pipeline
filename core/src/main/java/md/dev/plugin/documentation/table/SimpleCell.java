package md.dev.plugin.documentation.table;

public class SimpleCell implements TableCell {
    private final String CONTENT;

    public SimpleCell(String content) {
        CONTENT = content;
    }

    @Override
    public String toString() {
        return String.format("<td>%s</td>\n", CONTENT);
    }
}
