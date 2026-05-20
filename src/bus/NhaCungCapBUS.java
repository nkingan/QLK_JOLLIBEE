package bus;

import dao.NhaCungCap;
import model.NhaCungCap;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NhaCungCapBUS {

    public String validate(NhaCungCap ncc) {
        if (ncc.getMaNCC() == null || ncc.getMaNCC().trim().isEmpty()) 
            return "Mã nhà cung cấp không được để trống!";
        if (ncc.getTenNCC() == null || ncc.getTenNCC().trim().isEmpty()) 
            return "Tên nhà cung cấp không được để trống!";
        if (!ncc.getSdt().matches("\\d{10,11}")) 
            return "Số điện thoại phải có 10-11 chữ số!";
        if (!ncc.getEmail().contains("@")) 
            return "Email không hợp lệ!";
        return "OK";
    }
}