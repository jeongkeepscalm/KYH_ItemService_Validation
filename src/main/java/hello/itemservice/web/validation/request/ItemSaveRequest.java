package hello.itemservice.web.validation.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ItemSaveRequest {

  @NotBlank(message = "상품명을 입력해주세요(빈 값 허용 x)_ItemSaveRequest")
  private String itemName;

  @NotNull(message = "가격을 입력해주세요._ItemSaveRequest")
  @Range(min = 1_000, max = 1_000_000, message = "가격을 {min} ~ {max} 사이로 설정해주세요._ItemSaveRequest")
  private Integer price;

  @NotNull(message = "수량을 입력해주세요._ItemSaveRequest")
  @Max(value = 9_999, message = "최대 수량은 {value} 입니다._ItemSaveRequest")
  private Integer quantity;
  
}
