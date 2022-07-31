package md.dev.plugin.documentation.table;

import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DescriptionTable {
    @Setter
    private boolean outer;
    private final String[] headers;
    private final List<DescriptionRow> rows;


    public DescriptionTable(String[] headers) {
        this(headers, true);
    }


    public DescriptionTable(String[] headers, boolean outer) {
        this.headers = headers;
        rows = new ArrayList<>();
        this.outer = outer;
    }


    public void addRow(DescriptionRow descriptionRow) {
        rows.add(descriptionRow);
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("\n");
        builder.append(outerInitiator());
        builder.append("<table style='text-align: center'>\n<tr>\n");
        Arrays.stream(headers).forEach(header ->
            builder.append(String.format("<th>%s</th>\n", header))
        );
        builder.append("</tr>\n");

        rows.forEach(builder::append);
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
