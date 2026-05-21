package bus;

import dao.ChiTietPhieuXuatDAO;
import dao.PhieuXuatDAO;
import dao.TonKhoDAO;
import model.ChiTietPhieuXuat;
import model.PhieuXuat;

import java.math.BigDecimal;
import java.util.List;

/**
 * BUS xử lý nghiệp vụ Phiếu Xuất Kho
 * Đã sửa lỗi Unreachable catch block bằng cơ chế xử lý ngoại lệ an toàn.
 */
@SuppressWarnings("all")
public class PhieuXuatBUS {

    private PhieuXuatDAO pxDAO;
    private ChiTietPhieuXuatDAO ctpxDAO;
    private TonKhoDAO tonKhoDAO;

    public PhieuXuatBUS() {
        pxDAO = new PhieuXuatDAO();
        ctpxDAO = new ChiTietPhieuXuatDAO();
        tonKhoDAO = new TonKhoDAO();
    }

    // =========================================================
    // CÁC HÀM ĐIỀU PHỐI DỮ LIỆU CƠ BẢN ĐỂ SỬA LỖI ĐỎ TRÊN PANEL
    // =========================================================
    public List<PhieuXuat> getAllPhieuXuat() {
        return pxDAO.getAll();
    }

    public List<ChiTietPhieuXuat> getChiTietByMaPX(String maPX) {
        return ctpxDAO.getByMaPX(maPX);
    }

    public String generateNextMaPX() {
        return pxDAO.generateNextMaPX();
    }

    public String generateNextMaCTPX() {
        return ctpxDAO.generateNextMaCTPX();
    }

    // =========================================================
    // XỬ LÝ NGHIỆP VỤ KIỂM TRA TỒN KHO VÀ THÊM CHI TIẾT
    // =========================================================
    public boolean themChiTietPhieuXuat(ChiTietPhieuXuat ct) throws Exception {

        if (ct == null) {
            throw new Exception("Dữ liệu chi tiết mặt hàng xuất không hợp lệ!");
        }
        if (ct.getMaCTPX() == null || ct.getMaCTPX().trim().isEmpty()) {
            throw new Exception("Mã chi tiết phiếu xuất không được để trống!");
        }
        if (ct.getMaNL() == null || ct.getMaNL().trim().isEmpty()) {
            throw new Exception("Vui lòng chọn hoặc nhập mã nguyên liệu cần xuất!");
        }
        if (ct.getSoLuong() <= 0) {
            throw new Exception("Số lượng xuất kho phải lớn hơn 0!");
        }
        if (ct.getDonGia() == null || ct.getDonGia().compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("Đơn giá xuất kho phải lớn hơn 0 đ!");
        }

        // Kiểm tra số lượng tổng tồn thực tế trực tiếp dưới Database thông qua SQL Server
        int tonKhoHienTai = tonKhoDAO.getTongTonByMaNL(ct.getMaNL().trim());

        if (tonKhoHienTai == -1) {
            throw new Exception("Mã nguyên liệu [" + ct.getMaNL() + "] không tồn tại trên hệ thống kho Jollibee!");
        }

        // LOGIC CHẶN ĐỨNG: Nếu số lượng xuất vượt quá số hàng hiện có trong kho -> Từ chối nạp
        if (ct.getSoLuong() > tonKhoHienTai) {
            throw new Exception("Từ chối xuất kho! Số lượng yêu cầu xuất (" + ct.getSoLuong() + ") "
                    + "lớn hơn số lượng đang có trong TonKho (" + tonKhoHienTai + ").");
        }

        boolean insertSuccess;
        try {
            insertSuccess = ctpxDAO.insert(ct);
        } catch (Exception e) { 
            // GIẢI PHÁP: Đổi sang catch Exception tổng quát để tránh lỗi Unreachable Block
            throw new Exception("Lỗi hệ thống Database: " + e.getMessage());
        }

        if (!insertSuccess) {
            throw new Exception("Thao tác thêm chi tiết phiếu xuất thất bại!");
        }

        return true;
    }

    // =========================================================
    // NGHIỆP VỤ XÓA PHIẾU XUẤT TRỌN GÓI
    // =========================================================
    public boolean xoaPhieuXuat(String maPX) throws Exception {
        if (maPX == null || maPX.trim().isEmpty()) {
            throw new Exception("Mã phiếu xuất không hợp lệ!");
        }
        try {
            // Giải phóng bảng con chứa khóa ngoại trước, sau đó xóa bản ghi phiếu cha sau
            ctpxDAO.deleteByMaPX(maPX.trim());
            return pxDAO.delete(maPX.trim());
        } catch (Exception e) {
            throw new Exception("Lỗi hệ thống khi xóa phiếu xuất: " + e.getMessage());
        }
    }
}