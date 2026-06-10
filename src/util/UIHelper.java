package util;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

/**
 * Lớp tiện ích cung cấp các hàm hỗ trợ thiết kế giao diện (UI) cho hệ thống Jollibee.
 */
public class UIHelper {

    /**
     * Tự động điều chỉnh độ rộng các cột của JTable dựa trên độ dài của tiêu đề cột
     * và nội dung của các ô dữ liệu trong cột đó.
     *
     * @param table Đối tượng JTable cần tự động giãn cách cột.
     */
    public static void autoResizeColumnWidths(JTable table) {
        // Tắt chế độ tự co giãn cột tự động của JTable để thanh cuộn ngang xuất hiện nếu cần
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        TableColumnModel columnModel = table.getColumnModel();
        for (int col = 0; col < table.getColumnCount(); col++) {
            TableColumn tableColumn = columnModel.getColumn(col);
            
            // Lấy độ rộng của header làm giá trị khởi tạo tối thiểu
            int width = 50; // Chiều rộng tối thiểu mặc định
            TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
            if (headerRenderer != null) {
                Component headerComp = headerRenderer.getTableCellRendererComponent(
                    table, tableColumn.getHeaderValue(), false, false, 0, col
                );
                // Thêm khoảng đệm 20px để tránh chữ bị sát viền
                width = Math.max(width, headerComp.getPreferredSize().width + 20);
            } else {
                width = Math.max(width, tableColumn.getHeaderValue().toString().length() * 10);
            }
            
            // Duyệt qua từng dòng của cột để tìm ô có độ dài lớn nhất
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, col);
                Component comp = table.prepareRenderer(renderer, row, col);
                // Thêm khoảng đệm 20px cho ô dữ liệu
                width = Math.max(width, comp.getPreferredSize().width + 20);
            }
            
            // Giới hạn chiều rộng tối đa là 500px để tránh một ô quá dài làm hỏng bố cục chung
            width = Math.min(width, 500); 
            tableColumn.setPreferredWidth(width);
        }
    }
}
