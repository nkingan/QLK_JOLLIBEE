package bus;

import dao.ChiTietPhieuXuatDAO; 
import dao.PhieuXuatDAO;
import dao.TonKhoDAO;
import model.ChiTietPhieuXuat;

import java.math.BigDecimal; 


public class PhieuXuatBUS {

    private ChiTietPhieuXuatDAO ctpxDAO; 
    private TonKhoDAO tonKhoDAO;

    
    // CONSTRUCTOR
    
    public PhieuXuatBUS() {
        new PhieuXuatDAO();
        ctpxDAO = new ChiTietPhieuXuatDAO(); 
        tonKhoDAO = new TonKhoDAO();
    }

   
    // THÊM CHI TIẾT PHIẾU XUẤT
    
    public boolean themChiTietPhieuXuat(ChiTietPhieuXuat ct) throws Exception {

        
        // 1. KIỂM TRA NULL
        
        if (ct == null) {
            throw new Exception("Dữ liệu chi tiết phiếu xuất không hợp lệ!");
        }

        // 2. KIỂM TRA MÃ NGUYÊN LIỆU
        String maNL = ct.getMaNL();
        if (maNL == null || maNL.trim().isEmpty()) {
            throw new Exception("Mã nguyên liệu không được để trống!");
        }
        maNL = maNL.trim();

        // 3. KIỂM TRA SỐ LƯỢNG
        if (ct.getSoLuong() <= 0) {
            throw new Exception("Số lượng xuất phải lớn hơn 0!");
        }

        // 4. KIỂM TRA ĐƠN GIÁ 
        if (ct.getDonGia() == null || ct.getDonGia().compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("Đơn giá xuất kho phải lớn hơn 0 đ!");
        }

        // 5. KIỂM TRA TỒN KHO 
  
        int tonKho = tonKhoDAO.getTongTonByMaNL(maNL);

        if (tonKho == -1) {
            throw new Exception("Không tìm thấy nguyên liệu: " + maNL + " trên hệ thống kho!");
        }

        // 6. KHÔNG ĐỦ HÀNG 
        if (ct.getSoLuong() > tonKho) {
            throw new Exception("Từ chối xuất kho! Số lượng yêu cầu xuất (" + ct.getSoLuong() + ") "
                    + "lớn hơn số lượng đang có trong TonKho (" + tonKho + ").");
        }


        // 7. INSERT DATABASE 
        boolean result;
        result = ctpxDAO.insert(ct);

        if (!result) {
            throw new Exception("Không thể thêm chi tiết phiếu xuất! Vui lòng thử lại.");
        }

        return true;
    }
}