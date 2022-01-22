package javashope.jpabook.web;


import javashope.jpabook.domain.item.Book;
import javashope.jpabook.domain.item.Item;
import javashope.jpabook.service.ItemService;
import jdk.jfr.Registered;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model){
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("items/new")
    public String create(@Valid @ModelAttribute("form") BookForm form, BindingResult result){
        if(result.hasErrors()){
            return "items/createItemForm";
        }

        Book book = new Book();
        /**
         * 여기서는 setter를 사용해서 객체를 저장했지만
         * 좋은 코드는 하나로 묶어서 생성자를 만드는 것이다.
         */
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);
        return "redirect:/items";
    }

    @GetMapping(value = "/items")
    public String list(Model model){
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }


}
