package hello.itemservice.domain.item;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ScriptAssert(lang = "javascript", script = "_this.price * _this.quantity >= 10000")
public class Item {

  @NotNull(groups = {UpdateCheck.class}) // 수정할 경우에만 적용
  private Long id;

  @NotBlank(message = "공백 불가능(어노테이션 메시지 사용)"
          , groups = {SaveCheck.class, UpdateCheck.class}) // 빈값 && 공백 허용 x
  private String itemName;

  @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
  @Range(min = 1_000, max = 1_000_000, groups = {SaveCheck.class, UpdateCheck.class})
  private Integer price;

  @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
  @Max(value = 9_999, groups = {SaveCheck.class}) // 등록할 경우에만 적용
  private Integer quantity;

  public Item() {}

  public Item(String itemName, Integer price, Integer quantity) {
    this.itemName = itemName;
    this.price = price;
    this.quantity = quantity;
  }

}
