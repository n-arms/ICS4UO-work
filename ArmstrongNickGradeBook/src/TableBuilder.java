import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * TableBuilder is used to nicely format a table for the CLI. It avoids repetition by concentrating
 * all formatting code into a single class. It uses a column-major representation to allow users to
 * easily add parallel array style data. This is extremely convenient when data isn't stored in a
 * single array of data objects, such as the structure created by storing a list of Students but
 * frequently querying specific assignments.
 */
public class TableBuilder {
    /**
     * A column in a table.
     * @param header the text at the top of the column
     * @param width the width of the column in characters
     * @param entries the rows of the column
     */
    private record Column(String header, int width, String[] entries) {}
    private final ArrayList<Column> columns;
    static private final char BAR = '│';

    /**
     * Construct a new TableBuilder with no columns.
     */
    public TableBuilder() {
        columns = new ArrayList<>();
    }

    /**
     * Add a column to the table.
     *
     * @param header the header of the column
     * @param width the width of the column in characters
     * @param entries the column's entries
     */
    public void addColumn(String header, int width, String[] entries) {
        columns.add(new Column(header, width, entries));
    }
    /**
     * Add a column to the table.
     *
     * @param header the header of the column
     * @param width the width of the column in characters
     * @param entries the column's entries
     */
    public void addColumn(String header, int width, Collection<String> entries) {
        // if a column is added with a collection of entries, convert it to an array of strings first
        addColumn(header, width, entries.toArray(new String[] {}));
    }
    /**
     * Add a column to the table.
     *
     * @param header the header of the column
     * @param width the width of the column in characters
     * @param entries the column's entries
     */
    public void addColumn(String header, int width, Stream<String> entries) {
        // if a column is added with a stream of entries, convert it to an array of string first
        addColumn(header, width, entries.collect(Collectors.toList()));
    }

    /**
     * Turn the table into a string.
     *
     * @return the stringified table
     */
    public String build() {
        StringBuilder builder = new StringBuilder();

        // add the header

        // add a space to align text correctly with the leftmost border
        builder.append(' ');

        for (Column c : columns) {
            // add the correctly centred text
            centreAlignTextInto(builder, c.header, c.width);
            // add a space to align text correctly with the right border of each cell
            builder.append(' ');
        }
        builder.append('\n');

        // add the body

        // find the maximum height of any one column by iterating over each column and finding the number of entries
        int maxHeight = columns.stream().mapToInt(column -> column.entries.length).max().orElse(0);

        // iterate over all the rows
        for (int i = 0; i < maxHeight; i++) {
            // add a starting border
            builder.append(BAR);
            for (Column c : columns) {
                if (i < c.entries.length) {
                    // if the current column is tall enough, add its formatted text
                    centreAlignTextInto(builder, c.entries[i], c.width);
                } else {
                    // if the current column is too short, add spaces
                    centreAlignTextInto(builder, "", c.width);
                }
                // add the current cell's right border
                builder.append(BAR);
            }
            builder.append('\n');
        }

        return builder.toString();
    }

    /**
     * Centre some text by adding space characters to the front and back.
     *
     * @param output the string builder to write the result into
     * @param text the text to centre
     * @param width the width the result should be
     */
    private void centreAlignTextInto(StringBuilder output, String text, int width) {
        if (text.length() > width) {
            // if the text is too wide, cut it off
            output.append(text, 0, width);
            return;
        }
        // calculate the left and right padding to make the string to correct length
        int leftPadding = (width - text.length()) / 2;
        int rightPadding = (width - leftPadding - text.length());

        // add spaces and text appropriately
        output.append(" ".repeat(leftPadding));
        output.append(text);
        output.append(" ".repeat(rightPadding));
    }
}
