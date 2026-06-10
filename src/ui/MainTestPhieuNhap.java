package ui;

import javax.swing.*;
import model.TaiKhoan;

public class MainTestPhieuNhap {
    public static void main(String[] args) {
        // 1. Cấu hình giao diện theo giao diện hệ điều hành Windows/Mac
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Không thể cài đặt Look and Feel: " + e.getMessage());
        }

        // 2. Chạy giao diện an toàn trong luồng Event Dispatch Thread của Swing
        SwingUtilities.invokeLater(() -> {
            try {
                // Tạo một tài khoản ảo để test phân hệ, tránh lỗi NullPointerException
                TaiKhoan mockUser = new TaiKhoan();
                mockUser.setMaNV("NV01");

                // Khởi tạo cửa sổ Frame nền
                JFrame frame = new JFrame("Hệ thống Kiểm Thử Phân Hệ Nhập Kho - Jollibee");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(1050, 650); // Đặt kích thước rộng rãi
                frame.setLocationRelativeTo(null); // Căn giữa màn hình máy tính

                System.out.println("Đang nạp giao diện NhapKhoUI...");
                // Nhúng Panel NhapKhoUI vào khung chạy
                NhapKhoUI nhapKhoUI = new NhapKhoUI(mockUser);
                frame.add(nhapKhoUI);

                // 🌟 QUAN TRỌNG: Lệnh ép cửa sổ phải xuất hiện trên màn hình máy tính
                frame.setVisible(true); 
                System.out.println("Giao diện đã hiển thị thành công!");

            } catch (Exception ex) {
                System.err.println("Gặp lỗi nghiêm trọng khi khởi tạo giao diện:");
                ex.printStackTrace();
                
                // Nếu bị lỗi nạp dữ liệu từ DB, tạo một frame trống kèm thông báo để không bị treo luồng
                JFrame errorFrame = new JFrame("Lỗi hệ thống");
                errorFrame.setSize(400, 200);
                errorFrame.setLocationRelativeTo(null);
                errorFrame.add(new JLabel("Lỗi khởi tạo: " + ex.getMessage(), SwingConstants.CENTER));
                errorFrame.setVisible(true);
            }
        });
    }
}