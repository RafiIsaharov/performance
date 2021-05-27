package victor.training.performance.batch.sync;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "person")
public class PersonXml {
    private String name;
    private String city;
}
