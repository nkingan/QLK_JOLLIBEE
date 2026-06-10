package util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExcelExporter {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Xuất phiếu nhập kho ra file Excel (.xlsx)
     *
     * @param parent Khung giao diện gọi xuất file
     * @param maPN   Mã phiếu nhập cần xuất
     */
    public static void exportPhieuNhap(JFrame parent, String maPN) {
        if (maPN == null || maPN.trim().isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Mã phiếu nhập không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu Phiếu Nhập Excel");
        fileChooser.setSelectedFile(new File("PhieuNhap_" + maPN + ".xlsx"));

        int userSelection = fileChooser.showSaveDialog(parent);
        if (userSelection != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File fileToSave = fileChooser.getSelectedFile();
        if (!fileToSave.getName().toLowerCase().endsWith(".xlsx")) {
            fileToSave = new File(fileToSave.getParentFile(), fileToSave.getName() + ".xlsx");
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Phiếu Nhập " + maPN);

            // Create Fonts
            Font titleFont = workbook.createFont();
            titleFont.setFontName("Segoe UI");
            titleFont.setFontHeightInPoints((short) 16);
            titleFont.setBold(true);

            Font headerFont = workbook.createFont();
            headerFont.setFontName("Segoe UI");
            headerFont.setFontHeightInPoints((short) 11);
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());

            Font normalFont = workbook.createFont();
            normalFont.setFontName("Segoe UI");
            normalFont.setFontHeightInPoints((short) 11);

            Font boldFont = workbook.createFont();
            boldFont.setFontName("Segoe UI");
            boldFont.setFontHeightInPoints((short) 11);
            boldFont.setBold(true);

            // Create styles
            CellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.LEFT);

            // Red Header Style (#DA291C)
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            
            XSSFCellStyle xssfHeaderStyle = (XSSFCellStyle) headerStyle;
            byte[] redColor = new byte[]{(byte) 0xDA, (byte) 0x29, (byte) 0x1C};
            xssfHeaderStyle.setFillForegroundColor(new XSSFColor(redColor, null));
            xssfHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Data Style Left
            CellStyle cellStyleLeft = workbook.createCellStyle();
            cellStyleLeft.setFont(normalFont);
            cellStyleLeft.setBorderTop(BorderStyle.THIN);
            cellStyleLeft.setBorderBottom(BorderStyle.THIN);
            cellStyleLeft.setBorderLeft(BorderStyle.THIN);
            cellStyleLeft.setBorderRight(BorderStyle.THIN);

            // Data Style Center
            CellStyle cellStyleCenter = workbook.createCellStyle();
            cellStyleCenter.setFont(normalFont);
            cellStyleCenter.setAlignment(HorizontalAlignment.CENTER);
            cellStyleCenter.setBorderTop(BorderStyle.THIN);
            cellStyleCenter.setBorderBottom(BorderStyle.THIN);
            cellStyleCenter.setBorderLeft(BorderStyle.THIN);
            cellStyleCenter.setBorderRight(BorderStyle.THIN);

            // Data Style Right
            CellStyle cellStyleRight = workbook.createCellStyle();
            cellStyleRight.setFont(normalFont);
            cellStyleRight.setAlignment(HorizontalAlignment.RIGHT);
            cellStyleRight.setBorderTop(BorderStyle.THIN);
            cellStyleRight.setBorderBottom(BorderStyle.THIN);
            cellStyleRight.setBorderLeft(BorderStyle.THIN);
            cellStyleRight.setBorderRight(BorderStyle.THIN);
            cellStyleRight.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));

            // Total Label Style
            CellStyle totalLabelStyle = workbook.createCellStyle();
            totalLabelStyle.setFont(boldFont);
            totalLabelStyle.setAlignment(HorizontalAlignment.RIGHT);
            totalLabelStyle.setBorderTop(BorderStyle.THIN);
            totalLabelStyle.setBorderBottom(BorderStyle.THIN);
            totalLabelStyle.setBorderLeft(BorderStyle.THIN);
            totalLabelStyle.setBorderRight(BorderStyle.THIN);

            // Total Value Style
            CellStyle totalValueStyle = workbook.createCellStyle();
            totalValueStyle.setFont(boldFont);
            totalValueStyle.setAlignment(HorizontalAlignment.RIGHT);
            totalValueStyle.setBorderTop(BorderStyle.THIN);
            totalValueStyle.setBorderBottom(BorderStyle.THIN);
            totalValueStyle.setBorderLeft(BorderStyle.THIN);
            totalValueStyle.setBorderRight(BorderStyle.THIN);
            totalValueStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));

            // 1. Write Title
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("HỆ THỐNG KHO JOLLIBEE - PHIẾU NHẬP KHO");
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 9));

            // Metadata info
            Row metaRow1 = sheet.createRow(2);
            metaRow1.createCell(0).setCellValue("Mã phiếu nhập: " + maPN);
            metaRow1.createCell(5).setCellValue("Ngày xuất file: " + sdf.format(new Date()));

            // Columns headers
            String[] headers = {
                "STT", "Mã nguyên liệu", "Tên nguyên liệu", "ĐVT", "Số lượng", "Đơn giá", "Thành tiền", "Nhà cung cấp", "Ngày nhập", "Ghi chú"
            };

            Row headerRow = sheet.createRow(4);
            headerRow.setHeightInPoints(28);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Fetch from database
            String sql = "SELECT MaNL, TenNL, DonViTinh, SoLuong, DonGia, ThanhTien, TenNCC, NgayNhap " +
                         "FROM VW_ChiTietPhieuNhap WHERE MaPN = ? ORDER BY MaCTPN ASC";

            double totalAmount = 0;
            int stt = 1;
            int rowIdx = 5;

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setString(1, maPN);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Row row = sheet.createRow(rowIdx++);
                        row.setHeightInPoints(20);

                        // STT
                        Cell cellStt = row.createCell(0);
                        cellStt.setCellValue(stt++);
                        cellStt.setCellStyle(cellStyleCenter);

                        // MaNL
                        Cell cellMa = row.createCell(1);
                        cellMa.setCellValue(rs.getString("MaNL"));
                        cellMa.setCellStyle(cellStyleCenter);

                        // TenNL
                        Cell cellTen = row.createCell(2);
                        cellTen.setCellValue(rs.getString("TenNL"));
                        cellTen.setCellStyle(cellStyleLeft);

                        // DVT
                        Cell cellDvt = row.createCell(3);
                        cellDvt.setCellValue(rs.getString("DonViTinh"));
                        cellDvt.setCellStyle(cellStyleCenter);

                        // SoLuong
                        Cell cellQty = row.createCell(4);
                        cellQty.setCellValue(rs.getInt("SoLuong"));
                        cellQty.setCellStyle(cellStyleCenter);

                        // DonGia
                        double donGia = rs.getDouble("DonGia");
                        Cell cellPrice = row.createCell(5);
                        cellPrice.setCellValue(donGia);
                        cellPrice.setCellStyle(cellStyleRight);

                        // ThanhTien
                        double thanhTien = rs.getDouble("ThanhTien");
                        Cell cellTotal = row.createCell(6);
                        cellTotal.setCellValue(thanhTien);
                        cellTotal.setCellStyle(cellStyleRight);
                        totalAmount += thanhTien;

                        // NhaCungCap
                        Cell cellNcc = row.createCell(7);
                        cellNcc.setCellValue(rs.getString("TenNCC"));
                        cellNcc.setCellStyle(cellStyleLeft);

                        // NgayNhap
                        java.sql.Date date = rs.getDate("NgayNhap");
                        Cell cellDate = row.createCell(8);
                        cellDate.setCellValue(date != null ? sdf.format(date) : "");
                        cellDate.setCellStyle(cellStyleCenter);

                        // GhiChu
                        Cell cellGc = row.createCell(9);
                        cellGc.setCellValue("Nhập kho thành công");
                        cellGc.setCellStyle(cellStyleLeft);
                    }
                }
            }

            // Write total row
            Row sumRow = sheet.createRow(rowIdx);
            sumRow.setHeightInPoints(22);
            Cell sumLabelCell = sumRow.createCell(0);
            sumLabelCell.setCellValue("TỔNG CỘNG");
            sumLabelCell.setCellStyle(totalLabelStyle);
            
            // Fill empty cells for total styling borders
            for (int i = 1; i <= 5; i++) {
                sumRow.createCell(i).setCellStyle(totalLabelStyle);
            }
            sheet.addMergedRegion(new CellRangeAddress(rowIdx, rowIdx, 0, 5));

            Cell totalCellVal = sumRow.createCell(6);
            totalCellVal.setCellValue(totalAmount);
            totalCellVal.setCellStyle(totalValueStyle);

            for (int i = 7; i <= 9; i++) {
                sumRow.createCell(i).setCellStyle(cellStyleLeft);
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Save to File
            try (FileOutputStream fos = new FileOutputStream(fileToSave)) {
                workbook.write(fos);
            }

            JOptionPane.showMessageDialog(parent, "Xuất file thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Lỗi khi xuất file Excel: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Xuất phiếu xuất kho ra file Excel (.xlsx)
     *
     * @param parent Khung giao diện gọi xuất file
     * @param maPX   Mã phiếu xuất cần xuất
     */
    public static void exportPhieuXuat(JFrame parent, String maPX) {
        if (maPX == null || maPX.trim().isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Mã phiếu xuất không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu Phiếu Xuất Excel");
        fileChooser.setSelectedFile(new File("PhieuXuat_" + maPX + ".xlsx"));

        int userSelection = fileChooser.showSaveDialog(parent);
        if (userSelection != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File fileToSave = fileChooser.getSelectedFile();
        if (!fileToSave.getName().toLowerCase().endsWith(".xlsx")) {
            fileToSave = new File(fileToSave.getParentFile(), fileToSave.getName() + ".xlsx");
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Phiếu Xuất " + maPX);

            // Create Fonts
            Font titleFont = workbook.createFont();
            titleFont.setFontName("Segoe UI");
            titleFont.setFontHeightInPoints((short) 16);
            titleFont.setBold(true);

            Font headerFont = workbook.createFont();
            headerFont.setFontName("Segoe UI");
            headerFont.setFontHeightInPoints((short) 11);
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());

            Font normalFont = workbook.createFont();
            normalFont.setFontName("Segoe UI");
            normalFont.setFontHeightInPoints((short) 11);

            Font boldFont = workbook.createFont();
            boldFont.setFontName("Segoe UI");
            boldFont.setFontHeightInPoints((short) 11);
            boldFont.setBold(true);

            // Create styles
            CellStyle titleStyle = workbook.createCellStyle();
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.LEFT);

            // Yellow/Orange Header Style (#FFC000)
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            
            XSSFCellStyle xssfHeaderStyle = (XSSFCellStyle) headerStyle;
            byte[] yellowColor = new byte[]{(byte) 0xFF, (byte) 0xC0, (byte) 0x00};
            xssfHeaderStyle.setFillForegroundColor(new XSSFColor(yellowColor, null));
            xssfHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // Change font color to black for yellow background to improve readability
            headerFont.setColor(IndexedColors.BLACK.getIndex());

            // Data Style Left
            CellStyle cellStyleLeft = workbook.createCellStyle();
            cellStyleLeft.setFont(normalFont);
            cellStyleLeft.setBorderTop(BorderStyle.THIN);
            cellStyleLeft.setBorderBottom(BorderStyle.THIN);
            cellStyleLeft.setBorderLeft(BorderStyle.THIN);
            cellStyleLeft.setBorderRight(BorderStyle.THIN);

            // Data Style Center
            CellStyle cellStyleCenter = workbook.createCellStyle();
            cellStyleCenter.setFont(normalFont);
            cellStyleCenter.setAlignment(HorizontalAlignment.CENTER);
            cellStyleCenter.setBorderTop(BorderStyle.THIN);
            cellStyleCenter.setBorderBottom(BorderStyle.THIN);
            cellStyleCenter.setBorderLeft(BorderStyle.THIN);
            cellStyleCenter.setBorderRight(BorderStyle.THIN);

            // Data Style Right
            CellStyle cellStyleRight = workbook.createCellStyle();
            cellStyleRight.setFont(normalFont);
            cellStyleRight.setAlignment(HorizontalAlignment.RIGHT);
            cellStyleRight.setBorderTop(BorderStyle.THIN);
            cellStyleRight.setBorderBottom(BorderStyle.THIN);
            cellStyleRight.setBorderLeft(BorderStyle.THIN);
            cellStyleRight.setBorderRight(BorderStyle.THIN);
            cellStyleRight.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));

            // 1. Write Title
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("HỆ THỐNG KHO JOLLIBEE - PHIẾU XUẤT KHO");
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));

            // Metadata info
            Row metaRow1 = sheet.createRow(2);
            metaRow1.createCell(0).setCellValue("Mã phiếu xuất: " + maPX);
            metaRow1.createCell(5).setCellValue("Ngày xuất file: " + sdf.format(new Date()));

            // Columns headers
            String[] headers = {
                "STT", "Mã nguyên liệu", "Tên nguyên liệu", "ĐVT", "Số lượng", "Kho xuất", "Người nhận", "Ngày xuất", "Ghi chú"
            };

            Row headerRow = sheet.createRow(4);
            headerRow.setHeightInPoints(28);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Fetch from database
            String sql = "SELECT MaNL, TenNL, DonViTinh, SoLuong, TenKho, TenNV, NgayXuat " +
                         "FROM VW_ChiTietPhieuXuat WHERE MaPX = ? ORDER BY MaCTPX ASC";

            int stt = 1;
            int rowIdx = 5;

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setString(1, maPX);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Row row = sheet.createRow(rowIdx++);
                        row.setHeightInPoints(20);

                        // STT
                        Cell cellStt = row.createCell(0);
                        cellStt.setCellValue(stt++);
                        cellStt.setCellStyle(cellStyleCenter);

                        // MaNL
                        Cell cellMa = row.createCell(1);
                        cellMa.setCellValue(rs.getString("MaNL"));
                        cellMa.setCellStyle(cellStyleCenter);

                        // TenNL
                        Cell cellTen = row.createCell(2);
                        cellTen.setCellValue(rs.getString("TenNL"));
                        cellTen.setCellStyle(cellStyleLeft);

                        // DVT
                        Cell cellDvt = row.createCell(3);
                        cellDvt.setCellValue(rs.getString("DonViTinh"));
                        cellDvt.setCellStyle(cellStyleCenter);

                        // SoLuong
                        Cell cellQty = row.createCell(4);
                        cellQty.setCellValue(rs.getInt("SoLuong"));
                        cellQty.setCellStyle(cellStyleCenter);

                        // Kho xuat
                        Cell cellKho = row.createCell(5);
                        cellKho.setCellValue(rs.getString("TenKho"));
                        cellKho.setCellStyle(cellStyleLeft);

                        // Nguoi nhan
                        Cell cellNhan = row.createCell(6);
                        cellNhan.setCellValue(rs.getString("TenNV"));
                        cellNhan.setCellStyle(cellStyleLeft);

                        // Ngay xuat
                        java.sql.Date date = rs.getDate("NgayXuat");
                        Cell cellDate = row.createCell(7);
                        cellDate.setCellValue(date != null ? sdf.format(date) : "");
                        cellDate.setCellStyle(cellStyleCenter);

                        // GhiChu
                        Cell cellGc = row.createCell(8);
                        cellGc.setCellValue("Xuất kho thành công");
                        cellGc.setCellStyle(cellStyleLeft);
                    }
                }
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Save to File
            try (FileOutputStream fos = new FileOutputStream(fileToSave)) {
                workbook.write(fos);
            }

            JOptionPane.showMessageDialog(parent, "Xuất file thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Lỗi khi xuất file Excel: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
