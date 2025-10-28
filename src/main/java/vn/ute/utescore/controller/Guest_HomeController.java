package vn.ute.utescore.controller;

import vn.ute.utescore.dto.Guest_PitchDTO;  // ✅ import DTO
import vn.ute.utescore.service.Guest_PitchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class Guest_HomeController {

    @Autowired
    private Guest_PitchService pitchService; // interface

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        // ✅ đổi kiểu về PitchDTO
        List<Guest_PitchDTO> featuredPitches = pitchService.getTop3Featured();
        model.addAttribute("featuredPitches", featuredPitches);
        return "guest/index"; // gọi index.html
    }
}
