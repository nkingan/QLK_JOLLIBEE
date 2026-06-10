package service;

import dao.NguyenLieuDAO;
import model.NguyenLieu;
import model.TonKhoModel;
import model.CanhBaoTonKhoModel;

import java.util.List;

public class NguyenLieuService {
    private final NguyenLieuDAO nguyenLieuDAO = new NguyenLieuDAO();

    /**
     * Lấy danh sách toàn bộ nguyên liệu.
     */
    public List<NguyenLieu> getAllIngredients() {
        return nguyenLieuDAO.getAllNguyenLieu();
    }

    /**
     * Tìm nguyên liệu theo mã nguyên liệu.
     */
    public NguyenLieu getIngredientById(String maNL) {
        return nguyenLieuDAO.getNguyenLieuById(maNL);
    }

    /**
     * Thêm mới nguyên liệu vào kho.
     */
    public boolean addIngredient(NguyenLieu nguyenLieu) {
        if (nguyenLieu.getMaNL() == null || nguyenLieu.getMaNL().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã nguyên liệu không được để trống!");
        }
        if (nguyenLieu.getTenNL() == null || nguyenLieu.getTenNL().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên nguyên liệu không được để trống!");
        }
        return nguyenLieuDAO.addNguyenLieu(nguyenLieu);
    }

    /**
     * Cập nhật thông tin nguyên liệu.
     */
    public boolean updateIngredient(NguyenLieu nguyenLieu) {
        if (nguyenLieu.getMaNL() == null || nguyenLieu.getMaNL().trim().isEmpty()) {
            throw new IllegalArgumentException("Mã nguyên liệu không được để trống khi cập nhật!");
        }
        return nguyenLieuDAO.updateNguyenLieu(nguyenLieu);
    }

    /**
     * Xóa nguyên liệu khỏi hệ thống.
     */
    public boolean deleteIngredient(String maNL) {
        if (maNL == null || maNL.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã nguyên liệu không hợp lệ!");
        }
        return nguyenLieuDAO.deleteNguyenLieu(maNL);
    }

    /**
     * Tìm kiếm nguyên liệu theo tiêu chí và chuỗi tìm kiếm.
     */
    public List<NguyenLieu> searchIngredients(String criteria, String searchTerm) {
        if (searchTerm == null) {
            searchTerm = "";
        }
        return nguyenLieuDAO.searchNguyenLieu(criteria, searchTerm.trim());
    }

    /**
     * Lấy danh sách tồn kho từ View VW_TonKho.
     */
    public List<TonKhoModel> getDanhSachTonKho() {
        return nguyenLieuDAO.getDanhSachTonKho();
    }

    /**
     * Lấy danh sách các nguyên liệu sắp hết hàng (tồn kho dưới ngưỡng).
     */
    public List<NguyenLieu> getLowStockIngredients(int threshold) {
        return nguyenLieuDAO.getLowStockProducts(threshold);
    }

    /**
     * Lấy danh sách cảnh báo tồn kho thấp chưa xử lý (từ bảng CanhBaoTonKho).
     */
    public List<CanhBaoTonKhoModel> getLowStockAlerts() {
        return nguyenLieuDAO.getDanhSachCanhBao();
    }

    /**
     * Xác nhận xử lý cảnh báo tồn kho thấp.
     */
    public boolean resolveAlert(int maCanhBao) {
        return nguyenLieuDAO.updateTrangThaiCanhBao(maCanhBao, "Đã xử lý");
    }
}
