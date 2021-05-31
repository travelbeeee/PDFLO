package travelbeeee.PDFLO_V20.domain.dto;

import lombok.Getter;
import lombok.Setter;
import travelbeeee.PDFLO_V20.domain.entity.PointHistory;
import travelbeeee.PDFLO_V20.domain.enumType.PointType;

@Setter
@Getter
public class PointHistoryDto {
    private Integer amount;
    private PointType type;

    public PointHistoryDto(PointHistory ph) {
        this.amount = ph.getAmount();
        this.type = ph.getType();
    }
}
