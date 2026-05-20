package bus;

import dao.NguyenLieu;
import model.NguyenLieu;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class NguyenLieuBUS {
    public String validate(NguyenLieu nl) {
        if (nl.getMaNL() == null || nl.getMaNL().trim().isEmpty())
            return "Mã nguyên liệu không được để trống!";
        if (nl.getTenNL() == null || nl.getTenNL().trim().isEmpty())
            return "Tên nguyên liệu không được để trống!";
        if (nl.getDonViTinh() == null || nl.getDonViTinh().trim().isEmpty())
            return "Đơn vị tính không được để trống!";
        if (nl.getSoLuong() < 0)
            return "Số lượng không được âm!";
        if (nl.getMaKho() == null || nl.getMaKho().trim().isEmpty())
            return "Mã kho không được để trống!";
        return "OK";
    }
}