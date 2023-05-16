package victor.training.performance.concurrency;

import victor.training.spring.batch.util.PerformanceUtil;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExternalDependencyFake implements ExternalDependency {
   private final int total;

   public ExternalDependencyFake(int total) {
      this.total = total;
   }

   private boolean overlappingEmails = false;
   private boolean checkingEmails = false;

   public ExternalDependencyFake setHalfOverlappingEmails() {
      this.overlappingEmails = true;
      return this;
   }

   public ExternalDependencyFake setHalfInvalid() {
      this.checkingEmails = true;
      return this;
   }

   public String retrieveEmail(int i) {
      int emailId = i;
      if (overlappingEmails) {
         emailId %= total / 2;
      }
      return "email" + emailId + "@example.com";
   }


   private final AtomicInteger emailChecksCounter = new AtomicInteger(0);
   private static final Pattern emailPattern = Pattern.compile("email(\\d+)@example.com");

   @Override
   public boolean isEmailValid(String email) {
      Matcher matcher = emailPattern.matcher(email);
      if (!matcher.matches()) throw new IllegalArgumentException();
      int number = Integer.parseInt(matcher.group(1));

      emailChecksCounter.incrementAndGet();
      PerformanceUtil.sleepSomeTime(0, 1);
      if (!checkingEmails) return true;
      else return number % 2 == 0;
   }
   public int getNumberOfEmailChecksPerformed() {
      return emailChecksCounter.get();
   }
}
