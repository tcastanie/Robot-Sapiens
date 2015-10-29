package kernelsim;
import kernelsim.VecteurN;
//import <VecteurN>;

public class TestVecteurN {

   public static void main (String args[]){

   	VecteurN V1 = new VecteurN(3);
   	VecteurN V2 = new VecteurN(3);
   	V1.setComposante(0,1);
   	V1.setComposante(1,2);
   	V1.setComposante(2,3);
   	V2.setComposante(0,4);
   	V2.setComposante(1,5);
   	V2.setComposante(2,6);
   	VecteurN V3 = VecteurN.produitVectoriel(V1,V2);
   	V3.toString();

   }

}