# Flight Reservation Management System

## Περιγραφή

Υλοποίηση μιας Restful Web εφαρμογής που προσφέρει υπηρεσίες διαχείρισης κρατήσεων στην αεροπορική εταιρεία <span style="color:cyan"> «Small Planet Airways»</span>. Η ανάπτυξη λογισμικού θα γίνει με χρήση της γλώσσας προγραμματισμού Java και θα γίνεται σύνδεση με μία βάση δεδομένων (type: <span style="color:red">H2</span>). Πιο συγκεκριμένα, παρέχει ολοκληρωμένες διαδικασίες σχετικά με:

* Καταχώρηση Αεροδρομίου από το σύστημα
* Δημιουργία Λογαριασμού Χρήστη (αεροπορική ή χρήστης)
* Επεξεργασία Στοιχείων Λογαριασμού από την αεροπορική ή το χρήστη
* Καταχώρηση Πτήσης από την αεροπορική
* Αναζήτηση Πτήσεων από τον χρήστη (από ένα αεροδρόμιο σε κάποιο άλλο για συγκεκριμένες ημερομηνίες)
* Δημιουργία Κράτησης από τον χρήστη βάση αποτελέσματος αναζήτησης
* Προβολή Στοιχείων Κράτησης από τον χρήστη
* Ακύρωση Κράτησης ή μέρος αυτής από τον χρήστη
* Προβολή Στατιστικών Στοιχείων για την κάθε αεροπορική σχετικά με τις πτήσεις της (μέση πληρότητα όλων των πτήσεων, μέση πληρότητα κάθε πτήσης,δημοφιλές αεροδρόμιο)

## Απαιτήσεις

Μελετώντας την εφαρμογή εντοπίζουμε δύο διακριτούς <span style="color:blueviolet; font-weight:bold;">actors</span>. Παρακάτω αναφέρονται οι απαιτήσεις του καθένα:

* Το σύστημα έχει τη δυνατότητα να προοσθέσει/διαγράψει στοιχεία των αεροδρομίων,επιβατών,αεροπορικών εταιρειών. Σε περίπτωση που τα στοιχεία υπάρχουν ήδη στο σύστημα εμφανίζεται αντίστοιχο μήνυμα.
* Το κάθε αεροδρόμιο χαρακτηρίζεται από *όνομα*, *πόλη*, *χώρα* και τον μοναδικό *3-digit κωδικό*.
* Η κάθε **αεροπορική** χαρακτηρίζεται από την *ονομασία* της, τον *2-digit κωδικό* της, *username*, *password* και μια *λίστα με τις πτήσεις της*.
* Η **αεροπορική** μπορεί να επεξεργαστεί και να ανανεώσει το *password* της.
* Η **αεροπορική** καταχωρεί τα στοιχεία των πτήσεων στο σύστημα. Σε περίπτωση που η πτήση υπάρχει ήδη στο σύστημα εμφανίζεται αντίστοιχο μήνυμα.
* Η **αεροπορική** μπορεί να λαμβάνει *στατιστικά στοιχεία* για τις πτήσεις της όπως μέση πληρότητα όλων των πτήσεων, μέση πληρότητα κάθε πτήσης, δημοφιλές αεροδρόμιο.
* Η κάθε πτήση χαρακτηρίζεται από *κόστος απλού εισιτηρίου*, *αεροδρόμιο αναχώρησης*, *ημερομηνία & ώρα αναχώρησης*, *ημερομηνία & ώρα άφιξης*, *αεροδρόμιο άφιξης*, *χωρητικότητα αεροσκάφους*, *τύπος αεροσκάφους*, *αεροπορική εταιρεία*, *αριθμό διαθέσιμων θέσεων*, μια *λίστα με τα εισιτήρια* και μοναδικό *κωδικό πτήσης*.
* Ο κάθε **χρήστης** του συστήματος χαρακτηρίζεται από *username*, *password*, *email*, *αριθμό τηλεφώνου*, μια *λίστα με τις κρατήσεις* και *αριθμό διαβατηρίου/ταυτότητας*.
* Ο **χρήστης** μπορεί να επεξεργαστεί και να ανανεώσει το *password* του, το *email* του, τον *αριθμό τηλεφώνου* του και τον *αριθμό του διαβατηρίου/ταυτότητας* του.
* Ο **χρήστης** αναζητά πτήσεις επιλέγοντας *αεροδρόμιο αναχώρησης*, *αεροδρόμιο άφιξης*, *αεροπορική εταιρεία*. Όταν κατά την αναζήτηση ο χρήστης προσπαθήσει να εισάγει λάθος στοιχεία αναζήτησης, εμφανίζεται μήνυμα λάθους.
* Ο **χρήστης** μπορεί να δημιουργήσει μια κράτηση με βάση οποιοδήποτε αποτέλεσμα αναζήτησης. Κατά τη δημιουργία της κράτησης γίνεται καταχώρηση των στοιχείων των επιβατών.
* Η κάθε κράτηση χαρακτηρίζεται από *αριθμό κράτησης*, *πτήσεις αναχώρησης*, *πτήσεις επιστροφής*, *λογικός δείκτης δρομολογίου επιστροφής*, *συνολικό κόστος εισιτηρίων* και για κάθε επιβάτη καταχωρείτε *όνομα*, *επώνυμο*, *αριθμό διαβατηρίου/ταυτότητας*, *αριθμό εισιτηρίου*, *luggage allowance*, *κόστος εισιτηρίου* και *αριθμό θέσης*.
* Οι αποσκευές θα συμπεριλαμβάνονται στο σύστημα κράτησης εισιτηρίων και θα χαρακτηρίζονται από το *βάρος* και από τον *αριθμό αποσκευών*.
* Ο **χρήστης** μπορεί να κάνει προβολή των στοιχείων της κράτησης που έχει πραγματοποιήσει χρησιμοποιώντας τον αριθμό κράτησης.
* Ο **χρήστης** μπορεί να επεξεργαστεί την κράτηση που έχει πραγματοποιήσει και να αλλάξει τα ονόματα των επιβατών ή/και τις πτήσεις που έχει επιλέξει.
* Ο **χρήστης** μπορεί να ακυρώσει την κράτηση που έχει πραγματοποιήσει χρησιμοποιώντας τον αριθμό κράτησης.

* Για τον υπολογισμό του πιο δημοφιλή προορισμού (αεροδρομίου) θα χρησιμοποιηθεί ένας μετρητής (counter). Πιο συγκεριμένα, ο μετρητής θα διατηρεί τον αριθμό των επιβατών, που έχουν προσέλθει στο αεροδρόμιο άφιξης.

## Περιορισμοί

* Τα αποτελέσματα αναζήτησης περιλαμβάνουν πτήσεις οι οποίες είναι είτε απευθείας είτε με ανταπόκριση. Όταν είναι με ανταπόκριση, το κάθε αποτέλεσμα αναζήτησης περιλαμβάνει μόνο δύο ή περισσότερα σκέλη (flight legs) που αντιστοιχούν σε απευθείας πτήσεις από διαφορετικές εταιρείες.

* Το username της αεροπορικής και του χρήστη είναι μοναδικά.

* Η ονομασία και ο 2-digit κωδικός της κάθε αεροπορικής είναι μοναδικά.
* Το password πρέπει να περιέχει ειδικά σύμβολα,αριθμούς, να έχει μήκος από 8 έως και 20 χαρακτήρες καθώς και να περιέχει μικρά-κεφαλαία γράμματα.
* Το email πρέπει να περιέχει από 3-20 χαρακτήρες και να ακολουθείται από συγκεκριμένα domain names(aueb.gr,outlook.com,gmail.com,unipi.gr).
* Η κράτηση και χρέωση αφορά τη συνολική διαδρομή (με επιστροφή ή χωρίς). Όταν δημιουργείται μια κράτηση υπολογίζεται το συνολικό κόστος των εισιτηρίων ανάλογα τον αριθμό των πτήσεων και τον τύπο του εισιτηρίου (με ή χωρίς αποσκευή) ενώ παράλληλα μειώνονται οι διαθέσιμες θέσεις για την κάθε πτήση.
* Η κάθε αεροπορική κρατάει λίστα με τις πτήσεις της, στην οποία προσθαφαιρεί στοιχεία.
* Αν η πτήση περιέχεται ήδη στη λίστα, δεν μπορεί να προσθεθεί και εμφανίζεται αντίστοιχο μήνυμα.
* Αν η πτήση ,που είναι να προστεθέι στην αεροπορική, δεν ανήκει σε αυτήν τότε εμφανίζεται αντίστοιχο μήνυμα.
* Μόνο ο διαχειριστής θα είναι σε θέση να πραγματοποιεί αλλαγές στο σύστημα.
* Στην περίπτωση που δεν υπάρχουν διαθέσιμες θέσεις για μια πτήση, ο χρήστης δεν θα έχει την δυνατότητα να κάνει κράτηση.
* Η αναζήτηση για την πτήση θα γίνεται αποκλειστικά με βάση την αεροπορική εταιρεία και το αεροδρόμιο άφιξης.
* O επιβάτης όταν δημιουργεί κράτηση για μία πτήση χωρίς να υποβληθεί στην διαδικασία ακύρωσης εισητηρίου, συνεπάγεται την σίγουρη επιβίβασή του στο αεροπλάνο.
* Κάθε κράτηση αντιστοιχεί σε έναν μόνο επιβάτη.
* Ο μετρητής για τον υπολογισμό του δημοφιλέστερου αεροδρομίου ανανεώνεται αποκλειστικά όταν πετάξει ένα αεροπλάνο.
* Η κάθε βαλίτσα έχει κόστος 30€.
* Οι δημοφιλέστεροι προορισμοί υπολογίζονται με βάση τις πτήσεις.

## Τρόποι Αντιμετώπισης (Πιθανών) Ζητημάτων Υλοποίησης

* Χρήση composite keys (foreign & primary key) στην βάση δεδομένων για την ορθή διαφοροποίηση των ενδιάμεσων tables καθώς υπάρχει το ενδεχόμενο να διαμορφωθούν τα αντίστοιχα tables με πολλά foreign & primary keys, που θα κατέχουν κάποια κοινά γνωρίσματα ως αποτέλεσμα της απόπειρας να διατυπώσουμε τις σχέσεις μεταξύ των διαφορετικών tables.
* Κατά τον υπολογισμό στατιστικών στοιχείων για όλες τις πτήσεις για να μην προκύψει υπερχείλιση δεδομένων, θα ήταν δυνατή η ενσωμάτωση μεθόδου μετατροπής των ακεραίων (default type, integer) στον τύπο integer (unsigned long) και ταυτόχρονα μπορούμε να αποφύγουμε το ίδιο θέμα και στην βάση δεδομένων με την εγκαθίδρυση περιορισμών στην H2.
* Για την διευκόλυνση διεξαγωγής ρεαλιστικών στατιστικών στοιχείων, κατά τη διάρκεια των ελέγχων δημιουργήθηκαν αντικείμενα τύπου Flight με μικρή χωρητικότητα αεροσκάφους. 
* Κατά την εκτέλεση του προγράμματος για την καλύτερη ταχύτητα/απόδοση, μπορεί να προκύψει η χρήση βασικών τύπων δεδομένων εξαιρετικά μικρής χωρητικότητας (εφόσον το επιτρέπει το πλαίσιο) και ταυτόχρονα μπορούμε να αποφύγουμε το ίδιο θέμα και στην βάση δεδομένων με την εγκαθίδρυση περιορισμών στην Η2.

## 'Ελεγχος - Υλοποίηση

* Για την υλοποίηση απαιτείται Java version 17.0.2.
* Η βάση δεδομένων που θα χρησιμοποιήσουμε θα είναι τύπου h2.
* Για την συγγραφή του κώδικα χρησιμοποιείται το IntelliJ IDEA.
* Η εφαρμογή αναπτύσσεται σε Java με χρήση του JAX-RS 2.1 API και του Quarkus framework για την υλοποίηση υπηρεσιών REST.
* Η πρώτη υλοποίηση και ο έλεγχος του λογισμικού θα γίνεται με το εργαλείο Maven 3 και πιο συγκεκριμένα με το repository Jakarta (JPA). Η εγκατάσταση του Maven είναι σχετικά απλή. Αφού κατεβάσουμε το Maven (π.χ. έκδοση 3.5.0) και τα αποσυμπιέσουμε σε κατάλληλους καταλόγους π.χ.  C:\Program Files\apache-maven-3.5.0\ αντίστοιχα θα πρέπει:

  * Να ορίσουμε τη μεταβλητή περιβάλλοντος JAVA_HOME η οποία θα δείχνει στον κατάλογο εγκατάστασης του JDK,

  * Να προσθέσουμε τον κατάλογο C:\Program Files\apache-maven-3.5.0\bin στη μεταβλητή περιβάλλοντος PATH.

  * Να ορίσουμε τη μεταβλητή περιβάλλοντος M2_HOME. Στο παράδειγμά μας είναι ο κατάλογος C:\Program Files\apache-maven-3.5.0\.

Για να εκτελέσουμε από τη γραμμή εντολών εργασίες οικοδόμησης του λογισμικού χρησιμοποιούμε το Maven μέσω της εντολής mvn, αφού μετακινηθούμε στον κατάλογο όπου βρίσκεται το αρχείο pom.xml. Η τυπική εκτέλεση του Maven είναι:

    mvn [options] [target [target2 [target3] … ]]

Τα παραγόμενα αρχεία δημιουργούνται από το Maven στο κατάλογο /target.

Τυπικές εργασίες με το Maven είναι:

    mvn clean καθαρισμός του project. Διαγράφονται όλα τα αρχεία του καταλόγου /target.
    mvn compile μεταγλώττιση του πηγαίου κώδικα. Τα αρχεία .class παράγονται στον κατάλογο /target/classes.
    mvn test-compile μεταγλώττιση του κώδικα ελέγχου. Τα αρχεία .class παράγονται στον κατάλογο /target/test-classes.
    mvn test εκτέλεση των ελέγχων με το JUnit framework.
    mvn site παραγωγή στο site του έργου το οποίο περιλαμβάνει την τεκμηρίωση του έργου.
    mvn umlet:convert -Dumlet.targetDir=src/site/markdown/uml παράγει αρχεία εικόνας png για όλα τα διαγράμματα που βρίσκονται στην τοποθεσία src/site/markdown/uml. Συστήνεται η κλήση της εντολής πριν την υποβολή μιας νέας έκδοσης διαγραμμάτων στο git repository (git commit). Ως αποτέλεσμα τα παραγόμενα αρχεία εικόνας των διαγραμμάτων συνοδεύουν τα πηγαία αρχεία έτσι ώστε να είναι εύκολη η πλοήγηση στην τεκμηρίωση του project μέσω του github.

* Υλοποίηση αυτοματοποιημένων ελέγχων για το τρέχων λογισμικό με την χρησιμοποίηση των framework JUnit, RestAssured.
* Αναφορές καλύψεων κώδικα με χρήση του εργαλείου JaCoCo.  
* Λειτουργικότητα και υλοποίηση σεναρίων κλήσης Rest services με την χρήση εργαλείων όπως το Postman Agent.
* Αποθήκευση δεδομένων σε βάση δεδομένων με την υποδομή ORM & Jakarta Persistance API.

  
* Η συγκεκριμένη έκδοση αξιοποιεί το JPA 2.0 και το Hibernate ως JPA Provider για υποστήριξη πρόσβασης στη βάση δεδομένων. Η εφαρμογή παρέχεται ως μία απλή εφαρμογή με διεπαφή χρήστη σε Swing.

## Τεκμηρίωση 
Πραγματοποιήθηκε η χρήση του markdown markup για την συγγραφή των κειμένων (τεκμηρίωση λογισμικού) και για την κατασκευή διαγραμμάτων (UML), έγινε χρήση του UMLet plugin.

