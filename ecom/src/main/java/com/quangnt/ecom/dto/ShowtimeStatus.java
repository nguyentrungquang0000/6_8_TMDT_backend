package com.quangnt.ecom.dto;

public enum ShowtimeStatus {
    COMING_SOON, //Suất chiếu đã tạo nhưng chưa mở bán vé.
    UPCOMING,   // Suất chiếu đã lên lịch và đang mở bán vé (Thay cho SCHEDULED)
    ENDED,      // Suất chiếu đã kết thúc hoàn toàn
    CANCELLED   // Suất chiếu bị hủy bỏ (Do sự cố kỹ thuật, rạp trống khách...)
}
