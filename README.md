# FU Car Renting System

Hệ thống cho thuê xe ô tô hiện đại được xây dựng theo kiến trúc Microservices sử dụng Spring Boot, Spring Cloud và Docker.

## 🏗️ Tổng quan kiến trúc

Dự án này triển khai theo kiến trúc microservices. Nó bao gồm nhiều dịch vụ độc lập giao tiếp với nhau. Các nghiệp vụ cốt lõi được chia thành các domain riêng biệt, trong khi các vấn đề chung như định tuyến (routing) và khám phá dịch vụ (service discovery) được xử lý bởi các dịch vụ hạ tầng (infrastructure).

### Các dịch vụ (Services):

*   **infrastructure/eureka-server (Cổng: 8761)**: Máy chủ Netflix Eureka dùng để đăng ký và khám phá các dịch vụ.
*   **infrastructure/api-gateway (Cổng: 8080)**: Spring Cloud Gateway đóng vai trò định tuyến các yêu cầu từ bên ngoài đến đúng microservice bên trong.
*   **service/customer-service**: Quản lý hồ sơ và dữ liệu khách hàng.
*   **service/car-service**: Quản lý danh sách các xe ô tô cho thuê.
*   **service/renting-service**: Dịch vụ nghiệp vụ cốt lõi xử lý việc thuê xe và triển khai Saga pattern đóng vai trò là Orchestrator.

### Công nghệ sử dụng:
*   **Java 21**
*   **Spring Boot 3.2.3**
*   **Spring Cloud 2023.0.1**
*   **PostgreSQL**: Hệ quản trị cơ sở dữ liệu quan hệ cho tất cả các dịch vụ.
*   **Apache Kafka & Zookeeper**: Message broker cho giao tiếp bất đồng bộ và điều phối Saga pattern.
*   **Docker & Docker Compose**: Đóng gói container và triển khai.

## 🚀 Hướng dẫn cài đặt và chạy dự án

### Yêu cầu hệ thống 

*   Đã cài đặt [Docker](https://docs.docker.com/get-docker/) và [Docker Compose](https://docs.docker.com/compose/install/).
*   [Java 21](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) và [Maven](https://maven.apache.org/download.cgi) (nếu bạn muốn build/chạy bên ngoài Docker).

### Chạy dự án bằng Docker Compose

Cách đơn giản nhất để khởi chạy toàn bộ hệ thống là sử dụng Docker Compose. Lệnh này sẽ tự động khởi tạo cơ sở dữ liệu, message broker, các dịch vụ hạ tầng và các microservices nghiệp vụ.

1.  Đảm bảo Docker daemon đang hoạt động trên máy của bạn.
2.  Mở terminal tại thư mục gốc của dự án.
3.  Chạy lệnh sau:

    ```bash
    docker-compose up -d --build
    ```

4.  Đợi các dịch vụ khởi động hoàn tất. Bạn có thể xem log bằng lệnh `docker-compose logs -f`.

### Truy cập các dịch vụ

Sau khi các container đã chạy thành công, bạn có thể truy cập thông qua các địa chỉ sau:

*   **API Gateway**: `http://localhost:8080` (Sử dụng cổng này cho mọi yêu cầu API)
*   **Eureka Dashboard**: `http://localhost:8761` (Xem danh sách các microservices đã đăng ký)
*   **pgAdmin**: `http://localhost:5050`
    *   **Email đăng nhập:** `admin@example.com`
    *   **Mật khẩu:** `admin`

## 🛑 Dừng ứng dụng

Để dừng và xóa tất cả các container, networks và volumes (tùy chọn) đang chạy:

```bash
# Chỉ dừng các container
docker-compose stop

# Dừng và xóa các container và networks
docker-compose down

# Dừng và xóa các container, networks, và volumes (Cảnh báo: Lệnh này sẽ xóa toàn bộ dữ liệu trong database của bạn!)
docker-compose down -v
```

## 📁 Cấu trúc thư mục

```
fu_car_renting_system/
├── infrastructure/       # Các microservices hạ tầng
│   ├── api-gateway/
│   └── eureka-server/
├── service/              # Các microservices nghiệp vụ
│   ├── car-service/
│   ├── customer-service/
│   └── renting-service/
├── docker-compose.yml    # Cấu hình Docker compose
├── init-db.sql           # Script khởi tạo cơ sở dữ liệu
└── pom.xml               # File Maven POM gốc
```
