import javax.swing.*;
import java.awt.*;
import java.awt.print.*;
import javax.swing.table.DefaultTableModel;

public class PDFExportPanel extends JPanel implements Printable {

    private CalendarPanel calendarPanel;
    private JButton exportBtn;

    public PDFExportPanel(CalendarPanel calendarPanel) {
        this.calendarPanel = calendarPanel;
        setLayout(new FlowLayout());
        setBackground(new Color(34, 34, 34));

        exportBtn = new JButton("Export Calendar to PDF");
        exportBtn.addActionListener(e -> printCalendar());
        add(exportBtn);
    }

    private void printCalendar() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Weekly Meal Calendar");
        job.setPrintable(this);

        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error printing: " + ex.getMessage());
            }
        }
    }

    @Override
    public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
        if (pageIndex > 0) return NO_SUCH_PAGE;

        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());

        DefaultTableModel model = (DefaultTableModel) calendarPanel.getCalendarTable().getModel();
        int rows = model.getRowCount();
        int cols = model.getColumnCount();

        // Table settings
        int startX = 50;
        int startY = 50;
        int rowHeight = 25;
        int colWidth = 200;

        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.drawString("Weekly Meal Calendar", startX, startY - 20);

        // Draw headers
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        for (int col = 0; col < cols; col++) {
            g2d.drawRect(startX + col * colWidth, startY, colWidth, rowHeight);
            String header = model.getColumnName(col);
            g2d.drawString(header, startX + col * colWidth + 5, startY + 17);
        }

        // Draw rows
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        for (int row = 0; row < rows; row++) {
            int y = startY + (row + 1) * rowHeight;
            for (int col = 0; col < cols; col++) {
                int x = startX + col * colWidth;
                g2d.drawRect(x, y, colWidth, rowHeight);
                Object value = model.getValueAt(row, col);
                g2d.drawString(value != null ? value.toString() : "", x + 5, y + 17);
            }
        }

        return PAGE_EXISTS;
    }
}
