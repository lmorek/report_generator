package csv;

import lombok.Data;

import java.io.Serializable;
@Data
public class FileName {
    public String impactRelease;
    public String environment;
    public String executionDate;
    public String threads;
}
