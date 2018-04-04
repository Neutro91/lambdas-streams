import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Exemple1 {

    public static void main(String[] args) {

        DateTimeFormatter format = DateTimeFormatter.ofPattern("d/MM/yyy");
        Persona p1 = new Persona("Arya", Persona.Genere.DONA, LocalDate.parse("25/12/2002",format) );
        Persona p2 = new Persona("Tyrion", Persona.Genere.HOME, LocalDate.parse("12/10/1980",format));
        Persona p3 = new Persona("Cersei", Persona.Genere.DONA, LocalDate.parse("10/01/1984",format));
        Persona p4 = new Persona("Eddard", Persona.Genere.HOME, LocalDate.parse("24/04/1974",format));
        Persona p5 = new Persona("Sansa", Persona.Genere.DONA, LocalDate.parse("24/04/1992",format));
        Persona p6 = new Persona("Jaime", Persona.Genere.HOME, LocalDate.parse("24/04/1979",format));

        Cotxe c1 = new Cotxe("Renault");
        Cotxe c2 = new Cotxe("Toyota");
        Cotxe[] lcotxes = {c1,c2};
        List<Cotxe> llista_cotxes = Arrays.asList(lcotxes);

        Persona[] lpers = {p1,p2,p3,p4,p5,p6};
        List<Persona> llista_persones = new ArrayList<>();
        llista_persones = Arrays.asList(lpers);

        System.out.println("---- Persones ordenades per ordre alfabètic invers -----------");

        /* Versió class anòmnima */
        llista_persones.sort(new Comparator<Persona>() {
            @Override
            public int compare(Persona o1, Persona o2) {
                return o2.getNom().compareTo(o1.getNom());
            }
        });
        // Amb Lambda expression
        llista_persones.sort((o1,o2) -> o2.getNom().compareTo(o1.getNom()));
        for(Persona a: llista_persones) {
            System.out.println(a);
        }

        System.out.println("---- Persones ordenades per edat de més a menys -----------");

      /* Versió classe anònima */
      /*llista_persones.sort(new Comparator<Persona>() {
            @Override
            public int compare(Persona o1, Persona o2) {
                return (o1.getAge() > o2.getAge())?1:-1;
            }
        });*/

        llista_persones.sort((Persona a, Persona b) -> (a.getAge()<=b.getAge())?1:-1);
        for(Persona a: llista_persones) {
            System.out.println(a);
        }

        System.out.println("---- DONES (classe anònima) -----------");

        //Classe anònima
        printPerson(llista_persones, new CheckPerson() {
            @Override
            public boolean check(Persona p) {
                return p.getGenere().equals(Persona.Genere.DONA);
            }
        });

        System.out.println("---- HOMES (lambda) -------------------");

        printPerson(llista_persones, (Persona p) -> p.getGenere().equals(Persona.Genere.HOME));

        System.out.println("-------------- edat -------------------");
        printPerson(llista_persones, (Persona p) -> p.getAge()>=40);

        System.out.println("-------------- DONES i EDAT -------------------");
        printPerson(llista_persones, p -> p.getAge()<30 && p.getGenere().equals(Persona.Genere.DONA));

        System.out.println("-------------- Llistat amb PREDICAT -------------------");
        printPersonPredicate(llista_persones, p -> p.getAge()>25);

        System.out.println("-------------- Llistat cotxes amb PREDICAT -------------------");
        printCotxe(llista_cotxes,c -> c.getMarca().equals("Toyota"));

        System.out.println("--------------  mètodes anònims-------------------");
        OpPerson opPerson = p -> p.getDataNaixament().getYear();
        System.out.println(p2.getNom() + " és nascuda l'any " + getOperationPersona(p2,opPerson));

        System.out.println("--------------  num astral-------------------");
        OpPerson opPerson2 = (Persona p) -> {
            int y = p.getDataNaixament().getYear() +
                    p.getDataNaixament().getMonthValue() +
                    p.getDataNaixament().getDayOfMonth();
            return sumaDigits(y);
        };
        OpPerson op = p -> sumaDigits(p.getDataNaixament().getYear()+
                            p.getDataNaixament().getMonthValue()+
                            p.getDataNaixament().getDayOfMonth());

        System.out.println(p2.getNom() + " num. astral: " + op.operation(p2));
        System.out.println(p1.getNom() + " num. astral:  " + getOperationPersona(p1,opPerson2));

        System.out.println("--------------  default i static method -------------------");
        CheckPerson cp = p -> true;
        cp.p();
        CheckPerson.s();

        System.out.println("--------------  Cotxes i Persones -------------------");
        p1.setCotxe(c1);
        p2.setCotxe(c2);
        p3.setCotxe(c2);
        p4.setCotxe(c2);
        p5.setCotxe(new Cotxe("Seat"));
        p6.setCotxe(c1);
        printPersonCotxe(llista_persones,(p,c) -> p.getCotxe().getMarca().equals(c.getMarca()), c1);

        System.out.println("--------------  Mètodes de referència -------------------");
        Consumer<Persona> consumer = p -> System.out.println(p);
        consumer.accept(p6);

        processar(Exemple1::imprimir,p3);

        llista_cotxes.forEach(System.out::println);
        llista_persones.forEach(Exemple1::imprimir);

        System.out.println("--------------  Streams -------------------");
        int t = (int) llista_persones.stream()
                .filter(p->p.getAge()>30)
                .count();
        System.out.println(t);

        llista_persones.stream()
                .filter(p -> p.getGenere().equals(Persona.Genere.HOME))
                .forEach(System.out::println);

        List<Persona> dones = llista_persones.stream()
                .filter(p -> p.getGenere().equals(Persona.Genere.DONA))
                .collect(Collectors.toCollection(ArrayList::new));
        dones.forEach(Exemple1::imprimir);
    }

    public static void imprimir(Persona p) {
        System.out.println("<=== " + p + " ===>");
    }

    public static void processar(Consumer<Persona> c, Persona p) {
        c.accept(p);
    }


    public static void printPersonCotxe(List<Persona> llista, Predicat2<Persona,Cotxe> t, Cotxe c) {
        for(Persona a: llista) {
            if(t.test(a,c)) System.out.println(a + "\t" + c.getMarca());
        }
    }


    public static void printPerson(List<Persona> llista, CheckPerson test) {
        for(Persona a: llista) {
            if(test.check(a))  System.out.println(a);
        }
    }

    public static void printPersonPredicate(List<Persona> llista, Predicat<Persona> test) {
        for(Persona p: llista) {
            if(test.check(p)) System.out.println(p);
        }
    }

    public static void printCotxe(List<Cotxe> llista, Predicat<Cotxe> test) {
        for(Cotxe c: llista) {
            if(test.check(c)) System.out.println(c);
        }
    }

    public static int getOperationPersona(Persona p, OpPerson c) {
        return c.operation(p);

    }

    public static int sumaDigits(int y) {
        if(y==0) return y;
        else {
            int sum = sumaDigits(y / 10) + (y % 10);
            if(sum > 9) return sumaDigits(sum);
            else return sum;
        }

    }

}
