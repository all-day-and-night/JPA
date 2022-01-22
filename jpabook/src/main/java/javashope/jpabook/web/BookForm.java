package javashope.jpabook.web;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class BookForm {

    private Long id;

    @NotEmpty(message="물건 이름은 필수 입니다")
    private String name;
    @NotNull(message="물건 가격은 필수 입니다")
    private Integer price;
    @NotNull(message="물건 수량은 필수 입니다")
    private Integer stockQuantity;

    private String author;
    private String isbn;
}
