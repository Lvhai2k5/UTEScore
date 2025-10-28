package vn.ute.utescore.service;

import vn.ute.utescore.model.ThanhToan;

public interface CustomerEmailService {
	void sendBookingReminder(ThanhToan thanhToan);
    void sendUpcomingBookingReminders();
}
