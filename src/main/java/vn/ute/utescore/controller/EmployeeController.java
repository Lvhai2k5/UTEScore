package vn.ute.utescore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.ute.utescore.repository.DonDatSanRepository;

@Controller
public class EmployeeController {

    @Autowired
    private DonDatSanRepository donDatSanRepository;

    @GetMapping("/")              		  public String homepage()  { return "employee/employee"; }
    @GetMapping("/employee")              public String dashboard()  { return "employee/employee"; }
    @GetMapping("/employee/booking")      public String booking()    { return "employee/booking"; }
    @GetMapping("/employee/checkin")      public String checkin()    { return "employee/checkin"; }
    @GetMapping("/employee/payment")      public String payment()    { return "employee/payment"; }
    @GetMapping("/employee/customers")    public String customers()  { return "employee/customers"; }
    @GetMapping("/employee/schedule")     public String schedule()   { return "employee/schedule"; }
    @GetMapping("/employee/incidents")    public String incidents()  { return "employee/incidents"; }
    @GetMapping("/employee/notifications")public String notifications(){ return "employee/notifications"; }
    @GetMapping("/employee/report")       public String report()     { return "employee/report"; }
    @GetMapping("/employee/shifts")       public String shifts()     { return "employee/shifts"; }
    @GetMapping("/employee/profile")      public String profile()    { return "employee/profile"; }

}
