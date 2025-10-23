package vn.ute.utescore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EmployeeController {
    @GetMapping("/")
    public String home() {
        return "employee/employee"; 
    }

    @GetMapping("/employee")
    public String employee() {
        return "employee/employee";
    }
    
    @GetMapping("/employee/booking")
    public String booking() {
        return "employee/booking";
    }
    
    @GetMapping("/employee/checkin")
    public String checkin() {
        return "employee/checkin";
    }
    
    @GetMapping("/employee/report")
    public String report() {
        return "employee/report";
    }
}
