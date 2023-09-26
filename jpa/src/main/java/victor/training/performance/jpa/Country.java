package victor.training.performance.jpa;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Data
@Entity
//@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Country {
   @Id
   private Long id;
   private String name;
   @ManyToOne(cascade = CascadeType.PERSIST) // for easier testing
   private CountryRegion region;
   private String iso2Code;
   private String iso3Code;
   private String continent;

   private Country() {
//      Runtime.getRuntime().addShutdownHook(new Thread() {
//         @Override
//         public void run() {
//            System.out.println("clean some stuff");
//         }
//      });
   }

   public Country(Long id, String name) {
      this.id = id;
      this.name = name;
   }
   public Country(String name) {
      this.name = name;
   }

   public Long getId() {
      return id;
   }

   public String getName() {
      return name;
   }
}
