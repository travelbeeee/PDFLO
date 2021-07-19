package travelbeeee.PDFLO.domain.model.dto;

import lombok.Getter;
import lombok.Setter;
import travelbeeee.PDFLO.domain.model.entity.PointHistory;
import travelbeeee.PDFLO.domain.model.enumType.PointType;

import java.time.format.DateTimeFormatter;

@Setter
@Getter
public class PointHistoryDto {
    private Integer amount;
    private PointType type;
    private String createdDate;

    public PointHistoryDto(PointHistory ph) {
        this.amount = ph.getAmount();
        this.type = ph.getType();
        this.createdDate = ph.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy년 M월 d일 h시 m분"));
    }
}
