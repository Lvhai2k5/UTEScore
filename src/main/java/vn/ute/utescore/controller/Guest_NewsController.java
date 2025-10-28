//package vn.ute.utescore.controller;
//
//import vn.ute.utescore.model.TinTuc;
//import vn.ute.utescore.service.Guest_NewsService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//
//import java.util.*;
//
//@Controller
//public class Guest_NewsController {
//
//    @Autowired
//    private Guest_NewsService newsService;
//
//    @GetMapping("/news")
//    public String listNews(Model model) {
//
//        List<TinTuc> list = newsService.getTinTucMoi();
//
//        List<Map<String, Object>> newsList = list.stream().map(t -> {
//            Map<String, Object> m = new HashMap<>();
//            m.put("id", t.getTinID());
//            m.put("title", t.getTieuDe());
//            m.put("summary", t.getNoiDung());
//            m.put("date", t.getNgayDang());
//            m.put("image", (t.getHinhAnh() != null && !t.getHinhAnh().isEmpty())
//                    ? t.getHinhAnh()
//                    : "/images/news-default.jpg");
//            return m;
//        }).toList();
//
//        model.addAttribute("newsList", newsList); // ✅ Đúng tên dùng trong View
//        return "guest/news";
//    }
//
//    @GetMapping("/news/{id}") // ✅ sửa route
//    public String detailNews(@PathVariable Integer id, Model model) {
//        TinTuc t = newsService.getTinTucById(id);
//
//        if (t == null) return "redirect:/news";
//
//        model.addAttribute("title", t.getTieuDe());
//        model.addAttribute("date", t.getNgayDang());
//        model.addAttribute("content", t.getNoiDung());
//        model.addAttribute("image", (t.getHinhAnh() != null && !t.getHinhAnh().isEmpty())
//                ? t.getHinhAnh()
//                : "/images/news-default.jpg");
//
//        return "guest/news-detail";
//    }
//}


package vn.ute.utescore.controller;

import vn.ute.utescore.model.TinTuc;
import vn.ute.utescore.service.Guest_NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/news")   // ✅ THÊM DÒNG NÀY !!!
public class Guest_NewsController {

    @Autowired
    private Guest_NewsService newsService;

    @GetMapping
    public String listNews(Model model) {

        List<TinTuc> list = newsService.getTinTucMoi();

        List<Map<String, Object>> newsList = list.stream().map(t -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", t.getTinID());
            m.put("title", t.getTieuDe());
            m.put("summary", t.getNoiDung());
            m.put("date", t.getNgayDang());
            m.put("image", (t.getHinhAnh() != null && !t.getHinhAnh().isEmpty())
                    ? t.getHinhAnh()
                    : "/images/news-default.jpg");
            return m;
        }).toList();

        model.addAttribute("newsList", newsList);
        return "guest/news";
    }

    @GetMapping("/{id}")   // ✅ lúc này route đúng là /news/{id}
    public String detailNews(@PathVariable Integer id, Model model) {
        TinTuc t = newsService.getTinTucById(id);

        if (t == null) return "redirect:/news";

        model.addAttribute("title", t.getTieuDe());
        model.addAttribute("date", t.getNgayDang());
        model.addAttribute("content", t.getNoiDung());
        model.addAttribute("image", (t.getHinhAnh() != null && !t.getHinhAnh().isEmpty())
                ? t.getHinhAnh()
                : "/images/news-default.jpg");

        return "guest/news-detail";
    }
}

