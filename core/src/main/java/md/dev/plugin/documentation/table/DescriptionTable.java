package md.dev.plugin.documentation.table;

import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DescriptionTable {
    private final String[] HEADERS;
    private final List<DescriptionRow> ROWS;
    @Setter
    private boolean outer;


    public DescriptionTable(String[] headers) {
        this(headers, true);
    }

    public DescriptionTable(String[] headers, boolean outer) {
        HEADERS = headers;
        ROWS = new ArrayList<>();
        this.outer = outer;
    }

    public void addRow(DescriptionRow descriptionRow) {
        ROWS.add(descriptionRow);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("\n");
        builder.append(outerInitiator());
        builder.append("<table style='text-align: center'>\n<tr>\n");
        Arrays.stream(HEADERS).forEach(header ->
                builder.append(String.format("<th>%s</th>\n", header))
        );
        builder.append("</tr>\n");

        ROWS.forEach(builder::append);
        builder.append("</table>");
        builder.append(outerTerminator());
        builder.append("\n");
        return builder.toString();
    }

    private String outerTerminator() {
        return outer ? "" : "</td>";
    }

    private String outerInitiator() {
        return outer ? "" : "<td>";
    }
}
