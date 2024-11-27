# Library Management System

# Giới thiệu

+ Thành viên :
  + Nguyễn Lê Minh - MSV : 23020114
  + Nguyễn Tùng Lâm - MSV : 23020096
  + Đỗ Đình Nam - MSV : 23020120
+ Bài tập lớn : Phát triển phần mềm quản lý thư viện.

# Mô tả

+ Ứng dụng quản lý thư viện được cung cấp UI thuận tiện, giúp thực hiện các tác vụ như : quản lý mượn/trả sách, mượn sách từ thư viện...
+ Ứng dụng được chia làm 2 phần :
  + Một phần cung cấp giao diện cho người dùng, giúp thực hiện được các tác vụ như mượn, trả sách, quản lý những sách đang mượn, thống kê...
  + Phần còn lại cung cấp giao diện cho người quản lý thư viện, giúp thực hiện việc quản lý mượn trả của người dùng, thống kê, kiêm tra những người dùng đang online,...

# Các chức năng chính của ứng dụng

+ Về phần người dùng :
  + Người dùng được phép mượn sách, xem thông tin của sách, đánh giá số điểm cho sách.
  + Người dùng có thể xem được thống kê số lượng sách đã đọc xong và số sách đã mượn..
  + Người dùng có thể cập nhật ảnh đại diện, cập nhật thông tin cá nhân.  
  + Người dùng có thể tìm kiếm sách yêu thích dựa theo tên sách, thể loại một cách nhanh chóng.
  + Người dùng thanh toán qua giỏ hàng bao gồm những sách hiện muốn thuê.
+ Về phần người quản lý :
  + Người quản lý có thể duyệt yêu cầu mượn sách của người dùng (chấp nhận hoặc từ chối), kiểm tra những quyển sách đang được mượn và đã được trả, kiểm tra những yêu cầu hiện có.
  + Người quản lý có thể kiểm tra thông tin người dùng (như tên, tuổi, đang online hay offline...)
  + Các tính năng đều được tích hợp thanh tìm kiếm thuận tiện cho việc sử dụng.
+ Các thuật toán tìm kiếm có thể giúp người dùng / người quản lý tìm ra thông tin mình muốn mà không cần nhớ chính xác thông tin đó.
+ Người dùng có thể tương tác trực tiếp với người quản lý trên thời gian thực.

# Các kỹ thuật sử dụng

+ Các kĩ thuật cơ bản 
  + Sử dụng các khái niệm của OOP (Inheritance, Abstraction, Polymorphism, Encapsulation), các nguyên lý trong lập trình hướng đối tượng (SOLID, ...)
  + Sử dụng các mẫu Design Pattern (Singleton, Factory)
  + Thiết kế giao diện người dùng bằng JavaFX và CSS.
  + Code dựa theo nguyên tắc Coding convention.
  
+ Các kĩ thuật nâng cao
  + Sử dụng hệ quản trị cơ sở dữ liệu để lưu trữ thông tin của người dùng, sách mượn, trả... bằng MySQL. Sử dụng JDBC để có thể tương tác với MySQL thông qua Java.
  + Sử dụng API (ở đây dùng Google Book API) để có thể fetching thông tin sách, người dùng chỉ cần nhập vào ISBN và mọi thông tin sẽ được lấy về.
  + Lưu các dữ liệu đặc biệt như ảnh bìa sách, ảnh người dùng trong cơ sở dữ liệu thuận tiện, không cần lưu trữ trong máy cá nhân bằng cách fetch thumbnail của sách sau đó chuyển sang byte code để lưu trong dữ liệu.
  + Sử dụng Multithreading cho mọi Task cần giao tiếp với hệ cơ sở dữ liệu hoặc giao tiếp thông qua API giúp cải thiện trải nghiệm người dùng, giúp ứng dụng chạy mượt mà mà không bị đơ hay trễ.
+ Kĩ thuật đặc biệt
  + Lưu trữ hệ quản trị dữ liệu trên Cloud, được host bằng dịch vụ của AWS giúp người dùng và người quản lý có thể tương tác trực tiếp với nhau trong thời gian thực.
  + Lưu trữ trên Cloud giúp người dùng thuận tiện hơn trong việc kết nối thông tin đến người quản lý.
  + Với việc lưu trữ hệ cơ sở dữ liệu trên Cloud làm việc xử lý đa luồng càng trở nên ý nghĩa vì việc kết nối với database trên Cloud chắc chắn sẽ tốn thời gian hơn việc tương tác trên localhost. 


