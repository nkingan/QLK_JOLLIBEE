package bus;

import dao.ChiTietPhieuXuatDAO;
import dao.PhieuXuatDAO;
import dao.TonKhoDAO;
import model.ChiTietPhieuXuat;
import model.PhieuXuat;
import util.DBConnection; 

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

/**
 * BUS xử lý nghiệp vụ Phiếu Xuất Kho liên thông dữ liệu hệ thống
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

    public boolean themChiTietPhieuXuat(ChiTietPhieuXuat ct) throws Exception {
       
        if (ct == null) throw new Exception("Dữ liệu chi tiết mặt hàng xuất không hợp lệ!");
        if (ct.getMaCTPX() == null || ct.getMaCTPX().trim().isEmpty()) throw new Exception("Mã chi tiết phiếu xuất không được để trống!");
        if (ct.getMaNL() == null || ct.getMaNL().trim().isEmpty()) throw new Exception("Vui lòng chọn hoặc nhập mã nguyên liệu cần xuất!");
        if (ct.getSoLuong() <= 0) throw new Exception("Số lượng xuất kho phải lớn hơn 0!");
        if (ct.getDonGia() == null || ct.getDonGia().compareTo(BigDecimal.ZERO) <= 0) throw new Exception("Đơn giá xuất kho phải lớn hơn 0 đ!");

        int tonKhoHienTai = tonKhoDAO.getTongTonByMaNL(ct.getMaNL().trim());
        if (tonKhoHienTai == -1) throw new Exception("Mã nguyên liệu [" + ct.getMaNL() + "] không tồn tại trên hệ thống kho Jollibee!");
        if (ct.getSoLuong() > tonKhoHienTai) throw new Exception("Từ chối xuất kho! Số lượng yêu cầu xuất (" + ct.getSoLuong() + ") lớn hơn số lượng đang có trong TonKho (" + tonKhoHienTai + ").");

        return ctpxDAO.insert(ct);
    }

    public boolean xoaPhieuXuat(String maPX) throws Exception {
        if (maPX == null || maPX.trim().isEmpty()) throw new Exception("Mã phiếu xuất không hợp lệ!");
        try {
            ctpxDAO.deleteByMaPX(maPX.trim());
            return pxDAO.delete(maPX.trim());
        } catch (Exception e) {
            throw new Exception("Lỗi hệ thống khi xóa phiếu xuất: " + e.getMessage());
        }
    }

    
    public List<ChiTietPhieuXuat> getChiTietByMaPXWithNames(String maPX) {
        return ctpxDAO.getByMaPX(maPX); 
    }

   
    public boolean luuTronGoiPhieuXuat(PhieuXuat px, List<ChiTietPhieuXuat> dsChiTiet) throws Exception {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); 

            // 1. Lưu phiếu cha
            if (!pxDAO.insert(conn, px)) {
                conn.rollback();
                return false;
            }

            // 2. Lưu từng chi tiết con
            for (ChiTietPhieuXuat ct : dsChiTiet) {
                if (!ctpxDAO.insert(conn, ct)) {
                    conn.rollback();
                    return false;
                }
            }

            conn.commit(); 
            return true;
        } catch (Exception e) {
            if (conn != null) conn.rollback();
            throw new Exception("Lỗi hệ thống: " + e.getMessage());
        } finally {
            if (conn != null) conn.close();
        }
    }
}