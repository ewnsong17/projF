package proj.shared.model.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorRes {
    public String code;
    public String message;
}
