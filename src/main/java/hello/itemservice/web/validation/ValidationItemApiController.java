package hello.itemservice.web.validation;

import hello.itemservice.web.validation.request.ItemSaveRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/validation/api/items")
public class ValidationItemApiController {

  @PostMapping("/add")
  public Object addItem(@RequestBody @Validated ItemSaveRequest request, BindingResult bindingResult) {

    log.info("call api controller");

    if (bindingResult.hasErrors()) {
      log.info("validation errors occurred = {}", bindingResult);
      return bindingResult.getAllErrors();
    }

    log.info("successful logic ...");
    return request;

  }

}
