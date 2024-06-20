package hello.itemservice.web.validation.v4;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.SaveCheck;
import hello.itemservice.domain.item.UpdateCheck;
import hello.itemservice.web.validation.request.ItemSaveRequest;
import hello.itemservice.web.validation.request.ItemUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/validation/v4/items")
@RequiredArgsConstructor
public class ValidationItemControllerV4 {

  private final ItemRepository itemRepository;

  /*

    Bean Validator 오류 검증기와 중복되므로 필요 x

    private final ItemValidator itemValidator;

    @InitBinder
    public void init(WebDataBinder dataBinder) {
      log.info("init binder {}", dataBinder);
      dataBinder.addValidators(itemValidator);
    }
  */

  @GetMapping
  public String items(Model model) {
    List<Item> items = itemRepository.findAll();
    model.addAttribute("items", items);
    return "validation/v4/items";
  }

  @GetMapping("/{itemId}")
  public String item(@PathVariable long itemId, Model model) {
    Item item = itemRepository.findById(itemId);
    model.addAttribute("item", item);
    return "validation/v4/item";
  }

  @GetMapping("/add")
  public String addForm(Model model) {
    model.addAttribute("item", new Item());
    return "validation/v4/addForm";
  }

  @PostMapping("/add")
  public String addItem(@Validated @ModelAttribute("item") ItemSaveRequest form, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

    /**
     * @ModelAttribute("item")
     */

    // 특정 필드 예외가 아닌 전체 예외
    if (form.getPrice() != null && form.getQuantity() != null) {
      int resultPrice = form.getPrice() * form.getQuantity();
      if (resultPrice < 10000) {
        // MessageSource 파일에 저장된 메시지를 사용
        bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
      }
    }

    if (bindingResult.hasErrors()) {
      log.info("errors={}", bindingResult);
      return "validation/v4/addForm";
    }

    // 성공 로직
    Item item = new Item();
    item.setItemName(form.getItemName());
    item.setPrice(form.getPrice());
    item.setQuantity(form.getQuantity());
    Item savedItem = itemRepository.save(item);
    redirectAttributes.addAttribute("itemId", savedItem.getId());
    redirectAttributes.addAttribute("status", true);
    return "redirect:/validation/v4/items/{itemId}";

  }

  @GetMapping("/{itemId}/edit")
  public String editForm(@PathVariable Long itemId, Model model) {
    Item item = itemRepository.findById(itemId);
    model.addAttribute("item", item);
    return "validation/v4/editForm";
  }

  @PostMapping("/{itemId}/edit")
  public String edit(@PathVariable Long itemId, @Validated @ModelAttribute("item") ItemUpdateRequest form, BindingResult bindingResult) {

    // 특정 필드 예외가 아닌 전체 예외
    if (form.getPrice() != null && form.getQuantity() != null) {
      int resultPrice = form.getPrice() * form.getQuantity();
      if (resultPrice < 10000) {
        bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
      }
    }

    if (bindingResult.hasErrors()) {
      log.info("errors={}", bindingResult);
      return "validation/v4/editForm";
    }

    Item itemParam = new Item();
    itemParam.setItemName(form.getItemName());
    itemParam.setPrice(form.getPrice());
    itemParam.setQuantity(form.getQuantity());
    itemRepository.update(itemId, itemParam);
    return "redirect:/validation/v4/items/{itemId}";

  }

}

