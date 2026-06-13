
# JOLLIBEE — Phạm Ngọc Thạch
### Hệ Thống Quản Lý Kho Nguyên Liệu

> **Bài tập lớn môn Lập Trình Hướng Đối Tượng**
>
> Giảng viên hướng dẫn: **ThS. Nguyễn Dương Hùng**

Phần mềm quản lý kho nguyên liệu nội bộ dành cho cửa hàng **Jollibee Phạm Ngọc Thạch**

Xây dựng bằng **Java** + **Java Swing** + **SQL Server**

</div>

---

## Mục lục

- [Giới thiệu](#giới-thiệu)
- [Thành viên nhóm](#thành-viên-nhóm)
- [Chức năng chính](#chức-năng-chính)
- [Công nghệ sử dụng](#công-nghệ-sử-dụng)
- [Website tham khảo](#website-tham-khảo)

---

## Giới thiệu

Hệ thống quản lý kho nguyên liệu giúp cửa hàng "Jollibee Phạm Ngọc Thạch" theo dõi toàn bộ quy trình nhập - xuất - tồn kho nguyên liệu. Phần mềm hỗ trợ phân quyền người dùng, cảnh báo hạn sử dụng và xuất báo cáo hao hụt theo kỳ.

| Thông tin | Chi tiết |
|---|---|
| Cơ sở | Jollibee Phạm Ngọc Thạch |
| Loại ứng dụng | Desktop Application |
| Cơ sở dữ liệu | SQL Server |
| Môn học | Lập Trình Hướng Đối Tượng |
| Giảng viên | ThS. Nguyễn Dương Hùng |

---

## Thành viên nhóm 4

| STT | Họ và tên |
|:---:|---|
| 1 | Nguyễn Khánh Vân |
| 2 | Ngọ Kim Ngân |
| 3 | Trần Thị Mỹ Duyên |
| 4 | Nguyễn Huyền Linh |
| 5 | Nguyễn Thị Minh Thu |

---

## Chức năng chính

- **Tổng quan kho** - Dashboard hiển thị số liệu tổng hợp: tồn kho, nhập/xuất hôm nay, cảnh báo sắp hết hàng
- **Quản lý Nguyên Liệu** - Thêm, sửa, xoá, tìm kiếm nguyên liệu; phân loại theo danh mục
- **Quản lý Nhập Kho** - Tạo phiếu nhập, chọn nhà cung cấp, cập nhật số lượng tồn kho tự động
- **Quản lý Xuất Kho** - Tạo phiếu xuất theo ca/ngày, trừ tồn kho theo thời gian thực
- **Kiểm kê & Hạn sử dụng** - Cảnh báo nguyên liệu sắp hết hạn, hỗ trợ kiểm kê định kỳ
- **Quản lý Nhà Cung Cấp** - Thông tin nhà cung cấp, liên kết với phiếu nhập
- **Danh mục Nhà Kho** - Quản lý vị trí, khu vực lưu trữ trong kho
- **Quản lý Nhân Viên** - Phân quyền theo vai trò (Quản lý / Nhân viên kho)
- **Báo cáo Hao Hụt** - Thống kê hao hụt theo kỳ, xuất báo cáo file `.xls` và `.csv`

---

## Công nghệ sử dụng

| Công nghệ | Mô tả |
|---|---|
| Java | Ngôn ngữ lập trình chính |
| Java Swing | Xây dựng giao diện desktop |
| SQL Server | Cơ sở dữ liệu |
| JDBC | Kết nối Java ↔ SQL Server |
| Microsoft JDBC Driver | Driver kết nối SQL Server |
| FileOutputStream + OutputStreamWriter | Xuất file báo cáo Excel/CSV (Java I/O thuần) |

---

## Website tham khảo

- [docs.oracle.com/javase/tutorial/uiswing](https://docs.oracle.com/javase/tutorial/uiswing/)
- [learn.microsoft.com/sql/sql-server](https://learn.microsoft.com/en-us/sql/sql-server/)
- [docs.oracle.com/javase/tutorial/jdbc](https://docs.oracle.com/javase/tutorial/jdbc/)

---

<div align="center">

*Jollibee Phạm Ngọc Thạch — Hệ thống quản lý kho nguyên liệu*

</div>

