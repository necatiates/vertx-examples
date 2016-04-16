package apdprueba;

public class APDPrueba {

    public static void main(String[] args) throws Exception {
        AstroPayStreamline astro = new AstroPayStreamline();
            astro.construct();
            String response = astro.newinvoice("ASDAS234234SDSDDS984",(float) 10.5,"VI","TR","necati","647328476273","necati","necati.ates@gmail.com","TRY","Sample Payment","19891101","Adress",
                    "67543","Adana","TR","http://localhost:8080/acceptPayment","http://localhost:8080/confirm");
        System.out.println(response);

    }
    
}
