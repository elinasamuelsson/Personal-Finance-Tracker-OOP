/*

* Personal Finance Tracker program using OOP principles:
* - Lägga in transaktioner (manuellt; när du exempelvis har köpt något eller fått lön)
* - Radera transaktioner (manuellt)
* - Se nuvarande kontobalans
* - Se pengar spenderade årsvis, månadsvis, veckovis och dagvis
* - Se inkomst årsvis, månadsvis, veckovis och dagvis
*
* Krav för G:
* Använd git för versionshantering
* Formatera koden

* Krav för VG:
* Bygg hela applikationen på ett objekt-orienterat sätt, alla delar som kan och bör implementeras med OOP, skall implementeras med OOP

För VG skall även all information sparas till fil, nu på ett OOP sätt. Om du redan har implementerat filsparning ska systemet redesignas på ett OOP sätt. Exempelvis kan du skapa ett interface för det:

```
public interface TransactionSaver {
   void saveTransaction(Transaction transaction);
   void deleteTransaction(int id);
   // Och så vidare...
}
```

*/
package examinationsprojekt;

import examinationsprojekt.managers.ICommandManager;
import examinationsprojekt.managers.TerminalCommandManager;

public class Main {
    public static void main(String[] args) {
        ICommandManager manager = new TerminalCommandManager();
        manager.run();
    }
}
