package vn.ute.utescore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.ute.utescore.model.ThueSan;
import vn.ute.utescore.repository.ThueSanRepository;
import vn.ute.utescore.repository.SanBongRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Controller
public class ScheduleController {

   
    private ThueSanRepository thueSanRepository;
    private SanBongRepository sanBongRepository;
    
    public ScheduleController(ThueSanRepository thueSanRepository,SanBongRepository sanBongRepository)
    {
    	this.thueSanRepository=thueSanRepository;
    	this.sanBongRepository=sanBongRepository;
    }

    // 📅 Chọn 1 ngày và sân để lọc lại danh sách khung giờ
    @GetMapping("/employee/schedule")
    public String showSchedule(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,
            @RequestParam(required = false) Integer sanId,
            Model model) {

        // Mặc định là hôm nay
        if (date == null) {
            date = LocalDate.now();
        }

        // Danh sách sân cho combobox
        model.addAttribute("sanList", sanBongRepository.findAll());
        model.addAttribute("selectedDate", date);

        // Nếu chưa chọn sân thì chỉ hiển thị form
        if (sanId == null) {
            model.addAttribute("slots", Collections.emptyList());
            return "employee/schedule";
        }

        // Lấy các đơn đặt của sân trong ngày đã chọn
        List<ThueSan> bookedList = thueSanRepository.findBySanBong_MaSanAndNgayThueBetween(
                sanId,
                date.atStartOfDay(),
                date.atTime(23, 59, 59)
        );

        // Tạo danh sách khung giờ từ 6h - 22h
        List<Map<String, Object>> slots = new ArrayList<>();
        for (int hour = 6; hour < 22; hour++) {
            LocalTime start = LocalTime.of(hour, 0);
            LocalTime end = LocalTime.of(hour + 1, 0);

            boolean booked = bookedList.stream().anyMatch(ts ->
                    !start.isAfter(ts.getKhungGioKetThuc()) &&
                    !end.isBefore(ts.getKhungGioBatDau())
            );

            Map<String, Object> slot = new HashMap<>();
            slot.put("time", String.format("%02d:00 - %02d:00", hour, hour + 1));
            slot.put("status", booked ? "Đã đặt" : "Trống");
            slots.add(slot);
        }

        model.addAttribute("slots", slots);
        model.addAttribute("selectedSan", sanBongRepository.findById(sanId).orElse(null));
        return "employee/schedule";
    }
}
