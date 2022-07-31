package md.dev.plugin.documentation.table;

public record SimpleCell(String content) implements TableCell {
    @Override
    public String toString() {
        return String.format("<td>%s</td>\n", content);
    }
}
